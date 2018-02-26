package dungeonCrawler.Commands;

import dungeonCrawler.States.State;

public interface Command {

	public State updateState(State oldState);
	
	public boolean isExecuted();
	
	public void render();
}
