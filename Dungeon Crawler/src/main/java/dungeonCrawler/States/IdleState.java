package dungeonCrawler.States;

import org.joml.Vector3f;

public class IdleState extends State {

	//IdleState is a State for GameItems that do nothing but wait for commands.
	public IdleState(Vector3f pos) {
		super(pos);		
	}
	
	public IdleState(State state) {
		super(state);
	}
	
	@Override
	public boolean takesCommands() {
		return true;
	}

}
