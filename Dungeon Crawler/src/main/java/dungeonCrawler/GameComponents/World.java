package dungeonCrawler.GameComponents;

import java.util.ArrayList;
import java.util.Iterator;

import org.joml.Vector2f;
import org.joml.Vector2i;
import org.joml.Vector3f;

import dungeonCrawler.GameComponents.Structures.GameComponentMatrix;
import dungeonCrawler.GameComponents.Structures.WorldIterator;
import dungeonCrawler.UI.Ray;
import dungeonCrawler.UI.RayIntersection;

public class World extends GameItemSystem {
	
	private static final int AREA_SIZE = 10;
	
	private final String name;
	private final Player player;
	private final GameComponentMatrix areas;

	public World(String name, Player player) {
		super(new Vector2i(0,0));
		this.name = name;
		this.player = player;
		this.areas = new GameComponentMatrix();

	}
	
	@Override
	public Iterator<GameComponent> iterator() {
		return new WorldIterator(areas, player);
	}

	@Override
	public void printDbg() {
		System.out.println(this.toString());
		super.printDbg();
	}
	
	@Override
	public String toString() {
		return ("World '" + name + "': " + 1 + " units, "+ areas.size() + " areas");
	}
		
	@Override
	public ArrayList<RayIntersection> processRay(Ray r) {
		return super.processRay(r);
	}

	public void addTile(Tile tile) {
		
		
		Area targetArea = this.getAreaFromPos(tile.getPosition());
		
		if (targetArea == null) {
			
			Vector2i areaOrigin = getAreaOrigin(tile.getPosition());
			targetArea = new Area(areaOrigin, AREA_SIZE);
			areas.set(areaOrigin, targetArea);
			
		}
		targetArea.addTile(tile);
	}
	
	public boolean isPassable(Vector2f pos) {
		Area targetArea = getAreaFromPos(pos);
		if(targetArea == null) {
			return false;
		}else {
			return targetArea.isPassable(pos);
		}
	}
	
	public ArrayList<GameItem> getTiles(Vector3f pos){
		ArrayList<GameItem> tilesOut = new ArrayList<GameItem>(100);
		
		Area a = getAreaFromPos(pos);
		
		for(GameComponent i: a) {
			if (i instanceof GameItem) {
				tilesOut.add((GameItem)i);
			}
		}
		
		return tilesOut;
		
	}
	
	private Area getAreaFromPos(Vector2f pos) {
		Area existingArea = (Area)areas.get(getAreaOrigin(pos));
		return existingArea;
	}
	private Area getAreaFromPos(Vector2i pos) {
		return this.getAreaFromPos(new Vector2f(pos.x, pos.y));
	}
	private Area getAreaFromPos(Vector3f pos) {
		return this.getAreaFromPos(new Vector2f(pos.x, pos.y));
	}
	private Vector2i getAreaOrigin(Vector2f pos) {
		int areaX = ((int)Math.floor(pos.x/ AREA_SIZE))*AREA_SIZE;
		int areaY = ((int)Math.floor(pos.y/ AREA_SIZE))*AREA_SIZE;

		return new Vector2i(areaX, areaY);
	}
	private Vector2i getAreaOrigin(Vector3f pos) {
		return this.getAreaOrigin(new Vector2f(pos.x, pos.y));
	}
	
}
