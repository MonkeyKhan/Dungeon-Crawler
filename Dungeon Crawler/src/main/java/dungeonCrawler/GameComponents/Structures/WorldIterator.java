package dungeonCrawler.GameComponents.Structures;

import java.util.Iterator;

import dungeonCrawler.GameComponents.GameComponent;
import dungeonCrawler.GameComponents.Player;

import java.util.ArrayList;

public class WorldIterator implements Iterator<GameComponent>{

	private final ArrayList<GameComponent> areas;
	private final Player player;

	private int current = 0;
	
	
	public WorldIterator(GameComponentMatrix areas, Player player){
		this.areas = new ArrayList<GameComponent>(areas.getCollection());
		this.player = player;
	}
	
	@Override
	public boolean hasNext() {
		return current < (areas.size() + 1);
	}

	@Override
	public GameComponent next() {
		if (current < areas.size()) {
			return areas.get(current++);
		}else {
			current++;
			return player;
		}
	}

}
