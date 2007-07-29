/*
 * CameraController.java
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
 *  KanjiTori is distributed in the hope that it will be useful,
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

import com.jme.input.controls.GameControl;
import com.jme.math.Quaternion;
import com.jme.scene.Controller;
import com.jme.scene.Node;

/**
 *
 * @author Pirx
 */
class CameraController extends Controller {
    
    final static float MIN_ANGLE = 1.2f;
    final static float MIN_DISTANCE = 25f;
    final static float MAX_DISTANCE = 150f;
    final static float SPEED = 2f;

    float vAngle = 0;
    float hAngle = 0;
    float distance = 100;
    
    GameControl left;
    GameControl right;
    GameControl up;
    GameControl down;
    GameControl forward;
    GameControl backward;
    
    Node rotationNode;
    Node distanceNode;
    
    public CameraController(GameControl left, GameControl right, GameControl up,
            GameControl down, GameControl forward, GameControl backward,
            Node rotationNode, Node distanceNode) {
        this.left = left;
        this.right = right;
        this.up = up;
        this.down = down;
        this.forward = forward;
        this.backward = backward;
        this.rotationNode = rotationNode;
        this.distanceNode = distanceNode;
    }

    public void update(float time) {
        float newHAngle = hAngle + SPEED * time * (right.getValue() - left.getValue());
        if (-MIN_ANGLE < newHAngle && newHAngle < MIN_ANGLE) {
            hAngle = newHAngle;
        }
        float newVAngle = vAngle + SPEED * time * (down.getValue() - up.getValue());
        if (-MIN_ANGLE < newVAngle && newVAngle < MIN_ANGLE) {
            vAngle = newVAngle;
        }
        rotationNode.setLocalRotation(new Quaternion(new float[]{vAngle, hAngle, 0f}));    
    
        float newDist = distance + 20 * SPEED * time * (backward.getValue() - forward.getValue());
        if (MIN_DISTANCE < newDist && newDist < MAX_DISTANCE ) {
            distance = newDist;
        }
        distanceNode.setLocalTranslation(0, 0, distance);
    }

}