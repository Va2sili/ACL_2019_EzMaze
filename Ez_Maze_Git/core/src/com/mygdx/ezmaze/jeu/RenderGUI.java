package com.mygdx.ezmaze.jeu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import ezmaze.util.Constantes;

public class RenderGUI {
	static float temps = 0;
	
	

	public static void renderGuiNbMonstres(SpriteBatch batch) {
		float x = -15;
		float y = -15;
		batch.draw(Assets.instance.monstre.monster, x, y, 50, 50, 100, 100, 0.35f, -0.35f, 0);
		Assets.instance.police.gros.draw(batch, ""+WorldController.NbMonstres, x+75,y+37);
	}
	
	public static void renderGuiTimeCounter(SpriteBatch batch, OrthographicCamera cameraGUI) {
		float x = cameraGUI.viewportWidth - 150;
		float y = cameraGUI.viewportHeight - 55;
		temps +=  Gdx.graphics.getDeltaTime();
		BitmapFont timePolice = Assets.instance.police.moyen;
		if (temps < 60) {
			timePolice.setColor(0,1,0,1);
		}
		else {
			timePolice.setColor(1,0,0,1);
		}
		timePolice.draw(batch, "Temps : "+(int)temps, x, y);
		timePolice.setColor(1,1,1,1);
	}
}
