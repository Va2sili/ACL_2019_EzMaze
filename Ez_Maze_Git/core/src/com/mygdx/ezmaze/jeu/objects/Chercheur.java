package com.mygdx.ezmaze.jeu.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.ezmaze.jeu.Assets;
import com.mygdx.ezmaze.jeu.objects.PersonnagePrincipal.ARME_UTILISEE;
import com.mygdx.ezmaze.jeu.objects.PersonnagePrincipal.ETAT_COMBAT;

public class Chercheur extends AbstractGameObject {
	public static final String TAG = Fantome.class.getName();

	public enum ORIENTATION_FANTOME{
		GAUCHE,DROIT,HAUT,BAS;
	}
	public float DEGATS_ATTAQUE;
	public TextureRegion regChercheur;
	public ORIENTATION_FANTOME orientation;

	/*
	 * Ici on pourra ajouter autant d'attributs que nécessaire pour les bonus
	 * comme pour les malus.
	 * Dans le principe on aura envie de faire des 'enum' des différents états
	 * possibles pour un même caractère. 
	 * EXEMPLE :
	 */

	public enum ETAT_COMBAT{
		CHERCHEUR,ZOMBIE;
	}
	
	
	public ETAT_COMBAT etatCombat;
	
	
	public Chercheur(int z) {
		if (z==0) {
			initChercheur();
		}
		else
			initZombie();
	
	}

	public void initChercheur() {
		dimension.set(0.9f,0.9f);
		regChercheur = Assets.instance.monstre.chercheur;
	
		
		//Centrer l'origine de l'image
		origin.set(dimension.x/2, dimension.y/2);
		//Paramétrage des frontières
		frontiere.set(0,0,dimension.x,dimension.y);
		//Valeurs de mobilite
		vitesseMax.set(3f,3f);
		frottement.set(100,100);
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
		etatCombat = ETAT_COMBAT.CHERCHEUR;
		orientation = ORIENTATION_FANTOME.BAS;
		DEGATS_ATTAQUE = 0.05f;

	};
	public void initZombie() {
		dimension.set(0.9f,0.9f);
		regChercheur = Assets.instance.monstre.zombie;
	
		
		//Centrer l'origine de l'image
		origin.set(dimension.x/2, dimension.y/2);
		//Paramétrage des frontières
		frontiere.set(0,0,dimension.x,dimension.y);
		//Valeurs de mobilite
		vitesseMax.set(4f,4f);
		frottement.set(100,100);
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
		etatCombat = ETAT_COMBAT.ZOMBIE;
		orientation = ORIENTATION_FANTOME.BAS;
		DEGATS_ATTAQUE = 1f;

	};

	//FIN DES METHODE D'ETAT DU MONSTRE



	@Override
	public void update(float deltaTime) {
		super.update(deltaTime);
		
	}


	@Override
	protected void updateMvtY(float deltaTime) {
		super.updateMvtY(deltaTime);
	}


	@Override
	protected void updateMvtX(float deltaTime) {
		super.updateMvtX(deltaTime);

	}
	
	

	@Override
	public void render(SpriteBatch batch) {
		// TODO Auto-generated method stub
		TextureRegion reg = null;

		//On dessine le fantome
		reg = regChercheur;
		switch (orientation) {
		case DROIT:
			batch.draw(reg.getTexture(), position.x, position.y, origin.x, origin.y, dimension.x, dimension.y, scale.x, scale.y, rotation, reg.getRegionX(),reg.getRegionY(),reg.getRegionWidth(),reg.getRegionHeight(),true,false);
			break;

		case GAUCHE:
			batch.draw(reg.getTexture(), position.x, position.y, origin.x, origin.y, dimension.x, dimension.y, scale.x, scale.y, rotation, reg.getRegionX(),reg.getRegionY(),reg.getRegionWidth(),reg.getRegionHeight(),false,false);
			break;
		default:
			batch.draw(reg.getTexture(), position.x, position.y, origin.x, origin.y, dimension.x, dimension.y, scale.x, scale.y, rotation, reg.getRegionX(),reg.getRegionY(),reg.getRegionWidth(),reg.getRegionHeight(),false,false);
			break;
		}
		
		//Dans le cas où on aurrait modifié la couleur du batch, on la réinitialise (blanc)
		batch.setColor(1,1,1,1);
	}



}

