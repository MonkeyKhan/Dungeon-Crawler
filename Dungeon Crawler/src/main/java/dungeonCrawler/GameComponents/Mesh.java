package dungeonCrawler.GameComponents;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.system.MemoryUtil;

import dungeonCrawler.Rendering.ShaderProgram;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

public class Mesh {
	
	
	
	private final float[] positions;
	private final float[] colors;
	private final int[] indices;
	
	private final int vaoId;
	private final int colorVboId;
	private final int indicesVboId;
	private final int posVboId;
	private final int vertexCount;

	
	
	private boolean asWire;
	
	public Mesh(float[] positions, float[] colors, int[] indices, boolean asWire) {
		
		this.positions = positions;
		this.colors = colors;
		this.indices = indices;
		
		FloatBuffer posBuffer = null;
		FloatBuffer colorBuffer = null;
		IntBuffer indicesBuffer = null;
		this.asWire = asWire;
		
		try {
			vertexCount = indices.length;
			vaoId = glGenVertexArrays();
			glBindVertexArray(vaoId);
			
			posVboId = glGenBuffers();
			posBuffer = MemoryUtil.memAllocFloat(positions.length);
			posBuffer.put(positions).flip();
			glBindBuffer(GL_ARRAY_BUFFER, posVboId);
			glBufferData(GL_ARRAY_BUFFER, posBuffer, GL_STATIC_DRAW);
			glVertexAttribPointer(0,3,GL_FLOAT,false,0,0);
			
			colorVboId = glGenBuffers();
			colorBuffer = MemoryUtil.memAllocFloat(colors.length);
			colorBuffer.put(colors).flip();
			glBindBuffer(GL_ARRAY_BUFFER, colorVboId);
			glBufferData(GL_ARRAY_BUFFER, colorBuffer, GL_STATIC_DRAW);
			glVertexAttribPointer(1,3,GL_FLOAT,false,0,0);
			
			indicesVboId = glGenBuffers();
			indicesBuffer = MemoryUtil.memAllocInt(vertexCount);
			indicesBuffer.put(indices).flip();
			glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, indicesVboId);
			glBufferData(GL_ELEMENT_ARRAY_BUFFER, indicesBuffer,GL_STATIC_DRAW);
			
			glBindBuffer(GL_ARRAY_BUFFER, 0);
            glBindVertexArray(0);
			
			
		}finally {
			if (posBuffer != null) {
                MemoryUtil.memFree(posBuffer);
            }
            if (colorBuffer != null) {
                MemoryUtil.memFree(colorBuffer);
            }
            if (indicesBuffer != null) {
                MemoryUtil.memFree(indicesBuffer);
            }
		}
	}
	public int getVaoId() {
	        return vaoId;
	}

    public int getVertexCount() {
        return vertexCount;
    }

    public void render(Vector3f pos) {
    	
    	ShaderProgram shaderProgram = ShaderProgram.getInstance();
    	Matrix4f worldMatrix = new Matrix4f().identity().translate(new Vector3f(pos.x, pos.y, pos.z));
    	shaderProgram.setUniform("worldMatrix", worldMatrix);
        
    	// Draw the mesh
        glBindVertexArray(getVaoId());
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);
               
        if(asWire) {
        	glDrawElements(GL_LINES, getVertexCount(), GL_UNSIGNED_INT, 0);
        }else {
        	glDrawElements(GL_TRIANGLES, getVertexCount(), GL_UNSIGNED_INT, 0);
        }
        // Restore state
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glBindVertexArray(0);
    }
    
    public void render() {
    	this.render(new Vector3f(0f, 0f, 0f));
    }
    
    public ArrayList<Tri> makeTris() throws Exception{
    	ArrayList<Tri> tris = new ArrayList<Tri>();
    	if(asWire) {
    		throw new Exception("Unsupported mesh");
    	}else {
    		for(int i=0; i < indices.length; i=i+3) {
    			int v0 = indices[i];
    			int v1 = indices[i+1];
    			int v2 = indices[i+2];
    			
    			tris.add(new Tri(
    					new Vector3f(
    							positions[(v0*3)],
    							positions[(v0*3)+1],
    							positions[(v0*3)+2]),
    					new Vector3f(
    							positions[(v1*3)],
    							positions[(v1*3)+1],
    							positions[(v1*3)+2]),
    					new Vector3f(
    							positions[(v2*3)],
    							positions[(v2*3)+1],
    							positions[(v2*3)+2])));    							
    		}
    	}
    	
    	return tris;
    }

    public void cleanUp() {
        glDisableVertexAttribArray(0);

        // Delete the VBOs
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glDeleteBuffers(posVboId);
        glDeleteBuffers(colorVboId);
        glDeleteBuffers(indicesVboId);

        // Delete the VAO
        glBindVertexArray(0);
        glDeleteVertexArrays(vaoId);
    }
}
