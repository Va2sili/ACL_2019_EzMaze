package com.mygdx.ezmaze.jeu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.mygdx.ezmaze.jeu.objects.Case;
import com.mygdx.ezmaze.jeu.objects.Monstre;
import com.mygdx.ezmaze.jeu.objects.Mur;
import com.mygdx.ezmaze.jeu.objects.PersonnagePrincipal;

import ezmaze.util.CameraHelper;
import ezmaze.util.Constantes;

public class WorldController extends InputAdapter {
	private static final String TAG = WorldController.class.getName();

	public static int NbMonstres=0;

	//TESTS
	//public Sprite[] testSprites;
	//public int selectedSprite;
	//FIN TESTS


	public CameraHelper cameraHelper;

	public Level level;
	public int resurections;
	public int score;

	private void initLevel() {
		score = 0;
		level = new Level(Constantes.LEVEL_01);
		cameraHelper.setTarget(level.personnage);
	}

	public WorldController() {
		init();
	}

	//Pour intialiser la classe, ce code est placé dans le constructeur.
	//Cela permettra de pouvoir réinitialiser un objet sans devoir le recréer !
	private void init() {
		/*
		 * L'input processor est une interface très utile pour prendre en compte
		 * les évènements d'entrée. 
		 */
		Gdx.input.setInputProcessor(this);
		cameraHelper = new CameraHelper();
		//initTestObjets(); //CODE POUBELLE
		resurections = Constantes.RESU_INIT;
		initLevel();
	}





	//Contiendra la logique de jeu et sera appelée plusieurs fois par seconde !
	//Le delta time permettra d'actualiser correctement en fonction du temps écoulé
	//depuis le dernier affichage de la fenêtre...
	public void update (float deltaTime) {
		handleMonster(deltaTime);
		handleDebugInput(deltaTime);//Il est important de prendre d'abord en compte l'action du joueur !
		//updateTestObjets(deltaTime);//CODE POUBELLE
		handleInputGame(deltaTime);
		level.update(deltaTime);
		testCollision();
		cameraHelper.update(deltaTime);
	}

	private void handleDebugInput(float deltaTime) {
		// TODO Auto-generated method stub
		if (Gdx.app.getType() != ApplicationType.Desktop) return;


		//CODE POUBELLE
		//		//Sinon, si on est sur une application windows : On va contrôler le sprite sélectionné
		//		float spriteVitesseMobile = 5*deltaTime;
		//		if (Gdx.input.isKeyPressed(Keys.Q)) moveSelectedSprite(-spriteVitesseMobile,0);
		//		if (Gdx.input.isKeyPressed(Keys.D)) moveSelectedSprite(spriteVitesseMobile,0);
		//		if (Gdx.input.isKeyPressed(Keys.S)) moveSelectedSprite(0,-spriteVitesseMobile);
		//		if (Gdx.input.isKeyPressed(Keys.Z)) moveSelectedSprite(0,spriteVitesseMobile);
		//FIN DU CODE POUBELLE

		if(!cameraHelper.hasTarget(level.personnage)) {
			//Movement de la caméra libre (indépendente de la cible !)
			float camMoveSpeed = 5*deltaTime;
			float camMoveSpeedAccelerationFactor = 5;
			if (Gdx.input.isKeyPressed(Keys.SHIFT_LEFT)) camMoveSpeed *= camMoveSpeedAccelerationFactor;
			if (Gdx.input.isKeyPressed(Keys.LEFT)) moveCamera(-camMoveSpeed,0);
			if (Gdx.input.isKeyPressed(Keys.RIGHT)) moveCamera(camMoveSpeed,0);
			if (Gdx.input.isKeyPressed(Keys.UP)) moveCamera(0,camMoveSpeed);
			if (Gdx.input.isKeyPressed(Keys.DOWN)) moveCamera(0,-camMoveSpeed);
			if (Gdx.input.isKeyPressed(Keys.BACKSPACE)) cameraHelper.setPosition(0, 0);;

			//Contrôle du zoom de la caméra
			float camZoomSpeed = 1 * deltaTime;
			float camZoomSpeedAccelarationFactor = 5;
			if (Gdx.input.isKeyPressed(Keys.SHIFT_LEFT)) camZoomSpeed *= camZoomSpeedAccelarationFactor;
			if (Gdx.input.isKeyPressed(Keys.I)) cameraHelper.addZoom(-camZoomSpeed);
			if (Gdx.input.isKeyPressed(Keys.O)) cameraHelper.addZoom(camZoomSpeed);
			if (Gdx.input.isKeyPressed(Keys.U)) cameraHelper.setZoom(1);
		}
	}

	private void moveCamera(float x, float y) {
		// TODO Auto-generated method stub
		x += cameraHelper.getPosition().x;
		y += cameraHelper.getPosition().y;
		cameraHelper.setPosition(x, y);
	}

	private void handleInputGame (float deltaTime) {
		if (cameraHelper.hasTarget(level.personnage)) {
			//Mouvements du joueur
			if (Gdx.input.isKeyPressed(Keys.Q)) {
				level.personnage.vitesse.x = -level.personnage.vitesseMax.x;
			}
			else if (Gdx.input.isKeyPressed(Keys.D)) {
				level.personnage.vitesse.x = level.personnage.vitesseMax.x;
			}
			else if (Gdx.input.isKeyPressed(Keys.Z)) {
				level.personnage.vitesse.y = level.personnage.vitesseMax.y;
			}
			else if (Gdx.input.isKeyPressed(Keys.S)) {
				level.personnage.vitesse.y = -level.personnage.vitesseMax.y;
			}

			//ATTAQUE
			if (Gdx.input.isKeyPressed(Keys.A)) {
				level.personnage.setAttaque(true);
			}
			else {
				level.personnage.setAttaque(false);
			}
		}
	}

	private void handleMonster (float deltaTime) {
		
		//EN COURS D'IMPLEMENTATION
		
		//Mouvements du monstre
		//		int a=(int)(Math.random()*10);
		//
		//			level.monstre.vitesse.x = -level.monstre.vitesseMax.x;
		//				if(a%3==0) level.monstre.vitesse.x = level.monstre.vitesseMax.x;
		//				if(a%5==0) level.monstre.vitesse.y = level.monstre.vitesseMax.y;
		//			level.monstre.vitesse.y = -level.monstre.vitesseMax.y;

		/*for (Mur mur : level.murs) {
			float differenceVerticale = level.monstre.position.y-(mur.position.y);
			float differenceHorizontale = level.monstre.position.x-(mur.position.x);

			System.out.println("diff verticale: "+Math.abs(differenceVerticale));
			if (Math.abs(differenceVerticale) < 1.0f && differenceVerticale<=0) {
				level.monstre.vitesse.y = -level.monstre.vitesseMax.y;
			}			

			if (Math.abs(differenceVerticale) < 1.0f && differenceVerticale>=0) {
				level.monstre.vitesse.y = level.monstre.vitesseMax.y;
			}
			System.out.println("diff horizontale: "+differenceHorizontale);
			if (Math.abs(differenceHorizontale) < 1.0f && differenceHorizontale<=0)  {
				level.monstre.vitesse.x = -level.monstre.vitesseMax.x;
			}
			if (Math.abs(differenceHorizontale) < 1.0f && differenceHorizontale>=0) {
				level.monstre.vitesse.x = level.monstre.vitesseMax.x;
			}


		}







		for (Mur mur : level.murs) {
			r2.set(mur.position.x,mur.position.y,mur.frontiere.width,mur.frontiere.height);
			if(!r1.overlaps(r2) && !r3.overlaps(r2)) continue;
			collisionPersonnageMur(mur);
			collisionMonstreMur(mur);
		}*/


		//ATTAQUE
		//A IMPLEMENTER

	}

	//Contrôle des collisions
	private Rectangle r1 = new Rectangle();
	private Rectangle r2 = new Rectangle();
	private Rectangle r3 = new Rectangle();

	private void collisionPersonnageMur(Mur mur) {

		PersonnagePrincipal personnage = level.personnage;

		float differenceVerticale = personnage.position.y-(mur.position.y);
		float differenceHorizontale = personnage.position.x-(mur.position.x);

		//		System.out.println("	(x,X) = ("+personnage.position.x+","+mur.position.x+")"
		//				+"	(y,Y) = ("+personnage.position.y+","+mur.position.y+")"
		//				+"\n	Différence Verticale = "+differenceVerticale
		//				+"\n	Différence Horizontale = "+differenceHorizontale);

		if (Math.abs(differenceVerticale) < 1.0f) {
			personnage.position.y = personnage.anciennePosition.y;

		}
		if (Math.abs(differenceHorizontale) < 1.0f)  {
			personnage.position.x = personnage.anciennePosition.x;
		}
		System.out.println("HOP !");
	};

	private void collisionMonstreMur(Mur mur) {

		Monstre monster = level.monstre;

		float differenceVerticale = monster.position.y-(mur.position.y);
		float differenceHorizontale = monster.position.x-(mur.position.x);

		//		System.out.println("	(x,X) = ("+personnage.position.x+","+mur.position.x+")"
		//				+"	(y,Y) = ("+personnage.position.y+","+mur.position.y+")"
		//				+"\n	Différence Verticale = "+differenceVerticale
		//				+"\n	Différence Horizontale = "+differenceHorizontale);

		if (Math.abs(differenceVerticale) < 1.0f) {
			monster.position.y = monster.anciennePosition.y;

		}
		if (Math.abs(differenceHorizontale) < 1.0f)  {
			monster.position.x = monster.anciennePosition.x;
		}
	};

	private void collisionPersonnageEzCase(Case ezcase) {
			//dès qu'il arrive sur la case d'arrivée on le renvoit sur la case de départ
		PersonnagePrincipal personnage = level.personnage;
		float differenceH = personnage.position.x-(ezcase.position.x);
		if (Math.abs(differenceH)<1.0f)  {
			keyUp(Keys.R);
		
	}
	}
	private void collisionPersonnageMonstre() {
		/*
		 * Non implémenté
		 */
	};

	private void testCollision() {
		/*
		 * Les coefficients permettent d'adapter la taille de l'objet pour le moteur physique.
		 * On prend une taille d'objet plus petit que la taille de l'objet réellement affiché
		 * En effet, le bord des images comporte souvent une marge transparente... Ca évite aussi
		 * De détecter une collision lorsque qu'on ne touche qu'à peine un élément du bout du doigt de pixels...
		 */
		r1.set(level.personnage.position.x+0.2f,level.personnage.position.y+0.2f,level.personnage.frontiere.width-0.4f,level.personnage.frontiere.height-0.4f);
		r3.set(level.monstre.position.x+0.2f,level.monstre.position.y+0.2f,level.monstre.frontiere.width-0.4f,level.monstre.frontiere.height-0.4f);


		//test pour les collisions personnage <--> mur && monstre <--> mur
		for (Mur mur : level.murs) {
			r2.set(mur.position.x,mur.position.y,mur.frontiere.width,mur.frontiere.height);
			if (!r1.overlaps(r2) && !r3.overlaps(r2)) continue;
			collisionPersonnageMur(mur);
			collisionMonstreMur(mur);
		}

		//Test pour les collisions personnage <--> EzCase
		//dès qu'il y a chevauchement on fait appelle a la fonction collisionpersonnageezcase
		r2.set(level.ezmaze.position.x,level.ezmaze.position.y,level.ezmaze.frontiere.width,level.ezmaze.frontiere.height);
		if(r1.overlaps(r2)) {
		collisionPersonnageEzCase(level.ezmaze);}
		

		//Test pour les collisions personnage <--> Monstre
		/*
		 * Non implémenté
		 */
	}
	//FIN DE CONTROLE DES COLLISIONS



	@Override
	public boolean keyUp (int keycode) {
		//Reset
		if (keycode == Keys.R) {
			init();
			Gdx.app.debug(TAG,"La carte de jeu est remise à zéro avec succés !!!");
		}
		//Suivit par la caméra
		else if (keycode == Keys.ENTER) {
			cameraHelper.setTarget(cameraHelper.hasTarget()? null:level.personnage);
			Gdx.app.debug(TAG,"On suit avec la caméra : "+cameraHelper.hasTarget());
		}
		return false;
	}


}
