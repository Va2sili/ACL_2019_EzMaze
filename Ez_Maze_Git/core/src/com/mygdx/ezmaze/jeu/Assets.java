

package com.mygdx.ezmaze.jeu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetErrorListener;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.utils.Disposable;

import ezmaze.util.Constantes;

public class Assets implements Disposable, AssetErrorListener {
	public static final String TAG = Assets.class.getName();

	public static final Assets instance = new Assets();
	private AssetManager assetManager;

	public AssetThesee thesee;
	public AssetMur mur;
	public AssetCase carre;//On ne peut pas utiliser le nom 'case'
	public AssetMonstre monstre;
	public AssetCaseBoue CaseBoue;
	public AssetPoliceEcriture police;
	public AssetCaisse caisse;
	public AssetProjectile projectile;
	



	//On se pr�munit de l'instantiation par d'autres classes
	private Assets() {}

	//Ne sera appel�e qu'au d�marrage du jeu
	public void init(AssetManager assetManager) {
		this.assetManager = assetManager;
		//On permet � l'asset manager de comprendre les erreurs
		assetManager.setErrorListener(this);
		//On charge L'atlas de textures
		assetManager.load(Constantes.TEXTURE_ATLAS_OBJECTS,TextureAtlas.class);
		//On commence � charger et on attend jusqu'� ce que ce soit finit
		assetManager.finishLoading();
		Gdx.app.debug(TAG, "Nombre d'assets charg�s : "+assetManager.getAssetNames().size);
		for (String a : assetManager.getAssetNames()) {
			Gdx.app.debug(TAG, "asset : "+a);
		}

		TextureAtlas atlas = assetManager.get(Constantes.TEXTURE_ATLAS_OBJECTS);

		//On autorise le filtering pour adoucir la pixelisation
		for (Texture t : atlas.getTextures()) {
			t.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		}

		//On cr�e les objets ressources d'affichage du jeu
		thesee = new AssetThesee(atlas);
		mur = new AssetMur(atlas);
		carre = new AssetCase(atlas);
		monstre =new AssetMonstre(atlas);
		CaseBoue=new AssetCaseBoue(atlas);
		caisse = new AssetCaisse(atlas);
		projectile = new AssetProjectile(atlas);
		police = new AssetPoliceEcriture();
		
	}

	/*
	 * D�finition de quelques inner-classes qui permettront de regrouper logiquement
	 * les assets
	 */

	public class AssetProjectile{
		public final AtlasRegion ballon;
		
		public AssetProjectile(TextureAtlas atlas) {
			ballon = atlas.findRegion("BallonDeBasket");
		}
	}
	public class AssetCaisse{
		public final AtlasRegion caisse;
		
		public AssetCaisse(TextureAtlas atlas) {
			caisse = atlas.findRegion("Caisse");
		}
	}
	public class AssetThesee{
		public final AtlasRegion personnage;
		public final AtlasRegion coeurResu;

		public AssetThesee (TextureAtlas atlas) {
			personnage = atlas.findRegion("personnage");
			coeurResu = atlas.findRegion("Heart");
		}
	}


	public class AssetMur{
		public final AtlasRegion mur;

		public AssetMur(TextureAtlas atlas) {
			mur = atlas.findRegion("Mur");
		}
	}

	public class AssetCase{
		public final AtlasRegion EZCase;
		public final AtlasRegion arriveeCaisse;
		public AssetCase(TextureAtlas atlas) {
			EZCase = atlas.findRegion("victoire");
			arriveeCaisse = atlas.findRegion("PlaceArrivee");
		}
	}

	public class AssetMonstre{
		public final AtlasRegion monster;
		public final AtlasRegion fantome;
		public AssetMonstre(TextureAtlas atlas) {
			monster = atlas.findRegion("monstre");
			fantome = atlas.findRegion("ghost");
		}
	}
	public class AssetCaseBoue{
		public final AtlasRegion boue;
		public AssetCaseBoue(TextureAtlas atlas) {
			boue=atlas.findRegion("boue");
		}
	}
	public class AssetPoliceEcriture{
		public final BitmapFont petit;
		public final BitmapFont moyen;
		public final BitmapFont gros;
		
		public AssetPoliceEcriture() {
			//On va cr�er les polices dont on se servira dans le jeu
			petit = new BitmapFont(Gdx.files.internal("images/PoliceEzMaze.fnt"),true);
			moyen = new BitmapFont(Gdx.files.internal("images/PoliceEzMaze.fnt"),true);
			gros = new BitmapFont(Gdx.files.internal("images/PoliceEzMaze.fnt"),true);
			
			//Pour chaque, reste � d�finir une taille d'affichage
			petit.getData().setScale(0.75f);
			moyen.getData().setScale(1.0f);
			gros.getData().setScale(2.0f);
			
			//Et on applique un filtre anti pixelisation
			petit.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
			moyen.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
			gros.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
		}
	}

	/*
	 * Fin de la d�finition des inner-classes
	 */



	@Override
	public void error(AssetDescriptor asset, Throwable throwable) {
		// TODO Auto-generated method stub
		Gdx.app.error(TAG, "Chargement de l'asset impossible ! '"+asset.fileName+"'",(Exception)throwable);
	}


	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		assetManager.dispose();
		police.petit.dispose();
		police.moyen.dispose();
		police.gros.dispose();
	}

}
