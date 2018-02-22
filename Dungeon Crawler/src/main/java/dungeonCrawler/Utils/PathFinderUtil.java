package dungeonCrawler.Utils;

import org.joml.Vector2f;
import org.joml.Vector3f;

import java.util.Stack;
import java.util.PriorityQueue;
import java.util.ArrayList;
import java.util.HashMap;

import dungeonCrawler.Debug;
import dungeonCrawler.DataStructures.FlexPQueue;
import dungeonCrawler.GameComponents.World;

public class PathFinderUtil {

	private PathFinderUtil() {
		
	}
	
	public static Stack<Vector2f> findPath(Vector2f start, Vector2f dest, World world){
		Stack<Vector2f> path = new Stack<Vector2f>();
		
		Vector2f startTrunc = new Vector2f((float)Math.floor(start.x), (float)Math.floor(start.y));
		Vector2f destTrunc = new Vector2f((float)Math.floor(dest.x), (float)Math.floor(dest.y));
		//Need an initial capacity for the priorityQueue. Keeping it simple for now, and using the squared distance from start to dest
		float squaredDist = (dest.x - start.x)*(dest.x - start.x) + (dest.y - start.y)*(dest.y - start.y);
		
		//Create the priorityQueue for open nodes to consider. PriorityQueue works on an array and needs an initial capacity
		FlexPQueue<Node> open = new FlexPQueue<Node>((int)Math.ceil(squaredDist));
		//Create a HashMap for closed nodes for fast memberOf lookups
		HashMap<Vector2f, Node> closed = new HashMap<Vector2f, Node>((int)Math.ceil(squaredDist));
		
		//Node current holds the node currently evaluated
		Node current;
		Node origin = new Node(null, startTrunc, 0, 0);
		open.update(origin);
		//Search until the current node represents the destination
		if(Debug.p) {
			System.out.println(String.format("Searching for a path from %s to %s...", start.toString(), dest.toString()));
		}

		while(!open.peek().getPosition().equals(destTrunc)) {
			
			//Move the lowest cost node from open to closed
			current = open.poll();
			closed.put(current.getPosition(), current);
			//iterate through all neighbors	
			for(Node neighbor: getNeighbors(current, destTrunc, world)) {
				if(!closed.containsValue(neighbor.getPosition())) {
					//If the neighbor hasn't been processed yet, update the current list with it.
					//This will either add the neighboring node to the PQueue (if it's not in the queue yet), 
					//or update the existing node (if this one has a higher priority, i.e. lower cost)
					if( open.update(neighbor) && Debug.p) {
						System.out.println(String.format("Adding %s to open list", neighbor.toString()));
					}
				}
			}
			closed.put(current.getPosition(),current);			
		}


		//instead of pushing destination node (which contains the truncated destination pos), push the real destination position onto the stack
		path.push(dest);
		//open.poll() MUST return the destination node, since that was the break condition for previous loop
		current = open.poll();
		String pathStr = "";
		while(!current.equals(origin)) {//Check here to make sure a parent exists at all
			current = current.getParent();
			if(!current.equals(origin)) {//Check here to not add origin
				Vector2f nextPos = new Vector2f(current.getPosition().x + 0.5f, current.getPosition().y + 0.5f);
				path.push(nextPos);
				pathStr = nextPos.toString() + ", " + pathStr;
			}
		}
		
		if (Debug.p) {
			System.out.println("Pathfinder found destination via " + pathStr);
		}

		
		return path;
	}
	
	public static Stack<Vector2f> findPath(Vector3f start, Vector3f dest, World world){
		return findPath(
				new Vector2f(start.x, start.y),
				new Vector2f(dest.x, dest.y),
				world);
	}
	
	private static ArrayList<Node> getNeighbors(Node node, Vector2f dest, World world) {
		//Returns an ArrayList of all neighboring nodes of a node, that are valid for pathfinding, i.e. passable
		ArrayList<Node> neighbors = new ArrayList<Node>(8);
		
		//Iterate through all positions that neighbor the given node, not including diagonally
		Vector2f[] neighboringPositions = new Vector2f[4];
		neighboringPositions[0] = new Vector2f(-1,0);	
		neighboringPositions[1] = new Vector2f(1,0);	
		neighboringPositions[2] = new Vector2f(0,-1);	
		neighboringPositions[3] = new Vector2f(0,1);
		for(Vector2f n: neighboringPositions) {
			
			Vector2f neighborPos = new Vector2f(node.getPosition());
			neighborPos.add(n);
			//If the neighbor is passable and is not the node itself, add them
			if(world.isPassable(neighborPos) || neighborPos.equals(dest)) {
				
				//Calculate moveCost of neighboring nodes by adding the moveCost relative to the parent node
				//to the parent node's moveCost
				float moveCost = node.getMoveCost() + 1;
				//Add the neighbor node to ArrayList with the given node as its parent and set moveCost and heuristic
				neighbors.add(new Node(node, neighborPos, moveCost, calcHeuristics(neighborPos, dest)));
			}
		}
		
		return neighbors;
	}
	
	private static float calcHeuristics(Vector2f start, Vector2f dest) {
		//The heuristic is the estimated moveCost from start to dest. Using the distance here.
		return Math.abs(dest.distance(start));
	}
	
	private static class Node implements Comparable<Node>{

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
			/**
			 * Nodes are compared based on their moveCost
			 * A node of LOWER cost has a HIGHER priority:
			 * if this's cost is lower, compareTo returns a POSITIVE value
			 * if otherNode's cost is is lower, compareTo returns a NEGATIVE value
			 * if both nodes have an equal cost, compareTo returns 0
			 * IMPORTANT: equal in terms of compareTo does NOT imply equal in terms of equals()!
			 */
			return -1 * Float.compare(this.getCost(), otherNode.getCost());
		}
		
		@Override
		public boolean equals(Object o) {
			/**
			 * Compare this to the argument Object o and return true if this and o are equal or return false if not
			 * Testing for equality is based on the nodes' position
			 * IMPORTANT: equal in terms of equals() does NOT imply equal in terms of compareTo()!
			 **/
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
		
		@Override
		public String toString(){
			return String.format("Node %s, %s (cost: %s, heuristic: %s)", 1*pos.x, 1*pos.y, this.g, this.h);
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
