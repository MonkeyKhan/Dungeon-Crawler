package dungeonCrawler.GameComponents.CollisionBounds;

import org.joml.Vector3f;

public interface Collidable {

	
	public float getWidth();
	public boolean checkCollision(Collidable target, Vector3f actorPos, Vector3f targetPos);
	public boolean visit(Collidable actor, Vector3f actorPos, Vector3f targetPos);
	public boolean accept(CylindricBounds bounds, Vector3f actorPos, Vector3f targetPos);
	public boolean accept(PolygonalBounds bounds, Vector3f actorPos, Vector3f targetPos);
}
