package com.mygdx.ezmaze.jeu.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.ezmaze.jeu.Assets;
import com.mygdx.ezmaze.jeu.objects.PersonnagePrincipal.ARME_UTILISEE;
import com.mygdx.ezmaze.jeu.objects.PersonnagePrincipal.ETAT_COMBAT;

public class Fantome extends AbstractGameObject {
	public static final String TAG = Fantome.class.getName();

	public enum ORIENTATION_FANTOME{
		GAUCHE,DROIT,HAUT,BAS;
	}
	public float DEGATS_ATTAQUE = 0.01f;
	public TextureRegion regFantome;
	public Vector2 spawn;
	public ORIENTATION_FANTOME orientation;

	/*
	 * Ici on pourra ajouter autant d'attributs que nécessaire pour les bonus
	 * comme pour les malus.
	 * Dans le principe on aura envie de faire des 'enum' des différents états
	 * possibles pour un même caractère. 
	 * EXEMPLE :
	 */

	public enum ETAT_COMBAT{
		RECHERCHE,RETOUR;
	}
	
	public float tempsRetour;
	public ETAT_COMBAT etatCombat;
	
	
	public Fantome(int x, int y) {
		init(x, y);
	
	}

	public void init(float x, float y) {
		position.set(x,y);
		dimension.set(0.5f,0.5f);
		regFantome = Assets.instance.monstre.fantome;
	
		
		//Centrer l'origine de l'image
		origin.set(dimension.x/2, dimension.y/2);
		//Paramétrage des frontières
		frontiere.set(0,0,dimension.x,dimension.y);
		//Valeurs de mobilite
		vitesseMax.set(0.5f,0.5f);
		frottement.set(1,1);
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
		spawn = new Vector2(x,y);
		etatCombat = ETAT_COMBAT.RECHERCHE;
		tempsRetour=0;
		orientation = ORIENTATION_FANTOME.BAS;

	};


	//FIN DES METHODE D'ETAT DU MONSTRE



	@Override
	public void update(float deltaTime) {
		switch (etatCombat) {
		case RECHERCHE:
			super.update(deltaTime);
			break;

		case RETOUR:
			if (tempsRetour<3) {
				tempsRetour += deltaTime;
			}
			else {
				tempsRetour=0;
				etatCombat=ETAT_COMBAT.RECHERCHE;
			}
				
			break;
		}
		
		
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
		reg = regFantome;
		switch (etatCombat) {
		case RETOUR:
			batch.setColor(1f,0.5f,0.5f,1f);//Ajout d'une transparence !
			break;

		case RECHERCHE:
			batch.setColor(1,1,1,0.5f);//Ajout d'une transparence !
			break;
		}
		
		batch.draw(reg.getTexture(), position.x, position.y, origin.x, origin.y, dimension.x, dimension.y, scale.x, scale.y, rotation, reg.getRegionX(),reg.getRegionY(),reg.getRegionWidth(),reg.getRegionHeight(),false,false);
		//Dans le cas où on aurrait modifié la couleur du batch, on la réinitialise (blanc)
		batch.setColor(1,1,1,1);
	}



}
