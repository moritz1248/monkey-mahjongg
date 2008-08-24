/*
 *  CameraGameState.java
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

import com.jme.math.FastMath;
import com.jme.math.Quaternion;
import com.jme.math.Vector3f;
import com.jme.renderer.Camera;
import com.jme.scene.CameraNode;
import com.jme.scene.Node;
import com.jme.system.DisplaySystem;
import com.jmex.game.state.BasicGameState;

/**
 *
 * @author DGronau
 */
public class CameraGameState extends BasicGameState {

    private final static Vector3f FIXED_TRANSLATION = new Vector3f(0, 0, 20.0f);
    private final Node cameraRotationNode;
    private final Node cameraDistanceNode;
    private final CameraController cameraController = new CameraController(this);
    
    public CameraGameState() {
        super("camera");
        final Camera camera = DisplaySystem.getDisplaySystem().getRenderer().getCamera();
        cameraRotationNode = new Node("camRotation");
        cameraDistanceNode = new CameraNode("camDistance", camera);
        cameraDistanceNode.setLocalRotation(new Quaternion().fromAngleNormalAxis(FastMath.PI, new Vector3f(0, 1, 0)));
        cameraRotationNode.attachChild(cameraDistanceNode);
    
        rootNode.attachChild(cameraRotationNode);
        rootNode.addController(cameraController);
        rootNode.updateRenderState();
    }
    
    public Node getCameraRotationNode() {
        return cameraRotationNode;
    }

    public Node getCameraDistanceNode() {
        return cameraDistanceNode;
    } 
    
    public void setFixed() {
        cameraController.setActive(false);
        cameraDistanceNode.setLocalTranslation(FIXED_TRANSLATION);
        cameraRotationNode.setLocalRotation(new Quaternion());
    }
    
    public void setMoveable() {
        cameraController.setActive(true);
    }
}
