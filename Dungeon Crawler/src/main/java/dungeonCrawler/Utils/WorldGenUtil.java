package dungeonCrawler.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import org.joml.Vector2f;
import org.joml.Vector2i;
import org.joml.Vector3f;

import dungeonCrawler.GameComponents.*;
import dungeonCrawler.GameComponents.TerrainType;
import dungeonCrawler.GameComponents.WorldBuilding.Agent;
import dungeonCrawler.GameComponents.WorldBuilding.Boids;
import dungeonCrawler.GameComponents.WorldBuilding.Room;

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
		
		//createTilesFromArray(w, tileArr);
		
		newGen(w);

	}
	
	private static void createTilesFromArray(World w, int[][] arr) {
		for(int i=0; i<arr.length; i++) {
			for(int j=0; j<arr[0].length; j++) {
				w.addTile(new Terrain(new Vector3f(i,j,0), new TerrainType(arr[i][j])));
			}
		}
	}
	
	private static void newGen(World w) {
		
		
		int numCells = 50;
		int r = numCells/5;
		Room[] rooms = new Room[numCells];
		ArrayList<Room> mainRooms = new ArrayList<Room>(numCells);
		

		
		
		/**
		 * Phase 1: Dump rooms into world and let them spread out
		 */
		boolean p1 = false;
		
		for(int i=0; i<numCells;i++) {
			Random rand = new Random();
			int x = (int)Math.floor(rand.nextGaussian()*r)%r;
			int y = (int)Math.floor(rand.nextGaussian()*r)%r;
			int sizeX = (int)Math.abs(Math.floor((rand.nextGaussian()*3)))+1;
			int sizeY = (int)Math.abs(Math.floor((rand.nextGaussian()*3)))+1;
			rooms[i]=new Room(new Vector2i(x, y), new Vector2i(sizeX, sizeY));
			w.addRoom(rooms[i]);
		}
		Boids b = new Boids(new ArrayList<GameItem>(Arrays.asList(rooms)));

		//w.simulateBoids(b);
		while(!p1) {
			//TODO: This should probably be solved by a thread or something
			p1 = b.update(0.001f);
		}
		
		/**
		 * Phase 2: 
		 */
	
		//size is created from absolute of normal distribution with sigma = r/2, +3
		for(int i=0; i<rooms.length; i++) {
			if(rooms[i].getSize().x > 3 && rooms[i].getSize().y > 3) {
				//Room is larger than 1 sigma
				rooms[i].makeMainRoom(true);
				mainRooms.add(rooms[i]);
			}
		}
		
		
		
	}
	
	/*
	public static void createRandomTile(World w) {
		w.addTile(new Terrain(new Vector3f((int)(((Math.random()*2)-1)*50),(int)(((Math.random()*2)-1)*50),0)));
	}*/
	
}
