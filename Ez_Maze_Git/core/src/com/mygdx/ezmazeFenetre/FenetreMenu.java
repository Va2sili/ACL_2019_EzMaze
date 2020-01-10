package com.mygdx.ezmazeFenetre;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;


public class FenetreMenu extends AbstractFenetre{
	private static final String TAG = FenetreMenu.class.getName();
	
	public FenetreMenu(Game jeu) {
		super(jeu);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void render(float deltaTime) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		if (Gdx.input.isTouched()) {
			jeu.setScreen(new FenetreJeu(jeu));
		}
	}
	@Override
	public void resize (int width, int height) {
	}
	@Override
	public void show() {
	}
	@Override
	public void hide() {
	}
	@Override
	public void pause() {
	}

}
