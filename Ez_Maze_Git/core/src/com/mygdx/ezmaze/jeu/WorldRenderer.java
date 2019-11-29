package com.mygdx.ezmaze.jeu;


import java.util.Iterator;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Disposable;

import ezmaze.util.Constantes;


public class WorldRenderer implements Disposable {
	//Camera préimplémentée dans LibGDX
	private OrthographicCamera camera;
	//SpriteBatch implémente l'interface "disposable" et c'est pourquoi on implémentera
	//ici la fonction dispose() qui libérera de la mémoire au cours de l'exécution.
	private SpriteBatch batch;
	//Cette référence est nécessaire puisque c'est le controleur qui rassemble les 
	//informations utiles pour l'affichage (ou sont les objets, combiens il y en a,...)
	private WorldController worldController;
	
	
	
	public WorldRenderer (WorldController worldController) {
		this.worldController = worldController;//On accède aux informations importantes
		init();
	}
	//Pour intialiser la classe, ce code est placé dans le constructeur.
	//Cela permettra de pouvoir réinitialiser un objet sans devoir le recréer !
	private void init() {
		batch = new SpriteBatch();//C'est le batch qui permet vraiment d'afficher
		camera = new OrthographicCamera(Constantes.VIEWPORT_WIDTH, Constantes.VIEWPORT_HEIGHT);
		camera.position.set(0,0,0);
		camera.update();
	}
	
	//Contiendra la logique d'affichage des objets du jeu (ordre d'affichage, layers, ...)
	public void render() {
		//CODE POUBELLE
		//renderTestObjects();
		//FIN CODE POUBELLE
		
		renderWorld(batch);
	};
	
//CODE POUBELLE	
//	private void renderTestObjects() {
//		// TODO Auto-generated method stub
//		worldController.cameraHelper.applyTo(camera);
//		batch.setProjectionMatrix(camera.combined);
//		batch.begin();
//		for (Sprite sprite : worldController.testSprites) {
//			sprite.draw(batch);
//		}
//		batch.end();
//	}
//FIN CODE POUBELLE
	
	private void renderWorld (SpriteBatch batch) {
		worldController.cameraHelper.applyTo(camera);
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		worldController.level.render(batch);
		batch.end();
	}
	
	//Quand la taille de la fenêtre ou de l'écran est changée...
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
