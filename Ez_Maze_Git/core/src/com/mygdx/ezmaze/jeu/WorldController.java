package com.mygdx.ezmaze.jeu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.actions.DelayAction;
import com.badlogic.gdx.utils.Array;

import com.mygdx.ezmaze.jeu.objects.ArriveeCaisse;
import com.mygdx.ezmaze.jeu.objects.Caisse;
import com.mygdx.ezmaze.jeu.objects.Caisse.ETAT_CAISSE;
import com.mygdx.ezmaze.jeu.objects.Case;
import com.mygdx.ezmaze.jeu.objects.Fantome;
import com.mygdx.ezmaze.jeu.objects.Fantome.ORIENTATION_FANTOME;
import com.mygdx.ezmaze.jeu.objects.Monstre;
import com.mygdx.ezmaze.jeu.objects.Mur;
import com.mygdx.ezmaze.jeu.objects.PersonnagePrincipal;
import com.mygdx.ezmaze.jeu.objects.PersonnagePrincipal.ETAT_COMBAT;
import com.mygdx.ezmaze.jeu.objects.PersonnagePrincipal.ORIENTATION_PERSONNAGE;
import com.mygdx.ezmaze.jeu.objects.projectiles.ArmeLancee;
import com.mygdx.ezmaze.jeu.objects.projectiles.Projectile;

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
	public Array<Projectile> projectiles;
	public Array<ArmeLancee> armeslancees;
	public int numLevel=0;
	public int resurections=Constantes.RESU_INIT;
	public int score;

	private void initLevel() {
		score = 0;
		numLevel = numLevel%(Constantes.LEVEL.length);
		level = new Level(Constantes.LEVEL[numLevel]);
		projectiles = new Array<Projectile>();
		armeslancees = new Array<ArmeLancee>();
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
		temps = 0;
		initLevel();
	}





	//Contiendra la logique de jeu et sera appel�e plusieurs fois par seconde !
	//Le delta time permettra d'actualiser correctement en fonction du temps �coul�
	//depuis le dernier affichage de la fen�tre...
	public void update (float deltaTime) {
		if(resurections>0) {
			IaMonstre();
			IaFantome();
			handleDebugInput(deltaTime);//Il est important de prendre d'abord en compte l'action du joueur !
			handleInputGame(deltaTime);
			resurectiondupersonnage();

			//Udpdate des projectiles !
			for (Projectile p : projectiles) {
				p.update(deltaTime);
			}

			for (ArmeLancee a : armeslancees) {
				a.update(deltaTime);
			}

			level.update(deltaTime);
			testCollision();
		}
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
			else if (Gdx.input.isButtonPressed(Buttons.LEFT)) {
				if (projectiles.size==0) {
					Vector3 positionSouris = new Vector3(Gdx.input.getX(), Gdx.input.getY(),0);
					WorldRenderer.camera.unproject(positionSouris);
					//Les directions du tir
					float dx = (positionSouris.x-(level.personnage.position.x+level.personnage.origin.x))/(Math.abs(positionSouris.x-level.personnage.position.x+level.personnage.origin.x)+Math.abs(positionSouris.y-level.personnage.position.y+level.personnage.origin.y));
					float dy = (positionSouris.y-(level.personnage.position.y+level.personnage.origin.y))/(Math.abs(positionSouris.x-level.personnage.position.x+level.personnage.origin.x)+Math.abs(positionSouris.y-level.personnage.position.y+level.personnage.origin.y));
					//Cr�ation du projectile
					Projectile p = new Projectile(level.personnage.position.x+level.personnage.origin.x, level.personnage.position.y+level.personnage.origin.y, dx, dy);
					projectiles.add(p);

				}

			}
			else if(Gdx.input.isButtonPressed(Buttons.RIGHT)){
				if (armeslancees.size==0) {
					Vector3 positionSouris = new Vector3(Gdx.input.getX(), Gdx.input.getY(),0);
					WorldRenderer.camera.unproject(positionSouris);
					//Les directions du tir
					float dx = (positionSouris.x-(level.personnage.position.x+level.personnage.origin.x))/(Math.abs(positionSouris.x-level.personnage.position.x+level.personnage.origin.x)+Math.abs(positionSouris.y-level.personnage.position.y+level.personnage.origin.y));
					float dy = (positionSouris.y-(level.personnage.position.y+level.personnage.origin.y))/(Math.abs(positionSouris.x-level.personnage.position.x+level.personnage.origin.x)+Math.abs(positionSouris.y-level.personnage.position.y+level.personnage.origin.y));
					//Cr�ation du projectile
					ArmeLancee a = new ArmeLancee(level.personnage.position.x+level.personnage.origin.x, level.personnage.position.y+level.personnage.origin.y, dx, dy);
					armeslancees.add(a);

				}
			}


			else {
				level.personnage.pousse=false;
				level.personnage.setAttaque(false);
			}
		}
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

	/*private void collisionMonstreMur(Mur mur, Monstre m) {


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
	};*/

	private void collisionPersonnageEzCase(Case ezcase) {
		//d�s qu'il arrive sur la case d'arriv�e on le renvoit sur la case de d�part
		PersonnagePrincipal personnage = level.personnage;
		float differenceH = personnage.position.x-(ezcase.position.x);
		if (Math.abs(differenceH)<1.0f && level.arriveeCaisses.size==0)  {
			numLevel++;;
			keyUp(Keys.R);

		}
	}
	private void collisionPersonnagecaseboue(Case caseboue) {
		//d�s qu'il arrive sur la caseboue on r�duit sa vitesse pendant 3 secondes
		level.personnage.frottement.set(100,100);



	}

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

	private void collisionProjectile(Projectile p) {

		p.ballonCreuve = true;
		p.vitesse.set(0,0);
	}



	private void collisionArmeLancee(ArmeLancee a) {
		armeslancees.removeValue(a, false);

	}


	private void ramasseProjectile(Projectile p) {
		projectiles.removeValue(p, false);
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

			//inutile au vu de l'IA du monstre
			/*for (Monstre m : level.monstres) {
				r3.set(m.position.x+0.2f,m.position.y+0.2f,m.frontiere.width-0.4f,m.frontiere.height-0.4f);
				if (r3.overlaps(r2))
					collisionMonstreMur(mur,m);
			}*/

			for (Projectile p : projectiles) {
				if (!p.ballonCreuve) {
					r3.set(p.position.x,p.position.y,p.frontiere.width,p.frontiere.height);
					if (r2.overlaps(r3)) {
						collisionProjectile(p);
					}
				}
			}

			for (ArmeLancee a : armeslancees) {
				r3.set(a.position.x,a.position.y,a.frontiere.width,a.frontiere.height);
				if (r2.overlaps(r3)) {
					collisionArmeLancee(a);
				}

			}



		}
		for (Caisse c : level.caisses) {
			r3.set(c.position.x,c.position.y,c.frontiere.width,c.frontiere.height);
			//Caisse-Personnage
			if(r3.overlaps(r1)) {
				collisionCaissePersonnage(c);
			}
			//Ballon-Caisse
			for (Projectile p : projectiles) {
				if (!p.ballonCreuve) {
					r4.set(p.position.x,p.position.y,p.frontiere.width,p.frontiere.height);
					if (r3.overlaps(r4)) {
						collisionProjectile(p);
					}
				}
			}

			//Arme lancee-Caisse
			for (ArmeLancee a : armeslancees) {

				r4.set(a.position.x,a.position.y,a.frontiere.width,a.frontiere.height);
				if (r3.overlaps(r4)) {
					collisionArmeLancee(a);
				}

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
		//d�s qu'il y a chevauchement on fait appel a la fonction collisionpersonnageezcase
		r2.set(level.ezmaze.position.x,level.ezmaze.position.y,level.ezmaze.frontiere.width,level.ezmaze.frontiere.height);
		if(r1.overlaps(r2)) {
			collisionPersonnageEzCase(level.ezmaze);}
		//test collision personnage case boue
		r2.set(level.caseboue.position.x,level.caseboue.position.y,level.caseboue.frontiere.width,level.caseboue.frontiere.height);
		boolean onBoue = false;
		if(r1.overlaps(r2)) {
			onBoue = true;
			collisionPersonnagecaseboue(level.caseboue);
		}
		if(!onBoue) {
			level.personnage.frottement.set(10,10);
		}

		//Test pour les collisions personnage <--> Monstre


		for (Fantome f : level.fantomes) {
			//Test pour les collisions personnage <--> Fantome
			r4.set(f.position.x,f.position.y,f.frontiere.width,f.frontiere.height);
			if (r1.overlaps(r4)) {
				level.personnage.sommePdv(-f.DEGATS_ATTAQUE);
			}
			//Fantome - Ballon
			for (Projectile p : projectiles) {
				if (!p.ballonCreuve) {
					r3.set(p.position.x,p.position.y,p.frontiere.width,p.frontiere.height);
					if (r4.overlaps(r3)) {
						System.out.println("PLOP !");
						f.position.set(f.spawn.x,f.spawn.y);
						collisionProjectile(p);
					}
				}
			}
		}



		for (Monstre m : level.monstres) {
			//Test pour les collisions personnage <--> Fantome
			r4.set(m.position.x,m.position.y,m.frontiere.width,m.frontiere.height);
			if (r1.overlaps(r4)) {
				level.personnage.sommePdv(-level.personnage.getPdv());
			}

			//Monstre - Arme lancee
			for (ArmeLancee a : armeslancees) {
				r3.set(a.position.x,a.position.y,a.frontiere.width,a.frontiere.height);
				if (r4.overlaps(r3)) {
					System.out.println("BAM !");
					level.monstres.removeValue(m, false);
					collisionArmeLancee(a);
				}

			}
		}


		//Collision Personnage - Ballon creuv�
		for (Projectile p : projectiles) {
			if (p.ballonCreuve) {
				r3.set(p.position.x,p.position.y,p.frontiere.width,p.frontiere.height);
				if (r1.overlaps(r3)) {
					ramasseProjectile(p);
				}
			}
		}
	}
	//FIN DE CONTROLE DES COLLISIONS

	//IA POUR LES MONSTRES
	// IA FANTOMES
	private void IaFantome() {
		double d1,d2,d3,d4;
		for (Fantome f : level.fantomes) {
			if (f.etatCombat==Fantome.ETAT_COMBAT.RECHERCHE) {
				switch (f.orientation) {
				case HAUT:
					//Distance HAUT
					d1 = Math.pow(level.personnage.position.x-f.position.x,2)+Math.pow(level.personnage.position.y-f.position.y-1,2);
					//Distance DROIT
					d2 = Math.pow(level.personnage.position.x-f.position.x-1,2)+Math.pow(level.personnage.position.y-f.position.y,2);
					//Distance GAUCHE
					d4 = Math.pow(level.personnage.position.x-f.position.x+1,2)+Math.pow(level.personnage.position.y-f.position.y+1,2);

					if (d1<d2) {
						if(d1<d4) {//On va en HAUT
							f.vitesse.y = f.vitesseMax.y;
						}
						else {//On va � GAUCHE
							f.vitesse.x = - f.vitesseMax.x;
							f.orientation = Fantome.ORIENTATION_FANTOME.GAUCHE;
						}
					}
					else {
						if (d2<d4) {//On va � DROITE
							f.vitesse.x = f.vitesseMax.x;
							f.orientation = Fantome.ORIENTATION_FANTOME.DROIT;
						}
						else {//On va � gauche
							f.vitesse.x = - f.vitesseMax.x;
							f.orientation = Fantome.ORIENTATION_FANTOME.GAUCHE;
						}
					}

					break;

				case BAS:
					//Distance DROIT
					d2 = Math.pow(level.personnage.position.x-f.position.x-1,2)+Math.pow(level.personnage.position.y-f.position.y,2);
					//Distance BAS
					d3 = Math.pow(level.personnage.position.x-f.position.x,2)+Math.pow(level.personnage.position.y-f.position.y+1,2);
					//Distance GAUCHE
					d4 = Math.pow(level.personnage.position.x-f.position.x+1,2)+Math.pow(level.personnage.position.y-f.position.y+1,2);
					if (d3<d2) {
						if(d3<d4) {//On va en BAS
							f.vitesse.y = -f.vitesseMax.y;
						}
						else {//On va � GAUCHE
							f.vitesse.x = - f.vitesseMax.x;
							f.orientation = Fantome.ORIENTATION_FANTOME.GAUCHE;
						}
					}
					else {
						if (d2<d4) {//On va � DROITE
							f.vitesse.x = f.vitesseMax.x;
							f.orientation = Fantome.ORIENTATION_FANTOME.DROIT;
						}
						else {//On va � GAUCHE
							f.vitesse.x = - f.vitesseMax.x;
							f.orientation = Fantome.ORIENTATION_FANTOME.GAUCHE;
						}
					}
					break;
				case GAUCHE:
					d1 = Math.pow(level.personnage.position.x-f.position.x,2)+Math.pow(level.personnage.position.y-f.position.y-1,2);//Distance HAUT
					d3 = Math.pow(level.personnage.position.x-f.position.x,2)+Math.pow(level.personnage.position.y-f.position.y+1,2);//Distance BAS
					d4 = Math.pow(level.personnage.position.x-f.position.x+1,2)+Math.pow(level.personnage.position.y-f.position.y+1,2);//Distance GAUCHE
					if (d1<d3) {
						if(d1<d4) {//On va en HAUT
							f.vitesse.y = f.vitesseMax.y;
							f.orientation = Fantome.ORIENTATION_FANTOME.HAUT;
						}
						else {//On va � GAUCHE
							f.vitesse.x = - f.vitesseMax.x;

						}
					}
					else {
						if (d3<d4) {//On va � BAS
							f.vitesse.y = -f.vitesseMax.y;
							f.orientation = Fantome.ORIENTATION_FANTOME.BAS;
						}
						else {//On va � GAUCHE
							f.vitesse.x = - f.vitesseMax.x;

						}
					}
					break;
				case DROIT:
					d1 = Math.pow(level.personnage.position.x-f.position.x,2)+Math.pow(level.personnage.position.y-f.position.y-1,2);//Distance HAUT
					d2 = Math.pow(level.personnage.position.x-f.position.x-1,2)+Math.pow(level.personnage.position.y-f.position.y,2);//Distance DROIT
					d3 = Math.pow(level.personnage.position.x-f.position.x,2)+Math.pow(level.personnage.position.y-f.position.y+1,2);//Distance BAS
					if (d1<d3) {
						if(d1<d2) {//On va en HAUT
							f.vitesse.y = f.vitesseMax.y;
							f.orientation = Fantome.ORIENTATION_FANTOME.HAUT;
						}
						else {//On va � DROITE
							f.vitesse.x = f.vitesseMax.x;

						}
					}
					else {
						if (d3<d2) {//On va � BAS
							f.vitesse.y = -f.vitesseMax.y;
							f.orientation = Fantome.ORIENTATION_FANTOME.BAS;
						}
						else {//On va � DROITE
							f.vitesse.x = f.vitesseMax.x;

						}
					}
					break;
				}
			}
		}
	}

	private void IaMonstre() {

		//Mouvements du monstre
		//System.out.println(level.murs.size);
		for (Monstre m : level.monstres) {

			if (m.etatCombat==Monstre.ETAT_COMBAT.RECHERCHE && !m.marche) {
				m.marche = true;

				List<float[][]> differences = new ArrayList<float[][]>(); //liste contenant les diff horizont et vert des murs adjacents aux monstres

				for (Mur mur : level.murs) {
					float differenceVerticale = m.position.y-(mur.position.y);
					float differenceHorizontale = m.position.x-(mur.position.x);

					if(Math.abs(differenceVerticale)<=1 && Math.abs(differenceHorizontale)<=1 && Math.abs(differenceVerticale)!=Math.abs(differenceHorizontale)) { //choix des murs adjacents uniquement
						float[][]diff=new float[2][1];
						diff[0][0]=differenceVerticale;
						diff[1][0]=differenceHorizontale;

						differences.add(diff);
					}
				}

				for(float[][]i:differences) {
					System.out.println(Arrays.toString(i[0])+" "+Arrays.toString(i[1]));
				}
				System.out.println(m.orientation);


				float[][]droite=new float[2][1];
				droite[0][0]=0f;
				droite[1][0]=-1f;

				float[][]gauche=new float[2][1];
				gauche[0][0]=0f;
				gauche[1][0]=1f;

				float[][]haut=new float[2][1];
				haut[0][0]=-1f;
				haut[1][0]=0f;

				float[][]bas=new float[2][1];
				bas[0][0]=1f;
				bas[1][0]=0f;

				int n=differences.size();
				boolean[] config=new boolean[6];		
				config[0]=false;
				config[1]=false;
				config[2]=false;
				config[3]=false;
				config[4]=false;
				config[5]=false;

				if(n==3) {		//config 1 |-_	config 2 |-| config3 -_| config4 |_|
					if(differences.get(0)[0][0]==-1 && differences.get(1)[1][0]==1 && differences.get(2)[1][0]==0) config[0]=true;
					else if(differences.get(0)[0][0]==-1 && differences.get(1)[1][0]==1 && differences.get(2)[1][0]==-1) config[1]=true;
					else if(differences.get(0)[0][0]==-1 && differences.get(1)[1][0]==-1) config[2]=true;
					else config[3]=true;
				}

				if(n==2) {		//config 1 |-	config 2 -|	config3 _|	config4 |_	config5 | |	config6 -_
					if(differences.get(0)[0][0]==-1 && differences.get(1)[1][0]==1) config[0]=true;
					else if(differences.get(0)[0][0]==-1 && differences.get(1)[1][0]==-1) config[1]=true;
					else if(differences.get(0)[0][0]==-1) config[5]=true;
					else if(differences.get(0)[1][0]==1 && differences.get(1)[1][0]==-1) config[4]=true;
					else if(differences.get(0)[1][0]==1) config[3]=true;
					else config[2]=true;
				}

				if(n==1) {		//config 1 -	config 2 |. config3 .| config4 _
					if(differences.get(0)[0][0]==-1) config[0]=true;
					else if(differences.get(0)[1][0]==1) config[1]=true;
					else if(differences.get(0)[1][0]==-1) config[2]=true;
					else config[3]=true;
				}
				System.out.println(Arrays.toString(config));

				switch (m.orientation) {
				case HAUT:
					//Distance HAUT
					if (n==3) {
						if(config[0]) {
							m.position.x++; 
							//m.vitesse.x=m.vitesseMax.x;
							m.orientation=Monstre.ORIENTATION_MONSTRE.DROITE;
						}

						else if(config[1]) {
							m.position.y--; 
							//m.vitesse.y=-m.vitesseMax.y;
							m.orientation=Monstre.ORIENTATION_MONSTRE.BAS;
						}

						else if(config[2]) {
							m.position.x--; 
							//m.vitesse.x=-m.vitesseMax.x;
							m.orientation=Monstre.ORIENTATION_MONSTRE.GAUCHE;
						}

						else if(config[3]) {
							m.position.y++;
							//m.vitesse.y=m.vitesseMax.y;
							m.orientation=Monstre.ORIENTATION_MONSTRE.HAUT;
						}

					}

					if (n==2) {
						if(config[0]) {
							m.position.x++; 
							//m.vitesse.x=m.vitesseMax.x;
							m.orientation=Monstre.ORIENTATION_MONSTRE.DROITE;
						}

						else if(config[1]) {
							m.position.x--; 
							//m.vitesse.x=-m.vitesseMax.x;
							m.orientation=Monstre.ORIENTATION_MONSTRE.GAUCHE;
						}

						else if(config[2]) {
							m.position.y++; 
							//m.vitesse.y=m.vitesseMax.y;
							m.orientation=Monstre.ORIENTATION_MONSTRE.HAUT;
						}

						else if(config[3]) {
							m.position.x++; 
							//m.vitesse.x=m.vitesseMax.x;
							m.orientation=Monstre.ORIENTATION_MONSTRE.DROITE;
						}

						else if(config[4]) {
							m.position.y++; 
							//m.vitesse.y=m.vitesseMax.y;
							m.orientation=Monstre.ORIENTATION_MONSTRE.HAUT;
						}

						else if(config[5]) {
							m.position.x++; 
							//m.vitesse.x=m.vitesseMax.x;
							m.orientation=Monstre.ORIENTATION_MONSTRE.DROITE;
						}

					}

					if (n==1) {
						if(config[0]) {
							m.position.x++; 
							//m.vitesse.x=m.vitesseMax.x;
							m.orientation=Monstre.ORIENTATION_MONSTRE.DROITE;
						}

						else if(config[1]) {
							m.position.x++; 
							//m.vitesse.x=m.vitesseMax.x;
							m.orientation=Monstre.ORIENTATION_MONSTRE.DROITE;
						}

						else if(config[2]) {
							m.position.y++; 
							//m.vitesse.y=m.vitesseMax.y;
							m.orientation=Monstre.ORIENTATION_MONSTRE.HAUT;
						}

						else if(config[3]) {
							m.position.x++; 
							//m.vitesse.x=m.vitesseMax.x;
							m.orientation=Monstre.ORIENTATION_MONSTRE.DROITE;
						}

					}

					break;

				case BAS:
					//Distance DROIT
					if (n==3) {
						if(config[0]) {
							m.position.x++; 
							//m.vitesse.x=m.vitesseMax.x;
							m.orientation=Monstre.ORIENTATION_MONSTRE.DROITE;
						}

						else if(config[1]) {
							m.position.y--; 
							//m.vitesse.y=-m.vitesseMax.y;
							m.orientation=Monstre.ORIENTATION_MONSTRE.BAS;
						}

						else if(config[2]) {
							m.position.x--; 
							//m.vitesse.x=-m.vitesseMax.x;
							m.orientation=Monstre.ORIENTATION_MONSTRE.GAUCHE;
						}

						else if(config[3]) {
							m.position.y++; 
							//m.vitesse.y=m.vitesseMax.y;
							m.orientation=Monstre.ORIENTATION_MONSTRE.HAUT;
						}

					}

					if (n==2) {
						if(config[0]) {
							m.position.y--; 
							//m.vitesse.y=-m.vitesseMax.y;
							m.orientation=Monstre.ORIENTATION_MONSTRE.BAS;
						}

						else if(config[1]) {
							m.position.y--; 
							//m.vitesse.y=-m.vitesseMax.y;
							m.orientation=Monstre.ORIENTATION_MONSTRE.BAS;
						}

						else if(config[2]) {
							m.position.x--;
							//m.vitesse.x=-m.vitesseMax.x;
							m.orientation=Monstre.ORIENTATION_MONSTRE.GAUCHE;
						}

						else if(config[3]) {
							m.position.x++; 
							//m.vitesse.x=m.vitesseMax.x;
							m.orientation=Monstre.ORIENTATION_MONSTRE.DROITE;
						}

						else if(config[4]) {
							m.position.y--; 
							//m.vitesse.y=-m.vitesseMax.y;
							m.orientation=Monstre.ORIENTATION_MONSTRE.BAS;
						}

						else if(config[5]) {
							m.position.x--; 
							//m.vitesse.x=-m.vitesseMax.x;
							m.orientation=Monstre.ORIENTATION_MONSTRE.GAUCHE;
						}

					}

					if (n==1) {
						if(config[0]) {
							m.position.x--; 
							//m.vitesse.x=-m.vitesseMax.x;
							m.orientation=Monstre.ORIENTATION_MONSTRE.GAUCHE;
						}

						else if(config[1]) {
							m.position.y--; 
							//m.vitesse.y=-m.vitesseMax.y;
							m.orientation=Monstre.ORIENTATION_MONSTRE.BAS;
						}

						else if(config[2]) {
							m.position.x--; 
							//m.vitesse.x=-m.vitesseMax.x;
							m.orientation=Monstre.ORIENTATION_MONSTRE.GAUCHE;
						}

						else if(config[3]) {
							m.position.x--;
							//m.vitesse.x=-m.vitesseMax.x;
							m.orientation=Monstre.ORIENTATION_MONSTRE.GAUCHE;
						}
					}
					break;
				case GAUCHE:
					if (n==3) {
						if(config[0]) {
							m.position.x++; 
							//m.vitesse.x=m.vitesseMax.x;
							m.orientation=Monstre.ORIENTATION_MONSTRE.DROITE;
						}

						else if(config[1]) {
							m.position.y--; 
							//m.vitesse.y=-m.vitesseMax.y;
							m.orientation=Monstre.ORIENTATION_MONSTRE.BAS;
						}

						else if(config[2]) {
							m.position.x--;
							//m.vitesse.x=-m.vitesseMax.x;
							m.orientation=Monstre.ORIENTATION_MONSTRE.GAUCHE;
						}

						else if(config[3]) {
							m.position.y++; 
							//m.vitesse.y=m.vitesseMax.y;
							m.orientation=Monstre.ORIENTATION_MONSTRE.HAUT;
						}

					}

					if (n==2) {
						if(config[0]) {
							m.position.y--; 
							//m.vitesse.y=-m.vitesseMax.y;
							m.orientation=Monstre.ORIENTATION_MONSTRE.BAS;
						}

						else if(config[1]) {
							m.position.x--; 
							//m.vitesse.x=-m.vitesseMax.x;
							m.orientation=Monstre.ORIENTATION_MONSTRE.GAUCHE;
						}

						else if(config[2]) {
							m.position.x--; 
							//m.vitesse.x=-m.vitesseMax.x;
							m.orientation=Monstre.ORIENTATION_MONSTRE.GAUCHE;
						}

						else if(config[3]) {
							m.position.y++; 
							//m.vitesse.y=m.vitesseMax.y;
							m.orientation=Monstre.ORIENTATION_MONSTRE.HAUT;
						}

						else if(config[4]) {
							m.position.y++; 
							//m.vitesse.y=m.vitesseMax.y;
							m.orientation=Monstre.ORIENTATION_MONSTRE.HAUT;
						}

						else if(config[5]) {
							m.position.x--; 
							//m.vitesse.x=-m.vitesseMax.x;
							m.orientation=Monstre.ORIENTATION_MONSTRE.GAUCHE;
						}

					}

					if (n==1) {
						if(config[0]) {
							m.position.x--; 
							//m.vitesse.x=-m.vitesseMax.x;
							m.orientation=Monstre.ORIENTATION_MONSTRE.GAUCHE;
						}

						else if(config[1]) {
							m.position.y++;
							//m.vitesse.y=m.vitesseMax.y;
							m.orientation=Monstre.ORIENTATION_MONSTRE.HAUT;
						}

						else if(config[2]) {
							m.position.y++; 
							//m.vitesse.y=m.vitesseMax.y;
							m.orientation=Monstre.ORIENTATION_MONSTRE.HAUT;
						}

						else if(config[3]) {
							m.position.y++;
							//m.vitesse.y=m.vitesseMax.y;
							m.orientation=Monstre.ORIENTATION_MONSTRE.HAUT;
						}
					}
					break;
				case DROITE:
					if (n==3) {
						if(config[0]) {
							m.position.x++; 
							//m.vitesse.x=m.vitesseMax.x;
							m.orientation=Monstre.ORIENTATION_MONSTRE.DROITE;
						}

						else if(config[1]) {
							m.position.y--; 
							//m.vitesse.y=-m.vitesseMax.y;
							m.orientation=Monstre.ORIENTATION_MONSTRE.BAS;
						}

						else if(config[2]) {
							m.position.x--; 
							//m.vitesse.x=-m.vitesseMax.x;
							m.orientation=Monstre.ORIENTATION_MONSTRE.GAUCHE;
						}

						else if(config[3]) {
							m.position.y++; 
							//m.vitesse.y=m.vitesseMax.y;
							m.orientation=Monstre.ORIENTATION_MONSTRE.HAUT;
						}

					}

					if (n==2) {
						if(config[0]) {
							m.position.x++; 
							//m.vitesse.x=m.vitesseMax.x;
							m.orientation=Monstre.ORIENTATION_MONSTRE.DROITE;
						}

						else if(config[1]) {
							m.position.y--; 
							//m.vitesse.y=-m.vitesseMax.y;
							m.orientation=Monstre.ORIENTATION_MONSTRE.BAS;
						}

						else if(config[2]) {
							m.position.y++; 
							//m.vitesse.y=m.vitesseMax.y;
							m.orientation=Monstre.ORIENTATION_MONSTRE.HAUT;
						}

						else if(config[3]) {
							m.position.x++; 
							//m.vitesse.x=m.vitesseMax.x;
							m.orientation=Monstre.ORIENTATION_MONSTRE.DROITE;
						}

						else if(config[4]) {
							m.position.y--; 
							//m.vitesse.y=-m.vitesseMax.y;
							m.orientation=Monstre.ORIENTATION_MONSTRE.BAS;
						}

						else if(config[5]) {
							m.position.x++; 
							//m.vitesse.x=m.vitesseMax.x;
							m.orientation=Monstre.ORIENTATION_MONSTRE.DROITE;
						}

					}

					if (n==1) {
						if(config[0]) {
							m.position.y--; 
							//m.vitesse.y=-m.vitesseMax.y;
							m.orientation=Monstre.ORIENTATION_MONSTRE.BAS;
						}

						else if(config[1]) {
							m.position.y--;
							//m.vitesse.y=-m.vitesseMax.y;
							m.orientation=Monstre.ORIENTATION_MONSTRE.BAS;
						}

						else if(config[2]) {
							m.position.y--; 
							//m.vitesse.y=-m.vitesseMax.y;
							m.orientation=Monstre.ORIENTATION_MONSTRE.BAS;
						}

						else if(config[3]) {
							m.position.x++; 
							//m.vitesse.x=m.vitesseMax.x;
							m.orientation=Monstre.ORIENTATION_MONSTRE.DROITE;
						}
					}
					break;
				}
				System.out.println(m.orientation);
				System.out.println();

			}

		}
	}



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

	private void resurectiondupersonnage() {
		if(level.personnage.getPdv()<=0) {
			resurections=resurections-1;
			PersonnagePrincipal.setPOINTS_DE_VIE(3);
			keyUp(Keys.R);
		}

	}


}
