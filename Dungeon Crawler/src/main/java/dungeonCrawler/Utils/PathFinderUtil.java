package dungeonCrawler.Utils;

import org.joml.Vector2f;
import java.util.Stack;
import java.util.PriorityQueue;
import java.util.ArrayList;
import java.util.HashMap;
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
		HashMap<Vector2f, Node> closed = new HashMap<Vector2f, Node>((int)Math.ceil(squaredDist));
		
		//Node current holds the node currently evaluated
		Node current;
		//Search until the current node represents the destination
		while(!open.peek().getPosition().equals(dest)) {
			
			//Move the lowest cost node from open to closed
			current = open.poll();
			closed.put(current.getPosition(), current);
			//iterate through all neighbors	
			for(Node neighbor: getNeighbors(current, dest, world)) {
				//TODO: Check if neighbor already exists in open and if so, update its cost if neihgbor's cost is lower
				//Test pc1
				//Test laptop2
			}
			
			
		}
		Node origin = new Node(null, start, 0, 0);
		open.add(origin);
		
		return path;
	}
	
	private ArrayList<Node> getNeighbors(Node node, Vector2f dest, World world) {
		//Returns an ArrayList of all neighboring nodes of a node, that are valid for pathfinding, i.e. passable
		ArrayList<Node> neighbors = new ArrayList<Node>(8);
		
		//Iterate through all positions that neighbor the given node, including diagonally
		for(int i=-1; i<2; i++) {
			for(int j=-1; j<2; j++) {
				Vector2f neighborPos = new Vector2f(node.getPosition());
				neighborPos.add(i, j);
				//If the neighbor is passable and is not the node itself, add them
				if(!(i==0 && j== 0) & world.isPassable(neighborPos)) {
					
					//Calculate moveCost of neighboring nodes by adding the moveCost relative to the parent node
					//to the parent node's moveCost
					float moveCost = node.getMoveCost() + new Vector2f(i, j).length();
					//Add the neighbor node to ArrayList with the given node as its parent and set moveCost and heuristic
					neighbors.add(new Node(node, neighborPos, moveCost, calcHeuristics(neighborPos, dest)));
				}
			}
		}
		
		return neighbors;
	}
	
	private float calcHeuristics(Vector2f start, Vector2f dest) {
		//The heuristic is the estimated moveCost from start to dest. Using the distance here.
		return Math.abs(dest.distance(start));
	}
	
	private class Node implements Comparable<Node>{

		//A node represents a walkable position along a path. To find the shortest path from a to b, each node has a
		//cost associated to it. The cost is split up into a known cost (g) that represents the real cost to move from a
		//to the node, and an heuristic cost (h) that represents the estimated cost to move from the node to b.
		//In order to construct a path, each node (besides the original node at a) knows its parent node, from which it is reached the cheapest.
		
		private Node parent;
		private float g;
		private float h;
		
		private final Vector2f pos;
		
		public Node(Node parent, Vector2f pos, float moveCost, float heuristic) {
			this.parent = parent;
			this.pos = new Vector2f(pos);
			this.g = moveCost;
			this.h = heuristic;
		}
		
		
		@Override
		public int compareTo(Node otherNode) {
			//Nodes are compared based on their total cost
			return Float.compare(this.getCost(), otherNode.getCost());
		}
		
		@Override
		public boolean equals(Object o) {
			//Nodes are equal if their position matches
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
			return g + h;
		}
		
		public float getMoveCost() {
			return g;
		}
		
		public Vector2f getPosition() {
			return new Vector2f(pos);
		}
		
		public Node getParent() {
			return parent;
		}
		
		
	}
	
}
