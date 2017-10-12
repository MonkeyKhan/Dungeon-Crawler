package dungeonCrawler.GameComponents;

import java.util.ArrayList;

import org.joml.Vector3f;

import dungeonCrawler.Update;
import dungeonCrawler.Commands.Command;
import dungeonCrawler.Commands.NullCommand;
import dungeonCrawler.States.IdleState;
import dungeonCrawler.States.State;
import dungeonCrawler.Utils.MeshUtil;

public abstract class Unit extends GameItem{

	
	public Unit(Vector3f position) {
		super(position, MeshUtil.makePlayer());
		this.setState(new IdleState(this.getState()));
	}
	
}
