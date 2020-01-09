package com.mygdx.ezmaze;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.mygdx.ezmaze.jeu.Assets;
import com.mygdx.ezmaze.jeu.WorldController;
import com.mygdx.ezmaze.jeu.WorldRenderer;
import com.mygdx.ezmaze.fenetres.FenetreMenu;

public class EzMazeMain extends Game {
	//LibGDX nécessite d'avoir un TAG par classe (log des messages)
	private static final String TAG = EzMazeMain.class.getName();
	
	@Override
	public void create() {
		//Log Level
		Gdx.app.setLogLevel(Application.LOG_DEBUG);
		//Chargement des Assets
		Assets.instance.init(new AssetManager());
		//Démarrage du jeu sur le menu d'accueil
		setScreen(new FenetreMenu(this));
	}

}
