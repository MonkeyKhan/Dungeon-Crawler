package dungeonCrawler.UI;

import org.joml.Vector3f;

import dungeonCrawler.GameComponents.GameItem;

public class RayIntersection {

	private final GameItem item;
	private final Ray ray;
	private final float distance;
	
	public RayIntersection(GameItem item, Ray ray, float distance) {
		this.item = item;
		this.ray = ray;
		this.distance = distance;
	}

	public float getDistance() {
		return distance;
	}

	public GameItem getItem() {
		return item;
	}
	
	public Vector3f getPosition() {
		return new Vector3f(
				ray.getOrigin().x + (ray.getDir().x * distance),
				ray.getOrigin().y + (ray.getDir().y * distance),
				ray.getOrigin().z + (ray.getDir().z * distance)); 
	}
}
