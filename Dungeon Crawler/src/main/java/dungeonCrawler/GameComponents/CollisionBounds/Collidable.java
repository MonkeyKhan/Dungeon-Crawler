package dungeonCrawler.GameComponents.CollisionBounds;

import org.joml.Vector3f;

public interface Collidable {

	
	public float getWidth();
	public Vector3f resolveCollision(Collidable target, Vector3f actorPos, Vector3f targetPos);
	public Vector3f visit(Collidable actor, Vector3f actorPos, Vector3f targetPos);
	public Vector3f accept(CylindricBounds bounds, Vector3f actorPos, Vector3f targetPos);
	public Vector3f accept(PolygonalBounds bounds, Vector3f actorPos, Vector3f targetPos);
	public Vector3f accept(AABB bounds, Vector3f actorPos, Vector3f targetPos);
}
