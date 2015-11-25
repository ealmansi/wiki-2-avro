package de.mpg.mpi.inf.d5.wikipedia.export;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;

/**
 * WikipediaExportParser
 */
public class WikipediaExportParser {
  public static void parse(InputSource inputSource, WikipediaExportHandler wikipediaExportHandler)
      throws WikipediaExportException {
    try {
      SAXParserFactory factory = SAXParserFactory.newInstance();
      SAXParser saxParser = factory.newSAXParser();
      saxParser.parse(inputSource, wikipediaExportHandler);
    } catch (SAXException | ParserConfigurationException | IOException e) {
      throw new WikipediaExportException("Parsing Wikipedia XML failed.", e);
    }
  }
}
