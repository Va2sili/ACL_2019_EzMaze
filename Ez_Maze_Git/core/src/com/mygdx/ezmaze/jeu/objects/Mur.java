package com.mygdx.ezmaze.jeu.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.mygdx.ezmaze.jeu.Assets;

public class Mur extends AbstractGameObject {
	/*
	 * Pour l'instant on ne stocke que le sprite correspondant au mur de pierre.
	 * Si l'on veut ajouter d'autres types de murs par la suite, on pourra le faire.
	 * Eventuellement, on passera la classe en classe abtraite et diffinira les murs
	 * de différentes manières ?
	 */
	private TextureRegion MurPierre;
	private int length;
	
	public Mur() {
		init();
	}

	private void init() {
		dimension.set(1,1);
		MurPierre = Assets.instance.mur.mur;
		
		//Début de la longueur
		setLength(1);
		
	}
	
	
	
	public void setLength(int length) {
		this.length = length;
		frontiere.set(0,0,dimension.x*length,dimension.y);
	}
	
	public void increaseLength(int amount) {
		setLength(length + amount);
	}

	@Override
	public void render(SpriteBatch batch) {
		TextureRegion reg = null;//Si on finit par avoir plusieurs textures ça sera utile (cf. p186 du wiki)
		float relX = 0;
		float relY = 0;
		
		//Dessin
		reg = MurPierre;
		for (int i = 0; i < length; i++) {
			batch.draw(reg.getTexture(), position.x+relX, position.y+relY, origin.x, origin.y, dimension.x, dimension.y, scale.x, scale.y, rotation, reg.getRegionX(),reg.getRegionY(),reg.getRegionWidth(),reg.getRegionHeight(),false,false);
			relX+=dimension.x;
			
		}
		
		
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
