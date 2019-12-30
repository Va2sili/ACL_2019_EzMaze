package com.mygdx.ezmaze.jeu.objects.projectiles;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.mygdx.ezmaze.jeu.Assets;
import com.mygdx.ezmaze.jeu.objects.AbstractGameObject;


public class Projectile extends AbstractGameObject {
	public static final String TAG = Projectile.class.getName();

	public TextureRegion regProjectile;
	public boolean ballonCreuve;
	public Projectile(float x, float y, float dx, float dy) {
		init(x,y,dx,dy);

	}


	private void init(float x, float y, float dx, float dy) {
		position.set(x,y);
		vitesseMax.set(2,2);
		vitesse.set(dx*vitesseMax.x,dy*vitesseMax.y);
		frottement.set(0,0);
		dimension.set(0.2f,0.2f);
		frontiere.set(0,0,dimension.x,dimension.y);
		acceleration.set(0,0);
		ballonCreuve = false;
		regProjectile = Assets.instance.projectile.ballon;
	}


	@Override
	public void render(SpriteBatch batch) {
		TextureRegion reg = null;

		//On met une couleur rouge pour le ballon lorsqu'il est creuvé
		if (ballonCreuve) {
			batch.setColor(1f,0f,0f,1f);
		}
		//Dessin du ballon
		reg = regProjectile;
		//batch.draw(reg.getTexture(), position.x, position.y, origin.x, origin.y, dimension.x, dimension.y, scale.x, scale.y);
		batch.draw(reg.getTexture(), position.x, position.y, origin.x, origin.y, dimension.x, dimension.y, scale.x, scale.y, rotation, reg.getRegionX(),reg.getRegionY(),reg.getRegionWidth(),reg.getRegionHeight(),false,false);
		//Dans le cas où on aurrait modifié la couleur du batch, on la réinitialise (blanc)
		batch.setColor(1,1,1,1);




	}

}
