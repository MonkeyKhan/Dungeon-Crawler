package dungeonCrawler.GameComponents;

import org.joml.Vector3f;

import dungeonCrawler.Utils.MeshUtil;

public class Terrain extends Tile {

	private final int type;
	/**
	 * types:
	 * 0) - Full tile
	 * __________
	 *|2   /\   1|
	 *|  /    \  |
	 *|/        \|
	 *|\        /|
	 *|  \    /  |
	 *|3___\/___4|
	 *
	 */
	
	public Terrain (Vector3f position, int type) {
		super(position, MeshUtil.makeTile(type, (float)Math.random(), (float)Math.random(), (float)Math.random()));
		this.type = type;
	}
	@Override
	public boolean isPassable() {
		if(type == 0) {
			return true;
		}else {
			return false;
		}
	}

}
