package de.mpg.mpi.inf.d5.wikipedia.export;

import net.sourceforge.argparse4j.ArgumentParsers;
import net.sourceforge.argparse4j.inf.ArgumentParser;
import net.sourceforge.argparse4j.inf.ArgumentParserException;
import net.sourceforge.argparse4j.inf.Namespace;
import org.xml.sax.InputSource;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

/**
 * AvroCurrentExporter
 */
public class AvroCurrentExporter {
  private static final ArgumentParser argParser;

  static {
    argParser =
        ArgumentParsers
            .newArgumentParser(AvroCurrentExporter.class.getSimpleName().toLowerCase())
            .description("A tool for parsing Wikipedia XML dumps into Avro format.")
            .epilog("Reads XML from standard input; outputs two files:\n" +
                        "\tfile with an avro collection of page metadata for all current pages\n" +
                        "\tfile with an avro collection of wikilinks for all current pages");
    argParser
        .addArgument("-p", "--page-metadata-output-file")
        .required(true)
        .type(String.class)
        .help("File where the avro collection of current page metadata will be stored.");
    argParser
        .addArgument("-w", "--wikilinks-output-file")
        .required(true)
        .type(String.class)
        .help("File where the avro collection of wikilinks will be stored.");
  }

  public static void main(String[] args) throws WikipediaExportException, IOException {
    // Read command-line arguments.
    String pageMetadataOutputFile = null;
    String wikilinksOutputFile = null;
    try {
      Namespace namespace = argParser.parseArgs(args);
      pageMetadataOutputFile = namespace.get("page_metadata_output_file");
      wikilinksOutputFile = namespace.get("wikilinks_output_file");
    } catch (ArgumentParserException e) {
      argParser.handleError(e);
      System.exit(1);
    }

    // Generate input source from stdin.
    BufferedInputStream stdinBufferedInputStream = new BufferedInputStream(System.in);
    Reader stdinReader = new InputStreamReader(stdinBufferedInputStream);
    InputSource stdinInputSource = new InputSource(stdinReader);

    // Generate avro export handler and run parser.
    WikipediaExportParser.parse(stdinInputSource,
                                   new AvroCurrentExportHandler(pageMetadataOutputFile,
                                                                wikilinksOutputFile));
  }
}
