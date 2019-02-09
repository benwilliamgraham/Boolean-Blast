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


	public void play_steps(float x_dist, float z_dist, float y_dist) {
		float min_hear_dist = 5; //I've no clue what the actual dist ought to be
		
	}
}
