package ezmaze.util;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.mygdx.ezmaze.jeu.objects.AbstractGameObject;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class CameraHelper {
	private static String TAG = CameraHelper.class.getCanonicalName();

	private final float MAX_ZOOM_IN = 0.25f;
	private final float MAX_ZOOM_OUT = 10.0f;
	
	/*
	 * Cette classe stocke la position actuelle et le niveau de zoom courant
	 * de la  caméra.
	 * La caméra peut suivre un sprite donné (target) 
	 * ou arrêter de suivre (target==null)
	 * La méthode applyTo() devra toujours être appelée AU DEBUT du rendering puisqu'elle
	 * défini des attributs de la caméra !
	 */
	private Vector2 position;
	private float zoom;
	
	//CODE POUBELLE
	//private Sprite target;
	//FIN CODE POUBELLE
	
	private AbstractGameObject target;
	
	public CameraHelper() {
		position = new Vector2();
		zoom = 2.0f;
	}
	
	public void update(float deltaTime) {
		if (!hasTarget()) return;
		
		//CODE POUBELLE
		//position.x = target.getX()+target.getOriginX();
		//position.y = target.getY()+target.getOriginY();
		//FIN CODE POUBELLE
		
		position.x = target.position.x + target.origin.x;
		position.y = target.position.y + target.origin.y;
	}
	
	//CODE POUBELLE
	//public void setTarget(Sprite target) {this.target = target;}
	//FIN CODE POUBELLE
	
	public void setTarget(AbstractGameObject target) {
		this.target = target;
	}
	
	//CODE POUBELLE
	//public Sprite getTarget() {return target;}
	//FIN CODE POUBELLE
	
	public AbstractGameObject getTarget() {
		return target;
	}
	
	public void setPosition(float x, float y) {
		this.position.set(x,y);
	}
	
	public Vector2 getPosition () { return position;	}
	
	public void addZoom(float amount) { setZoom(zoom+amount);};
	public void setZoom(float zoom) {
		this.zoom = MathUtils.clamp(zoom, MAX_ZOOM_IN, MAX_ZOOM_OUT);
	}
	
	public float getZoom() {return zoom;}
	
	
	
	public boolean hasTarget() {return target != null;}
	//CODE POUBELLE
//	PUBLIC BOOLEAN HASTARGET(SPRITE TARGET) {
//		RETURN HASTARGET() && THIS.TARGET.EQUALS(TARGET);
//	}
	//FIN CODE POUBELLE
	
	public boolean hasTarget (AbstractGameObject target) {
		return hasTarget() && this.target.equals(target);
	}
	
	public void applyTo (OrthographicCamera camera) {
		camera.position.x = position.x;
		camera.position.y = position.y;
		camera.zoom = zoom;
		camera.update();
	}
	
	
}
