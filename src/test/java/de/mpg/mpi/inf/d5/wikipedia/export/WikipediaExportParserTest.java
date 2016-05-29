package de.mpg.mpi.inf.d5.wikipedia.export;

import org.junit.Assert;
import org.junit.Test;
import org.xml.sax.InputSource;

import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * WikipediaExportParserTest
 */
public class WikipediaExportParserTest {
  @Test
  public void testPagesMetaHistory() throws WikipediaExportException {
    InputSource inputSource = getInputSourceFromFile("/pages_meta_history_sample.xml");
    WikipediaExportTestHandler testHandler = new WikipediaExportTestHandler();
    WikipediaExportParser.parse(inputSource, testHandler);
    Assert.assertEquals(3, testHandler.getPageMetadataList().size());
    Assert.assertEquals(7, testHandler.getRevisionMetadataList().size());
    Assert.assertEquals(7, testHandler.getRevisionTextsList().size());
  }

  @Test
  public void testPagesMetaCurrent() throws WikipediaExportException {
    InputSource inputSource = getInputSourceFromFile("/pages_meta_current_sample.xml");
    WikipediaExportTestHandler testHandler = new WikipediaExportTestHandler();
    WikipediaExportParser.parse(inputSource, testHandler);
    Assert.assertEquals(19, testHandler.getPageMetadataList().size());
    Assert.assertEquals(19, testHandler.getRevisionMetadataList().size());
    Assert.assertEquals(19, testHandler.getRevisionTextsList().size());
  }

  @Test
  public void testStubMetaHistory() throws WikipediaExportException {
    InputSource inputSource = getInputSourceFromFile("/stub_meta_history_sample.xml");
    WikipediaExportTestHandler testHandler = new WikipediaExportTestHandler();
    WikipediaExportParser.parse(inputSource, testHandler);
    Assert.assertEquals(9, testHandler.getPageMetadataList().size());
    Assert.assertEquals(16, testHandler.getRevisionMetadataList().size());
    Assert.assertEquals(16, testHandler.getRevisionTextsList().size());
  }

  private InputSource getInputSourceFromFile(String file) {
    InputStream inputStream = this.getClass().getResourceAsStream(file);
    InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
    return new InputSource(inputStreamReader);
  }
}
