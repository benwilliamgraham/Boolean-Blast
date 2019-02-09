import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_INT;
import static org.lwjgl.opengl.GL11.glDrawElements;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_ELEMENT_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glBufferData;
import static org.lwjgl.opengl.GL15.glGenBuffers;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL40.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;

import de.matthiasmann.twl.utils.PNGDecoder;
import de.matthiasmann.twl.utils.PNGDecoder.Format;

class Texture {

    int id;

    public Texture(String fileName){
    	try {
    	// Load Texture file
        PNGDecoder decoder = new PNGDecoder(new FileInputStream(fileName));

        // Load texture contents into a byte buffer
        ByteBuffer buf = ByteBuffer.allocateDirect(
                4 * decoder.getWidth() * decoder.getHeight());
        decoder.decode(buf, decoder.getWidth() * 4, Format.RGBA);
        buf.flip();

        // Create a new OpenGL texture 
        id = glGenTextures();
        // Bind the texture
        glBindTexture(GL_TEXTURE_2D, id);

        // Tell OpenGL how to unpack the RGBA bytes. Each component is 1 byte size
        glPixelStorei(GL_UNPACK_ALIGNMENT, 1);

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);

        // Upload the texture data
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, decoder.getWidth(), decoder.getHeight(), 0,
                GL_RGBA, GL_UNSIGNED_BYTE, buf);
        // Generate Mip Map
        glGenerateMipmap(GL_TEXTURE_2D);
    	}
    	catch(IOException e) {
    		e.printStackTrace();
    	}
    }
}

class GUIModel{
	static int vao;
	static int index_count;
	
	GUIModel(float[] model, int[] indices){
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
		glVertexAttribPointer(0, 3, GL_FLOAT, false, 20, 0);
		glVertexAttribPointer(1, 2, GL_FLOAT, false, 20, 12);
		
		glEnableVertexAttribArray(0);
		glEnableVertexAttribArray(1);
	}
}

public class GUI {
	static float[] vertices = {
		//0
		-0.5f, -0.5f, -1f,
		0f, 1f,
		//1
		-0.5f,  0.5f, -1f,
		0f, 0f,
		//2
		 0.5f, -0.5f, -1f,
		 1f, 1f,
		//3
		 0.5f,  0.5f, -1f,
		 1f,  0f,
	};
	static int[] indices = {
		0, 2, 3,
		0, 3, 1,
	};
		
	Vector3f position = new Vector3f(0, 0, 0);
	Vector3f scale = new Vector3f(1);
	static GUIModel model = new GUIModel(vertices, indices);
	Texture texture;
	GUI(String filename){
		texture = new Texture(filename);
	}
	
	void render(Camera camera, ShaderProgram shaderProgram){
		//setup
		shaderProgram.loadMatrix(shaderProgram.u_MVP, new Matrix4f(camera.projectionMatrix).translate(position).scale(scale));
		glBindTexture(GL_TEXTURE_2D, texture.id);
		
		//draw edges
		glBindVertexArray(GUIModel.vao);
		glDrawElements(GL_TRIANGLES, GUIModel.index_count, GL_UNSIGNED_INT, 0);
	}
}
