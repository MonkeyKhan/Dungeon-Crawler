package dungeonCrawler.GameComponents;

import java.util.ArrayList;
import java.util.Iterator;

import org.joml.Vector2f;
import org.joml.Vector3f;

import dungeonCrawler.Update;
import dungeonCrawler.GameComponents.Structures.NullIterator;
import dungeonCrawler.States.NullState;
import dungeonCrawler.States.State;
import dungeonCrawler.UI.Ray;
import dungeonCrawler.UI.RayIntersection;

public abstract class GameComponent implements Iterable<GameComponent> {

	private State state;
	
	public GameComponent(Vector3f position) {
		this.state = new NullState(new Vector3f(position));
	}
	public Vector3f getPosition() {
		return new Vector3f(state.getPosition());
	}
	
	public Vector2f getGridPosition() {
		return new Vector2f(state.getGridPosition());
	}
	
	public void setState(State state) {
		this.state = state;
	}
	
	public State getState() {
		return this.state;
	}
	
	public abstract void render();
	
	public abstract ArrayList<Update> update(float interval);
	
	public Iterator<GameComponent> iterator() {
		return new NullIterator();
	}
	
	public abstract void printDbg();
	
	public abstract ArrayList<RayIntersection> processRay(Ray r);
	
}
