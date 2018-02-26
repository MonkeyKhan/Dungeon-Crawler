package dungeonCrawler.Commands;

import java.util.Stack;
import org.joml.Vector2f;

import dungeonCrawler.GameComponents.Mesh;
import dungeonCrawler.Utils.MeshUtil;

public class Path {

	
	private Stack<Vector2f> stack;
	private Mesh mesh;
	
	public Path() {
		stack = new Stack<Vector2f>();
		mesh = null;
	}
	
	public Path(Stack<Vector2f> stack) {
		this.stack = stack;
		mesh = MeshUtil.makePath(stack);
	}
	
	public Vector2f pop() {
		return stack.pop();
	}
	
	public Vector2f peek() {
		return stack.peek();
	}

	public void push(Vector2f node) {
		stack.push(node);
	}
	
	public int search(Vector2f node) {
		return stack.search(node);
	}
	
	
	public int size() {
		return stack.size();
	}
	public boolean empty() {
		return stack.empty();
	}
	
	
	public void render() {
		if (mesh!=null) {
			mesh.render();
		}
	}
	
	
}
