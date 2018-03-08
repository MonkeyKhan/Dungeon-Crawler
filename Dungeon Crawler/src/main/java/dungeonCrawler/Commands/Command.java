package dungeonCrawler.Commands;

import org.joml.Vector3f;

import dungeonCrawler.States.State;

public interface Command {

	public State updateState(State oldState);
	
	public boolean isExecuted();
	
	public void render();
	
	public void acceptRevision(CollisionEvent rev);
}
