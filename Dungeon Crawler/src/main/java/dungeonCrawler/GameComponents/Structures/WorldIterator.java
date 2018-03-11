package dungeonCrawler.GameComponents.Structures;

import java.util.Iterator;

import dungeonCrawler.GameComponents.GameComponent;
import dungeonCrawler.GameComponents.Player;
import dungeonCrawler.GameComponents.WorldBuilding.Room;

import java.util.ArrayList;

public class WorldIterator implements Iterator<GameComponent>{

	private final ArrayList<GameComponent> areas;
	private ArrayList<Room> rooms;
	private final Player player;

	private int current = 0;
	
	
	public WorldIterator(GameComponentMatrix areas, Player player,  ArrayList<Room> rooms){
		this.areas = new ArrayList<GameComponent>(areas.getCollection());
		this.player = player;
		this.rooms = rooms;
	}
	
	@Override
	public boolean hasNext() {
		return current < (areas.size() + rooms.size() + 1);
	}

	@Override
	public GameComponent next() {
		if (current < areas.size()) {
			return areas.get(current++);
		}
		if (current >= areas.size() && current < (areas.size() + rooms.size())){
			return rooms.get((current++)-areas.size());
		}
		current++;
		return player;
	}

}
