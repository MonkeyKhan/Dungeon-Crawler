package dungeonCrawler.GameComponents;

import org.joml.Vector3f;

public class Tri{

	private final Vector3f v0;
	private final Vector3f v1;
	private final Vector3f v2;
	private final Plane p;
	
	
	//TODO: Make tris fit in with polygonicBounds!!
	public Tri (Vector3f v0, Vector3f v1, Vector3f v2) {
		this.v0 = new Vector3f(v0);
		this.v1 = new Vector3f(v1);
		this.v2 = new Vector3f(v2);
		this.p = new Plane(
					new Vector3f(
						v1.x - v0.x,
						v1.y - v0.y,
						v1.z - v0.z),
					new Vector3f(
						v2.x - v0.x,
						v2.y - v0.y,
						v2.z - v0.z));		
	}
	
	public Vector3f getNormal() {
		return p.getNormal();
	}
	
	public Plane getPlane() {
		return p;
	}
	
	public Vector3f[] getPlaneVectors() {
		return new Vector3f[] {p.a(), p.b()};
	}
	
	public Vector3f[] getVertices() {
		return new Vector3f[] {
				new Vector3f(v0),
				new Vector3f(v1),
				new Vector3f(v2)};
		
	}

	@Override
	public String toString() {
		return String.format("Tri: %s,%s,%s; %s,%s,%s; %s,%s,%s", v0.x, v0.y, v0.z, v1.x, v1.y, v1.z, v2.x, v2.y, v2.z); 
	}
	
	

}
