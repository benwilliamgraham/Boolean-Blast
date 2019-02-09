import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL40.*;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import sun.audio.AudioData;
import sun.audio.AudioPlayer;
import sun.audio.AudioStream;
import sun.audio.ContinuousAudioDataStream;



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
		ShaderProgram GUIProgram = new ShaderProgram("src/shader/2d.vertex", "src/shader/2d.fragment");
		
		//run gameloop
		double lastTime = glfwGetTime();
		
		sunAudioPatcher.init();
		
		InputStream music = null;
		AudioStream musicS = null;
		AudioData musicD = null;
		ContinuousAudioDataStream audiostream;
		try {
			music = new FileInputStream("music assets/Battle Theme Complete Loop.wav");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		try {
			musicS = new AudioStream(music);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		
//		try {
//			musicD = new AudioStream(music).getData();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		audiostream = new ContinuousAudioDataStream(musicD);
	
		AudioPlayer.player.start(musicS);
				
		while (!glfwWindowShouldClose(window.id)) {
			//AudioPlayer.hit.play();
			
			//check events
			glfwPollEvents();
			
			//clear
			glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

			//update
			map.update(window, client);
			
			//draw
			map.render(map.activeCamera, shaderProgram, GUIProgram);
			
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