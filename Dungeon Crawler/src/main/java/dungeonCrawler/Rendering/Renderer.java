package dungeonCrawler.Rendering;

import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glViewport;

import java.util.ArrayList;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import dungeonCrawler.GameComponents.Tri;
import dungeonCrawler.Utils.ResourceUtil;
import dungeonCrawler.Utils.TransformUtil;

public class Renderer {

	private ShaderProgram shaderProgram;
	private final Camera cam;
	private Matrix4f projCamMatrix;
	private Matrix4f worldGridMatrix;

	
	public Renderer(Camera cam) {
		this.cam = cam;
		this.cam.setPosition(new Vector3f(0f,0f, 1f));
	}
	
	public void init (Window window) throws Exception{
		//create shader
		try {
			shaderProgram = ShaderProgram.getInstance();
			shaderProgram.createVertexShader(ResourceUtil.loadResource("/vertex.vs"));
			shaderProgram.createFragmentShader(ResourceUtil.loadResource("/fragment.fs"));
			shaderProgram.link();
		}catch(Exception e) {
			System.err.println(e.getMessage());
		}
		
        // Create uniforms for matrices, MUST be defined in shader-source!
        shaderProgram.createUniform("projCamMatrix"); //Combines matrices that are constant for all objects in a render cycle
    	shaderProgram.createUniform("worldMatrix");
        window.setClearColor(0.0f, 0.0f, 0.0f, 0.0f);
		
	}
	
	public void clear() {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
    }
	public void setUp(Window window) {
        clear();

        if ( window.isResized() ) {
            glViewport(0, 0, window.getWidth(), window.getHeight());
            window.setResized(false);
        }

        shaderProgram.bind();
    	
        // Update camera Matrix if camera was modified
        if(camMatrixNeedsUpdate()){
        	updateProjCamMatrix(window);
        }

    }
	
	public void unbind() {
		shaderProgram.unbind();
	}
	
	private boolean camMatrixNeedsUpdate() {
		boolean update = cam.isUpdated();
		cam.updateProcessed();
		return update;		
	}
	
	private void updateProjCamMatrix(Window window) {
		projCamMatrix = TransformUtil.getProjectionMatrix((float)window.getWidth()/(float)window.getHeight()).mul(TransformUtil.getCameraMatrix(cam));
        shaderProgram.setUniform("projCamMatrix", projCamMatrix);
	}
	
	public Camera getCamera() {
		return cam;
	}

    public void cleanUp() {
        if (shaderProgram != null) {
            shaderProgram.cleanup();
        }
    }
}
