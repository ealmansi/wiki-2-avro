package wikipedia;

import net.sourceforge.argparse4j.ArgumentParsers;
import net.sourceforge.argparse4j.inf.ArgumentParser;
import net.sourceforge.argparse4j.inf.ArgumentParserException;
import net.sourceforge.argparse4j.inf.Namespace;
import org.apache.avro.file.CodecFactory;
import org.xml.sax.InputSource;
import wikipedia.schemas.PageMetadata;
import wikipedia.schemas.RevisionContent;

import java.io.*;

/**
 * Wiki2Avro
 */
public class Wiki2Avro {
  private static final ArgumentParser argParser;

  static {
    argParser =
        ArgumentParsers
            .newArgumentParser(Wiki2Avro.class.getSimpleName().toLowerCase())
            .description("A tool for parsing Wikipedia XML dumps into Avro format.")
            .epilog("Reads XML from standard input, outputs two files:\n" +
                        "\tfile containing avro collection of page metadata for all pages\n" +
                        "\tfile containing avro collection of revision content for all revisions");
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

  public static void main(String[] args) throws Wiki2AvroException, IOException {
    // Read command-line arguments.
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

    // Generate input source from stdin.
    BufferedInputStream stdinBufferedInputStream = new BufferedInputStream(System.in);
    Reader stdinReader = new InputStreamReader(stdinBufferedInputStream);
    InputSource stdinInputSource = new InputSource(stdinReader);

    // Generate output streams and parse.
    Wiki2AvroOutputStream<PageMetadata> metadataOutputStream =
        new Wiki2AvroFileOutputStream<>(metadataOutputFile,
                                           PageMetadata.class,
                                           PageMetadata.getClassSchema(),
                                           CodecFactory.bzip2Codec());
    Wiki2AvroOutputStream<RevisionContent> contentOutputStream =
        new Wiki2AvroFileOutputStream<>(contentOutputFile,
                                           RevisionContent.class,
                                           RevisionContent.getClassSchema(),
                                           CodecFactory.bzip2Codec());
    Wiki2AvroXmlParser.parse(stdinInputSource, metadataOutputStream, contentOutputStream);

    // Close streams.
    metadataOutputStream.close();
    contentOutputStream.close();
  }
}
