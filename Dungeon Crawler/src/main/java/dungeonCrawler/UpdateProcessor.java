package dungeonCrawler;

import org.joml.Vector3f;

import dungeonCrawler.GameComponents.GameItem;
import dungeonCrawler.GameComponents.World;

public class UpdateProcessor {

	//the updateProcessor checks all updates for validity, mostly by detecting collisions. GameItems request state changes via updates and the 
	//updateProcessor may grant these upates, modify them, or deny them entirely
	private World world;
	
	public UpdateProcessor(World world) {
		this.world = world;
	}
	
	public void process(Update u) {
		//TODO: Collision detection happens here!
		checkCollision(u.getOwner());
		//Just pass the new State to its owner for now, in the future this will see a lot more checking
		if(Debug.p) {
			//System.out.println(String.format("%s: Passing new %s to %s", this.toString(), u.getNewState(), u.getOwner()));
		}
		u.getOwner().setState(u.getNewState());
	}

	private void checkCollision(GameItem item) {
		Vector3f pos = item.getPosition();
		
		for(GameItem i: world.getTiles(pos)) {
			if (i.checkCollision(item)){
				System.out.println(String.format("%s collided with %s", item.toString(), i.toString()));
			}
		}
	}
	@Override
	public String toString() {
		return this.getClass().getSimpleName();
	}
	
}
	