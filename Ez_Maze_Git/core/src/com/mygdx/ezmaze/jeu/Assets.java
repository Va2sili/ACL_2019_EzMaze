

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
	public AssetPoliceEcriture police;
	public AssetCaisse caisse;
	public AssetProjectile projectile;
	public AssetArmeLancee armelancee;




	//On se prémunit de l'instantiation par d'autres classes
	private Assets() {}

	//Ne sera appelée qu'au démarrage du jeu
	public void init(AssetManager assetManager) {
		this.assetManager = assetManager;
		//On permet à l'asset manager de comprendre les erreurs
		assetManager.setErrorListener(this);
		//On charge L'atlas de textures
		assetManager.load(Constantes.TEXTURE_ATLAS_OBJECTS,TextureAtlas.class);
		//On commence à charger et on attend jusqu'à ce que ce soit finit
		assetManager.finishLoading();
		Gdx.app.debug(TAG, "Nombre d'assets chargés : "+assetManager.getAssetNames().size);
		for (String a : assetManager.getAssetNames()) {
			Gdx.app.debug(TAG, "asset : "+a);
		}

		TextureAtlas atlas = assetManager.get(Constantes.TEXTURE_ATLAS_OBJECTS);

		//On autorise le filtering pour adoucir la pixelisation
		for (Texture t : atlas.getTextures()) {
			t.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		}

		//On crée les objets ressources d'affichage du jeu
		thesee = new AssetThesee(atlas);
		mur = new AssetMur(atlas);
		carre = new AssetCase(atlas);
		monstre =new AssetMonstre(atlas);
		caisse = new AssetCaisse(atlas);
		projectile = new AssetProjectile(atlas);
		armelancee = new AssetArmeLancee(atlas);
		police = new AssetPoliceEcriture();

	}

	/*
	 * Définition de quelques inner-classes qui permettront de regrouper logiquement
	 * les assets
	 */

	public class AssetProjectile{
		public final AtlasRegion ballon;

		public AssetProjectile(TextureAtlas atlas) {
			ballon = atlas.findRegion("BallonDeBasket");

		}
	}

	public class AssetArmeLancee{
		public final AtlasRegion fireball;

		public AssetArmeLancee(TextureAtlas atlas) {
			fireball = atlas.findRegion("Fireball");

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
		public final AtlasRegion boue;
		public final AtlasRegion tp;
		public AssetCase(TextureAtlas atlas) {
			EZCase = atlas.findRegion("victoire");
			arriveeCaisse = atlas.findRegion("PlaceArrivee");
			boue=atlas.findRegion("boue");
			tp=atlas.findRegion("caseTP");
		}
	}

	public class AssetMonstre{
		public final AtlasRegion monster;
		public final AtlasRegion fantome;
		public final AtlasRegion chercheur;
		public final AtlasRegion zombie;
		public AssetMonstre(TextureAtlas atlas) {
			monster = atlas.findRegion("monstre");
			fantome = atlas.findRegion("ghost");
			chercheur = atlas.findRegion("SSAD_0001_by_shiroikuro");
			zombie = atlas.findRegion("Zombie");
		}
	}
	
	public class AssetPoliceEcriture{
		public final BitmapFont petit;
		public final BitmapFont moyen;
		public final BitmapFont gros;

		public AssetPoliceEcriture() {
			//On va créer les polices dont on se servira dans le jeu
			petit = new BitmapFont(Gdx.files.internal("images/PoliceEzMaze.fnt"),true);
			moyen = new BitmapFont(Gdx.files.internal("images/PoliceEzMaze.fnt"),true);
			gros = new BitmapFont(Gdx.files.internal("images/PoliceEzMaze.fnt"),true);

			//Pour chaque, reste à définir une taille d'affichage
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
	 * Fin de la définition des inner-classes
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
