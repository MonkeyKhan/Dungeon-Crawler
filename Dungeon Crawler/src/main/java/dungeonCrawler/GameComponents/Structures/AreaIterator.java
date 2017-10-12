package dungeonCrawler.GameComponents.Structures;

import java.util.Iterator;

import dungeonCrawler.GameComponents.GameComponent;
import dungeonCrawler.GameComponents.GameItem;

import java.util.ArrayList;

public class AreaIterator implements Iterator<GameComponent> {

	private ArrayList<GameComponent> tiles;
	private ArrayList<GameItem> items;
	private int current = 0;
	
	public AreaIterator(GameComponentMatrix tiles, ArrayList<GameItem> items) {
		this.tiles = new ArrayList<GameComponent>(tiles.getCollection());
		this.items = items;
	}
	@Override
	public boolean hasNext() {
		return current < (tiles.size() + items.size());
	}

	@Override
	public GameComponent next() {
		if (current < tiles.size()) {
			return tiles.get(current++);
		}else {
			return items.get((current++)-(tiles.size()));
		}
	}

}
