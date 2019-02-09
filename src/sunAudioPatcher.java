import sun.audio.*;
import java.util.ArrayList;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
public class sunAudioPatcher {
	
	static InputStream drip, explosion, drop, hit, shot, step1, step2;
	
	static AudioStream dripS, explosionS, dropS, hitS, shotS, step1S, step2S;
	
	public static void init() {
		try {
			drip = new FileInputStream("assets/Drip.mp3");
			explosion = new FileInputStream("assets/Explosion.mp3");
			drop = new FileInputStream("assets/Falling Sound.mp3");
			hit = new FileInputStream("assets/Hit Sound.mp3");
			shot = new FileInputStream("music assets/SHOT.wav");
			step1 = new FileInputStream("assets/Step 1.mp3");
			step2 = new FileInputStream("assets/Step 2.mp3");
			
//			dripS = new AudioStream(drip);
//			explosionS = new AudioStream(explosion);
//			dropS = new AudioStream(drop);
//			hitS = new AudioStream(hit);
			shotS = new AudioStream(shot);
//			step1S = new AudioStream(step1);
//			step2S = new AudioStream(step2);
		} catch (IOException e) {
			e.printStackTrace();
		}		
	}
	
	public static void playShot() {
		AudioPlayer.player.start(shotS);
		try {
			shotS = new AudioStream(shot);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	

}
