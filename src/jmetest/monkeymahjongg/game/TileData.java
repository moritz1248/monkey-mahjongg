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
 *
 * @author Pirx
 */
 class TileData implements Savable {

    final private Coordinate c;
    final private int tileId;
     
    public TileData(int x, int y, int z, int tileId) {
        this.c = Coordinate.at(x, y, z);
        this.tileId = tileId;
    }
    
    public Coordinate getCoordinate() {
        return c;
    }
    
    public int getTileId() {
        return tileId;
    }

    public void write(JMEExporter ex) throws IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void read(JMEImporter im) throws IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Class<?> getClassTag() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    @Override
    public String toString() {
        return String.format("tile %d, %s", tileId, c);
    }
    
    public boolean matches(TileData that) {
        return (this.tileId ^ that.tileId) < 4;
    }

    @Override
    public boolean equals(Object o) {
        return (o instanceof TileData) ? tileId == ((TileData)o).tileId : false;
    }

    @Override
    public int hashCode() {
        return tileId;
    }

}