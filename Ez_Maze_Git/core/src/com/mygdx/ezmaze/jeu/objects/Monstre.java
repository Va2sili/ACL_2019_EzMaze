package com.mygdx.ezmaze.jeu.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.mygdx.ezmaze.jeu.Assets;
import com.mygdx.ezmaze.jeu.objects.Fantome.ORIENTATION_FANTOME;
import com.mygdx.ezmaze.jeu.objects.PersonnagePrincipal.ARME_UTILISEE;
import com.mygdx.ezmaze.jeu.objects.PersonnagePrincipal.ETAT_COMBAT;
import com.mygdx.ezmaze.jeu.objects.PersonnagePrincipal.ORIENTATION_PERSONNAGE;

public class Monstre extends AbstractGameObject {
	public static final String TAG = Monstre.class.getName();

	private static float POINTS_DE_VIE = 2;
	private static float DEGATS_ATTAQUE = 1f;
	public TextureRegion regMonstre;
	public boolean marche;
	public float timeSinceCollision;
	/*
	 * Ici on pourra ajouter autant d'attributs que nécessaire pour les bonus
	 * comme pour les malus.
	 * Dans le principe on aura envie de faire des 'enum' des différents états
	 * possibles pour un même caractère. 
	 * EXEMPLE :
	 */

	public enum ETAT_COMBAT{
		RECHERCHE,ATTAQUE_SIMPLE;
	}
	public enum ARME_UTILISEE{
		NONE,AXE,KNIFE;
	}

	public enum ORIENTATION_MONSTRE{
		GAUCHE,DROITE,HAUT,BAS;
	}

	public ARME_UTILISEE armeUtilisee;
	public ETAT_COMBAT etatCombat;
	public ORIENTATION_MONSTRE orientation;
	public boolean attaque;

	public Monstre() {
		init();

	}

	public void init() {
		dimension.set(1,1);
		regMonstre = Assets.instance.monstre.monster;
		//	System.out.println(regMonstre==null);

		//Centrer l'origine de l'image
		origin.set(dimension.x/2, dimension.y/2);
		//Paramétrage des frontières
		frontiere.set(0,0,dimension.x,dimension.y);
		//Valeurs de mobilite
		vitesseMax.set(1,1);
		frottement.set(0f,0f);
		/*
		 * On donne une grande valeur aux frottements pour éviter toute glissade
		 * incontrôlée...
		 */
		acceleration.set(0,0);
		/*
		 * On met une accélaration nulle parce qu'on n'a aucune raison d'en vouloir
		 * Mais on pourra introduire des champs de forces si l'on veut 
		 */

		//Mise à jour des attributs d'état du personnage
		armeUtilisee = ARME_UTILISEE.NONE;
		etatCombat = ETAT_COMBAT.RECHERCHE;
		orientation = ORIENTATION_MONSTRE.GAUCHE;
		marche=false;
		timeSinceCollision=0;
	};


	//Les méthodes pour définir les états du monstre
	public void setAttaque (boolean joueurRepere) {
		switch (armeUtilisee) {
		case AXE: //Le personnage porte une épée
			if(joueurRepere) {
				//On compte le temps de charge de l'attaque (on ne s'en sert pas)
				etatCombat = ETAT_COMBAT.ATTAQUE_SIMPLE;
			}
			break;
		case KNIFE://le monstre porte un couteau
			if(joueurRepere) {
				//On compte le temps de charge de l'attaque
				etatCombat = ETAT_COMBAT.ATTAQUE_SIMPLE;
			}
			break;
		}
	}

	public float getPdV() {
		return POINTS_DE_VIE;
	}
	//FIN DES METHODE D'ETAT DU MONSTRE



	@Override
	public void update(float deltaTime) {
		super.update(deltaTime);
		timeSinceCollision+=deltaTime;
		if(timeSinceCollision > 1f) {
			vitesse.set(0,0);
			marche=false;
			timeSinceCollision=0;
			
		}


		switch(armeUtilisee) {
		case AXE: //Le monstre porte une hache
			//DEGATS_ATTAQUE=3f;
			etatCombat = ETAT_COMBAT.ATTAQUE_SIMPLE;
			break;
		case KNIFE://le personnage porte un couteau
			//DEGATS_ATTAQUE=2f;
			etatCombat = ETAT_COMBAT.ATTAQUE_SIMPLE;
			break;
		}
	}


	@Override
	protected void updateMvtY(float deltaTime) {

		switch(armeUtilisee) {
		case AXE: //Le monstre porte une hache
			vitesse.y = vitesse.y/1.5f;
			break;
		case KNIFE://le personnage porte un couteau
			break;
		}
		super.updateMvtY(deltaTime);
	}


	@Override
	protected void updateMvtX(float deltaTime) {

		switch(armeUtilisee) {
		case AXE: //Le monstre porte une hache
			vitesse.x = vitesse.x/1.5f;
			break;
		case KNIFE://le personnage porte un couteau
			break;
		}
		super.updateMvtX(deltaTime);

	}



	@Override
	public void render(SpriteBatch batch) {
		// TODO Auto-generated method stub
		TextureRegion reg = null;

		//On dessine le monstre
		reg = regMonstre;
		//System.out.println(regMonstre==null);
		//batch.draw(reg.getTexture(), position.x, position.y, origin.x, origin.y, dimension.x, dimension.y, scale.x, scale.y);
		batch.draw(reg.getTexture(), position.x, position.y, origin.x, origin.y, dimension.x, dimension.y, scale.x, scale.y, rotation, reg.getRegionX(),reg.getRegionY(),reg.getRegionWidth(),reg.getRegionHeight(),false,false);
		//Dans le cas où on aurrait modifié la couleur du batch, on la réinitialise (blanc)
		batch.setColor(1,1,1,1);
	}



}
