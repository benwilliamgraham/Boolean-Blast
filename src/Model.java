import org.lwjgl.*;

import java.nio.*;

import static org.lwjgl.opengl.GL40.*;

public class Model {
	int vao;
	int index_count;
	int modelVBO;
	
	Model(float[] model, int[] indices){
		//set vao
		vao = glGenVertexArrays();
		glBindVertexArray(vao);
		index_count = indices.length;
		
		//push vertices to vbo
		modelVBO = glGenBuffers();
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
