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

	private Path path;
	private Vector2f dest = null;
	private Vector2f dir;
	private boolean executed = false;
	private final float PATH_RADIUS = 0.05f;
	
	public MoveCommand(Path path) {
		this.path = path;
		this.init();
	}
	
	public MoveCommand(Vector2f dest) {
		/**
		 * Constructor without path. Only the immediate destination gets passed
		 */
		this.path = new Path(new Stack<Vector2f>());
		this.dest = new Vector2f(dest);
	}
	
	@Override
	public State updateState(State oldState){
		
		//Calculate distance from current position and destination
		Vector2f oldPos = oldState.getGridPosition();
		float dist = new Vector2f(dest).sub(oldPos).length();
		
		//Check whether distance to destination is within a constant threshold
		if(dist < PATH_RADIUS) {
			if(!path.empty()) {
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
		dir = new Vector2f(dest);
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
	
	@Override
	public void render() {
		path.render();
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
	
	@Override
	public void acceptRevision(CollisionEvent rev) {
		/**
		 * After a collision has occurred, it must be ensured that the MoveCommand that has led to the collision is changed in a way
		 * where further collisions are avoided. To this end, the current destination, that dictates the direction of movement, is adjusted.
		 * From the direction of movement and the collisionMTV, an adjusting vector is calculated -> Doc 1)
		 */
		
		
		//First make sure there is a destination currently set. Should very rarely actually occur
		if(dest == null) {
			if(!path.empty()) {
				dest = path.pop();
			}else {
				return;
			}
		}
		
		Vector2f mtv = new Vector2f(rev.getCollisionMTV().x, rev.getCollisionMTV().y);
		mtv.normalize();
		Vector2f destAdjust = mtv.mul(dir.dot(mtv)*-1.1f);
		dest.add(destAdjust);
		if(Debug.p) {
			System.out.println(String.format("Adjusting next position on path by %s", destAdjust.toString()));
		}
		
		
	}
	
	

}
