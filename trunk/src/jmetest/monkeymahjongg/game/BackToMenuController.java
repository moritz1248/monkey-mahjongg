package jmetest.monkeymahjongg.game;

import jmetest.monkeymahjongg.Main;

import com.jme.input.KeyInput;
import com.jme.input.controls.GameControl;
import com.jme.input.controls.binding.KeyboardBinding;

public class BackToMenuController extends MahjonggGameController {
    
	private static final long serialVersionUID = 1L;
	private GameControl control;

	public BackToMenuController(MahjonggGameState mahjonggGameState) {
		super(mahjonggGameState);
		
		control = mahjonggGameState.getGameControlManager().addControl("backtomain");
		control.addBinding(new KeyboardBinding(KeyInput.KEY_ESCAPE));
	}
	
	@Override
	public void update(float time) {
		if( control.getValue() > 0 ) {
			Main.stopLevel();
        }    
	}
}
