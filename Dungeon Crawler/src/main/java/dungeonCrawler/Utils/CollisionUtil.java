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
	
	public static boolean checkCollision(CylindricBounds actor, CylindricBounds target, Vector3f actorPos, Vector3f targetPos) {
		return true;
	}
	public static boolean checkCollision(CylindricBounds actor, PolygonalBounds target, Vector3f actorPos, Vector3f targetPos) {
		//Collision detection via Separating Axis Theorem
		
		ArrayList<Vector3f> axes = target.getNormals();
		ArrayList<Vector3f> polygonVertices = target.getVertices();
		
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
			
			AxisProjection polygonProjection = new CollisionUtil().new AxisProjection(min+center, max+center);
			
			//Project the Cylinder onto the axis
			//First project center point of base onto axis
			center = actorPos.dot(axis);
			//Min and max are merely set off by radius of the cylinder
			AxisProjection cylinderProjection = new CollisionUtil().new AxisProjection(center-actor.getRadius(), center+actor.getRadius());
			
			
			//If projections do not overlap, there is no collision, take early out!
			if(!cylinderProjection.overlaps(polygonProjection)){
				return false;
			}
		}
		
		//If no early out has been taken by this point, triangle and cylinder overlap on every normal axis
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
		
		AxisProjection polygonProjection = new CollisionUtil().new AxisProjection(min+center, max+center);
		
		//Project the Cylinder onto the axis
		//First project center point of base onto axis
		center = actorPos.dot(connectingAxis);
		//Min and max are merely set off by radius of the cylinder
		AxisProjection cylinderProjection = new CollisionUtil().new AxisProjection(center-actor.getRadius(), center+actor.getRadius());
		
		
		//If projections do not overlap, there is no collision, take early out!
		if(!cylinderProjection.overlaps(polygonProjection)){
			return false;
		}
		
		//If no out has been taken by now, no axis could be found on which the shapes do not overlap -> they are colliding
		
		return true;
	}
	
	public static boolean checkCollision(PolygonalBounds actor, CylindricBounds target, Vector3f actorPos, Vector3f targetPos) {
		//same as checkCollision(CylindricBounds actor, PolygonalBounds target..)
		//-> swap actor and target and call that function
		return checkCollision(target, actor, targetPos, actorPos);
	}
	public static boolean checkCollision(PolygonalBounds actor, PolygonalBounds target, Vector3f actorPos, Vector3f targetPos) {
		//Collision detection via Separating Axis Theorem
		
		ArrayList<Vector3f> axes = target.getNormals();
		axes.addAll(actor.getNormals());
		
		ArrayList<Vector3f> polygon1Vertices = target.getVertices();
		ArrayList<Vector3f> polygon2Vertices = actor.getVertices();
		
		//Iterate over all axes, check if projections intersect, take early out if they don't
		for (Vector3f axis: axes) {
			
			//Project each Polygon onto the axis
			//First project position onto the axis
			
			float center = targetPos.dot(axis);
			//Iterate over all vertices of the polygon, project onto axis, find maximum and minimum
			float min, max;
			//Set min and max to projection of first point onto axis, will definitely be within projection
			max = polygon1Vertices.get(0).dot(axis);
			min = max;
			
			for(int i=1; i<target.getVertexCount(); i++) {
				float proj = polygon1Vertices.get(i).dot(axis);
				if(proj > max) {
					max = proj;
				}else if(proj < min) {
					min = proj;
				}
			}
			
			AxisProjection polygon1Projection = new CollisionUtil().new AxisProjection(min+center, max+center);
			
			//Repeat for second polygon
			center = actorPos.dot(axis);
			max = polygon2Vertices.get(0).dot(axis);
			min = max;
			
			for(int i=1; i<actor.getVertexCount(); i++) {
				float proj = polygon2Vertices.get(i).dot(axis);
				if(proj > max) {
					max = proj;
				}else if(proj < min) {
					min = proj;
				}
			}
			
			AxisProjection polygon2Projection = new CollisionUtil().new AxisProjection(min+center, max+center);
			//If projections overlap, take early out
			if(!polygon1Projection.overlaps(polygon2Projection)){
				return false;
			}
		}
		return true;
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
