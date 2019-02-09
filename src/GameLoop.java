import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL40.*;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;

import org.joml.Vector3f;

import sun.audio.AudioPlayer;
import sun.audio.AudioStream;

public class GameLoop {

	public static void main(String[] args) {
		
		//connect to server
		//Client client = new Client();
		
		//create and initialize window
		Window window = new Window();
		window.init();
		
		//create shaders
		ShaderProgram shaderProgram = new ShaderProgram("src/shader/3d.vertex", "src/shader/3d.fragment");
		
		//create map
		Map map = new Map("assets/Map1.png");
		map.initializeItems(window);
		
		//run gameloop
		double lastTime = glfwGetTime();
				
		while (!glfwWindowShouldClose(window.id)) {
			//check events
			glfwPollEvents();
			
			//clear
			glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

			//update
			map.update(window);
			
			//draw
			map.render(map.activeCamera, shaderProgram);
			
			//show
			glfwSwapBuffers(window.id);
			
			while (glfwGetTime() < lastTime + 1.0/64.0) {
		        //wait
		    }
		    lastTime = glfwGetTime();
		}
		
		//exit safely
		glfwFreeCallbacks(window.id);
		glfwDestroyWindow(window.id);
		
		glfwTerminate();
		glfwSetErrorCallback(null).free();
	}
}