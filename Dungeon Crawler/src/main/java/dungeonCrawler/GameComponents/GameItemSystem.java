package dungeonCrawler.GameComponents;

import java.util.ArrayList;
import java.util.Iterator;

import org.joml.Vector2i;
import org.joml.Vector3f;

import dungeonCrawler.Update;
import dungeonCrawler.UI.Ray;
import dungeonCrawler.UI.RayIntersection;

import org.joml.Vector2f;

public abstract class GameItemSystem extends GameComponent {
	
	public GameItemSystem(Vector2i position) {
		super(new Vector3f(position.x,position.y,0));
	}
	
	@Override
	public void render() {
		Iterator<GameComponent> iterator = iterator();
		while(iterator.hasNext() ){
			iterator.next().render();
		}
	}
	
	@Override
	public ArrayList<Update> update(float interval) {
		ArrayList<Update> updates = new ArrayList<Update>();
		Iterator<GameComponent> iterator = iterator();
		while(iterator.hasNext()){
			updates.addAll(iterator.next().update(interval));
		}
		return updates;

	}

	@Override
	public void printDbg() {
		Iterator<GameComponent> iterator = iterator();
		while(iterator.hasNext()){
			iterator.next().printDbg();
		}		
	}

	@Override
	public ArrayList<RayIntersection> processRay(Ray r) {
		ArrayList<RayIntersection> intersected = new ArrayList<RayIntersection>();
		Iterator<GameComponent> iterator = iterator();
		while(iterator.hasNext()){
			intersected.addAll(iterator.next().processRay(r));
		}
		return intersected;
	}
	
	
	
	
	
}
