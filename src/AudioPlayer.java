import javafx.scene.media.AudioClip;

public class AudioPlayer {
	static AudioClip explosion = new AudioClip("assets/Explosion.wav");
	static AudioClip fall = new AudioClip("assets/Falling_Sound.wav");
	static AudioClip hit = new AudioClip("assets/Hit_Sound.wav");
	static AudioClip shot = new AudioClip("assets/SHOT.wav");
	static AudioClip step_1 = new AudioClip("assets/Step_1.wav");
	static AudioClip step_2 = new AudioClip("assets/Step_2.wav");
	

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