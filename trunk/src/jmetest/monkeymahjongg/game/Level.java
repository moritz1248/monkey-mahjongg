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
import java.util.Map;
import java.util.logging.Logger;

/**
 *
 * @author Pirx
 */
public class Level {

    public final String name;
    public final int width;
    public final int height;
    public final int layers;
    private final Map<Coordinate, TileData> tiles;
    private Geometry selectedGeometry;

    Level(String name, int width, int height, int layers, Map<Coordinate, TileData> tiles) {
        this.name = name;
        this.width = width;
        this.height =height;
        this.layers = layers;
        this.tiles = tiles;
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
        final TileData tileData = getTileData(geometry);
        if (tileData == null) { //picked something else
            return;
        }
        final TileData selectedTile = getSelectedTile();
        if (tileData.equals(selectedTile)) {  //same tile -> unselect
            System.err.println("unselect " + selectedTile);
            selectedGeometry = null;
            unselect(geometry);
        } else if (!isBlocked(tileData)) {
            if (selectedGeometry == null) { //new selection
                Logger.getLogger(Level.class.getName()).info("selected " + tileData);
                selectedGeometry = geometry;
                select(geometry);
            } else if (tileData.matches(selectedTile)) { 
                Logger.getLogger(Level.class.getName()).info("matching new " + tileData + " and selected " + selectedTile);
                //vaporize(geometry);
                //vaporize(selectedGeometry);
                selectedGeometry.removeFromParent();
                geometry.removeFromParent();
                selectedGeometry = null;

                tiles.remove(tileData.getCoordinate());
                tiles.remove(selectedTile.getCoordinate());
            } else {
                Logger.getLogger(Level.class.getName()).info("tiles not matching");
            }
        } else {
            Logger.getLogger(Level.class.getName()).info("tile blocked");
        }
    }

    private void setEmissiveColor(Geometry tile, ColorRGBA color) {
        final MaterialState ms = DisplaySystem.getDisplaySystem().getRenderer().createMaterialState();
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
        Coordinate c = tileData.getCoordinate();

        //check for tiles on top
        for (int x = -1; x <= 1; x++) {
            for (int y = -1; y <= 1; y++) {
                if (isTile(c.add(x,y,1))) {
                    return true;
                }
            }
        }
        //blocked from both sides??
        return sideBlocked(c.add(- 2, 0, 0)) && 
               sideBlocked(c.add(2, 0, 0));
    }

    private boolean sideBlocked(Coordinate c) {
        return isTile(c) 
            || isTile(c.add(0,1,0))
            || isTile(c.add(0,-1,0));
    }

    boolean isTile(Coordinate c) {
        return tiles.containsKey(c);
    }

    TileData getTile(Coordinate c) {
        return tiles.get(c);
    }

}