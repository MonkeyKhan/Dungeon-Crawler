package dungeonCrawler.GameComponents;

import java.util.ArrayList;

import org.joml.Vector2f;
import org.joml.Vector3f;

import dungeonCrawler.Debug;
import dungeonCrawler.Update;
import dungeonCrawler.Commands.CollisionEvent;
import dungeonCrawler.Commands.Command;
import dungeonCrawler.Commands.CommandRevision;
import dungeonCrawler.Commands.NullCommand;
import dungeonCrawler.GameComponents.CollisionBounds.Collidable;
import dungeonCrawler.GameComponents.WorldBuilding.Room;
import dungeonCrawler.States.MovingState;
import dungeonCrawler.States.State;
import dungeonCrawler.UI.Ray;
import dungeonCrawler.UI.RayIntersection;

public abstract class GameItem extends GameComponent {

	private Mesh mesh;
	private Command command;
	private Collidable collisionBounds;
	
	//Temp
	private float rand;
	
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
			if (collisionBounds.resolveCollision(target.getCollisionBounds(), this.getPosition(), target.getPosition())!=null) {
				return true;
			}else {
				return false;
			}
		}else {
			return false;
		}
	}
	
	public CollisionEvent resolveCollision(GameItem target, Vector3f targetPos) {
		/**
		 * Resolves collision between this and another GameItem target, where the position of target is specified in targetPos
		 * Specifying the position of target is needed to check collisions resulting from change of state. In that case targetPos
		 * may carry the new, updated position.
		 */
		
		if(this.collisionBounds == null) {
			return null;
		}
		Vector3f mtv = collisionBounds.resolveCollision(target.getCollisionBounds(), this.getPosition(), targetPos);
		if(mtv == null) {
			return null;
		}
		Vector2f dir = target.getState().getDir();
		float t = mtv.dot(new Vector3f(dir.x, dir.y, 0))*(-1f)*mtv.length();
		return new CollisionEvent(mtv, this, target, t);
	}
	
	public CollisionEvent resolveCollision(GameItem target) {
		return resolveCollision(target, target.getPosition());
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
	
	public void acceptRevision(CommandRevision rev) {
		rev.revise(command);
	}
	
	public void setMesh(Mesh mesh){
		this.mesh = mesh;
	}
	

}
