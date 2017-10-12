package dungeonCrawler.GameComponents;

import org.joml.Vector3f;

public class Plane {

	private final Vector3f a;
	private final Vector3f b;
	private final Vector3f n;
	
	public Plane(Vector3f a, Vector3f b) {
		this.a = new Vector3f(a);
		this.b = new Vector3f(b);
		this.n = new Vector3f();
		a.cross(b, n);
	}
	
	public Vector3f a() {
		return new Vector3f(a);
	}
	
	public Vector3f b() {
		return new Vector3f(b);
	}
	
	public Vector3f getNormal() {
		return new Vector3f(n);
	}
}
