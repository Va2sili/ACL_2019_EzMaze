package ezmaze.util;

public class Constantes {
	/*
	 * La classe Constantes.java va nous permettre de stocker nos variables
	 * constantes tout au long du programme. On aurait aussi pu le stocker dans 
	 * un fichier *de param�tres qui �viterait d'avoir � recompiler tout le code 
	 * � chaque modification *de ces constantes. Mais on ne le fait pas.
	 */

	//On aura une carte dont on ne verra pas l'enti�ret�.
	//On d�fini la partie visible de la carte (la taille de la vue : un carr� de 5 par 5)
	public static final float VIEWPORT_WIDTH = 5.0f;
	public static final float VIEWPORT_HEIGHT = 5.0f;

	//Emplacement pour le fichier de description de l'atlas de texture
	public static final String TEXTURE_ATLAS_OBJECTS = "../core/assets/images/ezmaze.pack.atlas";

	//Emplacement du fichier image pour le level_01
	private static String chemin = "../core/assets/levels/";
	public static final String[] LEVEL = {chemin+"8.png"};

	//Mise en place d'un GUI
	//Il est � la taille de la fen�tre
	public static final float VIEWPORT_GUI_WIDTH = 5.0f;
	public static final float VIEWPORT_GUI_HEIGHT = 5.0f;

	//NOMBRE DE RESURECTIONS INITIALES
	public static final int RESU_INIT = 8;
}
