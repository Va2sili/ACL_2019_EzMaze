package com.mygdx.ezmaze.jeu.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public abstract class AbstractGameObject {
	//Cette classe stockes les attributs suivants des objets composant le jeu
	public Vector2 position;
	public Vector2 anciennePosition;
	public Vector2 dimension;
	public Vector2 origin;
	public Vector2 scale;
	public float rotation;
	
	//Tout ce qui concerne la mobilité
	public Vector2 vitesse;//vitesse actuelle de l'objet
	public Vector2 vitesseMax;//limite de vitesse
	public Vector2 frottement;//force de freinage
	public Vector2 acceleration;
	
	//Ce qui concerne les collisions (au sens large)
	public Rectangle frontiere;//Les frontières de l'objet sont INDEPENDANTES de la taille de l'image qui le représente
	
	public AbstractGameObject() {
		//Attributs de positionnement
		position = new Vector2();
		anciennePosition = new Vector2();
		dimension = new Vector2(1,1);
		origin = new Vector2();
		scale = new Vector2(1,1);
		rotation = 0;
		
		
		//Attributs de mouvement
		vitesse = new Vector2();
		vitesseMax = new Vector2();
		frottement = new Vector2();
		acceleration = new Vector2();
		frontiere = new Rectangle();
	}
	
	//Mise à jour des attributs de mobilité sur l'axe X puis Y
	protected void updateMvtX(float deltaTime) {
		if (vitesse.x != 0) {
			//On applique les frottements
			if (vitesse.x > 0) {
				vitesse.x = Math.max(0, vitesse.x - frottement.x * deltaTime);
			}
			else {
				vitesse.x = Math.min(0, vitesse.x + frottement.x * deltaTime);
			}
		}
		//On Applique toujours l'accélération
		vitesse.x = vitesse.x + acceleration.x * deltaTime;
		vitesse.x = MathUtils.clamp(vitesse.x, -vitesseMax.x, vitesseMax.x);
	}
	protected void updateMvtY(float deltaTime) {
		if (vitesse.y != 0) {
			//On applique les frottements
			if (vitesse.y > 0) {
				vitesse.y = Math.max(0, vitesse.y - frottement.y * deltaTime);
			}
			else {
				vitesse.y = Math.min(0, vitesse.y + frottement.y * deltaTime);
			}
		}
		//On Applique toujours l'accélération
		vitesse.y = vitesse.y + acceleration.y * deltaTime;
		vitesse.y = MathUtils.clamp(vitesse.y, -vitesseMax.y, vitesseMax.y);
	}
	
	public void update(float deltaTime) {
		updateMvtX(deltaTime);
		updateMvtY(deltaTime);
		//On stocke la position à temps t-1
		anciennePosition.x = position.x;
		anciennePosition.y = position.y;
		//On se déplace vers une nouvelle position
		position.x += vitesse.x*deltaTime;
		position.y += vitesse.y*deltaTime;
	}
	
	public abstract void render(SpriteBatch batch);
}
