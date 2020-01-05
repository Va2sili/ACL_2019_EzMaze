package com.mygdx.ezmaze.jeu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.ezmaze.jeu.objects.Monstre;

import ezmaze.util.Constantes;

public class RenderGUI {
	public static void renderGuiNbMonstres(SpriteBatch batch, WorldController worldController) {
		float x = -15;
		float y = -15;
		batch.draw(Assets.instance.monstre.monster, x, y, 50, 50, 100, 100, 0.35f, -0.35f, 0);
		Assets.instance.police.moyen.draw(batch, ""+worldController.getNbMonstres(), x+75,y+37);
	}
	
	public static void renderGuiPdvPerso(SpriteBatch batch, WorldController worldController) {
			float x  = -15;
			float y = 125;
			batch.draw(Assets.instance.thesee.personnage, x, y, 50, 50, 100, 100, 0.35f, -0.35f, 0);
			Assets.instance.police.moyen.draw(batch, ""+String.format("%.02f", worldController.level.personnage.getPdv()), x+75,y+37);
		}
	
	
	public static void renderGuiTimeCounter(SpriteBatch batch, OrthographicCamera cameraGUI) {
		float x = cameraGUI.viewportWidth - 150;
		float y = cameraGUI.viewportHeight - 55;
		WorldController.temps +=  Gdx.graphics.getDeltaTime();
		BitmapFont timePolice = Assets.instance.police.moyen;
		if (WorldController.temps < 60) {
			timePolice.setColor(0,1,0,1);
		}
		else {
			timePolice.setColor(1,0,0,1);
		}
		timePolice.draw(batch, "Temps : "+(int)WorldController.temps, x, y);
		timePolice.setColor(1,1,1,1);
	}
	
	public static void renderNbCaissesRestantes(SpriteBatch batch, WorldController worldController) {
		float x = -15;
		float y = 55;
		batch.draw(Assets.instance.caisse.caisse, x, y, 50, 50, 100, 100, 0.35f, -0.35f, 0);
		BitmapFont timePolice = Assets.instance.police.moyen;
		int s =worldController.level.arriveeCaisses.size;
		if (s == 0) {
			timePolice.setColor(0,1,0,1);
		}
		timePolice.draw(batch, ""+s, x+75,y+37);
		timePolice.setColor(1,1,1,1);
	}
	
	public static void renderGameOver(SpriteBatch batch) {
		float x  = 200;
		float y = 200;
		BitmapFont timePolice = Assets.instance.police.gros;
		timePolice.setColor(1,0.3f,0,1);
		timePolice.draw(batch,"GAME OVER !",x,y);
		timePolice.setColor(1,1,1,1);
	}
	
	public static void renderCoeurResu(SpriteBatch batch, WorldController worldController) {
		float x = -30;
		float y = 400;
		for (int i = 0; i<worldController.resurections; i++) {
			
			batch.draw(Assets.instance.thesee.coeurResu, x+i*25, y, 50, 50, 100, 100, 0.35f, -0.35f, 0);
		}
	}
}
