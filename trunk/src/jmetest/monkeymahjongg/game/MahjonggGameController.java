package jmetest.monkeymahjongg.game;

import com.jme.scene.Controller;

public abstract class MahjonggGameController extends Controller {

	protected MahjonggGameState mahjonggGameState;

	public MahjonggGameController( MahjonggGameState mahjonggGameState ) {
		super();
		this.mahjonggGameState = mahjonggGameState;
	}

}