package dungeonCrawler.Rendering;

import org.joml.Vector3f;


public class Camera {
	
	
	private Vector3f pos;
	private Vector3f rot;
	private boolean updated;
	private final float ROT_Z;
	private final float ROT_X;
	
	public Camera() {
		ROT_Z = -45f;
		ROT_X = -60f;
		pos = new Vector3f();
		rot = new Vector3f();
		updated = false;
	}
	
	public void setPosition(Vector3f camPos) {
		pos.set(camPos);
		updated = true;
	}
	
	public void setOrientation(float x, float y, float z) {
		rot.set(x, y, z);
		updated = true;
		
	}
	public void resetOrientation() {
		rot.set(0, 0, 0);
		updated = true;
	}

	public void incrementPosition(float x, float y, float z) {
		pos.x += x*pos.z*0.1f;
		pos.y += y*pos.z*0.1f;
		pos.z += 0.05f * z;
		updated = true;
	}
	public void incrementRotation(float x, float y, float z) {
		rot.x += 1f * x;
		rot.y += 1f * y;
		rot.z += 1f * z;
		updated = true;
	}
	public boolean isUpdated() {
		return updated;
	}
	public void updateProcessed() {
		updated = false;
	}
	public Vector3f getPosition() {
		return pos;
	}
	
	public Vector3f getOrientation() {
		return rot;
	}
	
	public float getInitialRotationX() {
		return ROT_X;
	}
	
	public float getInitialRotationZ() {
		return ROT_Z;
	}
}
