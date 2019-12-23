package com.mygdx.ezmaze.jeu;


import java.util.Iterator;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Disposable;

import ezmaze.util.Constantes;


public class WorldRenderer implements Disposable {
	//Camera pr�impl�ment�e dans LibGDX
	private OrthographicCamera camera;
	//Camera suppl�mentaire pour le GUI
	private OrthographicCamera cameraGUI;
	//SpriteBatch impl�mente l'interface "disposable" et c'est pourquoi on impl�mentera
	//ici la fonction dispose() qui lib�rera de la m�moire au cours de l'ex�cution.
	private SpriteBatch batch;
	//Cette r�f�rence est n�cessaire puisque c'est le controleur qui rassemble les 
	//informations utiles pour l'affichage (ou sont les objets, combiens il y en a,...)
	private WorldController worldController;
	
	
	
	public WorldRenderer (WorldController worldController) {
		this.worldController = worldController;//On acc�de aux informations importantes
		init();
	}
	//Pour intialiser la classe, ce code est plac� dans le constructeur.
	//Cela permettra de pouvoir r�initialiser un objet sans devoir le recr�er !
	private void init() {
		batch = new SpriteBatch();//C'est le batch qui permet vraiment d'afficher
		camera = new OrthographicCamera(Constantes.VIEWPORT_WIDTH, Constantes.VIEWPORT_HEIGHT);
		camera.position.set(0,0,0);
		camera.update();
		cameraGUI = new OrthographicCamera(Constantes.VIEWPORT_GUI_WIDTH,Constantes.VIEWPORT_GUI_HEIGHT);
		cameraGUI.position.set(0,0,0);
		cameraGUI.setToOrtho(true);//Inversion de l'axe vertical
		cameraGUI.update();
	}
	
	//Contiendra la logique d'affichage des objets du jeu (ordre d'affichage, layers, ...)
	public void render() {
		renderWorld(batch);
		renderGui(batch);
	};
	

	
	private void renderWorld (SpriteBatch batch) {
		worldController.cameraHelper.applyTo(camera);
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		worldController.level.render(batch);
		batch.end();
	}
	
	private void renderGui(SpriteBatch batch) {
		batch.setProjectionMatrix(cameraGUI.combined);
		batch.begin();
		RenderGUI.renderGuiNbMonstres(batch);
		RenderGUI.renderGuiTimeCounter(batch, cameraGUI);
		batch.end();
	}
	
	//Quand la taille de la fen�tre ou de l'�cran est chang�e...
	public void resize (int width, int height) {
		/*
		 * Ratio hauteur voulue sur hauteur disponible fois largeur disponible.
		 */
		camera.viewportWidth = (Constantes.VIEWPORT_HEIGHT/height)*width;
		camera.update();
	}

	@Override
	public void dispose() {
		batch.dispose();
	}
}
