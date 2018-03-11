package dungeonCrawler.GameComponents.WorldBuilding;

import org.joml.Vector2f;

public interface Separable {

	
	public void updateSeparation(float t);
	
	public void setSeparationVelocity(Vector2f v);
}
