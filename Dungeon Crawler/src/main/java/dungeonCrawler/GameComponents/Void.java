package dungeonCrawler.GameComponents;

import org.joml.Vector3f;

import dungeonCrawler.Utils.MeshUtil;

public class Void extends Tile {

	public Void (Vector3f position) {
		super(position, null);
	}
	@Override
	public boolean isPassable() {
		return false;
	}
}
