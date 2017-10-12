package dungeonCrawler.Commands;

import dungeonCrawler.States.State;

public class NullCommand implements Command {

	@Override
	public State updateState(State oldState) {
		return oldState;
	}

	@Override
	public boolean isExecuted() {
		//NullCommand is never fully executed
		return false;
	}

	@Override
	public String toString() {
		return this.getClass().getSimpleName();
	}
	
	

}
