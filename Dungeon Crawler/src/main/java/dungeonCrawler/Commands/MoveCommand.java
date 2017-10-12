package dungeonCrawler.Commands;


import java.util.Stack;

import org.joml.Vector2f;
import org.joml.Vector3f;

import dungeonCrawler.Debug;
import dungeonCrawler.GameComponents.Unit;
import dungeonCrawler.States.IdleState;
import dungeonCrawler.States.MovingState;
import dungeonCrawler.States.State;

public class MoveCommand implements Command {

	private Stack<Vector2f> path;
	private Vector2f dest = null;
	private boolean executed = false;
	private final float PATH_RADIUS = 0.2f;
	
	public MoveCommand(Stack<Vector2f> path) {
		this.path = path;
		this.init();
	}
	
	@Override
	public State updateState(State oldState){
		
		//Calculate distance from current position and destination
		Vector2f oldPos = oldState.getGridPosition();
		float dist = new Vector2f(dest).sub(oldPos).length();
		
		//Check whether distance to destination is within a constant threshold
		if(dist < PATH_RADIUS) {
			if(path.size() > 0) {
				//If within threshold and more destinations are available, pop next destination off stack
				dest = path.pop();
				if(Debug.p) {
					System.out.println(String.format("%s: New destination: %s, %s", this.toString(), dest.x, dest.y));
				}
			}else {
				//Stack is empty, last destination reached, MoveCommand is executed
				if(Debug.p) {
					System.out.println(String.format("%s: finished!", this.toString()));
				}
				executed = true;
				return new IdleState(oldState);
			}
		}
		
		//A new destination was set at some point, update state accordingly
		Vector2f dir = new Vector2f(dest);
		dir.sub(oldPos);
		return new MovingState(oldState, dir);
	}
	
	@Override
	public boolean isExecuted() {
		return executed;
	}
	@Override
	public String toString() {
		return String.format("%s (%s positions)", this.getClass().getSimpleName(), path.size());
	}
	
	private void init() {
		if (!path.empty()) {
			//Set first position as new destination
			dest = path.pop();
		}else {
			//MoveCommand was created with an empty stack, should never happen!
			System.err.println("MoveCommand created with empty stack!");
			executed = true;
		}	
	}
	
	

}
