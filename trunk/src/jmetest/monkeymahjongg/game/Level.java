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

import com.jme.renderer.ColorRGBA;
import com.jme.scene.Geometry;
import com.jme.scene.state.MaterialState;
import com.jme.system.DisplaySystem;
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

    private final static int EMPTY = -1;
    
    private String name;
    private int width;
    private int height;
    private int layers;
    private int[][][] tiles;
    
    private Geometry selectedGeometry;

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
    
    private TileData getSelectedTile() {
        return getTileData(selectedGeometry);
    }
    
    private TileData getTileData(Geometry geometry) {
        if (geometry == null) {
            return null;
        }
        Object userData = geometry.getUserData(MahjonggGameState.TILE_USER_DATA);
        return userData instanceof TileData ? (TileData) userData : null;
    }
    
    void picked(Geometry geometry) {
        TileData tileData = getTileData(geometry);
        TileData selectedTile = getSelectedTile();
       if (selectedTile != null && tileData.getTileId() == selectedTile.getTileId()) {  //same tile -> unselect
           System.err.println("unselect " + selectedTile);
           selectedGeometry = null;
           unselect(geometry);
       } else if (! isBlocked(tileData)) {
           if  (selectedGeometry == null) { //new selection
               System.err.println("select " + tileData);
               selectedGeometry = geometry;
               select(geometry);
           } else if ((tileData.getTileId() ^ selectedTile.getTileId()) < 4) {  //matching tiles
               System.err.println("matching new " + tileData + " and selected " + selectedTile);
              //vaporize(geometry);
              //vaporize(selectedGeometry);
               selectedGeometry.removeFromParent();
               geometry.removeFromParent();
               selectedGeometry = null;
               
               tiles[tileData.getX()][tileData.getY()][tileData.getZ()] = EMPTY;
               tiles[selectedTile.getX()][selectedTile.getY()][selectedTile.getZ()] = EMPTY;
           } else {
               System.err.println("not matching");
           }   
       } else {
           System.err.println("blocked");
       }     
    }

	private void setEmissiveColor(Geometry tile, ColorRGBA color) {
        MaterialState ms = DisplaySystem.getDisplaySystem().getRenderer().createMaterialState();
        ms.setEmissive(color);
        tile.setRenderState(ms);
        tile.updateRenderState();
	}

    private void select(Geometry tile) {
        setEmissiveColor(tile, ColorRGBA.yellow);
	}

    private void unselect(Geometry tile) {
        setEmissiveColor(tile, ColorRGBA.white);
	}

	private boolean isBlocked(TileData tileData) {
        int tx = tileData.getX();
        int ty = tileData.getY();
        int tz = tileData.getZ();

        //check for tiles on top
        for (int x = -1; x <= 1; x++) {
            for (int y = -1; y <= 1; y++) {
                if (isTile(tx + x,ty + y, tz + 1)) {
                    return true;
                }
            }
        }
        //check if left is free
        if (! isTile(tx - 2, ty - 1, tz) && ! isTile(tx - 2, ty, tz) 
                && ! isTile(tx - 2, ty + 1, tz)) {
            return false;
        }
        //check if rightis free
        if (! isTile(tx + 2, ty - 1, tz) && ! isTile(tx + 2, ty, tz) 
                && ! isTile(tx + 2, ty + 1, tz)) {
            return false;
        }
        //blocked from both sides
        return true;
    }    

    public boolean isTile(int x, int y, int z) {
        if (x < 0 || y < 0 || z < 0 || x >= width || y >= height || z >= layers) {
            return false;
        } else {
            return tiles[x][y][z] != EMPTY;
        }
    }

    public int getTile(int x, int y, int z) {
        if (x < 0 || y < 0 || z < 0 || x >= width || y >= height || z >= layers) {
            return EMPTY;
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

    public int getWidth() {
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
                        Arrays.fill(tiles[x][y], EMPTY);
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