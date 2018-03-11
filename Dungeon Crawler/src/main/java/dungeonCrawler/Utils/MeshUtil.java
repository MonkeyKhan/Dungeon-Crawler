package dungeonCrawler.Utils;

import java.util.Stack;

import org.joml.Vector2f;
import org.joml.Vector3f;

import dungeonCrawler.GameComponents.Mesh;
import dungeonCrawler.GameComponents.TerrainType;

public final class MeshUtil {
	private MeshUtil() {
		
	}
	public static Mesh makePit(boolean north, boolean south, boolean east, boolean west, float height) {
		float[] positions, colors;
		int[] indices;
		float r = 0.2f;
		float g = 0.2f;
		float b = 0.2f;
		
		Mesh mesh = null;
		
		if(north && !west) { //Adjacant Tile in +x direction, but not in +y direction
			positions = new float[]{
	        		0f, 1f, 0f,
	        		0f, 1f, -height,
	        		1f, 1f, 0f,
	        		1f, 1f, -height
	        		
	        };
	        
	        colors = new float[]{
	        		r, g, b,
	        		0, 0, 0,
	        		r, g, b,
	        		0, 0, 0
	        };
	        
	        indices = new int[]{
	        		0, 1, 3,
	        		0, 3, 2
	        		
	        };
	        mesh = new Mesh(positions, colors, indices, false);
		}
		if(west && !north) { //Adjacant Tile in +x direction, but not in +y direction
			positions = new float[]{
	        		0f, 1f, 0f,
	        		0f, 1f, -height,
	        		0f, 0f, 0f,
	        		0f, 0f, -height
	        		
	        };
	        
	        colors = new float[]{
	        		r, g, b,
	        		0, 0, 0,
	        		r, g, b,
	        		0, 0, 0
	        };
	        
	        indices = new int[]{
	        		0, 2, 1,
	        		2, 3, 1
	        		
	        };
	        mesh = new Mesh(positions, colors, indices, false);
		}
		if(west && north) {
			positions = new float[]{
					0f, 1f, 0f,
	        		0f, 1f, -height,
	        		1f, 1f, 0f,
	        		1f, 1f, -height,
	        		0f, 0f, 0f,
	        		0f, 0f, -height
	        		
	        };
	        
	        colors = new float[]{
	        		r, g, b,
	        		0, 0, 0,
	        		r, g, b,
	        		0, 0, 0,
	        		r, g, b,
	        		0, 0, 0
	        };
	        
	        indices = new int[]{
	        		0, 1, 3,
	        		0, 3, 2,
	        		0, 4, 1,
	        		4, 5, 1
	        		
	        };
	        mesh = new Mesh(positions, colors, indices, false);
		}
			
			
		
		
		
		return mesh;
	}
	public static Mesh makeTile(TerrainType type, float r, float g, float b) {
		
		//Empty tiles don't contain any vertices and do not have a mesh
		if(type.getVertices()==null) {
			return null;
		}
		
		float[] positions = new float[type.getVertices().length*3];
		float[] colors = new float[type.getVertices().length*3];
		int[] indices = new int[(type.getVertices().length-2)*3];
		
		for (int i=0; i<type.getVertices().length; i++) {
			positions[3*i] = type.getVertices()[i].x;
			positions[3*i +1] = type.getVertices()[i].y;
			positions[3*i +2] = type.getVertices()[i].z;
			
			colors[3*i] = r;
			colors[3*i +1] = g;
			colors[3*i +2] = b;
		}		
		
		for(int i=0; i<type.getVertices().length-2; i++) {
			indices[3*i] = 0;
			indices[3*i +1] = i+1;
			indices[3*i +2] = i+2;
		}
		/*
		if (type == 0) {
			 positions = new float[]{
	        		0f, 0f, 0f,
	        		1f, 0f, 0f,
	        		0f, 1f, 0f,
	        		1f, 1f, 0f
	        };
	        
	         colors = new float[]{
	        		r, g, b,
	        		r, g, b,
	        		r, g, b,
	        		r, g, b
	        };
	        
	        indices = new int[]{
	        		0, 1, 2,
	        		3, 2, 1
	        		
	        };
		}else {
	         colors = new float[]{
		        		r, g, b,
		        		r, g, b,
		        		r, g, b,
		      };
		        
		      indices = new int[]{
		        		0, 1, 2,
		      };
		      switch (type){
		      
		      case 1: 
		    	  positions = new float[]{
			        		1f, 0f, 0f,
			        		1f, 1f, 0f,
			        		0f, 1f, 0f
		    	  };
		    	  break;
		      case 2:
		    	  positions = new float[]{
			        		0f, 0f, 0f,
			        		1f, 0f, 0f,
			        		1f, 1f, 0f
		    	  };
		    	  break;
		      case 3:
		    	  positions = new float[]{
			        		0f, 0f, 0f,
			        		1f, 0f, 0f,
			        		0f, 1f, 0f
		    	  };
		    	  break;
		      case 4:
		    	  positions = new float[]{
			        		0f, 0f, 0f,
			        		1f, 1f, 0f,
			        		0f, 1f, 0f
		    	  };
		    	  break;
		      default: 
		    	  positions = new float[]{
			        		0f, 0f, 0f,
			        		0f, 0f, 0f,
			        		0f, 0f, 0f
		    	  };
		      }
		        	
		}*/
        
        return new Mesh(positions, colors, indices, false);
	}
	public static Mesh makeCoords() {
		
		float[] positions, colors;
		int[] indices;
		
		positions = new float[]{
        		0f, 0f, 0f,
        		1f, 0f, 0f,
        		0f, 0f, 0f,
        		0f, 1f, 0f,
        		0f, 0f, 0f,
        		0f, 0f, 1f
        };
        
        colors = new float[]{
        		1, 0, 0, //X = red
        		1, 0, 0,
        		0, 1, 0, //Y = green
        		0, 1, 0,
        		0, 0, 1, //Z = blue
        		0, 0, 1
        };
        
        indices = new int[]{
        		0, 1, 
        		2, 3,
        		4, 5
        		
        };
        
        return new Mesh(positions, colors, indices, true);
	}
	public static Mesh makeAABB(int size, float down, float up) {
		float r = 0;
		float g = 0;
		float b = 1;
		
		float[] positions = new float[]{
        		0f, 0f, down,
        		size, 0f, down,
        		size, size, down,
        		0f, size, down,
        		0f, 0f, up,
        		size, 0f, up,
        		size, size, up,
        		0f, size, up,
        };
        
        float[] colors = new float[]{
        		r, g, b,
        		r, g, b,
        		r, g, b,
        		r, g, b,
        		r, g, b,
        		r, g, b,
        		r, g, b,
        		r, g, b
        };
        
        int[] indices = new int[]{
        		0, 1,
        		1, 2,
        		2, 3,
        		3, 0,
        		4, 5,
        		5, 6,
        		6, 7,
        		7, 4,
        		0, 4,
        		1, 5,
        		2, 6,
        		3, 7,        		
        };
        
        return new Mesh(positions, colors, indices, true);
	}
	public static Mesh makePlayer() {
		
		float r = 1;
		float g = 0;
		float b = 0;
		
		float[] positions = new float[]{
        		-0.2f, -0.2f, 0f,
        		0.2f, -0.2f, 0f,
        		-0.2f, 0.2f, 0f,
        		0.2f, 0.2f, 0f,
        		-0.2f, -0.2f, 0.7f,
        		0.2f, -0.2f, 0.7f,
        		-0.2f, 0.2f, 0.7f,
        		0.2f, 0.2f, 0.7f,
        };
        
        float[] colors = new float[]{
        		r, g, b,
        		r, g, b,
        		r, g, b,
        		r, g, b,
        		r, g, b,
        		r, g, b,
        		r, g, b,
        		r, g, b
        };
        
        int[] indices = new int[]{
        		0, 1, 2,
        		1, 3, 2,
        		0, 6, 4,
        		0, 2, 6,
        		2, 3, 6,
        		3, 7, 6,
        		7, 1, 5,
        		7, 3, 1,
        		5, 1, 0,
        		0, 4, 5,
        		4, 6, 7,
        		7, 5, 4
        		
        };
        
        return new Mesh(positions, colors, indices, false);
	}
	public static Mesh makePlayer(float radius, float height) {
		
		float r = 1;
		float g = 0;
		float b = 0;
		
		float[] positions = new float[]{
        		-radius, -radius, 0f,
        		radius, -radius, 0f,
        		-radius, radius, 0f,
        		radius, radius, 0f,
        		-radius, -radius, height,
        		radius, -radius, height,
        		-radius, radius, height,
        		radius, radius, height,
        };
        
        float[] colors = new float[]{
        		r, g, b,
        		r, g, b,
        		r, g, b,
        		r, g, b,
        		r, g, b,
        		r, g, b,
        		r, g, b,
        		r, g, b
        };
        
        int[] indices = new int[]{
        		0, 1, 2,
        		1, 3, 2,
        		0, 6, 4,
        		0, 2, 6,
        		2, 3, 6,
        		3, 7, 6,
        		7, 1, 5,
        		7, 3, 1,
        		5, 1, 0,
        		0, 4, 5,
        		4, 6, 7,
        		7, 5, 4
        		
        };
        
        return new Mesh(positions, colors, indices, false);
	}
	public static Mesh makeLine(Vector3f pos1, Vector3f pos2) {
		float r = 1f;//(float)Math.random();
		float g = 1f;//(float)Math.random();
		float b = 1f;//(float)Math.random();
		
		float[] positions = new float[]{
        		pos1.x, pos1.y, pos1.z,
        		pos2.x, pos2.y, pos2.z
        };
        
        float[] colors = new float[]{
        		r, g, b,
        		r, g, b,
        };
        
        int[] indices = new int[]{
        		0, 1
        		
        };
        
        return new Mesh(positions, colors, indices, true);
	}
	public static Mesh makeTriangle(Vector3f v1, Vector3f v2, Vector3f v3) {
		float[] positions = new float[]{
        		v1.x, v1.y, v1.z,
        		v2.x, v2.y, v2.z,
        		v3.x, v3.y, v3.z,
        };
        
        float[] colors = new float[]{
        		1f, 0f, 0f,
        		1f, 0f, 0f,
        		1f, 0f, 0f,
        		1f, 0f, 0f,
        		1f, 0f, 0f,
        		1f, 0f, 0f
        };
        
        int[] indices = new int[]{
        		0, 1,
        		1, 2,
        		2, 0
        		
        };
        
        return new Mesh(positions, colors, indices, true);
	}	
	public static Mesh makeRectangle(Vector2f size, float r, float g, float b) {
		float[] positions = new float[]{
        		0f, 0f, 0f,
        		size.x, 0f, 0f,
        		size.x, size.y, 0f,
        		0f, size.y, 0f
        };
        
        float[] colors = new float[]{
        		r, g, b,
        		r, g, b,
        		r, g, b,
        		r, g, b
        };
        
        int[] indices = new int[]{
        		0, 1,
        		1, 2,
        		2, 3,
        		3, 0
        		
        };
        
        return new Mesh(positions, colors, indices, true);		
	}
	public static Mesh makePath(Stack<Vector2f> path) {
		
		Stack<Vector2f> p =  (Stack<Vector2f>) path.clone();
		
		float[] positions = new float[3*p.size()];
		float[] colors = new float[3*p.size()];
		int[] indices;
		if(p.size()<=1) {
			indices = new int[2];
		}else {
			indices = new int[2*(p.size()-1)];
		}
		
		
		int maxInd = p.size();
		
		for(int i=0; i<maxInd ;i++) {
			Vector2f current = p.pop();
			
			positions[3*i] = current.x;
			positions[3*i +1] = current.y;
			positions[3*i +2] = 0.05f;
			
			colors[3*i] = 1f;
			colors[3*i +1] = 1f;
			colors[3*i +2] = 1f;
			
			if (i<(maxInd-1)) {
				indices[2*i] = i;
				indices[2*i +1] = i+1;
			}
		}
		
		
		return new Mesh(positions, colors, indices, true);
		
	}
}


