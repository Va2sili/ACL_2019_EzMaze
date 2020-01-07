package com.mygdx.ezmaze.jeu.objects.projectiles;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.mygdx.ezmaze.jeu.Assets;
import com.mygdx.ezmaze.jeu.objects.AbstractGameObject;

public class ArmeLancee extends AbstractGameObject{

	public static final String TAG = ArmeLancee.class.getName();

	public TextureRegion regArmeLancee;
	public ArmeLancee(float x, float y, float dx, float dy) {
		init(x,y,dx,dy);

	}


	private void init(float x, float y, float dx, float dy) {
		position.set(x,y);
		vitesseMax.set(3,3);
		vitesse.set(dx*vitesseMax.x,dy*vitesseMax.y);
		frottement.set(0,0);
		dimension.set(0.3f,0.3f);
		frontiere.set(0,0,dimension.x,dimension.y);
		acceleration.set(0,0);
		regArmeLancee = Assets.instance.armelancee.fireball;


	}


	@Override
	public void render(SpriteBatch batch) {
		TextureRegion reg = null;

		//Dessin boule de feu
		reg = regArmeLancee;
		//batch.draw(reg.getTexture(), position.x, position.y, origin.x, origin.y, dimension.x, dimension.y, scale.x, scale.y);
		batch.draw(reg.getTexture(), position.x, position.y, origin.x, origin.y, dimension.x, dimension.y, scale.x, scale.y, rotation, reg.getRegionX(),reg.getRegionY(),reg.getRegionWidth(),reg.getRegionHeight(),false,false);
		//Dans le cas où on aurrait modifié la couleur du batch, on la réinitialise (blanc)
		batch.setColor(1,1,1,1);



	}
}
