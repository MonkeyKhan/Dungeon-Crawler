package dungeonCrawler.GameComponents;

import org.joml.Vector3f;

import dungeonCrawler.GameComponents.CollisionBounds.PolygonalBounds;

public class TerrainType {
	
	private Vector3f[] vertices;
	private Vector3f[] collision;
	private boolean passable;
	private final int type;
	
	public TerrainType(int type){
		
		switch (type) { 
		case 1:
			vertices = new Vector3f[] {
					new Vector3f(1f, 0f, 0f),
		        	new Vector3f(1f, 1f, 0f),
		        	new Vector3f(0f, 1f, 0f)};
			collision = new Vector3f[] {
					new Vector3f(0f, 0f, 0f),
		        	new Vector3f(1f, 0f, 0f),
		        	new Vector3f(0f, 1f, 0f)};
			passable = false;
			this.type = type;
			break;
		case 2:
			vertices = new Vector3f[] {
					new Vector3f(1f, 0f, 0f),
		        	new Vector3f(1f, 1f, 0f),
		        	new Vector3f(0f, 0f, 0f)};
			collision = new Vector3f[] {
					new Vector3f(0f, 0f, 0f),
		        	new Vector3f(1f, 1f, 0f),
		        	new Vector3f(0f, 1f, 0f)};
			passable = false;
			this.type = type;
			break;
		case 3:
			vertices = new Vector3f[] {
					new Vector3f(0f, 0f, 0f),
		        	new Vector3f(1f, 0f, 0f),
		        	new Vector3f(0f, 1f, 0f)};
			collision = new Vector3f[] {
					new Vector3f(1f, 0f, 0f),
		        	new Vector3f(1f, 1f, 0f),
		        	new Vector3f(0f, 1f, 0f)};
			passable = false;
			this.type = type;
			break;
		case 4:
			vertices = new Vector3f[] {
					new Vector3f(0f, 0f, 0f),
		        	new Vector3f(1f, 1f, 0f),
		        	new Vector3f(0f, 1f, 0f)};
			collision = new Vector3f[] {
					new Vector3f(1f, 0f, 0f),
		        	new Vector3f(1f, 1f, 0f),
		        	new Vector3f(0f, 0f, 0f)};
			passable = false;
			this.type = type;
			break;
		case 9:
			vertices = null;
			collision = new Vector3f[] {
					new Vector3f(0f, 0f, 0f),
		        	new Vector3f(1f, 0f, 0f),
		        	new Vector3f(1f, 1f, 0f),
		        	new Vector3f(0f, 1f, 0f)};
			passable = false;
			this.type = type;
			break;
		default:
			vertices = new Vector3f[] {
					new Vector3f(0f, 0f, 0f),
		        	new Vector3f(1f, 0f, 0f),
		        	new Vector3f(1f, 1f, 0f),
		        	new Vector3f(0f, 1f, 0f)};
			passable = true;
			this.type = 0;
			break;
			
		}
	}
	
	public Vector3f[] getVertices() {
		return vertices;
	}
	
	public boolean isPassable() {
		return passable;
	}
	
	public PolygonalBounds getCollisionBounds() {
		if(collision != null) {
			try {
				return new PolygonalBounds(collision);
			}catch(Exception e) {
				System.err.println(e.getMessage());
			}
		}
		return null;
	}
	
	public String toString() {
		return String.format("Type %s", type);
	}
}
