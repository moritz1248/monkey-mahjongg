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

/**
 * 
 * @author Pirx
 */
class CameraController extends MahjonggGameController {
	private static final long serialVersionUID = 1L;

	final static float MIN_ANGLE = 1.2f;
	final static float MIN_DISTANCE = 35f;
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

	public CameraController(MahjonggGameState mahjonggGameState) {
		super( mahjonggGameState );
		
		GameControlManager manager = mahjonggGameState.getGameControlManager();
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
		mahjonggGameState.getCameraRotationNode().setLocalRotation(new Quaternion(new float[] { vAngle,
				hAngle, 0f }));

		float newDist = distance + 20 * SPEED * time
				* (backward.getValue() - forward.getValue());
		if (MIN_DISTANCE < newDist && newDist < MAX_DISTANCE) {
			distance = newDist;
		}
		mahjonggGameState.getCameraDistanceNode().setLocalTranslation(0, 0, distance);
	}

}