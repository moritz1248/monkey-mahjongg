/*
 * TileViewer.java
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


package jmetest.monkeymahjongg.playground;

import com.jme.app.SimpleGame;
import com.jme.image.Image;
import com.jme.image.Texture;
import com.jme.input.KeyInput;
import com.jme.input.controls.GameControl;
import com.jme.input.controls.GameControlManager;
import com.jme.input.controls.binding.KeyboardBinding;
import com.jme.math.Vector3f;
import com.jme.renderer.ColorRGBA;
import com.jme.scene.Spatial;
import com.jme.scene.shape.Box;
import com.jme.scene.state.MaterialState;
import com.jme.scene.state.TextureState;
import com.jme.system.DisplaySystem;
import com.jme.util.TextureManager;
import java.nio.FloatBuffer;

import jmetest.monkeymahjongg.game.MahjonggGameState;

/**
 *
 * @author Pirx
 */
public class TileViewer extends SimpleGame {
	private static float[] picture = new float[] { 0.7f, 0, 0, 0, 0, 0.5f, 0.7f, 0.5f };
	private static float[] border = new float[] { 1, 0, 0.7f, 0, 0.7f, 1, 1, 1 };

 	private float dx = 3.5f;
	private float dy = 5f;
	private float dz = 3f;
    
    private GameControl plus;
    private GameControl minus;
    
    private int id = 200;
    
    private boolean released = true;
    
    protected void simpleInitGame() {
        Spatial tile = getTile();
        rootNode.attachChild(tile);
        setState(tile,id);
        
        GameControlManager manager = new GameControlManager();
        plus = manager.addControl("plus");
        plus.addBinding(new KeyboardBinding(KeyInput.KEY_PGUP));
        minus = manager.addControl("minus");
        minus.addBinding(new KeyboardBinding(KeyInput.KEY_PGDN));
    }
    
    private Spatial getTile() {
 		Box box = new Box("box", new Vector3f(), new Vector3f(2 * dx, 2 * dy,
				dz));
                BoxRemapper remapper = new BoxRemapper();
                remapper.setSide(BoxRemapper.ALL, border);
                remapper.setSide(BoxRemapper.FRONT + BoxRemapper.BACK, picture);
                remapper.remap(box);
       
        return box;
    }

    private void setState(Spatial tile, int tileId) {
        
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
        } else if (tileId < 144) {
            tex += "season" + (tileId - 140 + 1);
        } else {
            tex += "test";
        }
        tex += ".png";

        MaterialState ms = DisplaySystem.getDisplaySystem().getRenderer().createMaterialState();
        ms.setEmissive(ColorRGBA.white);
        tile.setRenderState(ms);
        TextureState ts = DisplaySystem.getDisplaySystem().getRenderer().createTextureState();
        Texture t = TextureManager.loadTexture(MahjonggGameState.class.getClassLoader().getResource(tex), Texture.MM_LINEAR_LINEAR, Texture.FM_LINEAR, Image.GUESS_FORMAT_NO_S3TC, ts.getMaxAnisotropic(), true);
        ts.setTexture(t);
        tile.setRenderState(ts);
        tile.updateRenderState();
        
        System.err.println(id);
    }
    
    @Override
    protected void simpleUpdate() {
        if (plus.getValue() == 0 && minus.getValue() == 0) {
            released = true;
        }
        
        if (released && (plus.getValue() > 0 || minus.getValue() > 0)) {
            int delta = (int) (plus.getValue() - minus.getValue());
            
                id += id > 136 ? delta : 4 * delta;
                id = (id + 144) % 144;
            setState(rootNode.getChild(0), id);
            released = false;
        }
    }
    
    public static void main(String... args) {
        TileViewer app = new TileViewer();
        app.setDialogBehaviour(ALWAYS_SHOW_PROPS_DIALOG);
        app.start();
    }
}    