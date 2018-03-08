package dungeonCrawler.Utils;

import java.util.ArrayList;

import org.joml.Vector3f;

import dungeonCrawler.GameComponents.GameItem;
import dungeonCrawler.GameComponents.CollisionBounds.CylindricBounds;
import dungeonCrawler.GameComponents.CollisionBounds.PolygonalBounds;

public class CollisionUtil {

	/*
	 * Target is the 'victim' of collision resolution -> the MTV needs to move the target!
	 * 
	 * All boolean checks are bidirectional, i.e. it doesn't matter who calls the check since nothing needs to be resolved
	 */
	
	public static Vector3f resolveCollision(CylindricBounds actor, CylindricBounds target, Vector3f actorPos, Vector3f targetPos) {
		return null;
	}
	public static Vector3f resolveCollision(CylindricBounds actor, PolygonalBounds target, Vector3f actorPos, Vector3f targetPos) {
		
		if(actor == null || target == null) {
			return null;
		}
		//Collision detection via Separating Axis Theorem
		
		ArrayList<Vector3f> axes = target.getNormals();
		ArrayList<Vector3f> polygonVertices = target.getVertices();
		
		ArrayList<Vector3f> mtvs = new ArrayList<Vector3f>(axes.size());
		
		//Iterate over all axes, check if projections intersect, take early out if they don't
		for (Vector3f axis: axes) {
			
			//Project the Polygon onto the axis
			//First project position onto the axis
			
			float center = targetPos.dot(axis);
			//Iterate over all vertices of the polygon, project onto axis, find maximum and minimum
			float min, max;
			//Set min and max to projection of first point onto axis, will definitely be within projection
			max = polygonVertices.get(0).dot(axis);
			min = max;
			
			for(int i=1; i<target.getVertexCount(); i++) {
				float proj = polygonVertices.get(i).dot(axis);
				if(proj > max) {
					max = proj;
				}else if(proj < min) {
					min = proj;
				}
			}
			
			AxisProjection targetProjection = new CollisionUtil().new AxisProjection(min+center, max+center);
			
			//Project the Cylinder onto the axis
			//First project center point of base onto axis
			center = actorPos.dot(axis);
			//Min and max are merely set off by radius of the cylinder
			AxisProjection actorProjection = new CollisionUtil().new AxisProjection(center-actor.getRadius(), center+actor.getRadius());
			
			
			//Calculate minimum translation vector for given projections. If target is moved by this vector, the collision is resolved
			//along the current axis.
			Vector3f MTV = calculateMTV(actorProjection, targetProjection, axis);
			//Check if an MTV exists. If not, there is no overlap along this axis and no collision in general. Take early out
			if(MTV == null) {
				return null;
			}
			mtvs.add(calculateMTV(actorProjection, targetProjection, axis));
		}
		
		//If no early out has been taken by this point, polyfgon and cylinder overlap on every normal axis
		//But we still need to check the axis between the cylinder's center and the polygon's closest vertex
		//The most efficient method of finding the closest method involves voronoi regions, but since most polygons
		//will likely be triangles and rectangles, it can be justified to just calculate the distance to all vertices
		//-> This will only be executed in case a collision cannot be ruled out by checking against normal axes
		
		//Find closest vertex to cylinder center in x-y-plane
		Vector3f closestVert = polygonVertices.get(0);
		float dx = targetPos.x - polygonVertices.get(0).x;
		float dy = targetPos.y - polygonVertices.get(0).y;
		
		float smallestSqdist = dx*dx + dy+dy;
		for(int i = 1; i<target.getVertexCount(); i++) {
			dx = targetPos.x - polygonVertices.get(i).x;
			dy = targetPos.y - polygonVertices.get(i).y;
			float sqdist = dx*dx + dy+dy;
			if (sqdist < smallestSqdist) {
				closestVert = polygonVertices.get(i);
			}
		}
		
		//Now check collision against axis between circle center and closest vertex
		
		Vector3f connectingAxis = new Vector3f(targetPos.x - closestVert.x, targetPos.y - closestVert.y, 0f);
		
		float center = targetPos.dot(connectingAxis);
		//Iterate over all vertices of the polygon, project onto axis, find maximum and minimum
		float min, max;
		//Set min and max to projection of first point onto axis, will definitely be within projection
		max = polygonVertices.get(0).dot(connectingAxis);
		min = max;
		
		for(int i=1; i<target.getVertexCount(); i++) {
			float proj = polygonVertices.get(i).dot(connectingAxis);
			if(proj > max) {
				max = proj;
			}else if(proj < min) {
				min = proj;
			}
		}
		
		AxisProjection targetProjection = new CollisionUtil().new AxisProjection(min+center, max+center);
		
		//Project the Cylinder onto the axis
		//First project center point of base onto axis
		center = actorPos.dot(connectingAxis);
		//Min and max are merely set off by radius of the cylinder
		AxisProjection actorProjection = new CollisionUtil().new AxisProjection(center-actor.getRadius(), center+actor.getRadius());
		
		//Calculate minimum translation vector for given projections. If target is moved by this vector, the collision is resolved
		//along the current axis.
		Vector3f MTV = calculateMTV(actorProjection, targetProjection, connectingAxis);
		//Check if an MTV exists. If not, there is no overlap along this axis and no collision in general. Take early out
		if(MTV == null) {
			return null;
		}
		mtvs.add(calculateMTV(actorProjection, targetProjection, connectingAxis));

		//If this gets to this point, there definitely is a collision (A MTV was found along each axis)
				//From all MTVs, find the shortest one
				Vector3f shortestMTV = mtvs.get(0);
				float shortestLength = shortestMTV.length(); 
				for(int i=1; i<mtvs.size(); i++) {
					float length = mtvs.get(i).length();
					if(length < shortestLength) {
						shortestLength = length;
						shortestMTV = mtvs.get(i);
					}
				}
				return shortestMTV;
	}
	
	public static Vector3f resolveCollision(PolygonalBounds actor, CylindricBounds target, Vector3f actorPos, Vector3f targetPos) {
		//same as checkCollision(CylindricBounds actor, PolygonalBounds target..), but with invered direction!
		//-> swap actor and target and call that function
		Vector3f mtv = resolveCollision(target, actor, targetPos, actorPos);
		if(mtv != null) {
			mtv = (new Vector3f(mtv)).mul(-1f);
		}
		return mtv;
	}
	public static Vector3f resolveCollision(PolygonalBounds actor, PolygonalBounds target, Vector3f actorPos, Vector3f targetPos) {
		
		if(actor == null || target == null) {
			return null;
		}
		//Collision detection via Separating Axis Theorem
		
		ArrayList<Vector3f> axes = target.getNormals();
		axes.addAll(actor.getNormals());
		
		ArrayList<Vector3f> targetVertices = target.getVertices();
		ArrayList<Vector3f> actorVertices = actor.getVertices();
		
		ArrayList<Vector3f> mtvs = new ArrayList<Vector3f>(axes.size());
		//Iterate over all axes, check if projections intersect, take early out if they don't
		for (Vector3f axis: axes) {
			
			//Project each Polygon onto the axis
			//First project position onto the axis
			
			float center = targetPos.dot(axis);
			//Iterate over all vertices of the polygon, project onto axis, find maximum and minimum
			float min, max;
			//Set min and max to projection of first point onto axis, will definitely be within projection
			max = targetVertices.get(0).dot(axis);
			min = max;
			
			for(int i=1; i<target.getVertexCount(); i++) {
				float proj = targetVertices.get(i).dot(axis);
				if(proj > max) {
					max = proj;
				}else if(proj < min) {
					min = proj;
				}
			}
			
			AxisProjection targetProjection = new CollisionUtil().new AxisProjection(min+center, max+center);
			
			//Repeat for second polygon
			center = actorPos.dot(axis);
			max = actorVertices.get(0).dot(axis);
			min = max;
			
			for(int i=1; i<actor.getVertexCount(); i++) {
				float proj = actorVertices.get(i).dot(axis);
				if(proj > max) {
					max = proj;
				}else if(proj < min) {
					min = proj;
				}
			}
			
			AxisProjection actorProjection = new CollisionUtil().new AxisProjection(min+center, max+center);
			
			//Calculate minimum translation vector for given projections. If target is moved by this vector, the collision is resolved
			//along the current axis.
			Vector3f MTV = calculateMTV(actorProjection, targetProjection, axis);
			//Check if an MTV exists. If not, there is no overlap along this axis and no collision in general. Take early out
			if(MTV == null) {
				return null;
			}
			mtvs.add(calculateMTV(actorProjection, targetProjection, axis));
			
		}
		//If this gets to this point, there definitely is a collision (A MTV was found along each axis)
		//From all MTVs, find the shortest one
		Vector3f shortestMTV = mtvs.get(0);
		float shortestLength = shortestMTV.length(); 
		for(int i=1; i<mtvs.size(); i++) {
			float length = mtvs.get(i).length();
			if(length < shortestLength) {
				shortestLength = length;
				shortestMTV = mtvs.get(i);
			}
		}
		return shortestMTV;
	}
	
	
	public static Vector3f calculateMTV(AxisProjection actor, AxisProjection target, Vector3f axis) {
		
		float tmin = target.getMin();
		float tmax = target.getMax();
		float amin = actor.getMin();
		float amax = actor.getMax();
		
		if(tmax <= amin | tmin >= amax) {	//No overlap
			return null;
		}else if(tmin < amax && tmax > amax) {				//Overlapping to positive side
			return (new Vector3f(axis)).mul(amax-tmin);		//axis is the normalized vector, multiply by pos. overlap to get MTV
		}else if(tmax > amin && tmin < amin) { 				//Overlapping to negative side
			return (new Vector3f(axis)).mul(amin-tmax); 	//multiply by negative overlap
		
		}else if((tmin > amin && tmax < amax) ||			//target contained
				(tmin<amin && tmax > amax))	{ 				//actor contained
			
			if( (tmax-amin)<(amax-tmin)) {					//closer to negative side
				return (new Vector3f(axis)).mul(amin-tmax);	
			}else {											//closer to positive side
				return(new Vector3f(axis)).mul(amax-tmin);
			}
		}else {
			System.out.println("Collision resolution MTV calculation went wrong, this case is unhandled!!");
			return null;
		}
	}
	
	public class AxisProjection{
		
		float min, max;
		
		public AxisProjection(float min, float max) {
			this.min = min;
			this.max = max;
		}
		
		
		
		public float getMin() {
			return min;
		}



		public float getMax() {
			return max;
		}



		public boolean overlaps(AxisProjection proj) {
			return ((this.getMin() < proj.getMax()) && (proj.getMin() < this.getMax()));
		}
		

	}
}
