package jmetest.monkeymahjongg.playground.model;

import java.io.IOException;
import java.util.Vector;


import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import com.sun.org.apache.xerces.internal.parsers.SAXParser;

public class XMLLevel {

	private LevelHandler levelHandler;

	public XMLLevel( String fileName )
	{
        try {
            XMLReader xmlReader = new SAXParser();
            levelHandler = new LevelHandler();
			xmlReader.setContentHandler(levelHandler);
            InputSource is = new InputSource(new java.io.FileInputStream(fileName));
            xmlReader.parse(is);
        } catch (IOException ex) {
            System.exit(-1);
        } catch (SAXException ex) {
            System.exit(-1);
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
