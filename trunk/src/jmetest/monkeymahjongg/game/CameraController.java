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

import com.jme.input.controls.GameControl;
import com.jme.input.controls.GameControlManager;
import com.jme.input.controls.binding.KeyboardBinding;
import com.jme.scene.Controller;

import static com.jme.input.KeyInput.*;
import static com.jme.math.FastMath.clamp;
import static jmetest.monkeymahjongg.game.CameraController.Direction.*;

/**
 * 
 * @author Pirx
 */
class CameraController extends Controller {

    private static final long serialVersionUID = 1L;
    
    enum Direction {LEFT, RIGHT, UP, DOWN, FORWARD, BACK};
    
    private final static float MIN_ANGLE = 1.2f;
    private final static float MIN_DISTANCE = 35f;
    private final static float MAX_DISTANCE = 150f;
    private final static float SPEED = 2f;
    private final GameControlManager manager = new GameControlManager();
    private final CameraGameState gameState;
    
    private float vAngle = 0;
    private float hAngle = 0;
    private float distance = 100;

    public CameraController(CameraGameState gameState) {
        this.gameState = gameState;

        bind(LEFT, KEY_LEFT);
        bind(RIGHT, KEY_RIGHT);
        bind(UP, KEY_UP);
        bind(DOWN, KEY_DOWN);
        bind(FORWARD, KEY_PGUP, KEY_ADD);
        bind(BACK, KEY_PGDN, KEY_SUBTRACT);
    }
    
    private void bind(Direction direction, int... keys) {
        final GameControl control = manager.addControl(direction.name());
        for (int key : keys) {
          control.addBinding(new KeyboardBinding(key));
        }
    }
    
    private float value(Direction direction) {
        return manager.getControl(direction.name()).getValue();
    }

    public void update(float time) {
        final float way = SPEED * time;
        hAngle = clamp(hAngle + way * (value(RIGHT) - value(LEFT)), -MIN_ANGLE, MIN_ANGLE);
        vAngle = clamp(vAngle + way * (value(DOWN) - value(UP)), -MIN_ANGLE, MIN_ANGLE);
        gameState.getCameraRotationNode().getLocalRotation().fromAngles(vAngle, hAngle, 0f);
        distance = clamp(distance + 20 * way * (value(BACK) - value(FORWARD)),
                MIN_DISTANCE, MAX_DISTANCE);
        gameState.getCameraDistanceNode().setLocalTranslation(0, 0, distance);
    }
}