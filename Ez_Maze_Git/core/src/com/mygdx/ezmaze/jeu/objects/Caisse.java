package com.mygdx.ezmaze.jeu.objects;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.mygdx.ezmaze.jeu.Assets;

public class Caisse extends AbstractGameObject {
	/*
	 * Pour l'instant on ne stocke que le sprite correspondant au mur de pierre.
	 * Si l'on veut ajouter d'autres types de murs par la suite, on pourra le faire.
	 * Eventuellement, on passera la classe en classe abtraite et diffinira les murs
	 * de différentes manières ?
	 */
	private TextureRegion caisse;
	
	public enum ETAT_CAISSE{
		MOVIBLE,IMMOBILE;
	}
	public ETAT_CAISSE etat;
	
	public Caisse() {
		init();
	}

	private void init() {
		dimension.set(1,1);
		caisse = Assets.instance.caisse.caisse;
		frontiere.set(0,0,dimension.x,dimension.y);
		etat = ETAT_CAISSE.MOVIBLE;
		vitesseMax.set(2,2);
		frottement.set(2,2);
		vitesse.set(0,0);
	}

	@Override
	public void render(SpriteBatch batch) {
		TextureRegion reg = null;//Si on finit par avoir plusieurs textures ça sera utile (cf. p186 du wiki)
		float relX = 0;
		float relY = 0;
		
		//Dessin
		if (etat == ETAT_CAISSE.IMMOBILE) {
			batch.setColor(0f,1f,0.3f,1f);
		}
		reg = caisse;
		batch.draw(reg.getTexture(), position.x+relX, position.y+relY, origin.x, origin.y, dimension.x, dimension.y, scale.x, scale.y, rotation, reg.getRegionX(),reg.getRegionY(),reg.getRegionWidth(),reg.getRegionHeight(),false,false);
		batch.setColor(1,1,1,1);
	}
	
	@Override
	public void update(float deltaTime) {
		// TODO Auto-generated method stub
		super.update(deltaTime);
		/*
		 * Pour l'instant on s'en fiche des murs, ils vont pas bouger !
		 */
	}
	
	

}

