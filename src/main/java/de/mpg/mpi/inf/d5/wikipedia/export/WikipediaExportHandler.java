package de.mpg.mpi.inf.d5.wikipedia.export;

import com.google.common.collect.ImmutableTable;
import com.google.common.collect.Table;
import de.mpg.mpi.inf.d5.wikipedia.export.schemas.PageMetadata;
import de.mpg.mpi.inf.d5.wikipedia.export.schemas.RevisionMetadata;
import de.mpg.mpi.inf.d5.wikipedia.export.schemas.RevisionWikilink;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * WikipediaExportHandler
 */
public abstract class WikipediaExportHandler extends DefaultHandler {
  /**
   * onStartDocument
   */
  protected abstract void onStartDocument() throws WikipediaExportException;

  /**
   * onEndDocument
   */
  protected abstract void onEndDocument() throws WikipediaExportException;

  /**
   * onPageMetadata
   */
  protected abstract void onPageMetadata(PageMetadata pageMetadata)
      throws WikipediaExportException;

  /**
   * onRevisionMetadata
   */
  protected abstract void onRevisionMetadata(RevisionMetadata revisionMetadata)
      throws WikipediaExportException;

  /**
   * onRevisionWikilink
   */
  protected abstract void onRevisionWikilink(RevisionWikilink revisionWikilink)
      throws WikipediaExportException;

  /**
   * Wikipedia XML element tags.
   */
  private static final String ROOT = "";
  private static final String MEDIAWIKI = "mediawiki";
  private static final String PAGE = "page";
  private static final String REVISION = "revision";
  private static final String CONTRIBUTOR = "contributor";
  private static final String TITLE = "title";
  private static final String NAMESPACE = "ns";
  private static final String ID = "id";
  private static final String REDIRECT = "redirect";
  private static final String PARENT_ID = "parentid";
  private static final String TIMESTAMP = "timestamp";
  private static final String MINOR = "minor";
  private static final String COMMENT = "comment";
  private static final String MODEL = "model";
  private static final String FORMAT = "format";
  private static final String TEXT = "text";
  private static final String SHA1 = "sha1";
  private static final String IP = "ip";
  private static final String USERNAME = "username";

  /**
   * ElementStack
   */
  private static class ElementStack {
    private final List<String> elementStack = new ArrayList<>();

    public void push(String element) {
      elementStack.add(element);
    }

    public void pop() {
      if (elementStack.isEmpty()) {
        throw new RuntimeException("Unexpected end of element.");
      }
      elementStack.remove(elementStack.size() - 1);
    }

    public String getParentElement() {
      String parentElement;
      if (elementStack.size() < 2) {
        parentElement = ROOT;
      } else {
        parentElement = elementStack.get(elementStack.size() - 2);
      }
      return parentElement;
    }
  }

  /**
   * State kept during parsing.
   */
  private ElementStack elementStack;
  private PageMetadata pageMetadata;
  private RevisionMetadata revisionMetadata;
  private StringBuffer elementTextBuffer;

  /**
   * State kept for logging.
   */
  private static final Logger logger = LoggerFactory.getLogger(WikipediaExportHandler.class);
  private long pageNumber = 0;
  private long revisionNumber = 0;

  /**
   * Abstract ElementHandlers.
   */
  private abstract class ElementHandler {
    abstract void startElement(String uri, String localName, String qName, Attributes attributes)
        throws WikipediaExportException;
    abstract void endElement(String uri, String localName, String qName)
        throws WikipediaExportException;
  }

  private abstract class ValueElementHandler extends ElementHandler {
    void startElement(String uri, String localName, String qName, Attributes attributes)
        throws WikipediaExportException {
      initializeElementTextBuffer();
    }
  }

  private abstract class BooleanElementHandler extends ValueElementHandler {
    abstract void endElement() throws WikipediaExportException;
    void endElement(String uri, String localName, String qName)
        throws WikipediaExportException {
      endElement();
    }
  }

  private abstract class IntegerElementHandler extends ValueElementHandler {
    abstract void endElement(Integer value) throws WikipediaExportException;
    void endElement(String uri, String localName, String qName) throws WikipediaExportException {
      endElement(Integer.valueOf(readElementTextBuffer()));
    }
  }

  private abstract class LongElementHandler extends ValueElementHandler {
    abstract void endElement(Long value) throws WikipediaExportException;
    void endElement(String uri, String localName, String qName) throws WikipediaExportException {
      endElement(Long.valueOf(readElementTextBuffer()));
    }
  }

  private abstract class StringElementHandler extends ValueElementHandler {
    abstract void endElement(String value) throws WikipediaExportException;
    void endElement(String uri, String localName, String qName) throws WikipediaExportException {
      endElement(readElementTextBuffer());
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

  /**
   * Concrete ElementHandlers.
   */
  private final ElementHandler PAGE_HANDLER = new ElementHandler() {
    void startElement(String uri, String localName, String qName, Attributes attributes)
        throws WikipediaExportException {
      pageMetadata = new PageMetadata();
    }
    void endElement(String uri, String localName, String qName)
        throws WikipediaExportException {
      onPageMetadata(pageMetadata);
      pageMetadata = null;
      logPageEvent();
    }
  };
  
  private final ElementHandler PAGE_TITLE_HANDLER = new StringElementHandler() {
    void endElement(String value) throws WikipediaExportException {
      pageMetadata.setTitle(value);
    }
  };
  
  private final ElementHandler PAGE_NAMESPACE_HANDLER = new IntegerElementHandler() {
    void endElement(Integer value) throws WikipediaExportException {
      pageMetadata.setNamespace(value);
    }
  };
  
  private final ElementHandler PAGE_ID_HANDLER = new LongElementHandler() {
    void endElement(Long value) throws WikipediaExportException {
      pageMetadata.setPageId(value);
    }
  };
  
  private final ElementHandler PAGE_REDIRECT_HANDLER = new ElementHandler() {
    void startElement(String uri, String localName, String qName, Attributes attributes)
        throws WikipediaExportException {
      pageMetadata.setRedirect(attributes.getValue(TITLE));
    }
    void endElement(String uri, String localName, String qName)
        throws WikipediaExportException {
    }
  };
  
  private final ElementHandler REVISION_HANDLER = new ElementHandler() {
    void startElement(String uri, String localName, String qName, Attributes attributes)
        throws WikipediaExportException {
      revisionMetadata = new RevisionMetadata();
      revisionMetadata.setPageId(pageMetadata.getPageId());
    }
    void endElement(String uri, String localName, String qName)
        throws WikipediaExportException {
      onRevisionMetadata(revisionMetadata);
      revisionMetadata = null;
      logRevisionEvent();
    }
  };
  
  private final ElementHandler REVISION_ID_HANDLER = new LongElementHandler() {
    void endElement(Long value) throws WikipediaExportException {
      revisionMetadata.setRevisionId(value);
    }
  };
  
  private final ElementHandler REVISION_PARENT_ID_HANDLER = new LongElementHandler() {
    void endElement(Long value) throws WikipediaExportException {
      revisionMetadata.setParentId(value);
    }
  };
  
  private final ElementHandler REVISION_TIMESTAMP_HANDLER = new StringElementHandler() {
    void endElement(String value) throws WikipediaExportException {
      revisionMetadata.setTimestamp(value);
    }
  };
  
  private final ElementHandler REVISION_MINOR_HANDLER = new BooleanElementHandler() {
    void endElement() throws WikipediaExportException {
      revisionMetadata.setMinor(true);
    }
  };
  
  private final ElementHandler REVISION_COMMENT_HANDLER = new StringElementHandler() {
    void endElement(String value) throws WikipediaExportException {
      revisionMetadata.setComment(value);
    }
  };
  
  private final ElementHandler REVISION_MODEL_HANDLER = new StringElementHandler() {
    void endElement(String value) throws WikipediaExportException {
      revisionMetadata.setModel(value);
    }
  };
  
  private final ElementHandler REVISION_FORMAT_HANDLER = new StringElementHandler() {
    void endElement(String value) throws WikipediaExportException {
      revisionMetadata.setFormat(value);
    }
  };
  
  private final ElementHandler REVISION_TEXT_HANDLER = new StringElementHandler() {
    void endElement(String value) throws WikipediaExportException {
      revisionMetadata.setTextSize(value.length());
      List<CharSequence> wikilinks = WikilinksExtractor.extractWikilinks(value);
      for (CharSequence wikilink : wikilinks) {
        onRevisionWikilink(new RevisionWikilink(pageMetadata.getPageId(),
                                                   revisionMetadata.getRevisionId(),
                                                   revisionMetadata.getTimestamp(),
                                                   wikilink));
      }
    }
  };
  
  private final ElementHandler REVISION_SHA1_HANDLER = new StringElementHandler() {
    void endElement(String value) throws WikipediaExportException {
      revisionMetadata.setSha1(value);
    }
  };
  
  private final ElementHandler CONTRIBUTOR_IP_HANDLER = new StringElementHandler() {
    void endElement(String value) throws WikipediaExportException {
      revisionMetadata.setContributorIp(value);
    }
  };
  
  private final ElementHandler CONTRIBUTOR_USER_ID_HANDLER = new LongElementHandler() {
    void endElement(Long value) throws WikipediaExportException {
      revisionMetadata.setContributorUserId(value);
    }
  };
  
  private final ElementHandler CONTRIBUTOR_USERNAME_HANDLER = new StringElementHandler() {
    void endElement(String value) throws WikipediaExportException {
      revisionMetadata.setContributorUsername(value);
    }
  };

  /**
   * Assignment of ElementHandlers to (parentElement, currentElement) events.
   */
  private final Table<String, String, ElementHandler> ELEMENT_HANDLERS =
      new ImmutableTable.Builder<String, String, ElementHandler>()
          .put(MEDIAWIKI, PAGE, PAGE_HANDLER)
              .put(PAGE, TITLE, PAGE_TITLE_HANDLER)
              .put(PAGE, NAMESPACE, PAGE_NAMESPACE_HANDLER)
              .put(PAGE, ID, PAGE_ID_HANDLER)
              .put(PAGE, REDIRECT, PAGE_REDIRECT_HANDLER)
              .put(PAGE, REVISION, REVISION_HANDLER)
                  .put(REVISION, ID, REVISION_ID_HANDLER)
                  .put(REVISION, PARENT_ID, REVISION_PARENT_ID_HANDLER)
                  .put(REVISION, TIMESTAMP, REVISION_TIMESTAMP_HANDLER)
                  .put(REVISION, MINOR, REVISION_MINOR_HANDLER)
                  .put(REVISION, COMMENT, REVISION_COMMENT_HANDLER)
                  .put(REVISION, MODEL, REVISION_MODEL_HANDLER)
                  .put(REVISION, FORMAT, REVISION_FORMAT_HANDLER)
                  .put(REVISION, TEXT, REVISION_TEXT_HANDLER)
                  .put(REVISION, SHA1, REVISION_SHA1_HANDLER)
                      .put(CONTRIBUTOR, IP, CONTRIBUTOR_IP_HANDLER)
                      .put(CONTRIBUTOR, ID, CONTRIBUTOR_USER_ID_HANDLER)
                      .put(CONTRIBUTOR, USERNAME, CONTRIBUTOR_USERNAME_HANDLER)
          .build();

  /**
   * DefaultHandler behaviour.
   */
  @Override
  public void startDocument() throws SAXException {
    try {
      elementStack = new ElementStack();
      onStartDocument();
      logStartDocument();
    } catch (WikipediaExportException e) {
      throw new SAXException(e);
    }
  }

  @Override
  public void endDocument() throws SAXException {
    try {
      elementStack = null;
      onEndDocument();
      logEndDocument();
    } catch (WikipediaExportException e) {
      throw new SAXException(e);
    }
  }

  @Override
  public void startElement(String uri, String localName, String qName, Attributes attributes)
      throws SAXException {
    elementStack.push(qName);
    ElementHandler elementHandler = ELEMENT_HANDLERS.get(elementStack.getParentElement(), qName);
    if (elementHandler != null) {
      try {
          elementHandler.startElement(uri, localName, qName, attributes);
      } catch (WikipediaExportException e) {
        throw new SAXException(e);
      }
    }
  }

  @Override
  public void endElement(String uri, String localName, String qName)
      throws SAXException {
    ElementHandler elementHandler = ELEMENT_HANDLERS.get(elementStack.getParentElement(), qName);
    if (elementHandler != null) {
      try {
        elementHandler.endElement(uri, localName, qName);
      } catch (WikipediaExportException e) {
        throw new SAXException(e);
      }
    }
    elementStack.pop();
  }

  @Override
  public void characters(char[] ch, int start, int length) throws SAXException {
    if (elementTextBuffer != null) {
      elementTextBuffer.append(ch, start, length);
    }
  }

  /**
   * Logging.
   */
  private void logStartDocument() {
    logger.info("Parsing of Wikipedia XML started.");
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
    logger.info("Parsing of Wikipedia XML finished successfully.");
    logger.info("Number of pages: " + pageNumber);
    logger.info("Number of revisions: " + revisionNumber);
  }
}