package dungeonCrawler.UI;

import org.joml.Vector3f;
import org.joml.Vector4f;

import dungeonCrawler.Utils.MeshUtil;

public class Ray{

	private final Vector3f o;
	private final Vector3f dir;
		
	public Ray (Vector3f origin, Vector3f pos2) {
		this.o = origin;
		this.dir = new Vector3f();
		this.o.sub(pos2, this.dir);
		this.dir.normalize();
		this.dir.mul(-1f);
	}
	
	public Vector3f getOrigin() {
		return new Vector3f(o.x, o.y, o.z);
	}
	
	public Vector3f getDir() {
		return new Vector3f(dir.x, dir.y, dir.z);
	}
	
	/*
	public Vector3f testIntersection(Triangle tri) {
		
	}*/

}
