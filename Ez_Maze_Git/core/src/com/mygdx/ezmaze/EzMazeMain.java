package com.mygdx.ezmaze;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.mygdx.ezmaze.jeu.Assets;
import com.mygdx.ezmaze.jeu.WorldController;
import com.mygdx.ezmaze.jeu.WorldRenderer;

public class EzMazeMain implements ApplicationListener {
	//LibGDX n�cessite d'avoir un TAG par classe (log des messages)
	private static final String TAG = EzMazeMain.class.getName();
	
	//Mise � jour et contr�le du jeu par cette classe en passant les deux 
	//r�ferences suivantes
	private WorldController worldController;
	private WorldRenderer worldRenderer;
	
	/*
	 * Pour l'instant cette variable sera inutile, on ajoutera �ventuellement
	 * une feature pause mettre le jeu sur pause
	 */
	private boolean pause;

	@Override
	public void create() {
		// TODO Auto-generated method stub
		/*
		 * On commence par permettre � la console d'afficher tout ce qu'il se passe
		 * pendant l'ex�cution. De sorte que l'on aura plein d'infomations. (mettre
		 * LOG_NONE ou LOG_INFO pour les versions d�finitives !)
		 */
		Gdx.app.setLogLevel(Application.LOG_DEBUG);
		
		//On charge les assets
		Assets.instance.init(new AssetManager());
		
		//On intitialise le worldController et le wolrdRenderer
		worldController = new WorldController();
		worldRenderer = new WorldRenderer(worldController);
		
		//Ici, on fait en sorte que notre jeu ne soit pas sur pause
		pause = false;
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		/*
		 * Tout redimensionnement de fen�tre sera pris en charge par worldRenderer
		 */
		worldRenderer.resize(width, height);

	}

	@Override
	//GAMELOOP : C'est ici qu'a lieu la boucle d'actualisation du jeu !
	public void render() {
		// TODO Auto-generated method stub
		/*
		 * On veut mettre � jour continuement, et donc tenir compte du temps �coul�
		 * entre chaque appels � 'render()' 
		 */
		
		//Si le jeu n'est pas sur pause, on actualise l'affichage
		if (!pause) {
			worldController.update(Gdx.graphics.getDeltaTime());
		}
		
		//On choisir une couleur de nettoyage de l'�cran (RGBA rammen� en 100%)
		Gdx.gl.glClearColor(0,47/255.0f,60/255.0f,1);
		//Et on nettoie l'�cran avec cette couleur
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		//Enfin on affiche
		worldRenderer.render();
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		//Pour mettre le jeu en pause
		pause = true;
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		//Pour reprendre le jeu apr�s une pause
		Assets.instance.init(new AssetManager());
		pause = false;

	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		//De la m�me mani�re que pour le resize, le dispose est pris en charge par 
		//le worldRenderer
		worldRenderer.dispose();
		Assets.instance.dispose();
	}

}
