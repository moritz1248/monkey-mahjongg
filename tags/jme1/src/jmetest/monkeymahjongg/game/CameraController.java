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

import com.jme.input.KeyInput;
import com.jme.input.controls.GameControl;
import com.jme.input.controls.GameControlManager;
import com.jme.input.controls.binding.KeyboardBinding;
import com.jme.math.Quaternion;
import com.jme.scene.Controller;

/**
 * 
 * @author Pirx
 */
class CameraController extends Controller {
	private static final long serialVersionUID = 1L;

	private final static float MIN_ANGLE = 1.2f;
	private final static float MIN_DISTANCE = 35f;
	private final static float MAX_DISTANCE = 150f;
	private final static float SPEED = 2f;

	private float vAngle = 0;
	private float hAngle = 0;
	private float distance = 100;

	private GameControl left;
	private GameControl right;
	private GameControl up;
	private GameControl down;
	private GameControl forward;
	private GameControl backward;
        
        private CameraGameState gameState;

	public CameraController(CameraGameState gameState) {
	         
                this.gameState = gameState;
            
		GameControlManager manager = new GameControlManager();
		left = manager.addControl("left");
		left.addBinding(new KeyboardBinding(KeyInput.KEY_LEFT));
		right = manager.addControl("right");
		right.addBinding(new KeyboardBinding(KeyInput.KEY_RIGHT));
		up = manager.addControl("up");
		up.addBinding(new KeyboardBinding(KeyInput.KEY_UP));
		down = manager.addControl("down");
		down.addBinding(new KeyboardBinding(KeyInput.KEY_DOWN));
		forward = manager.addControl("forward");
		forward.addBinding(new KeyboardBinding(KeyInput.KEY_PGUP));
		backward = manager.addControl("backward");
		backward.addBinding(new KeyboardBinding(KeyInput.KEY_PGDN));
	}


	public void update(float time) {
            
                //System.out.println("rot:" + gameState.getCameraRotationNode().getLocalRotation());
                //System.out.println("trans:" + gameState.getCameraDistanceNode().getLocalTranslation());
            
		float newHAngle = hAngle + SPEED * time
				* (right.getValue() - left.getValue());
		if (-MIN_ANGLE < newHAngle && newHAngle < MIN_ANGLE) {
			hAngle = newHAngle;
		}
		float newVAngle = vAngle + SPEED * time
				* (down.getValue() - up.getValue());
		if (-MIN_ANGLE < newVAngle && newVAngle < MIN_ANGLE) {
			vAngle = newVAngle;
		}
		gameState.getCameraRotationNode().setLocalRotation(new Quaternion(new float[] { vAngle,
				hAngle, 0f }));

		float newDist = distance + 20 * SPEED * time
				* (backward.getValue() - forward.getValue());
		if (MIN_DISTANCE < newDist && newDist < MAX_DISTANCE) {
			distance = newDist;
		}
		gameState.getCameraDistanceNode().setLocalTranslation(0, 0, distance);
	}

}