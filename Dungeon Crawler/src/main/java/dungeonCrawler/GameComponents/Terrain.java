package dungeonCrawler.GameComponents;

import org.joml.Vector3f;

import dungeonCrawler.Utils.MeshUtil;

public class Terrain extends Tile {

	public Terrain (Vector3f position) {
		super(position, MeshUtil.makeTile((float)Math.random(), (float)Math.random(), (float)Math.random()));
	}
	@Override
	public boolean isPassable() {
		return true;
	}

}
