package dungeonCrawler.GameComponents;

import org.joml.Vector3f;

import dungeonCrawler.Update;
import dungeonCrawler.Commands.Command;
import dungeonCrawler.Commands.NullCommand;
import dungeonCrawler.GameComponents.CollisionBounds.Collidable;
import dungeonCrawler.GameComponents.CollisionBounds.CylindricBounds;
import dungeonCrawler.GameComponents.CollisionBounds.PolygonalBounds;
import dungeonCrawler.States.IdleState;
import dungeonCrawler.States.NullState;
import dungeonCrawler.States.State;
import dungeonCrawler.Utils.MeshUtil;

public abstract class Unit extends GameItem{

	
	public Unit(Vector3f position) {
		super(position, MeshUtil.makePlayer());
		this.setState(new IdleState(this.getState()));
	}	
	
	public Unit(Vector3f position, float radius, float height ) {
		super(position, MeshUtil.makePlayer(radius, height), new CylindricBounds(radius, height));
				/*new PolygonalBounds(new Vector3f[] {
				new Vector3f(-0.2f, -0.2f, 0f),
				new Vector3f(0.2f, -0.2f, 0f),
				new Vector3f(0.2f, 0.2f, 0f),
				new Vector3f(-0.2f, 0.2f, 0f)}));
				*/
		this.setState(new IdleState(this.getState()));
	}
	
	public Unit(Vector3f position, Collidable bounds ) {
		super(position, null, bounds);
		this.setState(new NullState(this.getState()));
	}
	
	public float getWidth() {
		return this.getCollisionBounds().getWidth();
	}
	
}
