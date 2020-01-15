package com.mygdx.ezmaze.jeu;


import java.util.Iterator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Disposable;
import com.mygdx.ezmaze.jeu.objects.projectiles.ArmeLancee;
import com.mygdx.ezmaze.jeu.objects.projectiles.Projectile;

import ezmaze.util.Constantes;


public class WorldRenderer implements Disposable {
	//Camera préimplémentée dans LibGDX
	static OrthographicCamera camera;
	//Camera supplémentaire pour le GUI
	private OrthographicCamera cameraGUI;
	//SpriteBatch implémente l'interface "disposable" et c'est pourquoi on implémentera
	//ici la fonction dispose() qui libérera de la mémoire au cours de l'exécution.
	private SpriteBatch batch;
	//Cette référence est nécessaire puisque c'est le controleur qui rassemble les 
	//informations utiles pour l'affichage (ou sont les objets, combiens il y en a,...)
	private WorldController worldController;
	private Sprite backgroundSprite;


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
		cameraGUI = new OrthographicCamera(Constantes.VIEWPORT_GUI_WIDTH,Constantes.VIEWPORT_GUI_HEIGHT);
		cameraGUI.position.set(0,0,0);
		cameraGUI.setToOrtho(true);//Inversion de l'axe vertical
		cameraGUI.update();
			
		Pixmap pixmap200 = new Pixmap(Gdx.files.internal("../core/assets/images/background.png"));
		Pixmap pixmap100 = new Pixmap(800, 480, pixmap200.getFormat());
		pixmap100.drawPixmap(pixmap200,
		        0, 0, pixmap200.getWidth(), pixmap200.getHeight(),
		        0, 0, pixmap100.getWidth(), pixmap100.getHeight()
		);
		Texture imageDeFond = new Texture(pixmap100);
		pixmap200.dispose();
		pixmap100.dispose();
		backgroundSprite =new Sprite(imageDeFond);
	}

	//Contiendra la logique d'affichage des objets du jeu (ordre d'affichage, layers, ...)
	public void render() {
		renderFond(batch);
		renderWorld(batch);
		renderGui(batch);
	};


	private void renderFond(SpriteBatch batch) {
		
		batch.begin();
		backgroundSprite.draw(batch);
		batch.end();
	}

	private void renderWorld (SpriteBatch batch) {
		worldController.cameraHelper.applyTo(camera);
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		worldController.level.render(batch);
		for (Projectile p : worldController.projectiles) {
			p.render(batch);
		}
		for (ArmeLancee a : worldController.armeslancees) {
			a.render(batch);
		}
		batch.end();
	}

	private void renderGui(SpriteBatch batch) {
		batch.setProjectionMatrix(cameraGUI.combined);
		batch.begin();
		RenderGUI.renderGuiNbMonstres(batch,worldController);
		RenderGUI.renderGuiTimeCounter(batch, cameraGUI);
		RenderGUI.renderNbCaissesRestantes(batch, worldController);
		RenderGUI.renderGuiPdvPerso(batch, worldController);
		RenderGUI.renderCoeurResu(batch, worldController);
		if(worldController.resurections == 0) {
			RenderGUI.renderGameOver(batch);
		}
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
