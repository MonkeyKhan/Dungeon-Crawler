package dungeonCrawler.GameComponents;

import org.joml.Vector3f;

import dungeonCrawler.Utils.MeshUtil;

public class Terrain extends Tile {

	private TerrainType type;
	/**
	 * types:
	 * 0) - Full tile
	 * __________
	 *|3   /\   4|
	 *|  /    \  |
	 *|/        \|
	 *|\        /|
	 *|  \    /  |
	 *|2___\/___1|
	 *
	 *  o---> y
	 *  |
	 *  v
	 *  x
	 *
	 */
	
	public Terrain (Vector3f position, TerrainType type) {
		super(position, MeshUtil.makeTile(type, (float)Math.random(), (float)Math.random(), (float)Math.random()), type.getCollisionBounds());
		this.type = type;
		if(this.type == null) {
			System.err.println("no terrain type!");
		}
	}
	@Override
	public boolean isPassable() {
		return type.isPassable();
	}
	
	@Override
	public String toString() {
		return super.toString();
	}
	
	
	
	
}
