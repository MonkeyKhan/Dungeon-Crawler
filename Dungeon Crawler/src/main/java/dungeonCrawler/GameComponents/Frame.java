package dungeonCrawler.GameComponents;

import org.joml.Vector3f;

import dungeonCrawler.Utils.MeshUtil;

public class Frame extends GameItem {
	
	public Frame(Vector3f position) {
		super(position, MeshUtil.makeCoords());
	}
	
}
