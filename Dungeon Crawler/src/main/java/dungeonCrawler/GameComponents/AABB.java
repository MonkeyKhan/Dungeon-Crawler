package dungeonCrawler.GameComponents;

import java.util.ArrayList;

import org.joml.Vector2f;
import org.joml.Vector3f;

import dungeonCrawler.UI.Ray;
import dungeonCrawler.UI.RayIntersection;
import dungeonCrawler.Utils.MeshUtil;

public class AABB extends GameItem{
	
	private final Vector3f pMin;
	private final Vector3f pMax;
	
	public AABB(Vector3f pos, int size, float lowest, float highest) {	//Explicit constructor for when points are already known
		super(pos, MeshUtil.makeBoundingBox(size, lowest, highest));
		this.pMin = new Vector3f(pos.x, pos.y, lowest);
		this.pMax = new Vector3f(pos.x+size, pos.y+size, highest);
	}
	
	public Vector3f getMin() {
		return pMin;
	}
	
	public Vector3f getMax() {
		return pMax;
	}
	
	public ArrayList<RayIntersection> processRay(Ray ray) {
		//Check if ray and AABB intersect, return a ArrayList containing one RayIntersection if intersection is detected or a ArrayList containing nothing if no intersect is detected
		
		ArrayList<RayIntersection> intersect = new ArrayList<RayIntersection>();

		Vector3f o = ray.getOrigin();
		Vector3f dir = ray.getDir();
				
		Vector3f min = this.getMin();
		Vector3f max = this.getMax();

		boolean inside = true; //Check if ray's origin lies in the AABB - shouldn't be possible, keep for now
		
		
		//Check for trivial non-intersects first, i.e. origin being of lower x than AABB's minimum and dir's x being negative
		
		float xt;	
		if(o.x < min.x) {
			if (dir.x < 0) {
				return intersect;	//early out with empty list
			}
			xt = (min.x - o.x) / dir.x;	
			inside = false;
		}else if(o.x > max.x) {
			
			if (dir.x > 0) {
				return intersect; //early out with empty list
			}
			xt = (max.x - o.x) / dir.x;
			inside = false;
		}else {
			xt = -1f;
		}
		
		float yt;	
		if(o.y < min.y) {
			if (dir.y < 0) {
				return intersect; //early out with empty list
			}
			yt = (min.y - o.y) / dir.y;	
			inside = false;
		}else if(o.y > max.y) {
			
			if (dir.y > 0) {
				return intersect; //early out with empty list
			}
			yt = (max.y - o.y) / dir.y;
			inside = false;
		}else {
			yt = -1f;
		}
		
		float zt;	
		if(o.z < min.y) {
			if (dir.z < 0) {
				return intersect; //early out with empty list
			}
			zt = (min.z - o.z) / dir.z;	
			inside = false;
		}else if(o.z > max.z) {
			
			if (dir.z > 0) {
				return intersect; //early out with empty list
			}
			zt = (max.z - o.z) / dir.z;
			inside = false;
		}else {
			zt = -1f;
		}
		
		if(inside) {
			intersect.add(new RayIntersection(this, ray, 0f));	//ray begins inside AABB, intersect distance is set to 0
			return intersect;	
		}
		
		//At this point all trivial non-intersects are checked for. 
		//Find farthest plane from origin - this is the plane of intersection
		
		char plane = 'x';
		float t = xt;
		if(yt > t) {
			plane = 'y';
			t = yt;
		}
		if(zt > t) {
			plane = 'z';
			t = zt;
		}
		
		//At this point, t holds the final value for intersect with the correct plane. Now exclude all cases where the intersect happens outside the AABB
		
		float x, y, z; //coordinates of point of intersection on a given plane
		switch (plane) {
		case 'x':	//Intersects with yz-plane
			y = o.y + t * dir.y;
			//If either y or z are outside the AABB at the point of intersection with the yz-plane, ray misses the AABB
			if (y < min.y || y > max.y) {
				return intersect; //early out with empty list
			}
			z = o.z + t * dir.z;
			if (z < min.z || z > max.z) {
				return intersect; //early out with empty list
			}	
			break;
		case 'y':	//Intersects with xz-plane
			x = o.x + t * dir.x;
			//If either x or z are outside the AABB at the point of intersection with the yz-plane, ray misses the AABB
			if (x < min.x || x > max.x) {
				return intersect; //early out with empty list
			}
			z = o.z + t * dir.z;
			if (z < min.z || z > max.z) {
				return intersect; //early out with empty list
			}	
			break;
		case 'z':	//Intersects with xy-plane
			x = o.x + t * dir.x;
			//If either x or z are outside the AABB at the point of intersection with the yz-plane, ray misses the AABB
			if (x < min.x || x > max.x) {
				return intersect; //early out with empty list
			}
			y = o.y + t * dir.y;
			if (y < min.y || y > max.y) {
				return intersect; //early out with empty list
			}	
			break;
		}
		// If there hasn't been a return at this point, the ray intersects with the AABB
		intersect.add(new RayIntersection(this, ray, t));
		return intersect;	
		
		
	}
	
}
