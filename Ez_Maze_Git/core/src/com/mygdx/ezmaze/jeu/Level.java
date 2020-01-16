package com.mygdx.ezmaze.jeu;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.print.DocFlavor.URL;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.mygdx.ezmaze.jeu.objects.AbstractGameObject;
import com.mygdx.ezmaze.jeu.objects.ArriveeCaisse;
import com.mygdx.ezmaze.jeu.objects.Caisse;
import com.mygdx.ezmaze.jeu.objects.Case;
import com.mygdx.ezmaze.jeu.objects.Chercheur;
import com.mygdx.ezmaze.jeu.objects.Fantome;
import com.mygdx.ezmaze.jeu.objects.Monstre;
import com.mygdx.ezmaze.jeu.objects.Mur;
import com.mygdx.ezmaze.jeu.objects.PersonnagePrincipal;

public class Level {
	public static final String TAG = Level.class.getName();

	//On définit comment on doit lire les levels
	public enum BLOCK_TYPE{
		EMPTY(0,0,0),//Du noir
		MUR_DE_PIERRE(255,255,255),//Du blanc
		SPAWN_JOUEUR(255,0,0),//Du rouge
		CASE_ARRIVEE(0,255,0),//Du vert
		SPAWN_FANTOME(112,176,144),//Du verdatre
		SPAWN_MONSTRE(255,242,0),//Du jaune
		CAISSE(255,0,255),//Du magenta
		CASE_BOUE(211,211,211),//lightgray
		PLACE_CAISSE(0,255,255),//Du cyan
		CASE_TP(160,80,160),//Du violet
		CASE_TPOUT(160,0,160),//Du violet aussi
		SPAWN_CHERCHEUR(70,40,13),//Du marron
		SPAWN_ZOMBIE(130,190,180);//Du bleute

		private int color;


		private BLOCK_TYPE(int r, int g, int b) {		//def de l'enumeration (conversion rgb en rgba)
			color = r << 24 | g << 16 | b << 8 | 255;
		}



		public boolean sameColor (int color) {
			return this.color== color;
		}

		public int getColor() {
			return(color);
		}
	}

	//objets (pour le moment il n'y a que des murs, un personnage et un monstre !
	public Array<Mur> murs;
	public PersonnagePrincipal personnage;
	public Array<Monstre> monstres;
	public Array<Fantome> fantomes;
	public Array<Chercheur> chercheurs;
	public Case ezmaze;
	public Array<Case> caseboues;
	public Array<Caisse> caisses;
	public Array<ArriveeCaisse> arriveeCaisses;
	public Array<Case> casetps;
	public Array<Case> casetpouts;
	public Array<Integer> casetpparam;


	public Level(String filename) {
		init(filename);
	}

	private void init(String filename) {
		//Personnage joueur
		personnage = null;
		//objets
		murs =  new Array<Mur>();
		ezmaze=null;
		caseboues=new Array<Case>();
		casetps=new Array<Case>();
		casetpouts=new Array<Case>();
		casetpparam =new Array<Integer>();
		//monstres
		monstres= new Array<Monstre>();
		fantomes = new Array<Fantome>();
		chercheurs = new Array<Chercheur>();
		caisses = new Array<Caisse>();
		arriveeCaisses = new Array<ArriveeCaisse>();

		//Chargement du fichier PNG de représentation du level
		try {
			FileHandle handle = Gdx.files.internal(filename+".caseTP.txt");
			String text = handle.readString();
			String wordsArray[] = text.split("\\r?\\n");
			for(String word : wordsArray) {
				casetpparam.add(Integer.valueOf(word));
			}
		}
		catch (Exception e){

		}



		Pixmap pixmap = new Pixmap(Gdx.files.internal(filename));

		//On analyse les pixels de haut-gauche à bas-droit

		for (int pixelY=0; pixelY < pixmap.getHeight(); pixelY++) {
			//int lastPixel=0;
			for (int pixelX = 0; pixelX < pixmap.getWidth(); pixelX++) {
				AbstractGameObject obj = null;

				//On récupère la couleur du pixel ananlysé sous forme d'une valeur RGBA en 32-bit
				int pixelObserve = pixmap.getPixel(pixelX,pixelY);


				/*
				 * On doit maintenant trouver quelle est la couleur pour savoir
				 * quel est le type d'objets que l'on doit afficher à ces coordonnées
				 * dans notre jeu !
				 */

				//Soit il n'y a rien à afficher :
				if (BLOCK_TYPE.EMPTY.sameColor(pixelObserve)) {
					//On n'affiche rien
				}

				//Soit on affiche un mur
				else if (BLOCK_TYPE.MUR_DE_PIERRE.sameColor(pixelObserve)) {
					//if (lastPixel != pixelObserve) {
					obj = new Mur();
					//float heightIncreaseFactor = 0.25f;
					//offsetHeight = -2.5f;
					obj.position.set(pixelX,-pixelY);
					murs.add((Mur)obj);
					//}
					//					else {
					//						//Au lieu de réafficher un mur, on étend simplement la longueur du mur en cours de construction
					//						murs.get(murs.size-1).increaseLength(1);
					//					}
				}

				//Soit c'est le spawn joueur
				else if (BLOCK_TYPE.SPAWN_JOUEUR.sameColor(pixelObserve)) {
					obj = new PersonnagePrincipal();
					obj.position.set(pixelX,-pixelY);
					personnage = (PersonnagePrincipal) obj;
				}
				//Soit c'est la case de fin
				else if (BLOCK_TYPE.CASE_ARRIVEE.sameColor(pixelObserve)) {
					obj=new Case();
					obj.position.set(pixelX, -pixelY);
					ezmaze = (Case) obj;
				}
				else if (BLOCK_TYPE.CASE_BOUE.sameColor(pixelObserve)) {
					obj=new Case();
					obj.position.set(pixelX, -pixelY);
					caseboues.add((Case) obj);
				}

				else if (BLOCK_TYPE.CASE_TP.sameColor(pixelObserve)) {
					obj=new Case();
					obj.position.set(pixelX, -pixelY);
					casetps.add((Case) obj);
				}
				else if (BLOCK_TYPE.CASE_TPOUT.sameColor(pixelObserve)) {
					obj=new Case();
					obj.position.set(pixelX, -pixelY);
					casetpouts.add((Case) obj);
				}
				//Soit c'est le spawn monstre
				else if (BLOCK_TYPE.SPAWN_MONSTRE.sameColor(pixelObserve)) {
					obj = new Monstre();
					obj.position.set(pixelX,-pixelY);
					monstres.add((Monstre)obj);

				}
				else if (BLOCK_TYPE.SPAWN_CHERCHEUR.sameColor(pixelObserve)) {
					obj = new Chercheur(0);
					obj.position.set(pixelX,-pixelY);
					chercheurs.add((Chercheur)obj);

				}
				else if (BLOCK_TYPE.SPAWN_ZOMBIE.sameColor(pixelObserve)) {
					obj = new Chercheur(1);
					obj.position.set(pixelX,-pixelY);
					chercheurs.add((Chercheur)obj);

				}
				else if (BLOCK_TYPE.CAISSE.sameColor(pixelObserve)) {
					obj = new Caisse();
					obj.position.set(pixelX,-pixelY);
					caisses.add((Caisse)obj);

				}
				else if (BLOCK_TYPE.PLACE_CAISSE.sameColor(pixelObserve)) {
					obj = new ArriveeCaisse();
					obj.position.set(pixelX,-pixelY);
					arriveeCaisses.add((ArriveeCaisse)obj);

				}
				else if (BLOCK_TYPE.SPAWN_FANTOME.sameColor(pixelObserve)) {
					obj = new Fantome(pixelX,-pixelY);
					fantomes.add((Fantome)obj);

				}

				//Sinon c'est un objet inconnu
				else {
					Gdx.app.error(TAG, "Objet inconnu aux coordonnées x : "+pixelX+" y : "+pixelY);
				}




				//lastPixel = pixelObserve;
			}
		}

		/*
		 * Ici on peut mettre des éléments qui s'affichent toujours aux même endroits 
		 * mais nous on n'en a pas pour le moment...
		 */

		//On libère de la mémoire autant que l'on peut
		pixmap.dispose();
		Gdx.app.debug(TAG,"Le niveau '"+filename+"' est correctement chargé !");


	}

	public void render (SpriteBatch batch) {
		/*
		 * Quand on dessine, les couches de dessin sont mises les unes 
		 * par dessus les autres.
		 * Le dernier élément ajouter est celui situé sur le layer le plus
		 * élevé !
		 */
		//Dessiner les murs
		for (Mur mur : murs) {
			mur.render(batch);
		}
		//on dessine la case d'arrivée
		ezmaze.render(batch);


		for (Case c : caseboues) {
			c.render1(batch);
		}
		for (Case c : casetps) {
			c.render2(batch);
		}

		for (Caisse c : caisses) {
			c.render(batch);
		}
		for (ArriveeCaisse ac : arriveeCaisses) {
			ac.render(batch);
		}
		for (Monstre m : monstres) {
			m.render(batch);
		}
		for (Chercheur c : chercheurs) {
			c.render(batch);
		}

		for (Fantome f : fantomes) {
			f.render(batch);
		}
		personnage.render(batch);

	}

	public void update(float deltaTime) {
		personnage.update(deltaTime);
		ezmaze.update(deltaTime);
		for (Case c : caseboues) {
			c.update(deltaTime);
		}
		for (Case c : casetps) {
			c.update(deltaTime);
		}
		for (Monstre m : monstres) {
			m.update(deltaTime);
		}
		for (Mur mur : murs) {
			mur.update(deltaTime);
		}
		for (Caisse c : caisses) {
			c.update(deltaTime);
		}
		for (ArriveeCaisse ac : arriveeCaisses) {
			ac.update(deltaTime);
		}
		for (Fantome f : fantomes) {
			f.update(deltaTime);
		}
		for (Chercheur c : chercheurs) {
			c.update(deltaTime);
		}

	}
}
