package de.mpg.mpi.inf.d5.wikipedia.export;

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
 * AvroExportHandler
 */
public class AvroExportHandler extends WikipediaExportHandler {
  private static final CodecFactory CODEC_SNAPPY = CodecFactory.snappyCodec();

  private final String pageMetadataOutputFile;
  private final String revisionMetadataOutputFile;
  private final String revisionWikilinksOutputFile;

  private DataFileWriter<RevisionMetadata> revisionMetadataDataFileWriter;
  private DataFileWriter<PageMetadata> pageMetadataDataFileWriter;
  private DataFileWriter<RevisionWikilink> revisionWikilinksDataFileWriter;

  /**
   * AvroExportHandler
   */
  public AvroExportHandler(String pageMetadataOutputFile,
                           String revisionMetadataOutputFile,
                           String revisionWikilinksOutputFile) {
    this.pageMetadataOutputFile = pageMetadataOutputFile;
    this.revisionMetadataOutputFile = revisionMetadataOutputFile;
    this.revisionWikilinksOutputFile = revisionWikilinksOutputFile;
  }

  /**
   * onStartDocument
   */
  @Override
  protected void onStartDocument() throws WikipediaExportException {
    try {
      pageMetadataDataFileWriter = getDataFileWriter(pageMetadataOutputFile,
                                                        PageMetadata.class,
                                                        PageMetadata.getClassSchema(),
                                                        CODEC_SNAPPY);
      revisionMetadataDataFileWriter = getDataFileWriter(revisionMetadataOutputFile,
                                                            RevisionMetadata.class,
                                                            RevisionMetadata.getClassSchema(),
                                                            CODEC_SNAPPY);
      revisionWikilinksDataFileWriter = getDataFileWriter(revisionWikilinksOutputFile,
                                                             RevisionWikilink.class,
                                                             RevisionWikilink.getClassSchema(),
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
      revisionMetadataDataFileWriter.close();
      revisionWikilinksDataFileWriter.close();
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
      pageMetadataDataFileWriter.append(pageMetadata);
    } catch (IOException e) {
      throw new WikipediaExportException("Appending page metadata failed.", e);
    }
  }

  /**
   * onRevisionMetadata
   */
  @Override
  protected void onRevisionMetadata(RevisionMetadata revisionMetadata) throws WikipediaExportException {
    try {
      revisionMetadataDataFileWriter.append(revisionMetadata);
    } catch (IOException e) {
      throw new WikipediaExportException("Appending revision metadata failed.", e);
    }
  }

  /**
   * onRevisionWikilink
   */
  @Override
  protected void onRevisionWikilink(RevisionWikilink revisionWikilink) throws WikipediaExportException {
    try {
      revisionWikilinksDataFileWriter.append(revisionWikilink);
    } catch (IOException e) {
      throw new WikipediaExportException("Appending revision wikilinks failed.", e);
    }
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
