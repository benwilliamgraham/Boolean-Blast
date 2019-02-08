import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glBufferSubData;

import java.nio.FloatBuffer;
import java.util.ArrayList;

import org.lwjgl.BufferUtils;

public class Block {
	boolean collides = false;
	float shade = 0f;
	ArrayList<Integer> faceIndices;
	
	Block(Boolean collides){
		this.collides = collides;
		faceIndices = new ArrayList<Integer>();
	}
	
	void updateShade(float shade, Map map) {
		this.shade = shade;
		FloatBuffer shadeBuffer = BufferUtils.createFloatBuffer(1);
		shadeBuffer.put(shade);
		shadeBuffer.flip();
		glBindBuffer(GL_ARRAY_BUFFER, map.faces.modelVBO);
		for(int index : faceIndices) {
			glBufferSubData(GL_ARRAY_BUFFER, index, shadeBuffer);
		}
	}
}
