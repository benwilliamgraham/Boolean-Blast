import org.joml.Vector3f;
import org.lwjgl.openal.AL10;

import javafx.scene.media.AudioClip;

public class AudioPlayer {
	
	static int drip = AudioMaster.loadSound("assets/Drip.wav");
	static int explosion = AudioMaster.loadSound("assets/Explosion.wav");
	static int fall = AudioMaster.loadSound("assets/Falling_Sound.wav");
	static int hit = AudioMaster.loadSound("assets/Hit_Sound.wav");
	static int shot = AudioMaster.loadSound("assets/SHOT.wav");
	static int step1 = AudioMaster.loadSound("assets/Step_1.wav");
	static int step2 = AudioMaster.loadSound("assets/Step_2.wav");
	
	SoundSource fromCharacterHit = new SoundSource();
	SoundSource fromCharShoot = new SoundSource();
	SoundSource fromCharacterExplode = new SoundSource();
	SoundSource fromCharacterFall = new SoundSource();
	static SoundSource fromCharacterWalk = new SoundSource();
	SoundSource fromEnemy = new SoundSource();
	SoundSource fromWorld = new SoundSource();
	
	public void dripSound(Vector3f worldPosition) {
		AL10.alSource3f(fromWorld.getId(), AL10.AL_POSITION, worldPosition.x, worldPosition.y, worldPosition.z);
		fromWorld.play(drip);
	}
	
	public void fall() {
		fromCharacterFall.play(fall);
	}
	
	public void getShot() {
		fromCharacterHit.play(hit);
		fromCharacterExplode.play(explosion);
	}
	
	public void shoot() {
		fromCharShoot.stop(shot);
		fromCharShoot.play(shot);
	}
	
	public static void Walk1() {
		fromCharacterWalk.play(step1);
	}
	public void Walk2() {
		fromCharacterWalk.play(step2);
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