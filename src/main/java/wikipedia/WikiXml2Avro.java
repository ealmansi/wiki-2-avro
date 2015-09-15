package wikipedia;

import net.sourceforge.argparse4j.ArgumentParsers;
import net.sourceforge.argparse4j.inf.ArgumentParser;
import net.sourceforge.argparse4j.inf.ArgumentParserException;
import net.sourceforge.argparse4j.inf.Namespace;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.*;

/**
 *
 */
public class WikiXml2Avro {
  private static final ArgumentParser argParser;
  static {
    argParser =
        ArgumentParsers
            .newArgumentParser(WikiXml2Avro.class.getSimpleName().toLowerCase())
            .description("A tool for parsing Wikipedia XML dumps into Avro format.")
            .epilog("Reads XML from standard input, outputs two files:\n" +
                        "\tone file containing avro collection of page metadata for all pages\n" +
                        "\tother file containing avro collection of revision content for all revisions");
    argParser
        .addArgument("-m", "--metadata_output_file")
        .required(true)
        .type(String.class)
        .help("File where the avro collection of page metadata will be stored.");
    argParser
        .addArgument("-c", "--content_output_file")
        .required(true)
        .type(String.class)
        .help("File where the avro collection of revision content will be stored.");
  }

  public static void main(String[] args) throws IOException {
    String metadataOutputFile = null;
    String contentOutputFile = null;
    try {
      Namespace namespace = argParser.parseArgs(args);
      metadataOutputFile = namespace.get("metadata_output_file");
      contentOutputFile = namespace.get("content_output_file");
    } catch (ArgumentParserException e) {
      argParser.handleError(e);
      System.exit(1);
    }

    try {
      SAXParserFactory factory = SAXParserFactory.newInstance();
      SAXParser saxParser = factory.newSAXParser();
      BufferedInputStream bufferedStdin = new BufferedInputStream(System.in);
      Reader stdinReader = new InputStreamReader(bufferedStdin);
      InputSource stdinInputSource = new InputSource(stdinReader);
      DefaultHandler wikiXml2AvroHandler = new WikiXml2AvroHandler(metadataOutputFile, contentOutputFile);
      saxParser.parse(stdinInputSource, wikiXml2AvroHandler);
    } catch (SAXException | ParserConfigurationException | IOException e) {
      throw new IOException("Processing XML failed.", e);
    }
  }
}
