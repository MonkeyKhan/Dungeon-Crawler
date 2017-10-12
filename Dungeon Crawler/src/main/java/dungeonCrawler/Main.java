package dungeonCrawler;

import dungeonCrawler.Engine.GameEngine;

public class Main {

	public static void main(String[] args) {
        try {
            boolean vSync = true;
            GameLogic gameLogic = new GameLogic();
            GameEngine engine = new GameEngine("GAME", 1366, 768, vSync, gameLogic);
            engine.start();
        } catch (Exception excp) {
            excp.printStackTrace();
            System.exit(-1);
        }
    }
	
}
