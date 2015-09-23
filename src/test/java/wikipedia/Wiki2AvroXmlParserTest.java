package wikipedia;

import org.junit.Test;
import org.xml.sax.InputSource;
import wikipedia.schemas.PageMetadata;
import wikipedia.schemas.RevisionContent;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * wikipedia.Wiki2AvroXmlParserTest
 */
public class Wiki2AvroXmlParserTest {
  @Test
  public void test1() throws Wiki2AvroException {
    String testFile = "/test1.xml";
    List<PageMetadata> pages = new ArrayList<>();
    List<RevisionContent> revisions = new ArrayList<>();
    runTest(testFile, pages, revisions);
    assertEquals(2, pages.size());
    assertEquals(6, revisions.size());
  }

  private void runTest(String testFile, List<PageMetadata> pages, List<RevisionContent> revisions)
      throws Wiki2AvroException {
    InputStream inputStream = this.getClass().getResourceAsStream(testFile);
    InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
    InputSource inputSource = new InputSource(inputStreamReader);
    Wiki2AvroOutputStream<PageMetadata> metadataOutputStream =
        new Wiki2AvroMockOutputStream<>(pages);
    Wiki2AvroOutputStream<RevisionContent> contentOutputStream =
        new Wiki2AvroMockOutputStream<>(revisions);
    Wiki2AvroXmlParser.parse(inputSource, metadataOutputStream, contentOutputStream);
  }
}
