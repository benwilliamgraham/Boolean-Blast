import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;

import org.joml.Vector3f;

public class Client implements Runnable{

    ObjectInputStream in = null;
    ObjectOutputStream out = null;
    Socket socket = null;
    
    boolean active = true;
    
    Map map;
    
    public Client(Map map){
    	this.map = map;
    	
    	try {
			setup();
		} catch (IOException e) {
			e.printStackTrace();
		}
    	Thread clientThread = new Thread(this);
    	clientThread.start();
    }
    	
    public void setup() throws IOException{
    	// Make connection and initialize streams
    	String serverAddress = "128.237.181.58";
        socket = new Socket(serverAddress, 8001);
        out = new ObjectOutputStream(socket.getOutputStream());
        in = new ObjectInputStream(socket.getInputStream());
        
        //setup connection with the server
        int status = 0;        
        while(status != -1){
        	status = in.readInt();
        }
        
        //setup map with info
        map.load("assets/map1.png");
		int playerCount = in.readInt();
		int id = in.readInt();
		map.players = new Player[playerCount];
		for(int e = 0; e < playerCount; e++) {
			map.players[e] = new Player(e, this);
		}
		map.player = map.players[id];
		if (playerCount == 1) playerCount++;
		map.numPlayers = playerCount;
		map.player.position = new Vector3f(Map.spawnPoints[id]);
		map.addParticles(new Vector3f(map.player.position), 12, 0.3f);
    }
    
    public void sendData(float[] data){
    	try {
			out.writeObject(data);
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }

	public void run() {
		try {
			while(active){
				//receive input
				float[] input = (float[]) in.readObject();
				if(input[0] == 666) {
					map.numPlayers--;
					if (map.numPlayers == 1) map.player.mode = Mode.WIN;
				}
				else if(input[0] == -1) {
					map.particles.add(new Bullet(new Vector3f(input[1], input[2], input[3]), new Vector3f(input[4], input[5], input[6]), true));
				}
				else if(input[0] >= 0) {
					map.players[(int)input[0]].position = new Vector3f(input[1], input[2], input[3]);
				}
			}
		}
		catch(IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
}