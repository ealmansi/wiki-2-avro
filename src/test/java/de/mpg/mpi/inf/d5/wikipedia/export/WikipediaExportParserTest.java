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
  public void testWikipediaExportParser() throws WikipediaExportException {
    InputSource inputSource = getInputSourceFromFile("/wikipedia_export_parser_test.xml");
    WikipediaExportTestHandler testHandler = new WikipediaExportTestHandler();
    WikipediaExportParser.parse(inputSource, testHandler);
    Assert.assertEquals(3, testHandler.getPageMetadataList().size());
    Assert.assertEquals(7, testHandler.getRevisionMetadataList().size());
    Assert.assertEquals(28, testHandler.getRevisionWikilinksList().size());
  }

  private InputSource getInputSourceFromFile(String file) {
    InputStream inputStream = this.getClass().getResourceAsStream(file);
    InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
    return new InputSource(inputStreamReader);
  }
}
