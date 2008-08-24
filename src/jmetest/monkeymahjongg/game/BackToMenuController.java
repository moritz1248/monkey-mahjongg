/*
 *  BackToMenuController.java
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

import jmetest.monkeymahjongg.Main;

import com.jme.input.KeyInput;
import com.jme.input.controls.GameControl;
import com.jme.input.controls.binding.KeyboardBinding;

public class BackToMenuController extends MahjonggGameController {

    private final static long serialVersionUID = 1L;
    private final GameControl control;

    public BackToMenuController(MahjonggGameState mahjonggGameState) {
        super(mahjonggGameState);
        control = mahjonggGameState.getGameControlManager().addControl("backtomain");
        control.addBinding(new KeyboardBinding(KeyInput.KEY_ESCAPE));
    }

    @Override
    public void update(float time) {
        if (control.getValue() > 0) {
            Main.stopLevel();
        }
    }
}
