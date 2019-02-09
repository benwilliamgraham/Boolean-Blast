import org.joml.Vector3f;

public class Bullet extends Particle {

	Bullet(Vector3f position, Vector3f velocity) {
		super(position, velocity);
	}

	@Override
	boolean update(Map map) {
		
		for(int i = 0; i < 2; i++) {
			position.add(velocity);
			
			if(map.checkCollision(position)) {
				if((int) (position.y + 0.5) != map.Y - 1) {
					map.shadeBlock(position, 1.0f);
					map.addParticles(position.sub(velocity), 16, 0.5f);
				}
				return true;
			}
		}
		return false;
	}
	
}
