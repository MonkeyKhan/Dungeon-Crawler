package dungeonCrawler.Utils;

import org.joml.Vector2f;
import java.util.Stack;
import java.util.PriorityQueue;
import dungeonCrawler.GameComponents.World;

public class PathFinderUtil {

	private PathFinderUtil() {
		
	}
	
	public Stack<Vector2f> findPath(Vector2f start, Vector2f dest, World world){
		Stack<Vector2f> path = new Stack<Vector2f>();
		
		return path;
	}
	
	private class Node implements Comparable<Node>{

		private float g;
		private float h;
		private float f;
		
		private final Vector2f pos;
		
		public Node(Vector2f pos, float moveCost, float heuristic) {
			this.pos = new Vector2f(pos);
			this.g = moveCost;
			this.h = heuristic;
			this.f = moveCost + heuristic;
		}
		
		@Override
		public int compareTo(Node otherNode) {
			return Float.compare(this.getCost(), otherNode.getCost());
		}
		
		public float getCost() {
			return f;
		}
		
		
	}
	
}
