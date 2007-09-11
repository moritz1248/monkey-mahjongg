/*
 * CameraGameState.java
 * 
 * Created on Aug 11, 2007, 1:07:39 PM
 * 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
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

    private Node cameraRotationNode;
    private Node cameraDistanceNode;
    private CameraController cameraController;
    
    public CameraGameState() {
        super("camera");
        initCamera();
    }

    private void initCamera() {
       Camera camera = DisplaySystem.getDisplaySystem().getRenderer().getCamera();
        cameraRotationNode = new Node("camRotation");
        cameraDistanceNode = new CameraNode("camDistance", camera);
        cameraDistanceNode.setLocalRotation(new Quaternion().fromAngleNormalAxis(FastMath.PI, new Vector3f(0, 1, 0)));
        cameraRotationNode.attachChild(cameraDistanceNode);
    
        rootNode.attachChild(cameraRotationNode);
        
        rootNode.addController(cameraController = new CameraController(this));
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
        cameraDistanceNode.setLocalTranslation(new Vector3f(0, 0, 20.0f));
        cameraRotationNode.setLocalRotation(new Quaternion());

    }
    
    public void setMoveable() {
        cameraController.setActive(true);
    }
}
