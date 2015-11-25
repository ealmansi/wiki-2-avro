package de.mpg.mpi.inf.d5.wikipedia.export;

import de.mpg.mpi.inf.d5.wikipedia.export.schemas.PageMetadata;
import de.mpg.mpi.inf.d5.wikipedia.export.schemas.RevisionMetadata;
import de.mpg.mpi.inf.d5.wikipedia.export.schemas.RevisionWikilink;

import java.util.ArrayList;
import java.util.List;

/**
 * WikipediaExportTestHandler
 */
public class WikipediaExportTestHandler extends WikipediaExportHandler {
  private List<PageMetadata> pageMetadataList = null;
  private List<RevisionMetadata> revisionMetadataList = null;
  private List<RevisionWikilink> revisionWikilinksList = null;

  @Override
  protected void onStartDocument() throws WikipediaExportException {
    pageMetadataList = new ArrayList<>();
    revisionMetadataList = new ArrayList<>();
    revisionWikilinksList = new ArrayList<>();
  }

  @Override
  protected void onEndDocument() throws WikipediaExportException {
  }

  @Override
  protected void onPageMetadata(PageMetadata pageMetadata) throws WikipediaExportException {
    pageMetadataList.add(pageMetadata);
  }

  @Override
  protected void onRevisionMetadata(RevisionMetadata revisionMetadata) throws WikipediaExportException {
    revisionMetadataList.add(revisionMetadata);
  }

  @Override
  protected void onRevisionWikilink(RevisionWikilink revisionWikilink) throws WikipediaExportException {
    revisionWikilinksList.add(revisionWikilink);
  }

  public List<PageMetadata> getPageMetadataList() {
    return pageMetadataList;
  }

  public List<RevisionMetadata> getRevisionMetadataList() {
    return revisionMetadataList;
  }

  public List<RevisionWikilink> getRevisionWikilinksList() {
    return revisionWikilinksList;
  }
}
