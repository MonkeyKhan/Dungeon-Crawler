package dungeonCrawler;

import static org.lwjgl.glfw.GLFW.*;
import java.util.ArrayList;
import java.util.Stack;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import dungeonCrawler.Commands.MoveCommand;
import dungeonCrawler.GameComponents.Player;
import dungeonCrawler.GameComponents.Terrain;
import dungeonCrawler.GameComponents.World;
import dungeonCrawler.Rendering.Camera;
import dungeonCrawler.Rendering.Renderer;
import dungeonCrawler.Rendering.Window;
import dungeonCrawler.UI.Ray;
import dungeonCrawler.UI.RayIntersection;
import dungeonCrawler.Utils.MeshUtil;
import dungeonCrawler.Utils.TransformUtil;
import dungeonCrawler.Utils.WorldGenUtil;

public class GameLogic {
	
	private final Renderer renderer;
	private final Camera cam;
	private Window window;
	
	private World world;
	private Player player;
	private UpdateProcessor uProcessor;
	private Ray r;
	public GameLogic() {
		cam = new Camera();
		renderer = new Renderer(cam);
	}
	
	public void init(Window window) throws Exception{
        renderer.init(window);
        this.window = window;
        player =  new Player(new Vector3f(0f, 0f, 0.05f));
        world = new World("Hello World",  player);
        uProcessor = new UpdateProcessor(world);
        WorldGenUtil.gen(world);
	}
	public void input () {
		
		if(window.isClicked()) { //All inputs should be handled like this!
			//Handle clicks;
	
			Vector2f click = window.getClick();
			r = castRay(click);
			ArrayList<RayIntersection> intersections = world.processRay(r);
			
			for(RayIntersection ri: intersections) {
				System.out.println(String.format("Ray intersecting with %s at %s.",
				ri.getItem().toString(), 
				ri.getPosition()));
				if(ri.getItem() instanceof Terrain) {
					Stack<Vector2f> path = new Stack<Vector2f>();
					path.add(new Vector2f(ri.getPosition().x, ri.getPosition().y));
					player.issueCommand(new MoveCommand(path));
					break;
				}
				
			}
			
		}
		
		//TODOImplement Callbacks for these!!! See handling above!
		if (window.isKeyPressed(GLFW_KEY_RIGHT)) {
			cam.incrementPosition(1, 0, 0);
		}
		if (window.isKeyPressed(GLFW_KEY_LEFT)) {
			cam.incrementPosition(-1, 0, 0);
		}
		if (window.isKeyPressed(GLFW_KEY_UP)) {
			cam.incrementPosition(0, 1, 0);
		}
		if (window.isKeyPressed(GLFW_KEY_DOWN)) {
			cam.incrementPosition(0, -1, 0);
		}
		if (window.isKeyPressed(GLFW_KEY_LEFT_SHIFT)) {
			cam.incrementPosition(0, 0, 1);
		}
		if (window.isKeyPressed(GLFW_KEY_LEFT_CONTROL)) {
			cam.incrementPosition(0, 0, -1);
		}
		if (window.isKeyPressed(GLFW_KEY_W)) {
			cam.incrementRotation(1, 0, 0);
		}
		if (window.isKeyPressed(GLFW_KEY_S)) {
			cam.incrementRotation(-1, 0, 0);
		}
		if (window.isKeyPressed(GLFW_KEY_A)) {
			cam.incrementRotation(0, 0, 1);
		}
		if (window.isKeyPressed(GLFW_KEY_D)) {
			cam.incrementRotation(0, 0, -1);
		}
		if (window.isKeyPressed(GLFW_KEY_Q)) {
			cam.incrementRotation(0, 1, 0);
		}
		if (window.isKeyPressed(GLFW_KEY_E)) {
			cam.incrementRotation(0, -1, 0);
		}
		if (window.isKeyPressed(GLFW_KEY_0)) {
			cam.resetOrientation();
		}
		if (window.isKeyPressed(GLFW_KEY_P)) {
			world.printDbg();
		}
		if (window.isKeyPressed(GLFW_KEY_U)) {
			
			issueRandomMoveCommand();
		}
		if (window.isKeyPressed(GLFW_KEY_I)) {
			WorldGenUtil.createRandomTile(world);
		}
		
	}
	
	//TEMPORARY
	private void issueRandomMoveCommand() {
		Stack<Vector2f> path = new Stack<Vector2f>();
		
		for (int i=0; i<3; i++) {
			path.add(new Vector2f(
					(float)(Math.random()*5),
					(float)(Math.random()*5)));
		}
		player.issueCommand(new MoveCommand(path));
	}
	
	public void update(float interval) {
		for (Update u: world.update(interval)) {
			uProcessor.process(u);
		}
	}
	public void render() {
		renderer.setUp(window);
		world.render();
		if (r!= null) {
			MeshUtil.makeLine(
					r.getOrigin(), 
					new Vector3f(
							r.getOrigin().x+r.getDir().x*Float.MAX_VALUE, 
							r.getOrigin().y+r.getDir().y*Float.MAX_VALUE, 
							r.getOrigin().z+r.getDir().z*Float.MAX_VALUE)).render();
		}
		renderer.unbind();
		//renderer.render();
	}
	public void cleanUp() {
		renderer.cleanUp();
	}
	
	
	//Input handling methods
	
	private Ray castRay(Vector2f click) {
		//Translate a window-coordinate click into a ray that extends in the camera's direction, 
		//intersecting everything that gets projected to where the click happened.
		
		//click is the position of the click in window coordinates, calculate corresponding NDC
		
		float x = (float)click.x/(window.getWidth()/2f); 		//normalize x range from [0, width] to [0, 2]
		x -= 1f;												//shift x-range to [-1, 1]
		float y = (float)click.y/(window.getHeight()/2f);		//normalize y range from [0, height] to [0, 2]
		y -= 1f;												//shift y-range to [-1, 1]
		y *= -1f;												//invert y-direction
		
		//x and y hold the NDC, create to positions pos1 and pos2 with z=-1 and z=1 respectively
		
		Vector4f pos1 = new Vector4f(x, y, -1f, 1);				
		Vector4f pos2 = new Vector4f(x, y, 1f, 1);
		
		//Undo projection Matrix; we use an isometric projection, which only scales
		
		Matrix4f projectionMatrix = TransformUtil.getProjectionMatrix((float)window.getWidth()/(float)window.getHeight()).invertAffine();
		projectionMatrix.transform(pos1);
		projectionMatrix.transform(pos2);
		
		//Undo camera Matrix
		
		Matrix4f cameraMatrix = TransformUtil.getCameraMatrix(cam).invertAffine();
		cameraMatrix.transform(pos1);
		cameraMatrix.transform(pos2);
		
		//Positions are in world coordinates now, create a Ray from those and return it
		
		Ray ray = new Ray(new Vector3f(pos1.x, pos1.y, pos1.z), new Vector3f(pos2.x, pos2.y, pos2.z));

		return ray;
	}
}
