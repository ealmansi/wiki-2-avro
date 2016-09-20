package de.mpg.mpi.inf.d5.wikipedia.export;

import de.mpg.mpi.inf.d5.wikipedia.export.schemas.CurrentPage;
import de.mpg.mpi.inf.d5.wikipedia.export.schemas.CurrentWikilink;
import de.mpg.mpi.inf.d5.wikipedia.export.schemas.PageMetadata;
import de.mpg.mpi.inf.d5.wikipedia.export.schemas.RevisionMetadata;
import de.mpg.mpi.inf.d5.wikipedia.export.schemas.RevisionWikilink;
import org.apache.avro.Schema;
import org.apache.avro.file.CodecFactory;
import org.apache.avro.file.DataFileWriter;
import org.apache.avro.io.DatumWriter;
import org.apache.avro.specific.SpecificDatumWriter;

import java.io.File;
import java.io.IOException;

/**
 * AvroCurrentExportHandler
 */
public class AvroCurrentExportHandler extends WikipediaExportHandler {
  private static final CodecFactory CODEC_SNAPPY = CodecFactory.snappyCodec();

  private final String pageMetadataOutputFile;
  private final String wikilinksOutputFile;

  private DataFileWriter<CurrentPage> pageMetadataDataFileWriter;
  private DataFileWriter<CurrentWikilink> wikilinksDataFileWriter;

  /**
   * AvroCurrentExportHandler
   */
  public AvroCurrentExportHandler(String pageMetadataOutputFile,
                           String wikilinksOutputFile) {
    this.pageMetadataOutputFile = pageMetadataOutputFile;
    this.wikilinksOutputFile = wikilinksOutputFile;
  }

  /**
   * onStartDocument
   */
  @Override
  protected void onStartDocument() throws WikipediaExportException {
    try {
      pageMetadataDataFileWriter = getDataFileWriter(pageMetadataOutputFile,
                                                        CurrentPage.class,
                                                        CurrentPage.getClassSchema(),
                                                        CODEC_SNAPPY);
      wikilinksDataFileWriter = getDataFileWriter(wikilinksOutputFile,
                                                             CurrentWikilink.class,
                                                             CurrentWikilink.getClassSchema(),
                                                             CODEC_SNAPPY);
    } catch (IOException e) {
      throw new WikipediaExportException("Opening Avro data file writers failed.", e);
    }
  }

  /**
   * onEndDocument
   */
  @Override
  protected void onEndDocument() throws WikipediaExportException {
    try {
      pageMetadataDataFileWriter.close();
      wikilinksDataFileWriter.close();
    } catch (IOException e) {
      throw new WikipediaExportException("Closing Avro data file writers failed.", e);
    }
  }

  /**
   * onPageMetadata
   */
  @Override
  protected void onPageMetadata(PageMetadata pageMetadata) throws WikipediaExportException {
    try {
      if (pageMetadata.namespace == 0) {
        pageMetadataDataFileWriter.append(new CurrentPage(pageMetadata.getPageId(),
                                                          pageMetadata.getTitle(),
                                                          pageMetadata.getRedirect()));
      }
    } catch (IOException e) {
      throw new WikipediaExportException("Appending page metadata failed.", e);
    }
  }

  /**
   * onRevisionMetadata
   */
  @Override
  protected void onRevisionMetadata(PageMetadata pageMetadata,
                                      RevisionMetadata revisionMetadata) throws WikipediaExportException {
  }

  /**
   * onRevisionWikilink
   */
  @Override
  protected void onRevisionWikilink(PageMetadata pageMetadata,
                                      RevisionMetadata revisionMetadata,
                                      RevisionWikilink revisionWikilink) throws WikipediaExportException {
    try {
      if (pageMetadata.namespace == 0) {
        Long pageId = revisionWikilink.getPageId();
        String wikilink = revisionWikilink.getWikilink().toString();
        String normalizedWikilink = normalizeWikilink(wikilink);
        wikilinksDataFileWriter.append(new CurrentWikilink(pageId, normalizedWikilink));
      }
    } catch (IOException e) {
      throw new WikipediaExportException("Appending revision wikilinks failed.", e);
    }
  }

  String normalizeWikilink(String wikilink) {
    String normalized = wikilink.trim();
    normalized = normalized.replaceAll("_+", " ");
    normalized = normalized.replaceAll(" +", " ");
    normalized = normalized.replaceAll("^:+", "");
    if (normalized.indexOf('#') > -1) {
      normalized = normalized.substring(0, normalized.indexOf('#'));
    }
    if (normalized.indexOf('|') > -1) {
      normalized = normalized.substring(0, normalized.indexOf('|'));
    }
    return normalized.trim();
  }

  private <T> DataFileWriter<T> getDataFileWriter(String outputFile,
                                                    Class<T> avroClass,
                                                    Schema avroSchema,
                                                    CodecFactory codec) throws IOException {
    DatumWriter<T> datumWriter = new SpecificDatumWriter<>(avroClass);
    DataFileWriter<T> dataFileWriter = new DataFileWriter<>(datumWriter);
    dataFileWriter.setCodec(codec);
    return dataFileWriter.create(avroSchema, new File(outputFile));
  }
}
