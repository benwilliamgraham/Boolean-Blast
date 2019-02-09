import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL40.*;

public class GameLoop {

	public static void main(String[] args) {
		//create map
		Map map = new Map();
		
		//create and initialize window
		Window window = new Window(map);
		
		//connect to server
		Client client = new Client(map);
		
		//create shaders
		ShaderProgram shaderProgram = new ShaderProgram("src/shader/3d.vertex", "src/shader/3d.fragment");
		
		//run gameloop
		double lastTime = glfwGetTime();
				
		while (!glfwWindowShouldClose(window.id)) {
//			System.out.print(Audio_Player.drip.isPlaying());
//			if (!Audio_Player.drip.isPlaying()) {
//				Audio_Player.drip.play();
//			}
			
			//check events
			glfwPollEvents();
			
			//clear
			glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

			//update
			map.update(window, client);
			
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