package dungeonCrawler.GameComponents;

import org.joml.Vector3f;

import dungeonCrawler.GameComponents.CollisionBounds.Collidable;

public class DummyUnit extends Unit {

	
	public DummyUnit(Vector3f position, Collidable bounds) {
		super(position, bounds);
	}
}
