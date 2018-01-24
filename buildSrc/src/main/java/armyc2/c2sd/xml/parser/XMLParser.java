package armyc2.c2sd.xml.parser;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class XMLParser {

	  /**
	   * from www.androidhive.info/2011/11/android-xml-parsing-tutorial/
	   * @param xml
	   * @return
	   */
	  public static Document getDomElement(String xml)
	  {
		  Document doc = null;
		  DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		  try
		  {
			  DocumentBuilder db = dbf.newDocumentBuilder();
			  InputSource is = new InputSource();
			  is.setCharacterStream(new StringReader(xml));
			  doc = db.parse(is);
		  }
		  catch(ParserConfigurationException | IOException | SAXException e)
		  {
			  return null;
		  }
		  return doc;
	  }
}
