package dungeonCrawler.GameComponents;

import java.util.ArrayList;

import org.joml.Vector3f;

import dungeonCrawler.Debug;
import dungeonCrawler.Update;
import dungeonCrawler.Commands.Command;
import dungeonCrawler.Commands.NullCommand;
import dungeonCrawler.GameComponents.CollisionBounds.Collidable;
import dungeonCrawler.States.State;
import dungeonCrawler.UI.Ray;
import dungeonCrawler.UI.RayIntersection;

public abstract class GameItem extends GameComponent {

	private Mesh mesh;
	private Command command;
	private Collidable collisionBounds;
	
	public GameItem( Vector3f pos, Mesh mesh) {
		super(pos);
		this.mesh = mesh;
		this.issueCommand(new NullCommand());
		this.collisionBounds = null;
	}
	
	
	public GameItem(Vector3f pos, Mesh mesh, Collidable collisionBounds) {
		this(pos, mesh);
		this.collisionBounds = collisionBounds;
	}
	
	@Override
	public void render() {
		if(mesh != null) {
			mesh.render(this.getPosition());
		}
		command.render();
	}

	@Override
	public ArrayList<Update> update(float interval) {
		
		ArrayList<Update> update = new ArrayList<Update>();
		
		State newState = this.getState().update(interval);
		newState = command.updateState(newState);
		
		//Check whether command is finished executing
		if (command.isExecuted()) {
			command = new NullCommand();
		}
		if (!newState.equals(this.getState())) {
			update.add(new Update(this, newState));
		}
		return update;
	}
	
	@Override
	public String toString() {
		return this.getClass().getSimpleName();
	}

	@Override
	public void printDbg() {
		System.out.println("\t"+this.toString()+": " + this.getPosition().x + ", " + this.getPosition().y + "; current state: " + getState().toString());
	}

	@Override
	public ArrayList<RayIntersection> processRay(Ray r) {
		//Return empty list of intersections for now so subclasses that don't implement ray processing just inherit this
		return new ArrayList<RayIntersection>();
	}
	
	public boolean checkCollision(GameItem target) {
		if (this.hasCollisionBounds() && target.hasCollisionBounds()) {
			return collisionBounds.checkCollision(target.getCollisionBounds(), this.getPosition(), target.getPosition());
		}else {
			return false;
		}
	}
	
	protected Mesh getMesh() {
		return mesh;
	}
	
	protected Collidable getCollisionBounds() {
		return collisionBounds;
	}
	
	protected boolean hasCollisionBounds() {
		if(collisionBounds == null) {
			return false;
		}else{
			return true;
		}
	}
	
	public void issueCommand(Command command) {
		
		if(Debug.p) {
			System.out.println(String.format("Issuing new %s to %s", command.toString(), this.toString()));
		}
		this.command = command;
	}
	
	
	
	

}
