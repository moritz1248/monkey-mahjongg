package jmetest.monkeymahjongg.playground.model;

import java.io.IOException;
import java.util.Vector;


import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

public class XMLLevel {

	private LevelHandler levelHandler;
        private final static SAXParserFactory PARSER_FACTORY = SAXParserFactory.newInstance();

	public XMLLevel( String fileName )
	{
        try {
            XMLReader xmlReader = PARSER_FACTORY.newSAXParser().getXMLReader();
            levelHandler = new LevelHandler();
			xmlReader.setContentHandler(levelHandler);
            InputSource is = new InputSource(new java.io.FileInputStream(fileName));
            xmlReader.parse(is);
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(XMLLevel.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(XMLLevel.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SAXException ex) {
            Logger.getLogger(XMLLevel.class.getName()).log(Level.SEVERE, null, ex);
        }
	}

	public int getWidth() {
		return levelHandler.width;
	}

	public int getHeight() {
		return levelHandler.height;
	}

	public int getDepth() {
		return levelHandler.depth;
	}

	public Vector<String> getLines() {
		return levelHandler.lines;
	}
}
