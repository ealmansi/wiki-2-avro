package wikipedia;

import org.junit.Test;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Wiki2AvroWikilinksExtractorTest
 */
public class Wiki2AvroWikilinksExtractorTest {
  @Test
  public void test1() throws Wiki2AvroException, IOException {
    String testInputFile = "/wikilink_extractor_test_input.txt";
    String testOutputFile = "/wikilink_extractor_test_output.txt";
    assertEquals(getExpectedWikilinks(testOutputFile),
                    getActualWikilinks(testInputFile));
  }

  private List<CharSequence> getActualWikilinks(String testInputFile) throws IOException {
    String content = readTestFile(testInputFile);
    return Wiki2AvroWikilinksExtractor.extractWikilinks(content);
  }

  private List<CharSequence> getExpectedWikilinks(String testOutputFile) throws IOException {
    CharSequence[] lines = readTestFile(testOutputFile).split("\\r?\\n");
    return Arrays.asList(lines);
  }

  String readTestFile(String testFile) throws IOException {
    URL testFileURL = this.getClass().getResource(testFile);
    Path testFilePath = Paths.get(testFileURL.getFile());
    byte[] encodedContent = Files.readAllBytes(testFilePath);
    return new String(encodedContent, StandardCharsets.UTF_8);
  }
}
