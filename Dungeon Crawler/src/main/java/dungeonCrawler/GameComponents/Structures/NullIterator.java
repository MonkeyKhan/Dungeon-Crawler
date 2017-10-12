package dungeonCrawler.GameComponents.Structures;

import java.util.Iterator;

import dungeonCrawler.GameComponents.GameComponent;

public class NullIterator implements Iterator<GameComponent> {

	//no-op Iterator that never returns anything
	@Override
	public boolean hasNext() {
		return false;
	}

	@Override
	public GameComponent next() {
		return null;
	}

}
