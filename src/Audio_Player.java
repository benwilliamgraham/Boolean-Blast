import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.AudioClip;

public class Audio_Player {
	
	AudioClip drip = new AudioClip("assets/Drip.wav");
	AudioClip explosion = new AudioClip("assets/Explosion.wav");
	AudioClip fall = new AudioClip("assets/Falling_Sound.wav");
	AudioClip hit = new AudioClip("assets/Hit_Sound.wav");
	AudioClip shot = new AudioClip("assets/SHOT.wav");
	AudioClip step_1 = new AudioClip("assets/Step_1.wav");
	AudioClip step_2 = new AudioClip("assets/Step_2.wav");
	
		


	public void play_steps(float x_dist, float z_dist, float y_dist, float theta) {
		float max_hear_dist = 20;
		float total_dist = (x_dist + z_dist + y_dist) / 2;
		
		if (total_dist <= max_hear_dist) {
		
			float amplitude = (max_hear_dist - total_dist) / max_hear_dist;
					
			step_1.setVolume(amplitude);
			step_2.setVolume(amplitude);
			
			step_1.play();
			step_2.play();
		}
	}
}

//if(theta == Math.PI/2) {
//step_1.setPan(-1);
//step_2.setPan(-1);
//}
//else if(theta==0) {
//step_1.setPan(1);
//step_2.setPan(1);
//}
//else {
//step_1.setPan((Math.PI/4 - theta) / (Math.PI/4));
//step_2.setPan((Math.PI/4 - theta) / (Math.PI/4));
//}