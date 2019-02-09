import static org.lwjgl.opengl.GL11.GL_LINES;
import static org.lwjgl.opengl.GL11.GL_POLYGON_OFFSET_FILL;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_INT;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glDrawElements;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glPolygonOffset;
import static org.lwjgl.opengl.GL30.glBindVertexArray;

import org.joml.Matrix4f;
import org.joml.Vector3f;

public class Player {
	Vector3f position;
	float yVelocity = 0;
	boolean shooting = false;
	Vector3f rotation = new Vector3f(0f, 0f, 0f);
	Camera camera;
	
	//do all constant things
	private static Vector3f radius = new Vector3f(0.5f, 1.5f, 0.5f);
	private static Vector3f[] faceMesh = {
			new Vector3f(-radius.x, -radius.y, -radius.z),
			new Vector3f(-radius.x, -radius.y,  radius.z),
			new Vector3f(-radius.x,  radius.y, -radius.z),
			new Vector3f(-radius.x,  radius.y,  radius.z),
			new Vector3f( radius.x, -radius.y, -radius.z),
			new Vector3f( radius.x, -radius.y,  radius.z),
			new Vector3f( radius.x,  radius.y, -radius.z),
			new Vector3f( radius.x,  radius.y,  radius.z),
			new Vector3f(-radius.x, 0, -radius.z),
			new Vector3f(-radius.x, 0,  radius.z),
			new Vector3f( radius.x, 0, -radius.z),
			new Vector3f( radius.x, 0,  radius.z),
	};
	
	private static float ps = 0;
	private static float[] vertices = {
		faceMesh[0].x, faceMesh[0].y, faceMesh[0].z, ps,
		faceMesh[1].x, faceMesh[1].y, faceMesh[1].z, ps,
		faceMesh[2].x, faceMesh[2].y, faceMesh[2].z, ps,
		faceMesh[3].x, faceMesh[3].y, faceMesh[3].z, ps,
		faceMesh[4].x, faceMesh[4].y, faceMesh[4].z, ps,
		faceMesh[5].x, faceMesh[5].y, faceMesh[5].z, ps,
		faceMesh[6].x, faceMesh[6].y, faceMesh[6].z, ps,
		faceMesh[7].x, faceMesh[7].y, faceMesh[7].z, ps,
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
	
	Player(Window window, Vector3f position){
		this.position = position;
		camera = new Camera(window);
	}
	
	boolean checkCollision(Map map, Vector3f delta) {
		for(Vector3f point : faceMesh) {
			if(map.checkCollision(new Vector3f(position).add(delta).add(point))) {
				return true;
			}
		}
		return false;
	}
	
	void update(Window window, Map map) {
		Vector3f input = new Vector3f(0, 0, 0);
		
		//key movement
		if(window.KEY_UP) {
			input.z += 1;
		}
		else if(window.KEY_DOWN) {
			input.z -= 1;
		}
		if(window.KEY_RIGHT) {
			input.x += 1;
		}
		else if(window.KEY_LEFT) {
			input.x -= 1;
		}
		if(window.KEY_SPACE) {
			input.y += 2;
		}
		
		
		//mouse movement
		rotation.y += window.MOUSE_DELTA_X * 0.007;
		rotation.x += window.MOUSE_DELTA_Y * 0.007;
		rotation.x = (float) Math.min(Math.max(rotation.x, -1.2), 1.2);
		window.MOUSE_DELTA_X = 0;
		window.MOUSE_DELTA_Y = 0;
		
		//update position
		float speed = 0.4f;
		if(window.KEY_SHIFT) {
			speed = .2f;
		}
		Vector3f delta = new Vector3f(
				(float) (input.z * Math.sin(rotation.y) + input.x * Math.cos(rotation.y)) * speed,
				yVelocity,
				(float) (- input.z * Math.cos(rotation.y) + input.x * Math.sin(rotation.y)) * speed
		);
		
		//check collisions
		if(checkCollision(map, new Vector3f(delta.x, 0, 0))) {
			if(!checkCollision(map, new Vector3f(delta.x, 1, 0))) {
				delta.y += 1.1;
			}else {
				delta.x = 0;
			}
		}
		if(checkCollision(map, new Vector3f(0, 0, delta.z))) { 
			if(!checkCollision(map, new Vector3f(0, 1, delta.z))) {
				delta.y += 1.1;
			}else {
				delta.z = 0;
			}
		}
		
		//handle jumping
		if(checkCollision(map, new Vector3f(0, delta.y, 0))) {
			if(input.y == 2 && yVelocity < 0) {
				yVelocity = 0.5f;
				yVelocity -= map.gravity;
			}
			else{
				yVelocity = 0;
			}
			delta.y = yVelocity;
		}
		else {
			yVelocity -= map.gravity;
		}
		
//		//handle x movement
//		if(checkCollision(map, new Vector3f(delta.x, 0, 0))) {
//			if(input.x == 1 || input.x == -1) {
//				if(input.x == 1 && xVelocity < .3f) {
//					xVelocity += map.acceleration;
//					}
//				if(input.x == -1 && xVelocity < .3f) {
//					xVelocity -= map.acceleration;
//					}
//			}
//			else {
//				xVelocity = 20;
//			}
//			if(xVelocity > 0) {
//				xVelocity -= map.friction;
//			}
//			if(xVelocity < 0) {
//				xVelocity += map.friction;
//			}
//			delta.x = xVelocity;
//		}
			
		
			

		position.add(delta);
		
		//shooting
		if(window.MOUSE_LCLICK) {
			if(shooting == false) {
				map.particles.add(new Bullet(new Vector3f(position.x, position.y + 1.4f, position.z), new Vector3f(
					(float) (Math.sin(rotation.y) * Math.cos(rotation.x)), 
					(float) -Math.sin(rotation.x), 
					(float) (-Math.cos(rotation.y) * Math.cos(rotation.x))
				)));
				shooting = true;
			}
		} else {
			shooting = false;
		}
		
		//update camera
		camera.updateVP(new Vector3f(position.x, position.y + 1.4f, position.z), rotation);
	}
	
	void render(Camera camera, ShaderProgram shaderProgram){
		//setup
		shaderProgram.loadMatrix(shaderProgram.u_MVP, new Matrix4f(camera.VP).translate(position));
		
		//draw edges
		glBindVertexArray(model.vao);
		glDrawElements(GL_TRIANGLES, model.index_count, GL_UNSIGNED_INT, 0);
	}
}
