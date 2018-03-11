package dungeonCrawler.GameComponents.WorldBuilding;

import org.joml.Vector2f;
import org.joml.Vector2i;
import org.joml.Vector3f;

import dungeonCrawler.GameComponents.GameItem;
import dungeonCrawler.GameComponents.CollisionBounds.AABB;
import dungeonCrawler.Utils.MeshUtil;

public class Room extends GameItem{


	private Vector2i size;
	private boolean main;
	
	public Room (Vector2i pos, Vector2i size) {
		super(new Vector3f(pos.x, pos.y, 0f), MeshUtil.makeRectangle(new Vector2f(size.x, size.y), 0f, 1f, 0f), 
				new AABB(
						new Vector3f(pos.x, pos.y, 0f),
						new Vector3f(0f, 0f, 0f),
						new Vector3f(size.x, size.y, 0f)));
		this.size = size;
		main = false;
	}
	
	public Vector2f getSize() {
		return new Vector2f(size.x, size.y);
	}
	
	public void makeMainRoom(boolean main) {
		this.main = main;
		if(main) {
			this.setMesh(MeshUtil.makeRectangle(new Vector2f(size.x, size.y), 1f, 0f, 0f)); 
		}else {
			this.setMesh(MeshUtil.makeRectangle(new Vector2f(size.x, size.y), 0f, 1f, 0f));
		}
	}
	
	public boolean isMain() {
		return main;
	}
	
}
