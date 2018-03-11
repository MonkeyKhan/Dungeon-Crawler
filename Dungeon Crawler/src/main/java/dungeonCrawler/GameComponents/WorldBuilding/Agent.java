package dungeonCrawler.GameComponents.WorldBuilding;

import org.joml.Vector2f;
import org.joml.Vector3f;

import dungeonCrawler.GameComponents.GameItem;
import dungeonCrawler.Utils.MeshUtil;

public class Agent {
	
	private GameItem item;
	private Vector2f v;
	private Vector2f pos;
	private Vector2f size;
	private boolean locked;
	
	
	public Agent(GameItem item, Vector2f size){
		this.item = item;
		this.size = new Vector2f(size);
		this.pos = new Vector2f(item.getGridPosition());
		this.v = new Vector2f(pos);
		this.locked = false;
	}	
	
	public void update(float t) {
		
		
		
		this.pos.x += this.v.x * t;
		this.pos.y += this.v.y * t;
		
		item.forcePosition(new Vector3f(this.pos.x, this.pos.y, 0f));
		
	}
	
	public Vector2f getPos() {
		return new Vector2f(pos);
	}
	
	public Vector2f getSize() {
		return new Vector2f(size);
	}
	
	public Vector2f getVelocity() {
		return new Vector2f(v);
	}
	
	public void updateVelocity(Vector2f newVelocity) {
		if (!locked) {
			this.v = new Vector2f(newVelocity);
		}
	}
	
	public void lock() {
		this.pos.x = (float)Math.round(this.pos.x);
		this.pos.y = (float)Math.round(this.pos.y);
		item.forcePosition(new Vector3f(this.pos.x, this.pos.y, 0f));
		v = new Vector2f(0f, 0f);
		//item.setMesh(MeshUtil.makeRectangle(size, 1f, 0f, 0f));
		locked = true;
	}
	
	public boolean islocked() {
		return locked;
	}
}
