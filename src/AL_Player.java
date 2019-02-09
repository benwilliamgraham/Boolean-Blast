import org.lwjgl.openal.AL;
import org.lwjgl.BufferUtils;

import org.lwjgl.openal.AL10;
import java.util.ArrayList;

public class AL_Player {
	
	public static ArrayList<Integer> buffers = new ArrayList<Integer>();
	
	public static void init() {
		AL.createCapabilities(null);
	
	}
	
	public static void setListenerData() {
		AL10.alListener3f(AL10.AL_POSITION, 0, 0, 0);
		AL10.alListener3f(AL10.AL_VELOCITY, 0, 0, 0);
	}
	
	public static int loadSound(String file) {
		int buffer = AL10.alGenBuffers();
		buffers.add(buffer);
		WaveData waveFile = WaveData.create(file);
		AL10.alBufferData(buffer, waveFile.format, waveFile.data, waveFile.samplerate);
		waveFile.dispose();
		return buffer;
	}
	
	public static int cleanup() {
		for(int buffer : buffers) {
			AL10.alDeleteBuffers(buffer);
		}
		AL.destroy();
	}

	
	
//	static AudioClip drip = new AudioClip("musicAssets/Drip.wav");
//	static AudioClip drip2 = new AudioClip("assets/Drip.mp3");
//	
//	public static void main(String[] args) {
//		AudioTest.drip.play();
//		AudioTest.drip2.play();
//	}
}
