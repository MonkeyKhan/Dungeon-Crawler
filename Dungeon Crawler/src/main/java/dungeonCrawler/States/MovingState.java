package dungeonCrawler.States;

import org.joml.Vector2f;
import org.joml.Vector3f;

public final class MovingState extends State{
	
	private Vector2f dir;
	private final float MOVE_SPEED = 3; //Speeds are in units/s
	
	//Constructor for creating a state from scratch
	
	public MovingState(Vector3f pos, Vector2f dir) {
		super(pos);
		this.dir = new Vector2f(dir);
	}
	
	//Constructor to convert a State into a MovingState
	
	public MovingState(State state, Vector2f dir) {
		super(state);
		this.dir = new Vector2f(dir);
		this.dir.normalize();
		
	}
	
	//Constructor that copies attributes from passed state
	
	private MovingState (MovingState state) {
		this(state.getPosition(), state.getDir());
	}
	
	@Override
	protected MovingState copy() {
		return new MovingState(this);
	}
	
	public State updateDir(Vector2f dir) {
		MovingState updatedState = this.copy();
		updatedState.dir = (new Vector2f(dir)).normalize();
		
		return updatedState;
	}

	@Override
	public boolean takesCommands() {
		return true;
	}
	
	
	@Override
	protected boolean equalsSpecific(Object o) {
		return this.getDir().equals(((MovingState)o).getDir());
	}
	
	
	
	@Override
	public State update(float interval) {
		
		Vector2f step = new Vector2f(this.dir).mul(this.MOVE_SPEED * interval);
		Vector3f newPos = new Vector3f(this.getPosition().x + step.x, this.getPosition().y + step.y, + this.getPosition().z);
		
		return this.updatePos(newPos);
	}

	@Override
	public String toString() {
		return String.format("%s (direction: %s, %s)", super.toString(), dir.x, dir.y);
	}

	public Vector2f getDir() {
		return dir;
	}

}
