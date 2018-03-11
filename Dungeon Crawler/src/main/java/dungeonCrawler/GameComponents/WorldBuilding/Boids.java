package dungeonCrawler.GameComponents.WorldBuilding;

import java.util.ArrayList;

import org.joml.Vector2f;
import org.joml.Vector3f;

import dungeonCrawler.Commands.CollisionEvent;
import dungeonCrawler.Commands.MoveCommand;
import dungeonCrawler.GameComponents.GameItem;
import dungeonCrawler.Utils.CollisionUtil;
import dungeonCrawler.Utils.CollisionUtil.AxisProjection;

public class Boids {

	private final float neighbourhoodRadius = 5f; 
	private float acc = 0;
	private ArrayList<Agent> agents;
	private boolean finished = false;
	
	public Boids(ArrayList<GameItem>items) {
		agents = new ArrayList<Agent>(items.size());
		for(GameItem a: items) {
			agents.add(new Agent(a, ((Room)a).getSize()));
		}
		
	}
	
	public boolean update(float t){
		
		if(!finished) {
			acc += t;
			for(Agent a: agents) {
				
				
				Vector2f cohesion = cohesion(a).mul(0.5f);
				Vector2f destacking = destack(a).mul(2f);
				Vector2f separation = separation(a).mul(100f);		
				Vector2f tileSnap = tileSnap(a).mul(10f);
				
				a.updateVelocity(cohesion.add(destacking));//.add(separation));
				a.update(t);

			}
			
			if(acc > 3) {
				finished = true;
				Agent closest = agents.get(0);
				float closestDist = 10000000000f;
				for(Agent a: agents) {
					if(!a.islocked()) {
						finished = false;
						if(a.getPos().length()<closestDist) {
							closestDist = a.getPos().length();
							closest = a;
						}
					}
				}
				if(destack(closest).length() < closest.getSize().length()) {
					closest.lock();
				}
				acc = 2.9f;
			}
			return false;
		}else {
			return true;
		}
	}
	
	
	private Vector2f separation(Agent a) {
		/**
		 * Behaviour that makes agent align with other agents:
		 */
		Vector2f centerOfMass = new Vector2f();
		int numberOfNeighbouringAgents = 0;
		
		for(Agent otherAgent: agents) {
			
			if(a != otherAgent) {
				Vector2f dist = a.getPos();
				
				if(dist.length()<=neighbourhoodRadius) {
					centerOfMass.add(otherAgent.getPos());
					numberOfNeighbouringAgents++;
				}	
			}
		}
		//Divide aggregate position by number of agents in neighborhood to get center of mass
		if(numberOfNeighbouringAgents>0) {
			centerOfMass.mul(1f/numberOfNeighbouringAgents);
			return new Vector2f(a.getPos()).sub(centerOfMass).normalize().mul(a.getPos().length());
		}else {
			return new Vector2f();
		}
		
	}

	
	private Vector2f destack(Agent a) {
		/**
		 * Lets agents destack
		 */
		
		Vector2f destackVel = new Vector2f();
		float thisMinX = a.getPos().x;
		float thisMaxX = a.getPos().x + a.getSize().x;
		float thisMinY = a.getPos().y;
		float thisMaxY = a.getPos().y + a.getSize().y;
		
		AxisProjection thisXproj = new CollisionUtil().new AxisProjection(thisMinX, thisMaxX);
		AxisProjection thisYproj = new CollisionUtil().new AxisProjection(thisMinY, thisMaxY);
		
		Vector3f xMTV, yMTV;
		for(Agent otherAgent: agents) {
			if(a != otherAgent && a!= null && otherAgent != null) {
				float otherMinX = otherAgent.getPos().x;
				float otherMaxX = otherAgent.getPos().x + otherAgent.getSize().x;
				float otherMinY = otherAgent.getPos().y;
				float otherMaxY = otherAgent.getPos().y + otherAgent.getSize().y;
				
				AxisProjection otherXproj = new CollisionUtil().new AxisProjection(otherMinX, otherMaxX);
				AxisProjection otherYproj = new CollisionUtil().new AxisProjection(otherMinY, otherMaxY);
				
				xMTV = CollisionUtil.calculateMTV(otherXproj, thisXproj, new Vector3f(1,0,0));
				yMTV = CollisionUtil.calculateMTV(otherYproj, thisYproj, new Vector3f(0,1,0));
				
				if(xMTV != null && yMTV != null) {
					if(xMTV.length() > yMTV.length()) {
						destackVel.add(0, yMTV.y);
					}else {
						destackVel.add(xMTV.x, 0);
					}
					//sepVel.add(xMTV.x, yMTV.y);
				}
			}
		}
		
		if(destackVel.length()>0) {// && destackVel.length()<1) {
			destackVel.normalize();//
		}
		return destackVel.mul(a.getSize().length());
		
		
	}
	
	private Vector2f tileSnap(Agent a) {
		
		
		//Calculate offset to next integer. 
		float xOff = a.getPos().x%1f;
		float yOff = a.getPos().y%1f;
		//xOff and yOff will be in the range of -1 to +1, where negative numbers are the result of negative position and vice versa.
		// 0.5 - 1: Closer to next HIGHER integer
		// 0 - 0.5: Closer to next LOWER integer
		//-0.5 - 0: Closer to next HIGHER integer
		//-1 - -0.5: Closer to next LOWER integer
		
		float xSnap, ySnap;
		if(xOff >=0.5 ||  (xOff <= 0 && xOff >= -0.5)){
			xSnap = 1f;
		}else {
			xSnap = -1f;
		}
		
		if(yOff >=0.5 ||  (yOff <= 0 && yOff >= -0.5)){
			ySnap = 1f;
		}else {
			ySnap = -1f;
		}
		
		return new Vector2f(xSnap, ySnap).normalize();
	}
	
	private Vector2f cohesion(Agent a) {
		/**
		 * Lets agents tend towards the center
		 */
		Vector2f dist = new Vector2f( -1*a.getPos().x, -1*a.getPos().y);
		float m = (float)Math.exp(dist.length()/50);
		if(dist.length()>0){
			return dist.normalize();
		}
		return dist;//.mul((m));

	}
	
	private float cohesionFactor(GameItem a) {
		float dist = a.getPosition().length();
		return (float)(1d-Math.exp(-dist/50d));
	}
	/*
	private Vector2f calcCohesion(GameItem a) {
		/**
		 * Behavious that makes agents stick together:
		 * Calculate center of mass of all neighboring agents and steer towards it
		 *//*
		
		Vector2f cohVel = new Vector2f();
		int numberOfNeighbouringAgents = 0;
		for(Agent otherAgent: agents) {
			
			if(a != otherAgent) {
				Vector2f dist = new Vector2f(a.getPos().sub(otherAgent.getPos()));
				
				if(dist.length()<=neighbourhoodRadius) {
					cohVel.add(otherAgent.getPos());
					numberOfNeighbouringAgents++;
				}
				
			}
			
			
		}
		//Divide aggregate position by number of agents in neighborhood to get center of mass
		cohVel.mul(1f/numberOfNeighbouringAgents);
		//Sub a's position to get vector from a to center of mass
		cohVel.sub(a.getPos());
		//Normalize velocity
		cohVel.normalize();
		return cohVel;
		
	}
*/
	
}
