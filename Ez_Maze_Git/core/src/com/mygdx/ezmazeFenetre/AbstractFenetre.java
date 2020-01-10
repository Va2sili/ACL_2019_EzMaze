package com.mygdx.ezmazeFenetre;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.mygdx.ezmaze.jeu.Assets;

public abstract class AbstractFenetre implements Screen {
	protected Game jeu;/*
	Chaque fen�tre fait r�f�rence � une variable de la class Game
	Cela permettra de faire appel � des fonctions tr�s utiles comme setScreen().
	*/
	
	public AbstractFenetre(Game jeu) {
		this.jeu = jeu;
	}
	
	public abstract void render (float deltaTime);
	public abstract void resize (int width, int height);
	public abstract void show();
	public abstract void hide();
	public abstract void pause();
	
	public void resume() {
		Assets.instance.init(new AssetManager());
	}
	
	public void dispose() {
		Assets.instance.dispose();
	}
}
