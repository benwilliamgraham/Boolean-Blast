import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.FloatBuffer;

import org.joml.Matrix4f;
import org.lwjgl.BufferUtils;

import static org.lwjgl.opengl.GL40.*;

public class ShaderProgram {
	int id;
	
	int u_MVP;
	private FloatBuffer matrixBuffer = BufferUtils.createFloatBuffer(16);
	
	ShaderProgram(String vertexShaderFilename, String fragmentShaderFilename){
		//create shaders
		int vertexShader = loadShader(vertexShaderFilename, GL_VERTEX_SHADER);
		int fragmentShader = loadShader(fragmentShaderFilename, GL_FRAGMENT_SHADER);
		
		//construct program
		id = glCreateProgram();
		glAttachShader(id, vertexShader);
		glAttachShader(id, fragmentShader);
		glLinkProgram(id);
		glValidateProgram(id);
		getUniformLocations();
	}
	
	void start() {
		glUseProgram(id);
	}

	void getUniformLocations() {
		u_MVP = glGetUniformLocation(id, "u_MVP");
	}
	
	void loadFloat(int location, float value) {
		glUniform1f(location, value);
	}
	
	void loadFloats(int location, float[] values) {
		glUniform1fv(location, values);
	}
	
	void loadMatrix(int location, Matrix4f matrix) {
		matrix.get(matrixBuffer);
		glUniformMatrix4fv(location, false, matrixBuffer);
	}
	
	int loadShader(String filename, int type) {
		StringBuilder shaderSource = new StringBuilder();
		try {
			BufferedReader reader = new BufferedReader(new FileReader(filename));
			String line;
			while((line = reader.readLine()) != null) {
				shaderSource.append(line).append("\n");
			}
			reader.close();
		}
		catch(IOException e) {
			System.err.println("Could not read file!");
			e.printStackTrace();
			System.exit(-1);
		}
		int shaderID = glCreateShader(type);
		glShaderSource(shaderID, shaderSource);
		glCompileShader(shaderID);
		if(glGetShaderi(shaderID, GL_COMPILE_STATUS) == GL_FALSE) {
			System.out.println(glGetShaderInfoLog(shaderID, 500));
			System.err.println("Could not compile shader.");
			System.exit(-1);
		}
		
		return shaderID;
	}
}
