package dungeonCrawler.GameComponents;

import org.joml.Vector3f;

import dungeonCrawler.Utils.MeshUtil;

public class Player extends Unit{

	public Player(Vector3f position) {
		super(position);
	}
	
	public Player(Vector3f position, float radius, float height) {
		super(position, radius, height);
	}
	
	@Override
	public String toString() {
		return "Player";
	}
}
