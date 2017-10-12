package dungeonCrawler;

import dungeonCrawler.GameComponents.GameItem;
import dungeonCrawler.States.State;

public class Update {

	private final GameItem owner;
	private final State newState;
	
	public Update(GameItem owner, State newState) {
		this.owner = owner;
		this.newState = newState;
	}

	public GameItem getOwner() {
		return owner;
	}

	public State getNewState() {
		return newState;
	}
	
	@Override
	public String toString() {
		return String.format("Update for %s, %s -> %s", owner.toString(), owner.getState().toString(), newState);
	}
	
	
}
