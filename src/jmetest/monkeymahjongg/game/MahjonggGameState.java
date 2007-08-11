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
import com.jme.scene.shape.RoundedBox;
import com.jme.scene.state.CullState;
import com.jme.scene.state.LightState;
import com.jme.scene.state.MaterialState;
import com.jme.scene.state.TextureState;
import com.jme.system.DisplaySystem;
import com.jme.util.TextureManager;
import com.jmex.game.state.BasicGameState;

/**
 * 
 * @author Pirx
 */
public class MahjonggGameState extends BasicGameState {
    
    public static String TILE_USER_DATA = "tile";

	//private static float[] picture = new float[] { 0.7f, 0, 0, 0, 0, 1, 0.7f, 1 };
	//private static float[] border = new float[] { 1, 0, 0.7f, 0, 0.7f, 1, 1, 1 };

    private static float BORDER = 0.2f;
    private static float SLOPE = 0.1f;
    private static float RATIO = 0.5f * BORDER / (1 + SLOPE);
    private static float TEX = 0.7f;

	private static float[] picture = new float[] { 
        TEX, 0,
        0, 0, 
        TEX, 1, 
        0, 1,
        TEX*(1-RATIO), RATIO,
        TEX*RATIO, RATIO,
        TEX*(1-RATIO), 1-RATIO,
        TEX*RATIO, 1-RATIO};
    
	private static float[] border = new float[] { 
        1, 0,
        TEX, 0, 
        1, 1,
        TEX, 1, 
        1 - (1-TEX)*RATIO, RATIO,
        TEX + (1-TEX)*RATIO, RATIO,
        1 - (1-TEX)*RATIO, 1-RATIO,
        TEX + (1-TEX)*RATIO, 1-RATIO};
    
	private float dx = 3.5f;
	private float dy = 5f;
	private float dz = 1.5f;

	private Node cameraRotationNode;
	private Node cameraDistanceNode;
	private GameControlManager gameControlManager;

	private Level level;

	public MahjonggGameState() {
		super("mahjongg");
	}

	@Override
	public void setActive(boolean active) {
		if (active) {
			init(Main.getLevel());
		} else {
                    rootNode.detachAllChildren();
                    rootNode.removeController(2);
                    rootNode.removeController(1);
                    rootNode.removeController(0);
                }
		super.setActive(active);
	}

	public void init(Level level) {
		Camera camera = DisplaySystem.getDisplaySystem().getRenderer()
				.getCamera();
		cameraRotationNode = new Node("camRotation");
		cameraDistanceNode = new CameraNode("camDistance", camera);
		cameraRotationNode.attachChild(cameraDistanceNode);
		cameraDistanceNode.setLocalTranslation(new Vector3f(0, 0, 100f));
		cameraDistanceNode.setLocalRotation(new Quaternion()
				.fromAngleNormalAxis(FastMath.PI, new Vector3f(0, 1, 0)));

		addControllers();
            
                this.level = level;
		rootNode.detachAllChildren();
		rootNode.attachChild(cameraRotationNode);
		initLight();

		/*Box box = new Box("box", new Vector3f(), new Vector3f(2 * dx, 2 * dy,
				dz));
		FloatBuffer fb = box.getTextureBuffer(0, 0);
		fb.rewind();
		fb.put(picture);
		fb.put(border);
		fb.put(picture);
		fb.put(border);
		fb.put(border);
		fb.put(border);
		*/
        
        Vector3f size = new Vector3f(dx, dy, dz);
        RoundedBox box = new RoundedBox("box", size, size.mult(BORDER), size.mult(SLOPE));  
		FloatBuffer fb = box.getTextureBuffer(0, 0);
		fb.rewind();
		fb.put(border);
		fb.put(picture);
		fb.put(border);
		fb.put(picture);
		fb.put(border);
		fb.put(border);
        
		for (int x = 0; x < level.getWidth(); x++) {
			for (int y = 0; y < level.getHeight(); y++) {
				for (int z = 0; z < level.getLayers(); z++) {
					if (level.isTile(x, y, z) ) {
						int tileId = level.getTile(x, y, z);
						SharedMesh tile = new SharedMesh("tile", box);
						tile.setUserData(TILE_USER_DATA, new TileData(x, y, z, tileId));
						setState(tile, tileId);
						rootNode.attachChild(tile);
						Vector3f translation = new Vector3f(
                                dx	* (x - level.getWidth() / 2f) + dx / 2, 
                                dy * (level.getHeight() / 2f - y) - 0.5f * dy, 
                                2 * dz * z);
						tile.setLocalTranslation(translation);
						tile.setModelBound(new BoundingBox());
						tile.updateModelBound();
						tile.updateRenderState();
					}
				}
			}
		}
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
		} else {
			tex += "season" + (tileId - 140 + 1);
		}
		tex += ".png";

		MaterialState ms = DisplaySystem.getDisplaySystem().getRenderer()
				.createMaterialState();
		ms.setEmissive(ColorRGBA.white);
		tile.setRenderState(ms);
		TextureState ts = DisplaySystem.getDisplaySystem().getRenderer()
				.createTextureState();
		Texture t = TextureManager.loadTexture(MahjonggGameState.class
				.getClassLoader().getResource(tex), Texture.MM_LINEAR_LINEAR,
				Texture.FM_LINEAR, Image.GUESS_FORMAT_NO_S3TC, ts
						.getMaxAnisotropic(), true);
		ts.setTexture(t);
		tile.setRenderState(ts);
                
                CullState cs = DisplaySystem.getDisplaySystem().getRenderer()
				.createCullState();
                cs.setCullMode(CullState.CS_BACK);
                tile.setRenderState(cs);
	}

	private void initLight() {
		PointLight light = new PointLight();
		light.setDiffuse(new ColorRGBA(0.55f, 0.55f, 0.55f, 1.0f));
		light.setAmbient(new ColorRGBA(0.05f, 0.05f, 0.05f, 1.0f));
		light.setLocation(new Vector3f(100, 100, 100));
		light.setEnabled(true);

		/** Attach the light to a lightState and the lightState to rootNode. */
		LightState lightState = DisplaySystem.getDisplaySystem().getRenderer()
				.createLightState();
		lightState.setEnabled(true);
		lightState.attach(light);
		rootNode.setRenderState(lightState);
		rootNode.updateRenderState();
	}

	public void addControllers() {
		gameControlManager = new GameControlManager();
		rootNode.addController(new CameraController(this));
		rootNode.addController(new BackToMenuController(this));
		rootNode.addController(new MousePickController(this));
	}

	public Node getCameraRotationNode() {
		return cameraRotationNode;
	}

	public Node getCameraDistanceNode() {
		return cameraDistanceNode;
	}

	public Level getLevel() {
		return level;
	}

	public GameControlManager getGameControlManager() {
		return gameControlManager;
	}
}