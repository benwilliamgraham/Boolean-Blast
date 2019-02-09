import org.joml.Vector3f;

public class Bullet extends Particle {

	boolean lethal;
	
	Bullet(Vector3f position, Vector3f velocity, boolean lethal) {
		super(position, velocity);
		this.lethal = lethal;
	}

	@Override
	boolean update(Map map) {
		
		for(int i = 0; i < 2; i++) {
			position.add(velocity);
			
			if(map.checkCollision(position)) {
				if((int) (position.y + 0.5) != map.Y - 1) {
					map.shadeBlock(position, Map.lightShade);
					map.addParticles(position.sub(velocity), 16, 0.3f);
				}
				return true;
			}
			if( lethal && Math.abs(map.player.position.x - position.x) < Player.radius.x &&
				Math.abs(map.player.position.y - position.y) < Player.radius.y &&
				Math.abs(map.player.position.z - position.z) < Player.radius.z) {
				map.player.die();
			}
		}
		return false;
	}
	
}
