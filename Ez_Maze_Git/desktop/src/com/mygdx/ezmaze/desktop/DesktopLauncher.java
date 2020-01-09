package com.mygdx.ezmaze.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;

import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.tools.texturepacker.TexturePacker;
import com.badlogic.gdx.tools.texturepacker.TexturePacker.Settings;
import com.mygdx.ezmaze.EzMazeMain;

public class DesktopLauncher {
	private static boolean reconstruireAtlas = false;
	private static boolean dessineDebugLignesContour = false;


	public static void main (String[] arg) {
		if (reconstruireAtlas) {
			Settings settings = new Settings();
			/*
			 * Dimensions maximales de l'atlas.
			 * S'il y a trop d'images et que l'on dépasse... Ca créera automatiquement
			 * un nouvel atlas !
			 * Pour savoir quelles images seront ensembles sur un même atlas, il suffit
			 * de les regrouper dans un même sous-dossier ! (et de faire un appel pour chaque !)
			 */
			settings.maxWidth = 1024;
			settings.maxHeight = 1024;
			settings.duplicatePadding = false;
			settings.debug = dessineDebugLignesContour;
			TexturePacker.process(settings, "assets-raw/images", "../core/assets/images","ezmaze.pack");
		}

		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "Ez-MA-zE";
		config.width = 800;
		config.height = 480;
		new LwjglApplication(new EzMazeMain(), config);
	}
}
