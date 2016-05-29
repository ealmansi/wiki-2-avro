package de.mpg.mpi.inf.d5.wikipedia.export;

import de.mpg.mpi.inf.d5.wikipedia.export.schemas.WikipediaPageMetadata;
import de.mpg.mpi.inf.d5.wikipedia.export.schemas.WikipediaRevisionMetadata;
import de.mpg.mpi.inf.d5.wikipedia.export.schemas.WikipediaRevisionText;

import java.util.ArrayList;
import java.util.List;

/**
 * WikipediaExportTestHandler
 */
public class WikipediaExportTestHandler extends WikipediaExportHandler {
  private List<WikipediaPageMetadata> pageMetadataList = null;
  private List<WikipediaRevisionMetadata> revisionMetadataList = null;
  private List<WikipediaRevisionText> revisionTextsList = null;

  @Override
  protected void startExport() throws WikipediaExportException {
    pageMetadataList = new ArrayList<>();
    revisionMetadataList = new ArrayList<>();
    revisionTextsList = new ArrayList<>();
  }

  @Override
  protected void endExport() throws WikipediaExportException {
  }

  @Override
  protected void startPageMetadata(WikipediaPageMetadata pageMetadata)
      throws WikipediaExportException {
    pageMetadataList.add(pageMetadata);
  }

  @Override
  protected void endPageMetadata()
      throws WikipediaExportException {
  }

  @Override
  protected void startRevisionMetadata(WikipediaRevisionMetadata revisionMetadata)
      throws WikipediaExportException {
    revisionMetadataList.add(revisionMetadata);
  }

  @Override
  protected void endRevisionMetadata()
      throws WikipediaExportException {
  }

  @Override
  protected void startRevisionText(WikipediaRevisionText revisionText)
      throws WikipediaExportException {
    revisionTextsList.add(revisionText);
  }

  @Override
  protected void endRevisionText()
      throws WikipediaExportException {
  }

  public List<WikipediaPageMetadata> getPageMetadataList() {
    return pageMetadataList;
  }

  public List<WikipediaRevisionMetadata> getRevisionMetadataList() {
    return revisionMetadataList;
  }

  public List<WikipediaRevisionText> getRevisionTextsList() {
    return revisionTextsList;
  }
}
