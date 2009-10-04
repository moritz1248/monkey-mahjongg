/*
 * MahjonggGameState.java
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

import java.nio.FloatBuffer;
import jmetest.monkeymahjongg.Main;
import com.jme.bounding.BoundingBox;
import com.jme.image.Image;
import com.jme.image.Texture;
import com.jme.input.controls.GameControlManager;
import com.jme.light.PointLight;
import com.jme.math.Vector3f;
import com.jme.renderer.ColorRGBA;
import com.jme.scene.Node;
import com.jme.scene.SharedMesh;
import com.jme.scene.Spatial;
import com.jme.scene.shape.RoundedBox;
import com.jme.scene.state.CullState;
import com.jme.scene.state.LightState;
import com.jme.scene.state.MaterialState;
import com.jme.scene.state.TextureState;
import com.jme.system.DisplaySystem;
import com.jme.util.TextureManager;
import com.jmex.game.state.BasicGameState;

public class MahjonggGameState extends BasicGameState {

    public static final String TILE_USER_DATA = "tile";
    private static final float BORDER = 0.2f;
    private static final float SLOPE = 0.1f;
    private static final float RATIO = 0.5f * BORDER / (1 + SLOPE);
    private static final float TEX = 0.7f;
    private static final float[] picture = new float[]{
        TEX, 0,
        0, 0,
        TEX, 1,
        0, 1,
        TEX * (1 - RATIO), RATIO,
        TEX * RATIO, RATIO,
        TEX * (1 - RATIO), 1 - RATIO,
        TEX * RATIO, 1 - RATIO
    };
    private static final float[] border = new float[]{
        1, 0,
        TEX, 0,
        1, 1,
        TEX, 1,
        1 - (1 - TEX) * RATIO, RATIO,
        TEX + (1 - TEX) * RATIO, RATIO,
        1 - (1 - TEX) * RATIO, 1 - RATIO,
        TEX + (1 - TEX) * RATIO, 1 - RATIO
    };
    
    private final static float dx = 3.5F;
    private final static float dy = 5.0F;
    private final static float dz = 1.5F;

    private GameControlManager gameControlManager;
    private Level level;

    public MahjonggGameState() {
        super("mahjongg");
    }

    @Override
    public void setActive(boolean active) {
        super.setActive(active);
        if (active) {
            init(Main.getLevel());
        } else {
            Spatial sky = rootNode.getChild("skybox");
            rootNode.detachAllChildren();
            rootNode.attachChild(sky);
            rootNode.removeController(1);
            rootNode.removeController(0);
        }
    }

    private void init(Level level) {
        addControllers();

        this.level = level;
        initLight();

        Vector3f size = new Vector3f(dx, dy, dz);
        RoundedBox box = new RoundedBox("box", size, size.mult(BORDER), size.mult(SLOPE));
        FloatBuffer fb = box.getTextureCoords(0).coords;
        fb.rewind();
        fb.put(border);
        fb.put(picture);
        fb.put(border);
        fb.put(picture);
        fb.put(border);
        fb.put(border);

        for (int x = 0; x < level.width; x++) {
            for (int y = 0; y < level.height; y++) {
                for (int z = 0; z < level.layers; z++) {
                    Coordinate c = Coordinate.at(x,y,z);
                    if (level.isTile(c)) {
                        SharedMesh tile = new SharedMesh("tile", box);
                        TileData td = level.getTile(c);
                        tile.setUserData(TILE_USER_DATA, td);
                        setState(tile, td);
                        rootNode.attachChild(tile);
                        Vector3f translation = new Vector3f(
                                dx * (x - level.width / 2f) + dx / 2,
                                dy * (level.height / 2f - y) - 0.5f * dy,
                                2 * dz * z);
                        tile.setLocalTranslation(translation);
                        tile.setModelBound(new BoundingBox());
                        tile.updateModelBound();
                        tile.updateRenderState();
                    }
                }
            }
        }
        rootNode.updateRenderState();
    }

    private void setState(Spatial tile, TileData tileData) {
        int tileId = tileData.getTileId();
        String tex = "jmetest/monkeymahjongg/images/";
        if (tileId < 36) {
            tex += "banana" + ((tileId / 4) + 1);
        } else if (tileId < 72) {
            tex += "numbers" + (((tileId - 36) / 4) + 1);
        } else if (tileId < 108) {
            tex += "coconut" + (((tileId - 72) / 4) + 1);
        } else if (tileId < 124) {
            tex += "winds" + (((tileId - 108) / 4) + 1);
        } else if (tileId < 136) {
            tex += "dragon" + (((tileId - 124) / 4) + 1);
        } else if (tileId < 140) {
            tex += "flower" + (tileId - 136 + 1);
        } else {
            tex += "season" + (tileId - 140 + 1);
        }
        tex += ".png";

        MaterialState ms = DisplaySystem.getDisplaySystem().getRenderer().createMaterialState();
        ms.setEmissive(ColorRGBA.white);
        tile.setRenderState(ms);
        TextureState ts = DisplaySystem.getDisplaySystem().getRenderer().createTextureState();
        Texture t = TextureManager.loadTexture(
                MahjonggGameState.class.getClassLoader().getResource(tex),
                Texture.MinificationFilter.BilinearNoMipMaps,
                Texture.MagnificationFilter.Bilinear,
                Image.Format.GuessNoCompression, ts.getMaxAnisotropic(), true);
        ts.setTexture(t);
        tile.setRenderState(ts);

        CullState cs = DisplaySystem.getDisplaySystem().getRenderer().createCullState();
        cs.setCullFace(CullState.Face.Back);
        tile.setRenderState(cs);
    }

    private void initLight() {
        PointLight light = new PointLight();
        light.setDiffuse(new ColorRGBA(0.55F, 0.55F, 0.55F, 1.0F));
        light.setAmbient(new ColorRGBA(0.05F, 0.05F, 0.05F, 1.0F));
        light.setLocation(new Vector3f(100, 100, 100));
        light.setEnabled(true);

        /** Attach the light to a lightState and the lightState to rootNode. */
        LightState lightState = DisplaySystem.getDisplaySystem().getRenderer().createLightState();
        lightState.setEnabled(true);
        lightState.attach(light);
        rootNode.setRenderState(lightState);
    }

    public void addControllers() {
        gameControlManager = new GameControlManager();
        rootNode.addController(new BackToMenuController(this));
        rootNode.addController(new MousePickController(this));
    }

    public Level getLevel() {
        return level;
    }

    public GameControlManager getGameControlManager() {
        return gameControlManager;
    }

    @Override
    public Node getRootNode() {
        return rootNode;
    }
}