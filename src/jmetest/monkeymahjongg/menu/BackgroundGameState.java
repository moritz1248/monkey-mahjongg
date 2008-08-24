/*
 * BackgroundGameState.java
 *
 * Created on Jul 9, 2007, 6:33:10 PM
 *
 * Copyright (c) 2007 Daniel Gronau
 *
 * This file is part of Monkey Mahjongg.
 *
 * Monkey Mahjongg is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 *
 * Monkey Mahjongg is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 *
 */

package jmetest.monkeymahjongg.menu;

import com.jme.bounding.BoundingBox;
import com.jme.image.Texture;
import com.jme.light.PointLight;
import com.jme.math.FastMath;
import com.jme.math.Quaternion;
import com.jme.math.Vector3f;
import com.jme.renderer.Camera;
import com.jme.renderer.ColorRGBA;
import com.jme.scene.Controller;
import com.jme.scene.Spatial;
import com.jme.scene.shape.Box;
import com.jme.scene.state.LightState;
import com.jme.scene.state.MaterialState;
import com.jme.scene.state.TextureState;
import com.jme.system.DisplaySystem;
import com.jme.util.TextureManager;
import com.jmex.game.state.BasicGameState;

/**
 *
 * @author DGronau
 */
public class BackgroundGameState extends BasicGameState {

    private final Box box;
    private final Vector3f cameraLocation;
    private final Vector3f cameraDirection;
    
    public BackgroundGameState(String texture) {
        super("background");
        box = new Box("my box", new Vector3f(0, 0, 0), 5, 5, 5);
        
        final MaterialState ms = DisplaySystem.getDisplaySystem().getRenderer().createMaterialState();
        ms.setEmissive(new ColorRGBA(0.5f, 0.5f, 0.5f, 1));
        box.setRenderState(ms);
        final TextureState ts = DisplaySystem.getDisplaySystem().getRenderer().createTextureState();
        final Texture t = TextureManager.loadTexture(
                BasicGameState.class.getClassLoader().getResource(texture),
                Texture.MinificationFilter.BilinearNoMipMaps,
                Texture.MagnificationFilter.Bilinear);
        ts.setTexture(t);
        box.setRenderState(ts);
        box.setModelBound(new BoundingBox());
        box.updateModelBound();
        getRootNode().attachChild(box);

        final PointLight light = new PointLight();
        light.setDiffuse(new ColorRGBA(0.75f, 0.75f, 0.75f, 0.75f));
        light.setAmbient(new ColorRGBA(0.5f, 0.5f, 0.5f, 1.0f));
        light.setLocation(new Vector3f(100, 100, 100));
        light.setEnabled(true);
        
        final LightState lightState = DisplaySystem.getDisplaySystem().getRenderer().createLightState();
        lightState.setEnabled(true);
        lightState.attach(light);
        getRootNode().setRenderState(lightState);
        
        getRootNode().updateRenderState();
        getRootNode().addController(new RotController(box, new Vector3f(1, 1, 0.5f), 25));
        
        final Camera camera = DisplaySystem.getDisplaySystem().getRenderer().getCamera();
        cameraLocation = camera.getLocation();
        cameraDirection = camera.getDirection();
    }
    
    @Override
    public void setActive(boolean active) {
        super.setActive(active);
        if (active) {
            final Camera camera = DisplaySystem.getDisplaySystem().getRenderer().getCamera();
            camera.setLocation(cameraLocation);
            camera.setDirection(cameraDirection);
        }
    }
    
    private static class RotController extends Controller {
		private static final long serialVersionUID = 1L;

        private float angle = 0;
        private final Spatial s;
        private final int speed;
        private final Quaternion rotQuat = new Quaternion();
        private final Vector3f axis;

        public RotController(Spatial s, Vector3f axis, int speed) {
            this.s = s;
            this.axis = axis.normalizeLocal();
            this.speed = speed;
        }

        public void update(float time) {
            if (time < 0.1) {
                angle += time * speed;
                angle = (angle > 360) ? 0 : angle;
            }
            rotQuat.fromAngleNormalAxis(angle * FastMath.DEG_TO_RAD, axis);
            s.setLocalRotation(rotQuat);
        }
    }
}
