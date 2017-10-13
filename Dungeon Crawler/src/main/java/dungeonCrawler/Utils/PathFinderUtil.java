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
		//Need an initial capacity for the priorityQueue. Keeping it simple for now, and using the squared distance from start to dest
		float squaredDist = (dest.x - start.x)*(dest.x - start.x) + (dest.y - start.y)*(dest.y - start.y);
		
		//Create the priorityQueue for open nodes to consider. PriorityQueue works on an array and needs an initial capacity
		PriorityQueue<Node> open = new PriorityQueue<Node>((int)Math.ceil(squaredDist));
		//Create a HashMap for closed nodes for fast memberOf lookups
		
		Node origin = new Node(start, 0, 0);
		open.add(origin);
		
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
		
		@Override
		public boolean equals(Object o) {
			if (o == this) {
				return true;
			}
			if (!(o instanceof Node)) {
				return false;
			}
			return this.getPosition().equals(((Node)o).getPosition());
		}
		
		@Override
		public int hashCode() {
			return 31 * pos.hashCode() + 17;
		}
		
		public float getCost() {
			return f;
		}
		
		public Vector2f getPosition() {
			return new Vector2f(pos);
		}
		
		
	}
	
}
