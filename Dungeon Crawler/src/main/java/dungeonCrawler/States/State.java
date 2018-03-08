package dungeonCrawler.States;

import org.joml.Vector2f;
import org.joml.Vector3f;

public class State {

	private Vector3f pos;
	
	
	//Constructor for creating a state from scratch
	
	protected State (Vector3f pos) {
		this.pos = new Vector3f(pos);
	}
	
	//Constructors that copies attributes from passed state
	
	protected State (State state) {
		this(state.getPosition());
	}
	
	protected State copy () {
		return new State(this);
	}
	
	public State updatePos(Vector3f pos) {
		State updatedState = this.copy();
		updatedState.pos = pos;
		
		return updatedState;
	}
	
	//equals-Method that does trivial checking for null and class-equality. Specific checking for equality is delegated to abstract class equalsSpecific()
	//that may be implemented by subclasses. equals() is set to final so that the trivial checks are always executed by subclasses.
	//TODO: Implement hashCode!!!
	@Override	
	public final boolean equals(Object o) {
		if (o == this) {
			return true;
		}		

		if(!this.getClass().equals(o.getClass())) {
			return false;
		}
		
		if(!this.getPosition().equals(((State)o).getPosition())) {
			return false;
		}
		return equalsSpecific(o);
	}
	
	@Override
	public String toString() {
		return String.format("%s at %s, %s", this.getClass().getSimpleName(), pos.x, pos.y);
	}
	//specific checking for equality of states. Should check for any attributes that aren't shared among all states.
	protected boolean equalsSpecific(Object o) {
		return true;
	}
	
	//method for self-updating of states, mostly for time-dynamic behavior. Most states won't make use of this, so a default implementation is provided
	//where only the current state is returned. Dynamic states have to override accordingly.
	public State update(float interval) {
		return this; 
	}
	
	//Check if a state allows for taking commands.
	public boolean takesCommands() {
		return false;
	}
		
	public Vector3f getPosition() {
		return new Vector3f(pos);
	}
	
	public Vector2f getGridPosition() {
		return new Vector2f(pos.x, pos.y);
	}
	
	public Vector2f getDir() {
		return new Vector2f(0f, 0f);
	}
	
	public void forcePosition(Vector3f pos) {
		this.pos = pos;
	}
		
}
