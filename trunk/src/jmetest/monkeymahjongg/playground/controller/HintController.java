package jmetest.monkeymahjongg.playground.controller;

import jmetest.monkeymahjongg.playground.view.MahjonggGameState;

import com.jme.input.KeyInput;
import com.jme.input.controls.GameControl;
import com.jme.input.controls.binding.KeyboardBinding;

public class HintController extends MahjonggGameController {

	private static final long serialVersionUID = 1L;
	private GameControl control;

	public HintController(MahjonggGameState mahjonggGameState) {
		super(mahjonggGameState);
		control = mahjonggGameState.getGameControlManager().addControl("hint");
		control.addBinding(new KeyboardBinding(KeyInput.KEY_H));
	}


	@Override
	public void update(float time) {
		float value = control.getValue();
		if( value > 0 ) {
			mahjonggGameState.hint();
        }    
	}

}
