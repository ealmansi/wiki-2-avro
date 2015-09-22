package wikipedia;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import wikipedia.schemas.PageMetadata;
import wikipedia.schemas.RevisionContent;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;

/**
 * Wiki2AvroXmlParser
 */
public class Wiki2AvroXmlParser {
  public static void parse(InputSource inputSource,
                           Wiki2AvroOutputStream<PageMetadata> metadataOutputStream,
                           Wiki2AvroOutputStream<RevisionContent> contentOutputStream)
      throws Wiki2AvroException {
    try {
      SAXParserFactory factory = SAXParserFactory.newInstance();
      SAXParser saxParser = factory.newSAXParser();
      DefaultHandler wiki2AvroHandler =
          new Wiki2AvroXmlHandler(metadataOutputStream, contentOutputStream);
      saxParser.parse(inputSource, wiki2AvroHandler);
    } catch (SAXException | ParserConfigurationException | IOException e) {
      throw new Wiki2AvroException("Parsing XML to Avro failed.", e);
    }
  }
}
