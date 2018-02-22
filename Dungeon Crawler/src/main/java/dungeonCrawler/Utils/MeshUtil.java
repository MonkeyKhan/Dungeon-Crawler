package dungeonCrawler.Utils;

import org.joml.Vector3f;

import dungeonCrawler.GameComponents.Mesh;

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
	public static Mesh makeTile(int type, float r, float g, float b) {
		
		float[] positions, colors;
		int[] indices;
		
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
		        	
		}
        
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
	public static Mesh makeBoundingBox(int size, float down, float up) {
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
}


