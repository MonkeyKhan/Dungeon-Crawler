package dungeonCrawler.Commands;

import org.joml.Vector3f;

import dungeonCrawler.GameComponents.GameItem;

public class CollisionEvent implements CommandRevision {
	
	private Vector3f MTV;
	private GameItem actor, target;
	private float time;
	
	public CollisionEvent(Vector3f collisionMTV, GameItem actor, GameItem target, float collisionTime) {
		this.MTV =collisionMTV;
		this.actor = actor;
		this.target = target;
		this.time = collisionTime;
	}
	
	
	public Vector3f getCollisionMTV() {
		return new Vector3f(MTV);
	}

	public float getTime() {
		return time;
	}

	public void revise(Command command) {
		command.acceptRevision(this);
	}
}
