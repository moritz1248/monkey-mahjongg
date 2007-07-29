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

import com.jme.bounding.BoundingBox;
import com.jme.image.Texture;
import com.jme.input.KeyInput;
import com.jme.input.controls.GameControl;
import com.jme.input.controls.GameControlManager;
import com.jme.input.controls.binding.KeyboardBinding;
import com.jme.light.PointLight;
import com.jme.math.FastMath;
import com.jme.math.Quaternion;
import com.jme.math.Vector3f;
import com.jme.renderer.Camera;
import com.jme.renderer.ColorRGBA;
import com.jme.scene.CameraNode;
import com.jme.scene.Node;
import com.jme.scene.SharedMesh;
import com.jme.scene.Spatial;
import com.jme.scene.shape.Box;
import com.jme.scene.state.LightState;
import com.jme.scene.state.MaterialState;
import com.jme.scene.state.TextureState;
import com.jme.system.DisplaySystem;
import com.jme.util.TextureManager;
import com.jmex.game.state.BasicGameState;
import java.nio.FloatBuffer;
import jmetest.monkeymahjongg.Main;

/**
 *
 * @author Pirx
 */
public class MahjonggGameState extends BasicGameState {

    private static float[] picture = new float[]
       {0.7f,0,0,0,0,1,0.7f,1};
    private static float[] border = new float[]
       {1,0,0.7f,0,0.7f,1,1,1};
    
    private float dx = 3.5f;
    private float dy = 5f;
    private float dz = 2f;
    
    private Node cameraRotationNode;
    private Node cameraDistanceNode;
    
    public MahjonggGameState() {
        super("mahjongg");
        addController();
    }
    
    @Override
    public void setActive(boolean active) {
        if (active) {
            init(Main.getLevel());
        }
        super.setActive(active);
    }
    
    public void init(Level level) {
        rootNode.detachAllChildren();
        rootNode.attachChild(cameraRotationNode);
        initLight();
        
        Box box = new Box("box", new Vector3f(), new Vector3f(2*dx, 2*dy, dz));
        FloatBuffer fb = box.getTextureBuffer(0, 0);
        fb.rewind();
        fb.put(picture);
        fb.put(border);
        fb.put(picture);
        fb.put(border);
        fb.put(border);
        fb.put(border);
        for (int x = 0; x < level.getWidht(); x++) {
            for (int y = 0; y < level.getHeight(); y++) {
                for (int z = 0; z < level.getLayers(); z++) {
                    if (level.isTile(x,y,z)) {
                       int tileId = level.getTile(x, y, z);
                       SharedMesh tile = new SharedMesh("tile", box);
                       tile.setUserData("tile", new TileData(x,y,z,tileId));
                       setState(tile, tileId);
                       rootNode.attachChild(tile);
                       tile.setLocalTranslation(new Vector3f(
                               dx * (x-level.getWidht()/2f) - dx / 2,
                               dy * (y-level.getHeight()/2f) - dy / 2, 
                               dz * z));
                       tile.setModelBound(new BoundingBox());
                       tile.updateModelBound();
                       tile.updateRenderState();
                    }        
                }
            }
        }        
        
        
    }

    private void setState(Spatial tile, int tileId) {
        /*tileId /= 4;
        int r = tileId % 3;
        tileId /= 3;
        int g = tileId % 3;
        tileId /= 3;
        int b = tileId % 4;
        ColorRGBA color = new ColorRGBA(r * 0.5f, g * 0.5f, b * 0.33f, 1f);
        MaterialState ms = DisplaySystem.getDisplaySystem().getRenderer().createMaterialState();
        ms.setEmissive(color);
        tile.setRenderState(ms);*/
        MaterialState ms = DisplaySystem.getDisplaySystem().getRenderer().createMaterialState();
        ms.setEmissive(ColorRGBA.white);
        tile.setRenderState(ms);
        TextureState ts = DisplaySystem.getDisplaySystem().getRenderer().createTextureState();
        Texture t = TextureManager.loadTexture(BasicGameState.class.getClassLoader().getResource(
            "jmetest/monkeymahjongg/images/test.png"), Texture.MM_LINEAR_LINEAR, Texture.FM_LINEAR); 
        ts.setTexture(t);
        tile.setRenderState(ts);

    }
    
    private void initLight() {
        PointLight light = new PointLight();
        light.setDiffuse( new ColorRGBA( 0.55f, 0.55f, 0.55f, 1.0f ) );
        light.setAmbient( new ColorRGBA( 0.05f, 0.05f, 0.05f, 1.0f ) );
        light.setLocation( new Vector3f( 100, 100, 100 ) );
        light.setEnabled( true );

        /** Attach the light to a lightState and the lightState to rootNode. */
        LightState lightState = DisplaySystem.getDisplaySystem().getRenderer().createLightState();
        lightState.setEnabled( true );
        lightState.attach( light );
        rootNode.setRenderState( lightState );         
        rootNode.updateRenderState();
    }
    
    public void addController() {
        GameControlManager manager = new GameControlManager();
        Camera camera = DisplaySystem.getDisplaySystem().getRenderer().getCamera();
        cameraRotationNode = new Node("camRotation");
        cameraDistanceNode = new CameraNode("camDistance", camera);
        cameraRotationNode.attachChild(cameraDistanceNode);
        cameraDistanceNode.setLocalTranslation(new Vector3f(0, 0, 100f));
        cameraDistanceNode.setLocalRotation(
                new Quaternion().fromAngleNormalAxis(FastMath.PI, new Vector3f(0,1,0)));
        
        GameControl left = manager.addControl("left");
        left.addBinding(new KeyboardBinding(KeyInput.KEY_LEFT));
        GameControl right = manager.addControl("right");
        right.addBinding(new KeyboardBinding(KeyInput.KEY_RIGHT));
        GameControl up = manager.addControl("up");
        up.addBinding(new KeyboardBinding(KeyInput.KEY_UP));
        GameControl down = manager.addControl("down");
        down.addBinding(new KeyboardBinding(KeyInput.KEY_DOWN));
        GameControl forward = manager.addControl("forward");
        forward.addBinding(new KeyboardBinding(KeyInput.KEY_PGUP));
        GameControl backward = manager.addControl("backward");
        backward.addBinding(new KeyboardBinding(KeyInput.KEY_PGDN));
        
        rootNode.addController(new CameraController(
                left, right, up, down, forward, backward,
                cameraRotationNode, cameraDistanceNode));
    }
}