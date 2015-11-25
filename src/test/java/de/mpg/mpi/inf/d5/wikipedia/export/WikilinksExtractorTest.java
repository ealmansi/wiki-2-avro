package de.mpg.mpi.inf.d5.wikipedia.export;

import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

/**
 * WikilinksExtractorTest
 */
public class WikilinksExtractorTest {
  @Test
  public void testWikilinksExtractor() throws WikipediaExportException, IOException {
    String testInputFile = "/wikilink_extractor_test_input.txt";
    String testOutputFile = "/wikilink_extractor_test_output.txt";
    Assert.assertEquals(getExpectedWikilinks(testOutputFile),
                           getActualWikilinks(testInputFile));
  }

  private List<CharSequence> getActualWikilinks(String testInputFile) throws IOException {
    String content = readTestFile(testInputFile);
    return WikilinksExtractor.extractWikilinks(content);
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
