package dungeonCrawler.Rendering;

import static org.lwjgl.glfw.GLFW.*;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;

import dungeonCrawler.GameLogic;

import org.lwjgl.BufferUtils;

import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL11.GL_TRUE;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL20.glUniformMatrix4fv;
import static org.lwjgl.system.MemoryUtil.NULL;
import java.nio.DoubleBuffer;
import org.joml.Vector2f;


public class Window {
	
	private String title;
	private int width;
	private int height;
	private boolean vSync;
	private boolean resized;
	private long windowHandle;
	private boolean receivedClick;
	private boolean receivedDrag;
	private final Vector2f lastClick;
	private final Vector2f lastDrag;
	
	
	public Window(String title, int width, int height, boolean vSync) {
		this.title = title;
		this.width = width;
		this.height = height;
		this.vSync = vSync;
		this.resized = false;
		lastClick = new Vector2f();
		lastDrag = new Vector2f();
	}
	
	public void init(GameLogic logic) {
		// Setup an error callback. The default implementation
        // will print the error message in System.err.
        GLFWErrorCallback.createPrint(System.err).set();

        // Initialize GLFW. Most GLFW functions will not work before doing this.
        if (!glfwInit()) {
            throw new IllegalStateException("Unable to initialize GLFW");
        }

        glfwDefaultWindowHints(); // optional, the current window hints are already the default
        glfwWindowHint(GLFW_VISIBLE, GL_FALSE); // the window will stay hidden after creation
        glfwWindowHint(GLFW_RESIZABLE, GL_TRUE); // the window will be resizable
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 2);
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
        glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GL_TRUE);

        // Create the window
        windowHandle = glfwCreateWindow(width, height, title, NULL, NULL);
        if (windowHandle == NULL) {
            throw new RuntimeException("Failed to create the GLFW window");
        }

        // Setup resize callback
        glfwSetFramebufferSizeCallback(windowHandle, (window, width, height) -> {
            this.width = width;
            this.height = height;
            this.setResized(true);
        });
        

        // Setup a key callback. It will be called every time a key is pressed, repeated or released.
        glfwSetKeyCallback(windowHandle, (window, key, scancode, action, mods) -> {
            if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE) {
                glfwSetWindowShouldClose(window, true); // We will detect this in the rendering loop
            }
        }); 
        
        glfwSetCursorPosCallback(windowHandle, (window, xpos, ypos)-> {
        	//System.out.println((""+xpos+", "+ypos));
        });
        
        glfwSetMouseButtonCallback(windowHandle, (window, button, action, mods) -> click(window, button, action, mods));

        // Get the resolution of the primary monitor
        GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
        // Center our window
        glfwSetWindowPos(
                windowHandle,
                (vidmode.width() - width) / 2,
                (vidmode.height() - height) / 2
        );

        // Make the OpenGL context current
        glfwMakeContextCurrent(windowHandle);

        if (isvSync()) {
            // Enable v-sync
            glfwSwapInterval(1);
        }

        // Make the window visible
        glfwShowWindow(windowHandle);
        
        GL.createCapabilities();

        // Set initial clearColor as black
        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        
        glEnable(GL_DEPTH_TEST);
        glEnable(GL_CULL_FACE);
        
	}
	
	private void click(long w, int button, int action, int mods) {
		//For now, a click happens whenever mouse is clicked, including at the start of dragging motions!
		//TODO: Change this, click should only happen when button is released and no (substantial?) dragging has happened.
		if (button == GLFW_MOUSE_BUTTON_LEFT && action == GLFW_PRESS) {
			DoubleBuffer x = BufferUtils.createDoubleBuffer(1);
	        DoubleBuffer y = BufferUtils.createDoubleBuffer(1);
			glfwGetCursorPos(w, x, y);
			lastClick.x = (int)x.get();
			lastClick.y = (int)y.get();
			receivedClick = true;
    		x.clear();
    		y.clear();
			glfwSetCursorPosCallback(windowHandle, (window, xpos, ypos) -> drag(window, xpos, ypos));
		}else if (button == GLFW_MOUSE_BUTTON_LEFT && action == GLFW_RELEASE) {
			glfwSetCursorPosCallback(windowHandle, null);
    	}
		
	}
	
	private void drag(long w, double x, double y) {
		lastDrag.x = (int)x;
		lastDrag.y = (int)y;
	}
	
	public boolean isClicked() {
		return receivedClick;
	}
	public boolean isDragged() {
		return receivedDrag;
	}
	
	public Vector2f getClick() {
		receivedClick = false;
		return lastClick;
	}
	
	public Vector2f getDrag() {
		receivedDrag = false;
		return lastDrag;
	}
	
	public void setClearColor(float r, float g, float b, float alpha) {
        glClearColor(r, g, b, alpha);
    }
	
	public boolean isKeyPressed(int keyCode) {
        return glfwGetKey(windowHandle, keyCode) == GLFW_PRESS;
    }
	
	public boolean windowShouldClose() {
        return glfwWindowShouldClose(windowHandle);
    }
	
	public String getTitle() {
        return title;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
    
    public boolean isResized() {
        return resized;
    }

    public void setResized(boolean resized) {
        this.resized = resized;
    }

    public boolean isvSync() {
        return vSync;
    }

    public void setvSync(boolean vSync) {
        this.vSync = vSync;
    }

    public void update() {
        glfwSwapBuffers(windowHandle);
        glfwPollEvents();
    }
	

}
