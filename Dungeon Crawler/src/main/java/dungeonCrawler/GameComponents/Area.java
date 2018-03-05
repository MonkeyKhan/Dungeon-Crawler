package dungeonCrawler.GameComponents;

import java.util.ArrayList;
import java.util.Iterator;

import org.joml.Vector2f;
import org.joml.Vector2i;
import org.joml.Vector3f;

import dungeonCrawler.Update;
import dungeonCrawler.GameComponents.CollisionBounds.AABB;
import dungeonCrawler.GameComponents.Structures.AreaIterator;
import dungeonCrawler.GameComponents.Structures.GameComponentMatrix;
import dungeonCrawler.UI.Ray;
import dungeonCrawler.UI.RayIntersection;

public class Area extends GameItemSystem {
	
	private static int globalId = 0;
	
	private final int id;
	private final int size;
	private final GameComponentMatrix tiles;
	private final Frame frame;
	private AABB bBox;
	private final ArrayList<GameItem> miscItems;
	
	public Area(Vector2i pos, int size){
		super(pos);
		this.tiles = new GameComponentMatrix();
		this.id = globalId++;
		this.size = size;
		this.frame = new Frame(this.getPosition());
		this.bBox = new AABB(new Vector3f(pos.x, pos.y, 0), this.size, -2f, 2f);
		//miscItems is an ugly, temporary solution that helps not needing to update AreaIterator with every change to Area...
		miscItems = new ArrayList<GameItem>();
		miscItems.add(frame);
		miscItems.add(bBox);
	}
	
	@Override
	public Iterator<GameComponent> iterator() {
		return new AreaIterator(tiles, miscItems);
	}
	
	@Override
	public ArrayList<Update> update(float interval) {
		return super.update(interval);
	}

	@Override
	public void printDbg() {
		System.out.println(this.toString());
		super.printDbg();
	}

	@Override
	public String toString() {
		return ("Area " + id + " (" + tiles.size() + " tiles) at " + this.getPosition().x + ", " + getPosition().y);
	}
	
	@Override
	public ArrayList<RayIntersection> processRay(Ray r) {
		ArrayList<RayIntersection> intersect = bBox.processRay(r);
		if(!intersect.isEmpty()) { //ray intersects area's AABB
			System.out.println("Area " + id + " intersected, testing tiles...");
			return super.processRay(r);			
		}else {
			return new ArrayList<RayIntersection>(); 	//No hit, return an empty list
		}
		
	}

	public void addTile(Tile tile){
		tiles.set(new Vector2f(tile.getPosition().x, tile.getPosition().y), (GameComponent)tile);
	}
	
	public boolean isPassable(Vector2f pos) {
		Tile tile = (Tile)tiles.get(pos);
		if (tile == null) {
			return false;
		}else {
			return tile.isPassable();
		}
	}
	
	public void recalcAABB() {
		//TODO: iterator over tiles, determine the height
	}
}
