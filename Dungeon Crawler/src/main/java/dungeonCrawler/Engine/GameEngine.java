package dungeonCrawler.Engine;

import dungeonCrawler.GameLogic;
import dungeonCrawler.Rendering.Window;

public class GameEngine implements Runnable {
	
	public static final int TARGET_FPS = 60;
	public static final int TARGET_UPS = 60;
	private final Thread gameLoopThread;
	private final Window window;
	private final Timer timer;
	private final GameLogic logic;
	
	public GameEngine(String title, int width, int height, boolean vSync, GameLogic logic) {
		gameLoopThread = new Thread(this, "GAME_LOOP_THREAD");
		this.logic = logic;
		window = new Window(title, width, height, vSync);
		timer = new Timer();
		
	}
	
	public void start() {
		gameLoopThread.start();
	}
	@Override
	public void run() {
		try {
			init();
			gameLoop();
		}catch (Exception excp) {
			excp.printStackTrace();
		}finally {
			cleanUp();
		}
	}
	
	protected void init() throws Exception {
		window.init(logic);
		timer.init();
		logic.init(window);
	}
	
	protected void gameLoop() {
		float elapsedTime;
		float accumulator = 0f;
		float interval = 1f / TARGET_UPS;
		
		boolean running = true;
		
		while (running&& !window.windowShouldClose()) {
			elapsedTime = timer.getElapsedTime();
			accumulator +=elapsedTime;
			
			input();
			
			while (accumulator > interval) {
				update(interval);
				accumulator-=interval;
			}
			
			render();
			
			if(!window.isvSync()) {
				sync();
			}
		}
	}
	
	protected void cleanUp() {
		logic.cleanUp();
	}
	
	private void sync() {
		float loopSlot = 1f / TARGET_FPS;
		double endTime = timer.getLastLoopTime() + loopSlot;
		while (timer.getTime() < endTime) {
			try {
				Thread.sleep(1);
			}catch(InterruptedException ie) {
            }
        }
    }
	protected void input() {
        logic.input();
    }
	protected void update(float interval) {
        logic.update(interval);
    }
	protected void render() {
        logic.render();
        window.update();
    }
}
