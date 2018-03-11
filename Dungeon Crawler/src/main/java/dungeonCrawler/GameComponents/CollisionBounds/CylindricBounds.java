package dungeonCrawler.GameComponents.CollisionBounds;

import org.joml.Vector3f;

import dungeonCrawler.Utils.CollisionUtil;

public class CylindricBounds implements Collidable {

	/**CylindricBounds serve as cylinder shaped collision-boundaries, where the z-axis is the axis of the cylinder. 
	 * They have a height that measures their size in z-direction, starting at their z-position and a radius.
	 * 
	 * CylindricBounds can pass above or below other Collidable that have a defined height, like AABBs.
	 */
	
	private float radius;
	private float height;
	
	public CylindricBounds(float radius, float height) {
		this.radius = radius;
		this.height = height;
	}
	
	
	public float getRadius() {
		return radius;
	}


	public void setRadius(float radius) {
		this.radius = radius;
	}


	public float getHeight() {
		return height;
	}


	public void setHeight(float height) {
		this.height = height;
	}


	@Override
	public Vector3f resolveCollision(Collidable target, Vector3f actorPos, Vector3f targetPos) {
		return target.visit(this, actorPos, targetPos);
	}
	
	public Vector3f visit(Collidable actor, Vector3f actorPos, Vector3f targetPos) {
		return actor.accept(this, actorPos, targetPos);
	}
	
	public Vector3f accept(CylindricBounds target, Vector3f actorPos, Vector3f targetPos) {
		return CollisionUtil.resolveCollision(this, target, actorPos, targetPos);
	}

	public Vector3f accept(PolygonalBounds target, Vector3f actorPos, Vector3f targetPos) {
		return CollisionUtil.resolveCollision(this, target, actorPos, targetPos);
	}
	
	public Vector3f accept(AABB target, Vector3f actorPos, Vector3f targetPos) {
		return CollisionUtil.resolveCollision(this, target, actorPos, targetPos);
	}
	
	public float getWidth() {
		return this.radius*2;
	}
}
