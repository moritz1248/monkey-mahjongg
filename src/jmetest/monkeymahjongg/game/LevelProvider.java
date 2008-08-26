/*
 * LevelProvider.java
 *
 *  Copyright (c) 2007 Daniel Gronau
 *
 *  This file is part of Monkey Mahjongg.
 *
 *  Monkey Mahjongg is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  Monkey Mahjongg is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>
 *
 *
 */

package jmetest.monkeymahjongg.game;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import static java.util.logging.Level.*; 

/**
 *
 * @author Gronau
 */
public final class LevelProvider {
  
    private final static SAXParserFactory PARSER_FACTORY = SAXParserFactory.newInstance();

    
    private LevelProvider() {}
    
    public static Level load(String fileName) {
        try {
            XMLReader xmlReader = PARSER_FACTORY.newSAXParser().getXMLReader();
            LevelHandler handler = new LevelHandler();
            xmlReader.setContentHandler(handler);
            InputSource is = new InputSource(new java.io.FileInputStream(fileName));
            xmlReader.parse(is);
            return new Level(handler.name, 
                             handler.width, 
                             handler.height, 
                             handler.layers, 
                             handler.tiles);
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(LevelProvider.class.getName()).log(SEVERE, null, ex);
            throw new RuntimeException(ex);
        } catch (IOException ex) {
            Logger.getLogger(LevelProvider.class.getName()).log(SEVERE, null, ex);
            throw new RuntimeException(ex);
        } catch (SAXException ex) {
            Logger.getLogger(LevelProvider.class.getName()).log(SEVERE, null, ex);
            throw new RuntimeException(ex);
        }      
    }   
    
    
    private static class LevelHandler extends DefaultHandler {

        private final List<Integer> list = new ArrayList<Integer>();

        private int line = 0;
        private String name;
        private int width;
        private int height;
        private int layers;
        private Map<Coordinate, TileData> tiles = new HashMap<Coordinate, TileData>();

        public LevelHandler() {
            for (int i = 0; i < 144; i++) {
                list.add(i);
            }
            Collections.shuffle(list);
        }

        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes) {
            if ("mahjongg".equals(qName)) {
                name = attributes.getValue("name");
                width = Integer.valueOf(attributes.getValue("width"));
                height = Integer.valueOf(attributes.getValue("height"));
                layers = Integer.valueOf(attributes.getValue("layers"));
            } else if ("line".equals(qName)) {
                final String data = attributes.getValue("data");
                for (int x = 0; x < data.length(); x++) {
                    final char c = data.charAt(x);
                    if ('1' <= c && c <= '9') {
                        final int n = c - '1';
                        for (int z = 0; z <= n; z++) {
                            Coordinate co = Coordinate.at(x, line, z);
                            if (! tiles.containsKey(co.add(-1,-1, 0))
                             && ! tiles.containsKey(co.add( 0,-1, 0))
                             && ! tiles.containsKey(co.add( 1,-1, 0)) 
                             && ! tiles.containsKey(co.add(-1, 0, 0))) {
                                tiles.put(co, new TileData(x, line, z, list.remove(0)));
                            }
                        }
                    }
                }
                line++;
            }
        }
    }
}
