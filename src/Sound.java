import java.io.*;
import sun.audio.*;
   
public enum Sound {
	EXPLODE("assets/bang.wav"),
	GONG("assets/bang.wav"),
	SHOOT("assets/bang.wav");
	
	float volume = 1;
	InputStream audioFile;
	Sound(String filename){
		try {
			audioFile = new FileInputStream(filename);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}	
	}
	
	void play() {
		try {
			AudioPlayer.player.start(new AudioStream(audioFile));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	static void init() {
		values();
	}
}