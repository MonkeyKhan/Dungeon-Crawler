package dungeonCrawler.States;

import org.joml.Vector3f;

public class NullState extends State {

	//NullState is a state for all GameItems that aren't supposed to change (Tiles etc..)
	
	public NullState (Vector3f pos) {
		super(pos);
	}
	
	public NullState(State state) {
		super(state);
	}
	@Override
	public boolean takesCommands() {
		return false;
	}

}
