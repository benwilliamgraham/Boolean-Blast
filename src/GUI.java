import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_ELEMENT_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glBufferData;
import static org.lwjgl.opengl.GL15.glGenBuffers;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;

public class GUI {
	static float[] vertices = {
		-0.5f, -0.5f, -0.5f,
		-0.5f, -0.5f,
		-0.5f, -0.5f, -0.5f,
		-0.5f, -0.5f,
		-0.5f, -0.5f, -0.5f,
		-0.5f, -0.5f,
		-0.5f, -0.5f, -0.5f,
		-0.5f, -0.5f,
	};
	static int[] indices = {
		3, 2, 0,
		1, 3, 0,
	};
	
	int vao;
	int index_count;
	
	GUI(float[] model, int[] indices){
		//set vao
		vao = glGenVertexArrays();
		glBindVertexArray(vao);
		index_count = indices.length;
		
		//push vertices to vbo
		int modelVBO = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER, modelVBO);
		FloatBuffer vertexBuffer = BufferUtils.createFloatBuffer(model.length);
		vertexBuffer.put(model);
		vertexBuffer.flip();
		glBufferData(GL_ARRAY_BUFFER, vertexBuffer, GL_STATIC_DRAW);
		
		//push indices
		int indexVBO = glGenBuffers();
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, indexVBO);
		IntBuffer indexBuffer = BufferUtils.createIntBuffer(indices.length);
		indexBuffer.put(indices);
		indexBuffer.flip();
		glBufferData(GL_ELEMENT_ARRAY_BUFFER, indexBuffer, GL_STATIC_DRAW);
		
		//establish attribute settings
		glVertexAttribPointer(0, 3, GL_FLOAT, false, 16, 0);
		glVertexAttribPointer(1, 1, GL_FLOAT, false, 16, 12);
		
		glEnableVertexAttribArray(0);
		glEnableVertexAttribArray(1);
	}
}
