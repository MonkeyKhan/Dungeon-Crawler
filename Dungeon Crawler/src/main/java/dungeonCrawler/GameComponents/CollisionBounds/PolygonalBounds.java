package dungeonCrawler.GameComponents.CollisionBounds;

import java.util.ArrayList;

import org.joml.Vector3f;

import dungeonCrawler.Utils.CollisionUtil;

public class PolygonalBounds implements Collidable {

	/**CylindricBounds serve as cylinder shaped collision-boundaries, where the z-axis is the axis of the cylinder. 
	 * They have a height that measures their size in z-direction, starting at their z-position and a radius.
	 * 
	 * CylindricBounds can pass above or below other Collidable that have a defined height, like AABBs.
	 */
	
	private ArrayList<LineSegment> lines;
	private ArrayList<Vector3f> points;
	private ArrayList<Vector3f> normals;
	
	public PolygonalBounds(Vector3f pt1, Vector3f pt2, Vector3f pt3) {
		lines = new ArrayList<LineSegment>(4);
		lines.add(new LineSegment(new Vector3f(pt1), new Vector3f(pt2)));
		lines.add(new LineSegment(new Vector3f(pt2), new Vector3f(pt3)));
		lines.add(new LineSegment(new Vector3f(pt3), new Vector3f(pt1)));
		
		points = new ArrayList<Vector3f>(3);
		points.add(new Vector3f(pt1));
		points.add(new Vector3f(pt2));
		points.add(new Vector3f(pt3));
		
		normals = new ArrayList<Vector3f>(3);
		for(LineSegment l: lines) {
			normals.add(l.getNormal());
		}
		
	}
	
	public PolygonalBounds(Vector3f[] collision) {// throws Exception{
		//A polygon must contain at least 3 points
		/*if (collision.length < 3) {
			throw new Exception("Polygons cannot consist of fewer than 3 vertices!");
		}*/
		lines = new ArrayList<LineSegment>(collision.length);
		points = new ArrayList<Vector3f>(collision.length);
		for(int i=0; i<collision.length; i++) {
			LineSegment line = new LineSegment(new Vector3f(collision[i]), new Vector3f(collision[(i+1)%collision.length]));
			lines.add(line);
			points.add(new Vector3f(collision[i]));
		}
		
	}

	public int getVertexCount() {
		return points.size();
	}
	
	public ArrayList<LineSegment> getLines(){
		
		return new ArrayList<LineSegment>(lines);
	}
	
	public ArrayList<Vector3f> getNormals(){
		
		ArrayList<Vector3f> normOut = new ArrayList<Vector3f>(lines.size());
		for(LineSegment n: lines) {
			normOut.add(n.getNormal());
		}
		
		return normOut;
	}
	
	
	public ArrayList<Vector3f> getVertices(){
		
		//create copies of all points
		ArrayList<Vector3f> ptsOut = new ArrayList<Vector3f>(points.size());
		for(Vector3f p: points) {
			ptsOut.add(new Vector3f(p));
		}
		
		return ptsOut;
	}
	
	@Override
	public Vector3f resolveCollision(Collidable target, Vector3f actorPos, Vector3f targetPos) {
		return target.visit(this, actorPos, targetPos);
	}
	
	@Override
	public Vector3f visit(Collidable actor, Vector3f actorPos, Vector3f targetPos) {
		return actor.accept(this, actorPos, targetPos);
	}
	
	@Override
	public Vector3f accept(CylindricBounds target, Vector3f actorPos, Vector3f targetPos) {
		return CollisionUtil.resolveCollision(this, target, actorPos, targetPos);
	}
	
	@Override
	public Vector3f accept(PolygonalBounds target, Vector3f actorPos, Vector3f targetPos) {
		return CollisionUtil.resolveCollision(this, target, actorPos, targetPos);
	}
	
	@Override
	
	public Vector3f accept(AABB target, Vector3f actorPos, Vector3f targetPos) {
		return CollisionUtil.resolveCollision(this, target, actorPos, targetPos);
	}
	
	public float getWidth() {
		//TODO: implement (some projection?..)
		return 0;
	}
	
	public class LineSegment{
		
		private Vector3f startPt, endPt, normal, dir;
		
		public LineSegment(Vector3f startPt, Vector3f endPt) {
			this.startPt = new Vector3f(startPt);
			this.endPt = new Vector3f(endPt);
			this.dir = new Vector3f(endPt.x-startPt.x, endPt.y-startPt.y, endPt.z-startPt.z);
			//Normal is defined so that it points to the outside of the polygon if the polygon's points are defined counterclockwise!
			//Normal direction is always 0 in z direction.
			this.normal = new Vector3f(dir.y, -1f*dir.x, 0);
			this.normal.normalize();
		}

		public Vector3f getNormal() {
			return new Vector3f(normal);
		}

		public Vector3f getDir() {
			return new Vector3f(dir);
		}

		public Vector3f getStart() {
			return new Vector3f(startPt);
		}

		public Vector3f getEnd() {
			return new Vector3f(endPt);
		}		
		
		
	}

}
