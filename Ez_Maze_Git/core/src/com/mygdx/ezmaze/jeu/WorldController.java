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
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.mygdx.ezmaze.jeu.objects.ArriveeCaisse;
import com.mygdx.ezmaze.jeu.objects.Caisse;
import com.mygdx.ezmaze.jeu.objects.Caisse.ETAT_CAISSE;
import com.mygdx.ezmaze.jeu.objects.Case;
import com.mygdx.ezmaze.jeu.objects.Monstre;
import com.mygdx.ezmaze.jeu.objects.Mur;
import com.mygdx.ezmaze.jeu.objects.PersonnagePrincipal;
import com.mygdx.ezmaze.jeu.objects.PersonnagePrincipal.ETAT_COMBAT;
import com.mygdx.ezmaze.jeu.objects.PersonnagePrincipal.ORIENTATION_PERSONNAGE;

import ezmaze.util.CameraHelper;
import ezmaze.util.Constantes;

public class WorldController extends InputAdapter {
	private static final String TAG = WorldController.class.getName();


	public static float temps = 0;

	//TESTS
	//public Sprite[] testSprites;
	//public int selectedSprite;
	//FIN TESTS


	public CameraHelper cameraHelper;

	public Level level;
	public int numLevel=0;
	public int resurections;
	public int score;

	private void initLevel() {
		score = 0;
		numLevel = numLevel%(Constantes.LEVEL.length);
		level = new Level(Constantes.LEVEL[numLevel]);
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
		temps = 0;
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
				level.personnage.orientation = ORIENTATION_PERSONNAGE.GAUCHE;
			}
			else if (Gdx.input.isKeyPressed(Keys.D)) {
				level.personnage.vitesse.x = level.personnage.vitesseMax.x;
				level.personnage.orientation = ORIENTATION_PERSONNAGE.DROIT;
			}
			else if (Gdx.input.isKeyPressed(Keys.Z)) {
				level.personnage.vitesse.y = level.personnage.vitesseMax.y;
				level.personnage.orientation = ORIENTATION_PERSONNAGE.HAUT;
			}
			else if (Gdx.input.isKeyPressed(Keys.S)) {
				level.personnage.vitesse.y = -level.personnage.vitesseMax.y;
				level.personnage.orientation = ORIENTATION_PERSONNAGE.BAS;
			}

			//ATTAQUE
			if (Gdx.input.isKeyPressed(Keys.A)) {
				level.personnage.setAttaque(true);
			}
			else if (Gdx.input.isKeyJustPressed(Keys.F)) {
				level.personnage.pousse = true;
			}
			else {
				level.personnage.pousse=false;
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
	private Rectangle r4 = new Rectangle();

	private void collisionPersonnageMur(Mur mur) {

		PersonnagePrincipal personnage = level.personnage;

		float differenceVerticale = personnage.position.y-(mur.position.y);
		float differenceHorizontale = personnage.position.x-(mur.position.x);

		if (Math.abs(differenceVerticale) < 1.0f) {
			personnage.position.y = personnage.anciennePosition.y;

		}
		if (Math.abs(differenceHorizontale) < 1.0f)  {
			personnage.position.x = personnage.anciennePosition.x;
		}

	};

	private void collisionMonstreMur(Mur mur, Monstre m) {


		float differenceVerticale = m.position.y-(mur.position.y);
		float differenceHorizontale = m.position.x-(mur.position.x);

		//		System.out.println("	(x,X) = ("+personnage.position.x+","+mur.position.x+")"
		//				+"	(y,Y) = ("+personnage.position.y+","+mur.position.y+")"
		//				+"\n	Diff�rence Verticale = "+differenceVerticale
		//				+"\n	Diff�rence Horizontale = "+differenceHorizontale);

		if (Math.abs(differenceVerticale) < 1.0f) {
			m.position.y = m.anciennePosition.y;

		}
		if (Math.abs(differenceHorizontale) < 1.0f)  {
			m.position.x = m.anciennePosition.x;
		}
	};

	private void collisionPersonnageEzCase(Case ezcase) {
		//d�s qu'il arrive sur la case d'arriv�e on le renvoit sur la case de d�part
		PersonnagePrincipal personnage = level.personnage;
		float differenceH = personnage.position.x-(ezcase.position.x);
		if (Math.abs(differenceH)<1.0f && level.arriveeCaisses.size==0)  {
			numLevel++;;
			keyUp(Keys.R);

		}
	}
	private void collisionPersonnageMonstre() {
		/*
		 * Non impl�ment�
		 */
	};

	private void collisionCaisseMur(Caisse c) {
		PersonnagePrincipal personnage = level.personnage;
		switch (personnage.orientation) {
		case HAUT:
			c.position.y = c.anciennePosition.y-1;
			break;
		case BAS:
			c.position.y = c.anciennePosition.y+1;
			break;
		case GAUCHE:
			c.position.x = c.anciennePosition.x+1;
			break;
		case DROIT:
			c.position.x = c.anciennePosition.x-1;
			break;

		}
	};



	private void collisionCaissePersonnage(Caisse c) {
		PersonnagePrincipal personnage = level.personnage;

		float differenceVerticale = personnage.position.y-(c.position.y);
		float differenceHorizontale = personnage.position.x-(c.position.x);

		if (Math.abs(differenceVerticale) < 1.0f) {
			personnage.position.y = personnage.anciennePosition.y;

		}
		if (Math.abs(differenceHorizontale) < 1.0f)  {
			personnage.position.x = personnage.anciennePosition.x;
		}

	};

	private void collisionCaisseArriveeCaisse(ArriveeCaisse ac, Caisse c) {
		c.etat = ETAT_CAISSE.IMMOBILE;
		c.position = ac.position;
		c.vitesse.x = 0;
		c.vitesse.y = 0;
	};

	private void pousseCaisse(Caisse c) {
		PersonnagePrincipal p = level.personnage;
		r3.set(c.position.x,c.position.y,c.frontiere.width,c.frontiere.height);
		boolean mouvementPossible = true;
		switch (p.orientation) {
		case BAS:
			r4.set(p.position.x,p.position.y,p.frontiere.width,p.frontiere.height);
			//System.out.println(r4+"   "+r3+"\n");
			if (r4.overlaps(r3)) {
				r3.set(c.position.x,c.position.y-c.frontiere.height,c.frontiere.width,c.frontiere.height);
				for (Mur mur : level.murs) {
					r2.set(mur.position.x,mur.position.y,mur.frontiere.width,mur.frontiere.height);
					
					if(r3.overlaps(r2)) {
						
						mouvementPossible = false;
					}
				}
				for (int i = 0; i<level.caisses.size; i++) {
					Caisse c2 = level.caisses.get(i);
					if (c2!=c) {
						r2.set(c2.position.x,c2.position.y,c2.frontiere.width,c2.frontiere.height);
						
						if(r3.overlaps(r2)) {
							
							mouvementPossible = false;
						}
					}
				}
				if (mouvementPossible) {
					c.position.y = c.anciennePosition.y - 1;
				}
			}

			break;

		case HAUT:
			r4.set(p.position.x,p.position.y,p.frontiere.width,p.frontiere.height);
			//System.out.println(r4+"   "+r3+"\n");
			if (r4.overlaps(r3)) {
				r3.set(c.position.x,c.position.y,c.frontiere.width,c.frontiere.height+c.dimension.y);
				for (Mur mur : level.murs) {
					r2.set(mur.position.x,mur.position.y,mur.frontiere.width,mur.frontiere.height);
					
					if(r3.overlaps(r2)) {
						
						mouvementPossible = false;
					}
				}
				for (int i = 0; i<level.caisses.size; i++) {
					Caisse c2 = level.caisses.get(i);
					if (c2!=c) {
						r2.set(c2.position.x,c2.position.y,c2.frontiere.width,c2.frontiere.height);
						
						if(r3.overlaps(r2)) {
							
							mouvementPossible = false;
						}
					}
				}
				if (mouvementPossible) {
					c.position.y = c.anciennePosition.y + 1;
				}
			}
			break;
		case DROIT:
			r4.set(p.position.x,p.position.y,p.frontiere.width,p.frontiere.height);
			//System.out.println(r4+"   "+r3+"\n");
			if (r4.overlaps(r3)) {
				r3.set(c.position.x+c.dimension.x,c.position.y,c.frontiere.width,c.frontiere.height);
				for (Mur mur : level.murs) {
					r2.set(mur.position.x,mur.position.y,mur.frontiere.width,mur.frontiere.height);
					
					if(r3.overlaps(r2)) {
						
						mouvementPossible = false;
					}
				}
				for (int i = 0; i<level.caisses.size; i++) {
					Caisse c2 = level.caisses.get(i);
					if (c2!=c) {
						r2.set(c2.position.x,c2.position.y,c2.frontiere.width,c2.frontiere.height);
						
						if(r3.overlaps(r2)) {
							
							mouvementPossible = false;
						}
					}
				}
				if (mouvementPossible) { 
					c.position.x = c.anciennePosition.x + 1;
				}
			}
			break;
		case GAUCHE:
			r4.set(p.position.x,p.position.y,p.frontiere.width,p.frontiere.height);
			//System.out.println(r4+"   "+r3+"\n");
			if (r4.overlaps(r3)) {
				r3.set(c.position.x-c.dimension.x,c.position.y,c.frontiere.width,c.frontiere.height);
				for (Mur mur : level.murs) {
					r2.set(mur.position.x,mur.position.y,mur.frontiere.width,mur.frontiere.height);
					
					if(r3.overlaps(r2)) {
						
						mouvementPossible = false;
					}
				}
				for (int i = 0; i<level.caisses.size; i++) {
					Caisse c2 = level.caisses.get(i);
					if (c2!=c) {
						r2.set(c2.position.x,c2.position.y,c2.frontiere.width,c2.frontiere.height);
						
						if(r3.overlaps(r2)) {
							
							mouvementPossible = false;
						}
					}
				}
				if (mouvementPossible) {
					c.position.x = c.anciennePosition.x - 1;
				}
			}
			break;
		}
	}

	private void testCollision() {
		/*
		 * Les coefficients permettent d'adapter la taille de l'objet pour le moteur physique.
		 * On prend une taille d'objet plus petit que la taille de l'objet r�ellement affich�
		 * En effet, le bord des images comporte souvent une marge transparente... Ca �vite aussi
		 * De d�tecter une collision lorsque qu'on ne touche qu'� peine un �l�ment du bout du doigt de pixels...
		 */
		//r1.set(level.personnage.position.x+0.2f,level.personnage.position.y+0.2f,level.personnage.frontiere.width-0.4f,level.personnage.frontiere.height-0.4f);
		r1.set(level.personnage.position.x,level.personnage.position.y,level.personnage.frontiere.width,level.personnage.frontiere.height);

		//Caisse Collision
		if (level.personnage.pousse) {
			for (Caisse c : level.caisses) {
				if (c.etat != ETAT_CAISSE.IMMOBILE) {
					pousseCaisse(c);
				}
			}
		}

		//test pour les collisions personnage <--> mur && monstre <--> mur
		for (Mur mur : level.murs) {
			r2.set(mur.position.x,mur.position.y,mur.frontiere.width,mur.frontiere.height);
			if (r1.overlaps(r2)) {
				collisionPersonnageMur(mur);
			}
			for (Monstre m : level.monstres) {
				r3.set(m.position.x+0.2f,m.position.y+0.2f,m.frontiere.width-0.4f,m.frontiere.height-0.4f);
				if (r3.overlaps(r2))
					collisionMonstreMur(mur,m);
			}



		}
		for (Caisse c : level.caisses) {
			r3.set(c.position.x,c.position.y,c.frontiere.width,c.frontiere.height);
			//Caisse-Personnage
			if(r3.overlaps(r1)) {
				collisionCaissePersonnage(c);
			}

			//Caisse-Case arrivee des caisses
			int index= 0;
			for (ArriveeCaisse ac : level.arriveeCaisses) {
				r4.set(ac.position.x,ac.position.y,ac.frontiere.width,ac.frontiere.height);
				if (r3.overlaps(r4)) {
					collisionCaisseArriveeCaisse(ac,c);
					level.arriveeCaisses.removeIndex(index);
				}
				index++;
			}

		}



		//Test pour les collisions personnage <--> EzCase
		//d�s qu'il y a chevauchement on fait appelle a la fonction collisionpersonnageezcase
		r2.set(level.ezmaze.position.x,level.ezmaze.position.y,level.ezmaze.frontiere.width,level.ezmaze.frontiere.height);
		if(r1.overlaps(r2)) {
			collisionPersonnageEzCase(level.ezmaze);}


		//Test pour les collisions personnage <--> Monstre
		/*
		 * Non impl�ment�
		 */


	}
	//FIN DE CONTROLE DES COLLISIONS


	public int getNbMonstres() {
		int n = level.monstres.size;
		return n;
	}

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


}
