package com.mygdx.ezmazeFenetre;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.mygdx.ezmaze.jeu.WorldController;
import com.mygdx.ezmaze.jeu.WorldRenderer;

public class FenetreJeu extends AbstractFenetre {
	private static final String TAG = FenetreJeu.class.getName();

	private WorldController worldController;
	private WorldRenderer worldRenderer;
	private boolean pause;//utile seulement pour android, mais nécessaire quand même

	public FenetreJeu(Game jeu) {
		super(jeu);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void show() {
		worldController= new WorldController(jeu);
		worldRenderer =  new WorldRenderer(worldController);
	}

	@Override
	public void render(float deltaTime) {
		//Pas de rendering quand on est sur pause :
		if(!pause) {
			worldController.update(deltaTime);
		}
		//On choisir une couleur de nettoyage de l'écran (RGBA rammené en 100%)
		Gdx.gl.glClearColor(0,47/255.0f,60/255.0f,1);
		//Et on nettoie l'écran avec cette couleur
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		//Enfin on affiche
		worldRenderer.render();

	}

	@Override
	public void resize(int width, int height) {
		worldRenderer.resize(width, height);

	}

	@Override
	public void pause() {
		pause = true;

	}

	@Override
	public void resume() {
		super.resume();
	}

	@Override
	public void hide() {
		worldRenderer.dispose();
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

}
