package dungeonCrawler.GameComponents.Structures;

import java.util.Map;
import java.util.Collection;
import java.util.HashMap;
import org.joml.Vector2i;

import dungeonCrawler.GameComponents.GameComponent;

import org.joml.Vector2f;

public class GameComponentMatrix {

	  private Map<Vector2i, GameComponent> values = new HashMap<Vector2i, GameComponent>();

	  public GameComponentMatrix() {
		  
	  }

	  public GameComponent get(int x, int y) {
	     return values.get(new Vector2i(x,y));
	  }
	  
	  public GameComponent get(Vector2i v) {
		  return values.get(v);
	  }
	  
	  public GameComponent get(Vector2f v) {
		  return values.get(new Vector2i((int)Math.floor(v.x),(int)Math.floor(v.y)));
	  }

	  public GameComponent set(int x, int y, GameComponent value) {
		  return values.put(new Vector2i(x,y), value);
	  }
	  
	  public GameComponent set(Vector2i v, GameComponent value) {
		  return values.put(new Vector2i(v.x, v.y), value);
	  }
	  
	  public GameComponent set(Vector2f v, GameComponent value) {
		  return values.put(new Vector2i((int)Math.floor(v.x),(int)Math.floor(v.y)), value);
	  }
	  
	  public int size() {
		  return values.size();
	  }
	  
	  public Collection<GameComponent> getCollection() {
		  return values.values();
	  }
		  
}
