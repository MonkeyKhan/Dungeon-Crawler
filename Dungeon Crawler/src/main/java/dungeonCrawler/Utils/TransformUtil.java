package dungeonCrawler.Utils;

import org.joml.Matrix4f;
import org.joml.Vector3f;

import dungeonCrawler.Rendering.Camera;

public class TransformUtil {

	
	public static final Matrix4f getProjectionMatrix(float fov, float width, float height, float zNear, float zFar) {
		//Calculate and return a perspective projection frustum transformation matrix
		
		Matrix4f projectionMatrix = new Matrix4f();
		
		float aspectRatio = width/height;
		projectionMatrix.identity(); //reset matrix to identity
		projectionMatrix.perspective(fov, aspectRatio, zNear, zFar);
		
		
		return projectionMatrix;
	}
	public static final Matrix4f getProjectionMatrix(float aspectRatio) {
		
		//Return an identity matrix, effectively skipping perspective projection
		
		Matrix4f projectionMatrix = new Matrix4f();
		
		projectionMatrix.identity().scale(1,aspectRatio,-0.01f);
		return projectionMatrix;
	}
	
	public static final Matrix4f getCameraMatrix(Camera cam) {
		
		Matrix4f cameraMatrix = new Matrix4f();
		
		cameraMatrix.identity()
		.scale((float)(Math.exp(cam.getPosition().z*(-1f))))
		.translate(0f,0f,cam.getPosition().z*(-1f))
		.rotateX((float)Math.toRadians(cam.getInitialRotationX()))
		.rotateX((float)Math.toRadians(cam.getOrientation().x))
		.rotateY((float)Math.toRadians(cam.getOrientation().y))
		.rotateZ((float)Math.toRadians(cam.getOrientation().z))
		.rotateX((float)Math.toRadians(cam.getInitialRotationX()*-1f))
		.translate(cam.getPosition().x*(-1f),cam.getPosition().y*(-1f),0)
		.rotateX((float)Math.toRadians(cam.getInitialRotationX()))
		.rotateZ((float)Math.toRadians(cam.getInitialRotationZ()));	
		
		return cameraMatrix;
	}
	
	public static final Matrix4f getGridMatrix(Vector3f offset, Vector3f rotation, Vector3f scale) {
		
		Matrix4f worldMatrix = new Matrix4f();
		
		worldMatrix.identity().translate(offset).
		rotateX((float)Math.toRadians(rotation.x)).
		rotateY((float)Math.toRadians(rotation.y)).
		rotateZ((float)Math.toRadians(rotation.z)).
		scale(scale);
		return worldMatrix;
	}
	
}
