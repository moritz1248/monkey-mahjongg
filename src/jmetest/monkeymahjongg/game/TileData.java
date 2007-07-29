/*
 * TileData.java
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

import com.jme.util.export.JMEExporter;
import com.jme.util.export.JMEImporter;
import com.jme.util.export.Savable;
import java.io.IOException;

/**
 *
 * @author Pirx
 */
 class TileData implements Savable {

    private int[] data; 
     
    public TileData(int x, int y, int z, int tileId) {
        data = new int[]{x, y, z, tileId};
    }
    
    public int getX() {
        return data[0];
    }

    public int getY() {
        return data[1];
    }
    
    public int getZ() {
        return data[2];
    }
    
    public int getTileId() {
        return data[3];
    }

    public void write(JMEExporter ex) throws IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void read(JMEImporter im) throws IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Class getClassTag() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}