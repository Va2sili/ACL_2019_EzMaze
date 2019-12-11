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
import com.mygdx.ezmaze.jeu.objects.Monstre;
import com.mygdx.ezmaze.jeu.objects.Mur;
import com.mygdx.ezmaze.jeu.objects.PersonnagePrincipal;

import ezmaze.util.CameraHelper;
import ezmaze.util.Constantes;

public class WorldController extends InputAdapter {
	private static final String TAG = WorldController.class.getName();

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

	//Pour intialiser la classe, ce code est plac� dans le constructeur.
	//Cela permettra de pouvoir r�initialiser un objet sans devoir le recr�er !
	private void init() {
		/*
		 * L'input processor est une interface tr�s utile pour prendre en compte
		 * les �v�nements d'entr�e. 
		 */
		Gdx.input.setInputProcessor(this);
		cameraHelper = new CameraHelper();
		//initTestObjets(); //CODE POUBELLE
		resurections = Constantes.RESU_INIT;
		initLevel();
	}





	//Contiendra la logique de jeu et sera appel�e plusieurs fois par seconde !
	//Le delta time permettra d'actualiser correctement en fonction du temps �coul�
	//depuis le dernier affichage de la fen�tre...
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
		//		//Sinon, si on est sur une application windows : On va contr�ler le sprite s�lectionn�
		//		float spriteVitesseMobile = 5*deltaTime;
		//		if (Gdx.input.isKeyPressed(Keys.Q)) moveSelectedSprite(-spriteVitesseMobile,0);
		//		if (Gdx.input.isKeyPressed(Keys.D)) moveSelectedSprite(spriteVitesseMobile,0);
		//		if (Gdx.input.isKeyPressed(Keys.S)) moveSelectedSprite(0,-spriteVitesseMobile);
		//		if (Gdx.input.isKeyPressed(Keys.Z)) moveSelectedSprite(0,spriteVitesseMobile);
		//FIN DU CODE POUBELLE

		if(!cameraHelper.hasTarget(level.personnage)) {
			//Movement de la cam�ra libre (ind�pendente de la cible !)
			float camMoveSpeed = 5*deltaTime;
			float camMoveSpeedAccelerationFactor = 5;
			if (Gdx.input.isKeyPressed(Keys.SHIFT_LEFT)) camMoveSpeed *= camMoveSpeedAccelerationFactor;
			if (Gdx.input.isKeyPressed(Keys.LEFT)) moveCamera(-camMoveSpeed,0);
			if (Gdx.input.isKeyPressed(Keys.RIGHT)) moveCamera(camMoveSpeed,0);
			if (Gdx.input.isKeyPressed(Keys.UP)) moveCamera(0,camMoveSpeed);
			if (Gdx.input.isKeyPressed(Keys.DOWN)) moveCamera(0,-camMoveSpeed);
			if (Gdx.input.isKeyPressed(Keys.BACKSPACE)) cameraHelper.setPosition(0, 0);;

			//Contr�le du zoom de la cam�ra
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

	//Contr�le des collisions
	private Rectangle r1 = new Rectangle();
	private Rectangle r2 = new Rectangle();
	private Rectangle r3 = new Rectangle();

	private void collisionPersonnageMur(Mur mur) {

		PersonnagePrincipal personnage = level.personnage;

		float differenceVerticale = personnage.position.y-(mur.position.y);
		float differenceHorizontale = personnage.position.x-(mur.position.x);

		//		System.out.println("	(x,X) = ("+personnage.position.x+","+mur.position.x+")"
		//				+"	(y,Y) = ("+personnage.position.y+","+mur.position.y+")"
		//				+"\n	Diff�rence Verticale = "+differenceVerticale
		//				+"\n	Diff�rence Horizontale = "+differenceHorizontale);

		if (Math.abs(differenceVerticale) < 1.0f) {
			personnage.position.y = personnage.anciennePosition.y;

		}
		if (Math.abs(differenceHorizontale) < 1.0f)  {
			personnage.position.x = personnage.anciennePosition.x;
		}
	};

	private void collisionMonstreMur(Mur mur) {

		Monstre monster = level.monstre;

		float differenceVerticale = monster.position.y-(mur.position.y);
		float differenceHorizontale = monster.position.x-(mur.position.x);

		//		System.out.println("	(x,X) = ("+personnage.position.x+","+mur.position.x+")"
		//				+"	(y,Y) = ("+personnage.position.y+","+mur.position.y+")"
		//				+"\n	Diff�rence Verticale = "+differenceVerticale
		//				+"\n	Diff�rence Horizontale = "+differenceHorizontale);

		if (Math.abs(differenceVerticale) < 1.0f) {
			monster.position.y = monster.anciennePosition.y;

		}
		if (Math.abs(differenceHorizontale) < 1.0f)  {
			monster.position.x = monster.anciennePosition.x;
		}
	};

	private void collisionPersonnageEzCase() {
		/*
		 * Non impl�ment�
		 */
	};
	private void collisionPersonnageMonstre() {
		/*
		 * Non impl�ment�
		 */
	};

	private void testCollision() {
		/*
		 * Les coefficients permettent d'adapter la taille de l'objet pour le moteur physique.
		 * On prend une taille d'objet plus petit que la taille de l'objet r�ellement affich�
		 * En effet, le bord des images comporte souvent une marge transparente... Ca �vite aussi
		 * De d�tecter une collision lorsque qu'on ne touche qu'� peine un �l�ment du bout du doigt de pixels...
		 */
		r1.set(level.personnage.position.x+0.2f,level.personnage.position.y+0.2f,level.personnage.frontiere.width-0.4f,level.personnage.frontiere.height-0.4f);
		r3.set(level.monstre.position.x+0.2f,level.monstre.position.y+0.2f,level.monstre.frontiere.width-0.4f,level.monstre.frontiere.height-0.4f);


		//test pour les collisions personnage <--> mur
		for (Mur mur : level.murs) {
			r2.set(mur.position.x,mur.position.y,mur.frontiere.width,mur.frontiere.height);
			if(!r1.overlaps(r2) && !r3.overlaps(r2)) continue;
			collisionPersonnageMur(mur);
			collisionMonstreMur(mur);
		}

		//Test pour les collisions personnage <--> EzCase
		/*
		 * Non impl�ment�
		 */

		//Test pour les collisions personnage <--> Monstre
		/*
		 * Non impl�ment�
		 */
	}
	//FIN DE CONTROLE DES COLLISIONS



	@Override
	public boolean keyUp (int keycode) {
		//Reset
		if (keycode == Keys.R) {
			init();
			Gdx.app.debug(TAG,"La carte de jeu est remise � z�ro avec succ�s !!!");
		}
		//Suivit par la cam�ra
		else if (keycode == Keys.ENTER) {
			cameraHelper.setTarget(cameraHelper.hasTarget()? null:level.personnage);
			Gdx.app.debug(TAG,"On suit avec la cam�ra : "+cameraHelper.hasTarget());
		}
		return false;
	}






	//**************** TESTS - CODE POUBELLE ****************
	//	private void initTestObjets() {
	//		//On commence par cr�er un tableau de 5 Sprites
	//		testSprites = new Sprite[5];
	//		
	////UNE POSSIBILITE - GENERATION ALEATOIRE
	////		//On cr�e une Pixmap vide et POT-sized avec des pixels d'information de 8bit RGBA
	////		int width = 32;
	////		int height = 32;
	////		Pixmap pixmap = createProceduralPixmap(width,height);
	////		//Cr�e une nouvelle texture d'information pixmap
	////		//On pourra par le suite importer des textures, mais pour l'instant on
	////		//Les fabrique avec une pixmap !
	////		Texture texture = new Texture(pixmap);
	////		for (int i = 0; i < testSprites.length; i++) {
	////			Sprite sprite = new Sprite(texture);
	////			//On d�fini un sprite comme �tant un �l�ment de taille 1x1 dans le monde du jeu
	////			sprite.setSize(1, 1);
	////			//On place l'origine au niveau du centre du sprite (!on met des floats)
	////			//Ceci sera particuli�rement utile pour les ROTATIONS !
	////			sprite.setOrigin(sprite.getWidth()/2.0f, sprite.getHeight()/2.0f);
	////			//On calcule une position pour le sprite, au hasard pour l'instant
	////			float randposX = MathUtils.random(-2.0f,2.0f);
	////			float randposY = MathUtils.random(-2.0f,2.0f);
	////			sprite.setPosition(randposX, randposY);
	////			//On place le sprite nouvellement cr�� dans le tabelau de sprites
	////			testSprites[i]=sprite;
	////FIN DE LA POSSIBILITE - GENERATION ALEATOIRE
	//		
	//			
	////UNE POSSIBILITE - UTILISATION D'UN ATLAS ET DES REGIONS
	//		//On cr�e une liste des r�gions de texture
	//		Array<TextureRegion> regions = new Array<TextureRegion>();
	//		regions.add(Assets.instance.thesee.personnage);
	//		regions.add(Assets.instance.mur.mur);
	//		regions.add(Assets.instance.carre.EZCase);
	//		//Cr�ation des nouveaux sprites qui utilisent un partie random de la region
	//		//On cr�e de nouveaux sprites en utilisant la texture cr��e ci-dessus
	//		for (int i = 0; i < testSprites.length; i++) {
	//			TextureRegion TR = regions.random();
	//			System.out.println(TR);
	//			Sprite sprite = new Sprite(TR);
	//			//On d�fini un sprite comme �tant un �l�ment de taille 1x1 dans le monde du jeu
	//			sprite.setSize(1, 1);
	//			//On place l'origine au niveau du centre du sprite (!on met des floats)
	//			//Ceci sera particuli�rement utile pour les ROTATIONS !
	//			sprite.setOrigin(sprite.getWidth()/2.0f, sprite.getHeight()/2.0f);
	//			//On calcule une position pour le sprite, au hasard pour l'instant
	//			float randposX = MathUtils.random(-2.0f,2.0f);
	//			float randposY = MathUtils.random(-2.0f,2.0f);
	//			sprite.setPosition(randposX, randposY);
	//			//On place le sprite nouvellement cr�� dans le tabelau de sprites
	//			testSprites[i]=sprite;
	//		}
	////FIN DE LA POSSIBILITE - UTILISATION D'UN ATLAS ET DES REGIONS
	//		
	//		
	//		//On prend le premier sprite comme �tant celui s�lectionn�
	//		selectedSprite = 0;
	//	}
	//	
	//	private Pixmap createProceduralPixmap(int width, int height) {
	//		// TODO Auto-generated method stub
	//		Pixmap pixmap = new Pixmap(width,height,Format.RGBA8888);
	//		//On remplit le carr� d'une couleur rouge d'opacit� 0.5
	//		pixmap.setColor(1,0,0,0.5f);//rouge
	//		pixmap.fill();
	//		//On dessine une forme en X color�e de jaune et vert sur le carr�
	//		pixmap.setColor(1,1,0,1);//jaune
	//		pixmap.drawLine(0, 0, width, height);//Une droite \
	//		pixmap.setColor(0,1,0,1);//vert
	//		pixmap.drawLine(width, 0, 0, height);//Une autre droite /
	//		//Et maintenant un cadre bleu autour du carr�
	//		pixmap.setColor(0,0,1,1);
	//		pixmap.drawRectangle(0, 0, width, height);
	//		return pixmap;
	//	}

	//	private void updateTestObjets(float deltaTime) {
	//		// TODO Auto-generated method stub
	//		// On r�cup�re la rotation actuelle du sprite AUTOUR DE SON ORIGINE !
	//		float rotation = testSprites[selectedSprite].getRotation();
	//		//On fait tourner le sprite � pi/2 rad/s
	//		rotation += 90 * deltaTime;
	//		//On applique cette rotation au sprite
	//		testSprites[selectedSprite].setRotation(rotation);
	//	}
	//	
	//	/*
	//	 * Ci-apr�s, on g�re les �v�nements de InputAdapter pour le rel�chement de
	//	 * certaines touches qui nous int�ressent :
	//	 * 'R' : pour reset
	//	 * 'SPACE' : pour changer le sprite que l'on contr�le
	//	 */
	//	

	//	private void moveSelectedSprite(float v_x, float v_y) {
	//		// TODO Auto-generated method stub
	//		testSprites[selectedSprite].translate(v_x, v_y);
	//	}

	//****************FIN DU CODE POUBELLE ****************

}
