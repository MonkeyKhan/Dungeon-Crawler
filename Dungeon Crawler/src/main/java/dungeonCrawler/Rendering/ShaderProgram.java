package dungeonCrawler.Rendering;

import java.nio.FloatBuffer;
import java.util.HashMap;
import java.util.Map;
import org.joml.Matrix4f;
import org.lwjgl.system.MemoryStack;
import static org.lwjgl.opengl.GL20.*;

public class ShaderProgram {
	
	private static ShaderProgram uniqueInstance;
	
	private final int programId;
	private int vertexShaderId;
	private int fragmentShaderId;

	private final Map<String, Integer> uniforms;
	
	
	private ShaderProgram() throws Exception {
		//create a program, can only happen once per instance of ShaderProgram and is always called in the constructor
		programId = glCreateProgram();
		if (programId == 0) {
			throw new Exception("Could not create Shader!");
		}
		//create a Hashmap for mapping a uniform's name to its location
		uniforms = new HashMap<>();
	}
	
	public static ShaderProgram getInstance() {
		if(uniqueInstance == null) {
			try {
				uniqueInstance = new ShaderProgram();
			}catch(Exception e) {
				System.err.println(e.getMessage());
			}finally{
				
			};
		}
		return uniqueInstance;
	}
	public void createUniform(String uniformName) throws Exception{
		//Get location of a uniform from the program. There needs to be a Uniform with uniformName as its name defined inside the program.
		//A Uniform gets assigned a location once the program is linked, so this method may only be called after successful linking.
		int uniformLocation = glGetUniformLocation(programId, uniformName);
		if (uniformLocation < 0) { //glGetUniformLocation returns -1 for uniformNames that aren't defined inside the program
            throw new Exception("Could not find uniform:" + uniformName);
        }
		//Map the uniform's location to its name
		uniforms.put(uniformName, uniformLocation);
	}
	public void setUniform(String uniformName, Matrix4f value) {
		//create a MemoryStack in try-with-resources (closes MemoryStack if exception is thrown)
		try (MemoryStack stack = MemoryStack.stackPush()) {
            // Dump the matrix into a float buffer
            FloatBuffer fb = stack.mallocFloat(16);
            //store supplied matrix inside floatbuffer
            value.get(fb);
            //Finally, pass the value matrix to the uniform uniformName
            glUniformMatrix4fv(uniforms.get(uniformName), false, fb);
        }
	}
	
	//Dedicated methods for creation of specific shaders, all type-specific stuff happens in these
	public void createVertexShader(String shaderCode) throws Exception {
        vertexShaderId = createShader(shaderCode, GL_VERTEX_SHADER);
    }
	
    public void createFragmentShader(String shaderCode) throws Exception {
        fragmentShaderId = createShader(shaderCode, GL_FRAGMENT_SHADER);
    }
    
    protected int createShader(String shaderCode, int shaderType) throws Exception {
    	//Method works with all types of shaderTypes
        int shaderId = glCreateShader(shaderType);
        if (shaderId == 0) {
            throw new Exception("Error creating shader. Type: " + shaderType);
        }

        glShaderSource(shaderId, shaderCode);
        glCompileShader(shaderId);

        //Verify successful compilation of shader
        if (glGetShaderi(shaderId, GL_COMPILE_STATUS) == 0) {
            throw new Exception("Error compiling Shader code: " + glGetShaderInfoLog(shaderId, 1024));
        }
        
        //attach shader to program on GL-side
        glAttachShader(programId, shaderId);

        return shaderId;
    }
    public void link() throws Exception {
    	//Link program to GL, it can now be used. Shaders need to be compiled, created and attached before
        glLinkProgram(programId);
        if (glGetProgrami(programId, GL_LINK_STATUS) == 0) {
            throw new Exception("Error linking Shader code: " + glGetProgramInfoLog(programId, 1024));
        }
        
        //Once program is linked, detach shaders
        if (vertexShaderId != 0) {
            glDetachShader(programId, vertexShaderId);
        }
        if (fragmentShaderId != 0) {
            glDetachShader(programId, fragmentShaderId);
        }
        
        //Program validation for debugging purposes only
        glValidateProgram(programId);
        if (glGetProgrami(programId, GL_VALIDATE_STATUS) == 0) {
            System.err.println("Warning validating Shader code: " + glGetProgramInfoLog(programId, 1024));
        }
    }
    //For the shaders to be part of the rendering pipeline, the progrem they were attached to need to be used.
    public void bind() {
        glUseProgram(programId);
    }

    public void unbind() {
        glUseProgram(0);
    }
    
    //Remove everything on GL-side
    public void cleanup() {
        unbind();
        if (programId != 0) {
            glDeleteProgram(programId);
        }
    }
}
