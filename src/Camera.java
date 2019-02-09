import org.joml.Matrix4f;
import org.joml.Vector3f;

public class Camera {
	Matrix4f projectionMatrix;
	Matrix4f VP;
	
	Camera(){
		projectionMatrix = new Matrix4f().perspective((float) Math.toRadians(70.0f), (float) Window.width / (float) Window.height, 0.001f, 1000f);
	}
	
	void updateVP(Vector3f position, Vector3f rotation) {
		VP = new Matrix4f(projectionMatrix)
			.rotate(rotation.x, new Vector3f(1f, 0f, 0f))	
			.rotate(rotation.y, new Vector3f(0f, 1f, 0f))
			.translate(new Vector3f(-position.x, -position.y, -position.z));
	}
}
