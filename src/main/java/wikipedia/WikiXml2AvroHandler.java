package wikipedia;

import org.apache.avro.Schema;
import org.apache.avro.file.CodecFactory;
import org.apache.avro.file.DataFileWriter;
import org.apache.avro.io.DatumWriter;
import org.apache.avro.specific.SpecificDatumWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import wikipedia.schemas.Contributor;
import wikipedia.schemas.PageMetadata;
import wikipedia.schemas.RevisionContent;
import wikipedia.schemas.RevisionMetadata;

import java.io.File;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

/**
 * WikiXml2AvroHandler
 */
public class WikiXml2AvroHandler extends DefaultHandler {
  private static final String ROOT = "";
  private static final String MEDIAWIKI = "mediawiki";
  private static final String PAGE = "page";
  private static final String REVISION = "revision";
  private static final String CONTRIBUTOR = "contributor";
  private static final String TITLE = "title";
  private static final String NS = "ns";
  private static final String ID = "id";
  private static final String PARENTID = "parentid";
  private static final String TIMESTAMP = "timestamp";
  private static final String MINOR = "minor";
  private static final String COMMENT = "comment";
  private static final String MODEL = "model";
  private static final String FORMAT = "format";
  private static final String TEXT = "text";
  private static final String SHA1 = "sha1";
  private static final String IP = "ip";
  private static final String USERNAME = "username";

  private static final Logger logger = LoggerFactory.getLogger(WikiXml2AvroHandler.class);
  private long pageNumber = 0;
  private long revisionNumber = 0;

  private final String metadataOutputFile;
  private final String contentOutputFile;
  private DataFileWriter<PageMetadata> pageMetadataFileWriter;
  private DataFileWriter<RevisionContent> revisionContentFileWriter;

  private Deque<String> elementStack;
  private String parentElement;

  private PageMetadata pageMetadata;
  private List<RevisionMetadata> pageMetadataRevisions;
  private RevisionMetadata revisionMetadata;
  private Contributor contributor;
  private RevisionContent revisionContent;

  private StringBuffer elementTextBuffer;

  public WikiXml2AvroHandler(String metadataOutputFile, String contentOutputFile) {
    this.metadataOutputFile = metadataOutputFile;
    this.contentOutputFile = contentOutputFile;
  }

  @Override
  public void startDocument() throws SAXException {
    logStartDocument();
    try {
      initializeFileWriters();
    } catch (IOException e) {
      throw new SAXException("File writer initialization failed.", e);
    }
    elementStack = new ArrayDeque<>();
    parentElement = ROOT;
  }

  @Override
  public void endDocument() throws SAXException {
    try {
      closeFileWriter(pageMetadataFileWriter);
      closeFileWriter(revisionContentFileWriter);
    } catch (IOException e) {
      throw new SAXException("File writer closing failed.", e);
    }
    elementStack = null;
    logEndDocument();
  }

  @Override
  public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
    updateParentElement();
    elementStack.push(qName);
    switch (parentElement) {
      case MEDIAWIKI:
        startElementParentMediawiki(uri, localName, qName, attributes);
        break;
      case PAGE:
        startElementParentPage(uri, localName, qName, attributes);
        break;
      case REVISION:
        startElementParentRevision(uri, localName, qName, attributes);
        break;
      case CONTRIBUTOR:
        startElementParentContributor(uri, localName, qName, attributes);
        break;
      default:
        break;
    }
  }

  @Override
  public void endElement(String uri, String localName, String qName) throws SAXException {
    elementStack.pop();
    updateParentElement();
    switch (parentElement) {
      case MEDIAWIKI:
        endElementParentMediawiki(uri, localName, qName);
        break;
      case PAGE:
        endElementParentPage(uri, localName, qName);
        break;
      case REVISION:
        endElementParentRevision(uri, localName, qName);
        break;
      case CONTRIBUTOR:
        endElementParentContributor(uri, localName, qName);
        break;
      default:
        break;
    }
  }

  @SuppressWarnings("unused")
  private void startElementParentMediawiki(String uri, String localName, String qName, Attributes attributes) {
    switch (qName) {
      case PAGE:
        logPageEvent();
        pageMetadata = new PageMetadata();
        pageMetadataRevisions = new ArrayList<>();
        break;
      default:
        break;
    }
  }

  @SuppressWarnings("unused")
  private void endElementParentMediawiki(String uri, String localName, String qName) throws SAXException {
    switch (qName) {
      case PAGE:
        try {
          pageMetadata.setRevisions(pageMetadataRevisions);
          pageMetadataRevisions = null;
          pageMetadataFileWriter.append(pageMetadata);
          pageMetadata = null;
        } catch (IOException e) {
          throw new SAXException("Appending new page metadata failed.", e);
        }
        break;
      default:
        break;
    }
  }

  @SuppressWarnings("unused")
  private void startElementParentPage(String uri, String localName, String qName, Attributes attributes) {
    switch (qName) {
      case TITLE:
      case NS:
      case ID:
        initializeElementTextBuffer();
        break;
      case REVISION:
        logRevisionEvent();
        revisionMetadata = new RevisionMetadata();
        break;
      default:
        break;
    }
  }

  @SuppressWarnings("unused")
  private void endElementParentPage(String uri, String localName, String qName) throws SAXException {
    switch (qName) {
      case TITLE:
        pageMetadata.setTitle(readElementTextBuffer());
        break;
      case NS:
        pageMetadata.setNs(Integer.valueOf(readElementTextBuffer()));
        break;
      case ID:
        pageMetadata.setPageId(Long.valueOf(readElementTextBuffer()));
        break;
      case REVISION:
        pageMetadataRevisions.add(revisionMetadata);
        revisionMetadata = null;
        break;
      default:
        break;
    }
  }

  @SuppressWarnings("unused")
  private void startElementParentRevision(String uri, String localName, String qName, Attributes attributes) {
    switch (qName) {
      case ID:
      case PARENTID:
      case TIMESTAMP:
      case COMMENT:
      case MODEL:
      case FORMAT:
      case SHA1:
        initializeElementTextBuffer();
        break;
      case CONTRIBUTOR:
        contributor = new Contributor();
        break;
      case MINOR:
        break;
      case TEXT:
        revisionContent = new RevisionContent();
        initializeElementTextBuffer();
        break;
      default:
        break;
    }
  }

  @SuppressWarnings("unused")
  private void endElementParentRevision(String uri, String localName, String qName) throws SAXException {
    switch (qName) {
      case ID:
        revisionMetadata.setRevisionId(Long.valueOf(readElementTextBuffer()));
        break;
      case PARENTID:
        revisionMetadata.setParentId(Long.valueOf(readElementTextBuffer()));
        break;
      case TIMESTAMP:
        revisionMetadata.setTimestamp(readElementTextBuffer());
        break;
      case COMMENT:
        revisionMetadata.setComment(readElementTextBuffer());
        break;
      case MODEL:
        revisionMetadata.setModel(readElementTextBuffer());
        break;
      case FORMAT:
        revisionMetadata.setFormat(readElementTextBuffer());
        break;
      case SHA1:
        revisionMetadata.setSha1(readElementTextBuffer());
        break;
      case CONTRIBUTOR:
        revisionMetadata.setContributor(contributor);
        contributor = null;
        break;
      case MINOR:
        revisionMetadata.setMinor(true);
        break;
      case TEXT:
        try {
          revisionContent.setPageId(pageMetadata.getPageId());
          revisionContent.setRevisionId(revisionMetadata.getRevisionId());
          revisionContent.setContent(readElementTextBuffer());
          revisionContentFileWriter.append(revisionContent);
          revisionContent = null;
        } catch (IOException e) {
          throw new SAXException("Appending new revision content failed.", e);
        }
        break;
      default:
        break;
    }
  }

  @SuppressWarnings("unused")
  private void startElementParentContributor(String uri, String localName, String qName, Attributes attributes) {
    switch (qName) {
      case IP:
      case USERNAME:
      case ID:
        initializeElementTextBuffer();
        break;
      default:
        break;
    }
  }

  @SuppressWarnings("unused")
  private void endElementParentContributor(String uri, String localName, String qName) {
    switch (qName) {
      case IP:
        contributor.setIp(readElementTextBuffer());
        break;
      case USERNAME:
        contributor.setUsername(readElementTextBuffer());
        break;
      case ID:
        contributor.setUserId(Long.valueOf(readElementTextBuffer()));
        break;
      default:
        break;
    }
  }

  @Override
  public void characters(char[] ch, int start, int length) throws SAXException {
    if (elementTextBuffer != null) {
      elementTextBuffer.append(ch, start, length);
    }
  }

  private void initializeElementTextBuffer() {
    elementTextBuffer = new StringBuffer();
  }

  private String readElementTextBuffer() {
    String elementText = elementTextBuffer.toString();
    elementTextBuffer = null;
    return elementText;
  }

  private void initializeFileWriters() throws IOException {
    pageMetadataFileWriter = null;
    revisionContentFileWriter = null;
    try {
      pageMetadataFileWriter = initializeFileWriter(metadataOutputFile, PageMetadata.class, PageMetadata.getClassSchema());
      revisionContentFileWriter = initializeFileWriter(contentOutputFile, RevisionContent.class, RevisionContent.getClassSchema());
    } catch (IOException e) {
      try {
        closeFileWriter(pageMetadataFileWriter);
        closeFileWriter(revisionContentFileWriter);
      } catch (IOException oe) {
        /* Lose this exception in favour of the original one. */
      }
      throw e;
    }
  }

  @SuppressWarnings("unchecked")
  private <T> DataFileWriter<T> initializeFileWriter(String outputFile, Class avroClass, Schema avroSchema) throws IOException {
    DatumWriter<T> datumWriter = new SpecificDatumWriter<>(avroClass);
    DataFileWriter<T> dataFileWriter = new DataFileWriter<>(datumWriter);
    dataFileWriter.setCodec(CodecFactory.bzip2Codec());
    return dataFileWriter.create(avroSchema, new File(outputFile));
  }

  private <T> void closeFileWriter(DataFileWriter<T> fileWriter) throws IOException {
    if (fileWriter != null) {
      fileWriter.close();
    }
  }

  private void updateParentElement() {
    if (elementStack.isEmpty()) {
      parentElement = ROOT;
    } else {
      parentElement = elementStack.peek();
    }
  }

  private void logStartDocument() {
    logger.debug("Process started.");
    logger.debug("Writing page metadata collection to: " + metadataOutputFile);
    logger.debug("Writing revision content collection to: " + contentOutputFile);
  }

  private void logPageEvent() {
    ++pageNumber;
    logger.debug("Opening page number: " + pageNumber);
  }

  private void logRevisionEvent() {
    ++revisionNumber;
    logger.debug("Opening revision number: " + revisionNumber);
  }

  private void logEndDocument() {
    logger.debug("Process finished successfully.");
    logger.debug("Number of pages: " + pageNumber);
    logger.debug("Number of revisions: " + revisionNumber);
  }
}
