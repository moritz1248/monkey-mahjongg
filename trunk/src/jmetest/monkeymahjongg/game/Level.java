/*
 * Level.java
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

import com.sun.org.apache.xerces.internal.parsers.SAXParser;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

/**
 *
 * @author Pirx
 */
public class Level {

    private String name;
    private int width;
    private int height;
    private int layers;
    private int[][][] tiles;

    public Level(String fileName) {
        try {
            XMLReader xmlReader = new SAXParser();
            xmlReader.setContentHandler(new LevelHandler());
            InputSource is = new InputSource(new java.io.FileInputStream(fileName));
            xmlReader.parse(is);
        } catch (IOException ex) {
            System.exit(-1);
        } catch (SAXException ex) {
            System.exit(-1);
        }
    }

    public boolean isTile(int x, int y, int z) {
        if (x < 0 || y < 0 || z < 0 || x >= width || y >= height || z >= layers) {
            return false;
        } else {
            return tiles[x][y][z] != -1;
        }
    }

    public int getTile(int x, int y, int z) {
        if (x < 0 || y < 0 || z < 0 || x >= width || y >= height || z >= layers) {
            return -1;
        } else {
            return tiles[x][y][z];
        }
    }

    public void setTile(int x, int y, int z, int n) {
        tiles[x][y][z] = n;
    }

    public String getName() {
        return name;
    }

    public int getWidht() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getLayers() {
        return layers;
    }

    private class LevelHandler extends DefaultHandler {

        private int line = 0;
        private List<Integer> list = new ArrayList<Integer>();

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
                tiles = new int[width][height][layers];
                for (int x = 0; x < width; x++) {
                    for (int y = 0; y < height; y++) {
                        Arrays.fill(tiles[x][y], -1);
                    }
                }
            } else if ("line".equals(qName)) {
                String data = attributes.getValue("data");
                for (int x = 0; x < data.length(); x++) {
                    char c = data.charAt(x);
                    if ('1' <= c && c <= '9') {
                        int n = c - '1';
                        for (int z = 0; z <= n; z++) {
                            if (!isTile(x - 1, line - 1, z) && !isTile(x, line - 1, z) && !isTile(x + 1, line - 1, z) && !isTile(x - 1, line, z)) {
                                setTile(x, line, z, list.remove(0));
                            }
                        }
                    }
                }
                line++;
            }
        }
    }
}