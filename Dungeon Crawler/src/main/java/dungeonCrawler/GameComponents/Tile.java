package dungeonCrawler.GameComponents;

import java.util.ArrayList;
import org.joml.Vector2f;
import org.joml.Vector3f;

import dungeonCrawler.GameComponents.CollisionBounds.Collidable;
import dungeonCrawler.UI.Ray;
import dungeonCrawler.UI.RayIntersection;
import dungeonCrawler.Utils.MeshUtil;

public abstract class Tile extends GameItem{

	private ArrayList<Tri> tris;
	
	public Tile(Vector3f position, Mesh mesh, Collidable bounds) {
		super(position, mesh, bounds);
		
		//Calculate collision triangles from mesh
		if(mesh != null) {
			try {
				tris = super.getMesh().makeTris();
			}catch(Exception e) {
				System.out.println(e.getMessage());
			}
		}else {
			tris = new ArrayList<Tri>(0);
		}
	}
	
	public Tile(Vector3f position, Mesh mesh) {
		this(position, mesh, null);
	}
	
	@Override
	public ArrayList<RayIntersection> processRay(Ray r) {
		
		ArrayList<RayIntersection> intersections = new ArrayList<RayIntersection>();
		Vector3f o = r.getOrigin();
		Vector3f dir = r.getDir();
		for (Tri tri: tris) {

			Vector3f p0 = tri.getVertices()[0].add(this.getPosition().x, this.getPosition().y, 0f);
			Vector3f p1 = tri.getVertices()[1].add(this.getPosition().x, this.getPosition().y, 0f);
			Vector3f p2 = tri.getVertices()[2].add(this.getPosition().x, this.getPosition().y, 0f);
			
			//calculate edge vectors. triangle lies within plane of these two vectors.
			Vector3f e1 = tri.getPlane().a();
			Vector3f e2 = tri.getPlane().b();
			Vector3f n = tri.getNormal();

			
			//calculate the dot product (scalar product) between ray direction vector and normal vector.
			//If dot == 0, normal vector and ray have an angle of 90°, ray is within plane of triangle! -> This is a no-hit!
			//If dot > 0, normal vector and ray point into same direction, ray hits plane from backside! -> This is a no-hit!
			//If dot < 0, normal vector and ray point in opposite direction, only scenario where a hit can occur!
			//TODO: This needs to be verified to work in every case. Might be dependant on tri-orientation and winding order.
			float dot = dir.dot(n);
			if(dot >= 0) {
				continue;
			}
			//calculate plane in the form of Ax + By + Cz = d
			
			float d = n.dot(p0);
			
			//The plane can be now be described by <n,p0> = d
			//The ray can be described by q = o + t * dir
			//Ray-plane intersect happens at q = p0, solving for t gives t = (d-<n,o>)/(<n,dir>), where the denominator has already been calculated in dot
			float t = (d-n.dot(o))/dot; //TODO: The numerator's sign might tell something about hitting the plane from behind, verify!
			
			//Calculate intersect of ray with plane
			Vector3f intersect = new Vector3f(
					o.x + t* dir.x,
					o.y + t* dir.y,
					o.z + t* dir.z);
			
			//Check if point of intersect lies within the triangle by converting it into barycentric coordinates
			//This will be done in 2D rather than 3D, so the triangle gets projected onto one of the basic planes.
			//For this, the dimension in which the triangle takes up the smallest range, will be ignored.
			
			float u1, u2, u3, u4; //Temporary variables for barycentric calculation
			float v1, v2, v3, v4;
			
			if((Math.abs(n.z)>Math.abs(n.y)) && (Math.abs(n.z)>Math.abs(n.x))){
				//Discard z-dimension, project onto xy-plane, should be default case
				u1 = p0.x - p2.x;
				u2 = p1.x - p2.x;
				u3 = intersect.x - p0.x;
				u4 = intersect.x - p2.x;
				
				v1 = p0.y - p2.y;
				v2 = p1.y - p2.y;
				v3 = intersect.y - p0.y;
				v4 = intersect.y - p2.y;
			}else if(Math.abs(n.x)>Math.abs(n.y)) {
				//Discard x-dimension, project onto yz-plane
				u1 = p0.y - p2.y;
				u2 = p1.y - p2.y;
				u3 = intersect.y - p0.y;
				u4 = intersect.y - p2.y;
				
				v1 = p0.z - p2.z;
				v2 = p1.z - p2.z;
				v3 = intersect.z - p0.z;
				v4 = intersect.z - p2.z;
				
			}else {
				//Discard x-dimension, project onto xz-plane
				u1 = p0.z - p2.z;
				u2 = p1.z - p2.z;
				u3 = intersect.z - p0.z;
				u4 = intersect.z - p2.z;
				
				v1 = p0.x - p2.x;
				v2 = p1.x - p2.x;
				v3 = intersect.x - p0.x;
				v4 = intersect.x - p2.x;
			}
			//Discard x-dimension, project onto yz-plane
			
			float denom = v1 * u2 - v2* u1;
			if (denom == 0f) {
				continue;//Bogus triangle, zero area?
			}
			denom = 1/denom;
			
			float b0, b1, b2;
			b0 = (v4*u2 - v2*u4) * denom;
			//System.out.println(b0);
			if( b0 < 0f) {
				//System.out.println("0");
				continue; //Intersect outside triangle
			}
			b1 = (v1*u3 - v3*u1) * denom;
			if(b1 < 0f) {
				//System.out.println("1");
				continue;//Intersect outside triangle
			}
			b2 = 1 - b0 - b1;
			if(b2 < 0f) {
				//System.out.println("2");
				continue;//Intersect outside triangle
			}
			intersections.add(new RayIntersection(this, r, t));
		}
		return intersections;
	}

	public String toString() {
		return String.format("Tile at %s", this.getPosition().toString());
	}

	public abstract boolean isPassable();

}
