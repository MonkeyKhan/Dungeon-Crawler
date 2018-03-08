package dungeonCrawler;

import java.util.ArrayList;

import org.joml.Vector3f;

import dungeonCrawler.Commands.CollisionEvent;
import dungeonCrawler.Commands.NullCommand;
import dungeonCrawler.GameComponents.GameItem;
import dungeonCrawler.GameComponents.World;
import dungeonCrawler.States.IdleState;
import dungeonCrawler.States.MovingState;
import dungeonCrawler.States.State;

public class UpdateProcessor {

	//the updateProcessor checks all updates for validity, mostly by detecting collisions. GameItems request state changes via updates and the 
	//updateProcessor may grant these upates, modify them, or deny them entirely
	private World world;
	
	public UpdateProcessor(World world) {
		this.world = world;
	}
	
	public void process(Update u) {
		
		
		State updatedState = u.getNewState();
		CollisionEvent collision = resolveCollision(u.getOwner(), updatedState);
		
		if(collision != null) {
			Vector3f resolvedPos = new Vector3f(u.getOwner().getPosition());
			resolvedPos.add(collision.getCollisionMTV().mul(1.1f));									
			updatedState.forcePosition(resolvedPos);
			
			u.getOwner().acceptRevision(collision);
			//u.getOwner().forcePosition(resolvedPos);
		}
		u.getOwner().setState(updatedState);
		//Just pass the new State to its owner for now, in the future this will see a lot more checking
		if(Debug.p) {
			//System.out.println(String.format("%s: Passing new %s to %s", this.toString(), u.getNewState(), u.getOwner()));
		}

	}

	/* Can probably be deleted
	private void checkCollision(GameItem item) {
		Vector3f pos = item.getPosition();
		
		for(GameItem i: world.getTiles(pos)) {
			if (i.checkCollision(item)){
				System.out.println(String.format("%s collided with %s", item.toString(), i.toString()));
			}
		}
	}*/
	
	private CollisionEvent resolveCollision(GameItem item, State updatedState) {
		
		Vector3f pos = item.getPosition();
		ArrayList<CollisionEvent> collisions = new ArrayList<CollisionEvent>();
		
		//collect all CollisionEvents from all colliding gameItems
		for(GameItem i: world.getTiles(pos)) {
			CollisionEvent c = i.resolveCollision(item, updatedState);
			if(c!=null) {
				collisions.add(c);
			}
		}
		//If there is more than one collision, return the earliest collision
		
		int i = collisions.size();
		switch (i) {
			case 0:
				return null;
			case 1:
				return collisions.get(0);
			default:
				CollisionEvent firstCollision = collisions.get(0);
				float t = firstCollision.getTime();
				for(int j=1; j<i; j++) {
					if(collisions.get(j).getTime()<t) {
						firstCollision = collisions.get(j);
						t = firstCollision.getTime();
					}
				}
				return firstCollision;
		}		
		
	}
	@Override
	public String toString() {
		return this.getClass().getSimpleName();
	}
	
}
	