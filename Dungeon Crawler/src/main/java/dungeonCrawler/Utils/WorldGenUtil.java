package dungeonCrawler.Utils;

import org.joml.Vector2f;
import org.joml.Vector2i;
import org.joml.Vector3f;

import dungeonCrawler.GameComponents.*;

public class WorldGenUtil {

	private WorldGenUtil() {
		//Static class, constructor is private
	}
	
	
	public static void gen(World w) {
		
		int[][] tileArr = {
			{0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
			{0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
			{0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
			{0, 0, 3, 9, 9, 9, 9, 4, 0, 0},
			{0, 0, 9, 9, 9, 9, 9, 9, 0, 0},
			{0, 0, 9, 9, 0, 0, 9, 9, 0, 0},
			{0, 0, 2, 9, 0, 0, 9, 1, 0, 0},
			{0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
			{0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
			{0, 0, 0, 0, 0, 0, 0, 0, 0, 0}};
		
		createTilesFromArray(w, tileArr);

	}
	
	private static void createTilesFromArray(World w, int[][] arr) {
		for(int i=0; i<arr.length; i++) {
			for(int j=0; j<arr[0].length; j++) {
				if(arr[i][j] < 5) {
					w.addTile(new Terrain(new Vector3f(i,j,0), arr[i][j]));
				}
			}
		}
	}
	
	/*
	public static void createRandomTile(World w) {
		w.addTile(new Terrain(new Vector3f((int)(((Math.random()*2)-1)*50),(int)(((Math.random()*2)-1)*50),0)));
	}*/
	
}
