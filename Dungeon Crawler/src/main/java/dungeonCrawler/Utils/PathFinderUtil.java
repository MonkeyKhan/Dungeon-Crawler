package dungeonCrawler.Utils;

import org.joml.Vector2f;
import org.joml.Vector3f;

import java.util.Stack;
import java.util.PriorityQueue;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import dungeonCrawler.Debug;
import dungeonCrawler.Commands.Path;
import dungeonCrawler.DataStructures.FlexPQueue;
import dungeonCrawler.GameComponents.DummyUnit;
import dungeonCrawler.GameComponents.GameItem;
import dungeonCrawler.GameComponents.World;
import dungeonCrawler.GameComponents.CollisionBounds.PolygonalBounds;

public class PathFinderUtil {

	private PathFinderUtil() {
		
	}
	
	public static Path findPath(Vector2f start, Vector2f dest, World world, float unitRadius){
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

		path.push(start);
		
		//Only build the final path object here so that the mesh is not recalculated so often
		Stack<Vector2f> smoothedPath = smoothPath(path, world, unitRadius);
		return new Path(smoothedPath);
	}
	
	public static Path findPath(Vector3f start, Vector3f dest, World world, float unitRadius){
		return findPath(
				new Vector2f(start.x, start.y),
				new Vector2f(dest.x, dest.y),
				world, unitRadius);
	}
	
	private static Stack<Vector2f> smoothPath(Stack<Vector2f> path, World world, float unitRadius){
		
		/**
		 * The goal of path smoothing is to cut out intermediate steps where a direct connection between two points is possible.
		 * 
		 */
		
		//If path has only positions, no smoothing can be done
		if(path.size()==2) {
			return path;
		}
		Stack<Vector2f> smoothedPath = new Stack<Vector2f>();
		
		Vector2f current = path.pop();
		smoothedPath.push(current);
		//The immediately following position should always be reachable.
		Vector2f next = path.pop();
		
		
		while(!path.empty()) {
			
			//Iterate through positions along path until a position is found that is not reachable
			while(!path.empty() && !pathYieldsCollision(current, path.peek(), unitRadius, world)) {
				//If there was no collision with path.peek(), save this as next position -> move "next forward until it is the
				//last directly reachable position
				next = path.pop();
			}
			//Next contains the last position that was directly reachable from current -> push onto stack
			smoothedPath.push(next);
			current = next;
			//We either got here because there was a collision with the position at top of the stack, or because the stack is empty.
			//3 cases here:
			// a) - 0 positions left in path -> smoothing is done, outer loop will be exited
			// b) - 1 position left in path -> cannot smooth any further, add last position to smoothed path and done
			// c) - >1 position left in path -> potential for further smoothing, return to outer loop
			// Treat case b special
			if (path.size()==1) {
				smoothedPath.push(path.pop());	//case 2: 1 position left
				Collections.reverse(smoothedPath);		//Positions were pushed in reverse direction, reverse again
				return smoothedPath;
			}
			//case a & c are covered by normal loop evaluation

		}
		Collections.reverse(smoothedPath); 		//Positions were pushed in reverse direction, reverse again
		return smoothedPath;
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
	
	private static boolean pathYieldsCollision(Vector2f start, Vector2f end, float unitRadius, World world) {
		
		
		Vector3f start3 = new Vector3f(start.x, start.y, 0f);
		DummyUnit path = new DummyUnit(start3, createPathBounds(start, end, unitRadius));
		
		for(GameItem i: world.getTiles(start3)) {
			if (i.checkCollision(path)){
				return true;
			}
		}
		return false;
		
	}
	
	private static PolygonalBounds createPathBounds(Vector2f start, Vector2f end, float unitRadius) {
		/**
		 * To check whether a path is available from start to end, create rectangular bounds that cover that path and check
		 * against environment. This rectangular bounding box has the length of the path and the width of the unit and is oriented
		 * along the path.
		 */
		Vector3f dir = new Vector3f(end.x-start.x, end.y-start.y,0);
		Vector3f n = new Vector3f(-1*dir.y, dir.x,0);
		n = n.normalize();
		Vector3f p1 = (new Vector3f(n)).mul(unitRadius);
		Vector3f p2 = (new Vector3f(n)).mul(-1*unitRadius);
		Vector3f p3 = (new Vector3f(p1)).add(dir);
		Vector3f p4 = (new Vector3f(p2)).add(dir);
		
		try {
			return new PolygonalBounds(new Vector3f[] {p1,p2,p3,p4});
		}catch(Exception e) {
			return null;
		}
		
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
