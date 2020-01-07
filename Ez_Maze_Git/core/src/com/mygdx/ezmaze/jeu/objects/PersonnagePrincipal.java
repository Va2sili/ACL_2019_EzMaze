package com.mygdx.ezmaze.jeu.objects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.mygdx.ezmaze.jeu.Assets;

public class PersonnagePrincipal extends AbstractGameObject {
	public static final String TAG = PersonnagePrincipal.class.getName();

	private static float POINTS_DE_VIE = 3;
	

	public static void setPOINTS_DE_VIE(float pOINTS_DE_VIE) {
		POINTS_DE_VIE = pOINTS_DE_VIE;
	}

	private static float DEGATS_ATTAQUE = 1f;
	private Pixmap pixmapAttaque;

	public TextureRegion regPersonnage;

	/*
	 * Ici on pourra ajouter autant d'attributs que nécessaire pour les bonus
	 * comme pour les malus.
	 * Dans le principe on aura envie de faire des 'enum' des différents états
	 * possibles pour un même caractère. 
	 * EXEMPLE :
	 */
	public enum ETAT_COMBAT{
		PAISIBLE,DEFENSE,ATTAQUE_SIMPLE,ATTAQUE_CHARGEE;
	}
	public float tempsChargeAttaque;
	public enum ARME_UTILISEE{
		NONE,EPEE,BOUCLIER,BALLON_DE_BASKET;
	}
	public ARME_UTILISEE armeUtilisee;
	public boolean attaqueChargee;
	public ETAT_COMBAT etatCombat;
	public boolean pousse;
	public enum ORIENTATION_PERSONNAGE{
		GAUCHE,DROIT,HAUT,BAS;
	}
	public ORIENTATION_PERSONNAGE orientation; 
	
	public PersonnagePrincipal() {
		// TODO Auto-generated constructor stub
		init();
	}

	public void init() {
		dimension.set(0.6f,0.8f);
		regPersonnage = Assets.instance.thesee.personnage;
		//Centrer l'origine de l'image
		origin.set(dimension.x/2, dimension.y/2);
		//Paramétrage des frontières
		frontiere.set(0,0,dimension.x,dimension.y);
		//Valeurs de mobilite
		vitesseMax.set(2,2);
		frottement.set(10,10);
		/*
		 * On donne une grande valeur aux frottements pour éviter toute glissade
		 * incontrôlée...
		 */
		acceleration.set(0,0);
		/*
		 * On met une accélaration nulle parce qu'on n'a aucune raison d'en vouloir
		 * Mais on pourra introduire des champs de forces si l'on veut ! YOUPI
		 */
		
		//Mise à jour des attributs d'état du personnage
		armeUtilisee = ARME_UTILISEE.NONE;
		etatCombat = ETAT_COMBAT.PAISIBLE;
		tempsChargeAttaque = 0f;
		orientation = ORIENTATION_PERSONNAGE.BAS;
		
		//pixmapAttaque = new Pixmap(1,1,Format.RGB888);
		//pixmapAttaque.setColor(1,0.6f,0,1);
		//pixmapAttaque.fill();
	};

	//Les méthodes pour définir les états du personnage
	public void setAttaque (boolean presseToucheAttaque) {
		switch (armeUtilisee) {
		case EPEE: //Le personnage porte une épée
			if(presseToucheAttaque) {
				//On compte le temps de charge de l'attaque (on ne s'en sert pas)
				tempsChargeAttaque = 0;
				etatCombat = ETAT_COMBAT.ATTAQUE_SIMPLE;
			}
			break;
		case BOUCLIER://le personnage porte le bouclier
			if(presseToucheAttaque) {
				//On compte le temps de charge de l'attaque
				tempsChargeAttaque = 0;
				etatCombat = ETAT_COMBAT.ATTAQUE_SIMPLE;
			}
			break;
		case BALLON_DE_BASKET://le personnage porte un ballon de basket
			/*
			 * NON IMPLEMENTE :p
			 */
			break;
		}
	}
	
	public float getPdv() {
		return POINTS_DE_VIE;
	}
	public void sommePdv(float n) {
		POINTS_DE_VIE += n;
	}
	
	//FIN DES METHODE D'ETAT DU PERSONNAGE

	
	@Override
	public void update(float deltaTime) {
		super.update(deltaTime);
		switch(armeUtilisee) {
		case EPEE: //Le personnage porte une épée
			tempsChargeAttaque += deltaTime;
			if (tempsChargeAttaque>3) {
				etatCombat = ETAT_COMBAT.ATTAQUE_CHARGEE;
				attaqueChargee = true;
			}
			break;
		case BOUCLIER://le personnage porte le bouclier
			break;
		case BALLON_DE_BASKET://le personnage porte un ballon de basket
			/*
			 * NON IMPLEMENTE :p
			 */
			break;
		}
	}
	
	@Override
	protected void updateMvtY(float deltaTime) {
		
		switch(armeUtilisee) {
		case EPEE: //Le personnage porte une épée
			break;
		case BOUCLIER://le personnage porte le bouclier
			vitesse.y = vitesse.y/1.5f;
			break;
		case BALLON_DE_BASKET://le personnage porte un ballon de basket
			/*
			 * NON IMPLEMENTE :p
			 */
			break;
		}
		super.updateMvtY(deltaTime);
	}
	@Override
	protected void updateMvtX(float deltaTime) {
		
		switch(armeUtilisee) {
		case EPEE: //Le personnage porte une épée
			break;
		case BOUCLIER://le personnage porte le bouclier
			vitesse.x = vitesse.x/1.5f;
			break;
		case BALLON_DE_BASKET://le personnage porte un ballon de basket
			/*
			 * NON IMPLEMENTE :p
			 */
			break;
		}
		super.updateMvtX(deltaTime);
		
	}
	
	@Override
	public void render(SpriteBatch batch) {
		// TODO Auto-generated method stub
		TextureRegion reg = null;
		
		
		
		
		/*
		Texture texture = new Texture(pixmapAttaque);
		Sprite spr = new Sprite(texture);
		
		spr.setOrigin(origin.x, origin.y);
		spr.setPosition(position.x, position.y);
		switch (orientation) {
		case GAUCHE:
			spr.setRotation(180);
			break;

		case DROIT:
			
			break;
		case HAUT:
			spr.setRotation(90);
			break;
		case BAS:
			spr.setRotation(-90);
			break;
		}
		
		//On dessine la zone d'attaque
		spr.draw(batch);
		batch.setColor(1,1,1,1);*/
		
		//On met une couleur rouge pour le personnage lorsque son attaque est chargée
		if (attaqueChargee) {
			batch.setColor(1f,0f,0f,1f);
		}
		//On dessine le personnage
		reg = regPersonnage;
		//batch.draw(reg.getTexture(), position.x, position.y, origin.x, origin.y, dimension.x, dimension.y, scale.x, scale.y);
		batch.draw(reg.getTexture(), position.x, position.y, origin.x, origin.y, dimension.x, dimension.y, scale.x, scale.y, rotation, reg.getRegionX(),reg.getRegionY(),reg.getRegionWidth(),reg.getRegionHeight(),false,false);
		//Dans le cas où on aurrait modifié la couleur du batch, on la réinitialise (blanc)
		batch.setColor(1,1,1,1);
	}

}
