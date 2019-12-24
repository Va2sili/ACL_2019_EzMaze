package com.mygdx.ezmaze.jeu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.mygdx.ezmaze.jeu.objects.AbstractGameObject;
import com.mygdx.ezmaze.jeu.objects.Case;
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
		SPAWN_MONSTRE(255,242,0);//Du jaune

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
    public Case ezmaze;

	public Level(String filename) {
		init(filename);
	}

	private void init(String filename) {
		//Personnage joueur
		personnage = null;
		//objets
		murs =  new Array<Mur>();
        ezmaze=null;
		//monstres
		monstres= new Array<Monstre>();

		System.out.println(" Empty :"+BLOCK_TYPE.EMPTY.getColor()
		+"\n PIERRE :"+BLOCK_TYPE.MUR_DE_PIERRE.getColor()
		+"\n JOUEUR :"+BLOCK_TYPE.SPAWN_JOUEUR.getColor()
		+"\n ARRIVEE :"+BLOCK_TYPE.CASE_ARRIVEE.getColor()
		+"\n MONSTRE :"+BLOCK_TYPE.SPAWN_MONSTRE.getColor()
				);


		//Chargement du fichier PNG de représentation du level
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

				//Soit c'est le spawn monstre
				else if (BLOCK_TYPE.SPAWN_MONSTRE.sameColor(pixelObserve)) {
					obj = new Monstre();
					obj.position.set(pixelX,-pixelY);
					monstres.add((Monstre)obj);
					
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
		//Dessiner le personnage joueur

		personnage.render(batch);
		for (Monstre m : monstres) {
			m.render(batch);
		}

	}

	public void update(float deltaTime) {
		personnage.update(deltaTime);
		ezmaze.update(deltaTime);
		for (Monstre m : monstres) {
			m.update(deltaTime);
		}
		for (Mur mur : murs) {
			mur.update(deltaTime);
		}
	}
}
