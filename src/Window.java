import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL40.*;
import static org.lwjgl.system.MemoryUtil.*;

public class Window {
	long id;
	
	static int width = 1800, height = 900;
	
	boolean KEY_UP = false, KEY_DOWN = false, KEY_LEFT = false, KEY_RIGHT = false, 
			KEY_SPACE = false, KEY_SHIFT = false, 
			KEY_R = false,
			MOUSE_LCLICK = false, MOUSE_RCLICK = false,
			MOUSE_LOCKED = false;
	double MOUSE_X = 0, MOUSE_Y = 0, MOUSE_DELTA_X = 0, MOUSE_DELTA_Y = 0;
	
	Window(Map map) {
		//will print any errors that occur
		GLFWErrorCallback.createPrint(System.err).set();
		
		//initialize glfw
		if(!glfwInit())
			throw new IllegalStateException("Unable to initialize GLFW");
		
		//configure glfw window
		glfwDefaultWindowHints();
		glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
		glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 0);
		glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);
		
		//create the window
		id = glfwCreateWindow(width, height, "Boolean Blast!", NULL, NULL);
		if(id == NULL) throw new RuntimeException("Failed to create the GLFW window");
		
		//setup key callbacks
		glfwSetKeyCallback(id, (window, key, scancode, action, mods) -> {
			//TEMP exit
			if(key == GLFW_KEY_ESCAPE) glfwSetWindowShouldClose(id, true);
			
			//move
			if(key == GLFW_KEY_P && action == GLFW_PRESS) {
				if(MOUSE_LOCKED) glfwSetInputMode(id, GLFW_CURSOR, GLFW_CURSOR_NORMAL);
				else glfwSetInputMode(id, GLFW_CURSOR, GLFW_CURSOR_DISABLED);
				MOUSE_LOCKED = !MOUSE_LOCKED;
			}
			
			//up
			if(key == GLFW_KEY_W && action == GLFW_PRESS) KEY_UP = true;
			else if(key == GLFW_KEY_W && action == GLFW_RELEASE) KEY_UP = false;
			
			//down
			if(key == GLFW_KEY_S && action == GLFW_PRESS) KEY_DOWN = true;
			else if(key == GLFW_KEY_S && action == GLFW_RELEASE) KEY_DOWN = false;
			
			//left
			if(key == GLFW_KEY_A && action == GLFW_PRESS) KEY_LEFT = true;
			else if(key == GLFW_KEY_A && action == GLFW_RELEASE) KEY_LEFT = false;
			
			//right
			if(key == GLFW_KEY_D && action == GLFW_PRESS) KEY_RIGHT = true;
			else if(key == GLFW_KEY_D && action == GLFW_RELEASE) KEY_RIGHT = false;
			
			//r
			if(key == GLFW_KEY_R && action == GLFW_PRESS) KEY_R = true;
			else if(key == GLFW_KEY_R && action == GLFW_RELEASE) KEY_R = false;
			
			//space
			if(key == GLFW_KEY_SPACE && action == GLFW_PRESS) KEY_SPACE = true;
			else if(key == GLFW_KEY_SPACE && action == GLFW_RELEASE) KEY_SPACE = false;
			
			//shift
			if(key == GLFW_KEY_LEFT_SHIFT && action == GLFW_PRESS) KEY_SHIFT = true;
			else if(key == GLFW_KEY_LEFT_SHIFT && action == GLFW_RELEASE) KEY_SHIFT = false;
		});
		
		//setup mouse movement callbacks
		glfwSetCursorPosCallback(id, (window, xpos, ypos) -> {
			MOUSE_DELTA_X = xpos - MOUSE_X;
			MOUSE_DELTA_Y = ypos - MOUSE_Y;
			MOUSE_X = xpos;
			MOUSE_Y = ypos;
		});
		
		//setup mouse click callbacks
		glfwSetMouseButtonCallback(id, (window, button, action, mods) -> {
			//left click
			if (button == GLFW_MOUSE_BUTTON_LEFT && action == GLFW_PRESS) MOUSE_LCLICK = true;
			else if (button == GLFW_MOUSE_BUTTON_LEFT && action == GLFW_RELEASE) MOUSE_LCLICK = false;
			
			//right click
			if (button == GLFW_MOUSE_BUTTON_RIGHT && action == GLFW_PRESS) MOUSE_RCLICK = true;
			else if (button == GLFW_MOUSE_BUTTON_RIGHT && action == GLFW_RELEASE) MOUSE_RCLICK = false;
		});
		
		//set the OpenGL context
		glfwMakeContextCurrent(id);
		
		//show the window
		glfwShowWindow(id);
		
		//set the OpenGL properties
		GL.createCapabilities();
		glClearColor(Map.mediumShade, Map.mediumShade, Map.mediumShade, 1.0f);
		glEnable(GL_CULL_FACE);
		glEnable(GL_DEPTH_TEST);
		glEnable(GL_LINE_SMOOTH);
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		glLineWidth(1.1f);
	}
}
