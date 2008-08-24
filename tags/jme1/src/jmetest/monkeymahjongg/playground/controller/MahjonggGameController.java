package jmetest.monkeymahjongg.playground.controller;

import jmetest.monkeymahjongg.playground.view.MahjonggGameState;

import com.jme.scene.Controller;

public abstract class MahjonggGameController extends Controller {

	protected MahjonggGameState mahjonggGameState;

	public MahjonggGameController( MahjonggGameState mahjonggGameState ) {
		super();
		this.mahjonggGameState = mahjonggGameState;
	}

}