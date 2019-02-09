import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_INT;
import static org.lwjgl.opengl.GL11.glDrawElements;
import static org.lwjgl.opengl.GL30.glBindVertexArray;

import java.util.Random;

import org.joml.Matrix4f;
import org.joml.Vector3f;

public class Particle {
	Vector3f position;
	Vector3f velocity;
	
	private static float ps = 1;
	private static Vector3f radius = new Vector3f(0.1f, 0.1f, 0.1f);
	private static float[] vertices = {
		-radius.x, -radius.y, -radius.z, ps,
		-radius.x, -radius.y,  radius.z, ps,
		-radius.x,  radius.y, -radius.z, ps,
		-radius.x,  radius.y,  radius.z, ps,
		 radius.x, -radius.y, -radius.z, ps,
		 radius.x, -radius.y,  radius.z, ps,
		 radius.x,  radius.y, -radius.z, ps,
		 radius.x,  radius.y,  radius.z, ps,
	};
	private static int[] indices = {
		//x-
		3, 2, 0,
		1, 3, 0,
		//x+
		4, 6, 7,
		4, 7, 5,
		//y-
		4, 1, 0,
		4, 5, 1,
		//y+
		3, 6, 2,
		3, 7, 6,
		//z-
		0, 6, 4,
		0, 2, 6,
		//z+
		5, 7, 1,
		7, 3, 1,
	};
	private static Model model = new Model(vertices, indices);
	
	Particle(Vector3f position, Vector3f velocity){
		this.position = position;
		this.velocity = velocity;
	}
	
	Particle(Vector3f position, float intensity, Random random){
		this.position = position;
		this.velocity = new Vector3f(
				2.f * (random.nextFloat() - 0.5f) * intensity,
				2.f * (random.nextFloat() - 0.5f) * intensity, 
				2.f * (random.nextFloat() - 0.5f) * intensity
		);
	}
	
	boolean update(Map map) {
		velocity.y -= map.gravity;
		
		if(map.checkCollision(position)) {
			if((int) (position.y + 0.5) != map.Y - 1) {
				map.shadeBlock(position, 1.0f);
			}
			return true;
		}
		
		position.add(velocity);
		
		return false;
	}
	
	void render(Camera camera, ShaderProgram shaderProgram){
		//setup
		shaderProgram.loadMatrix(shaderProgram.u_MVP, new Matrix4f(camera.VP).translate(position));
		
		//draw edges
		glBindVertexArray(model.vao);
		glDrawElements(GL_TRIANGLES, model.index_count, GL_UNSIGNED_INT, 0);
	}
}
