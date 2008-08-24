/*
 * MousePickController.java
 *
 *  Copyright (c) 2007 Daniel Gronau
 *
 *  This file is part of KanjiTori.
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

import com.jme.input.MouseInput;
import com.jme.input.controls.GameControl;
import com.jme.input.controls.binding.MouseButtonBinding;
import com.jme.intersection.BoundingPickResults;
import com.jme.intersection.PickResults;
import com.jme.math.Ray;
import com.jme.math.Vector2f;
import com.jme.math.Vector3f;
import com.jme.renderer.ColorRGBA;
import com.jme.scene.Geometry;
import com.jme.scene.batch.GeomBatch;
import com.jme.scene.state.MaterialState;
import com.jme.system.DisplaySystem;

/**
 *
 * @author Pirx
 */
public class MousePickController extends MahjonggGameController {

    private GameControl pick;

    private PickResults pr;
    private boolean released = true;

    public MousePickController(MahjonggGameState mahjonggGameState) {
         super(mahjonggGameState);
         pick = mahjonggGameState.getGameControlManager().addControl("pick");
         pick.addBinding(new MouseButtonBinding(0));

         pr = new BoundingPickResults();
         pr.setCheckDistance(true);
    }

    public void update(float time) {
        if (pick.getValue() == 0) {
            released = true;
        }

        if (released && pick.getValue() > 0) {
            released = false;
            MouseInput mouseInput = MouseInput.get();
            Vector2f screenPos = new Vector2f(mouseInput.getXAbsolute(),
                                              mouseInput.getYAbsolute());
			Vector3f worldCoords0 = DisplaySystem.getDisplaySystem().getWorldCoordinates(screenPos, 0);
			Vector3f worldCoords1 = DisplaySystem.getDisplaySystem().getWorldCoordinates(screenPos, 1);
			Ray mouseRay = new Ray(worldCoords0, worldCoords1
					.subtractLocal(worldCoords0).normalizeLocal());
			pr.clear();
            mahjonggGameState.getRootNode().findPick(mouseRay, pr);

            if (pr.getNumber() > 0) {
				GeomBatch gb = pr.getPickData(0).getTargetMesh();
                Geometry tile = gb.getParentGeom();
				mahjonggGameState.getLevel().picked(tile);
			}
        }
    }

}
