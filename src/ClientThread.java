import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientThread extends Thread implements Runnable {
	final ObjectInputStream ois; 
    final ObjectOutputStream oos; 
    final Socket server;
	private volatile float[][] playerInfo;
	private int playerNum;
	
	public ClientThread(Socket server, ObjectInputStream ois, ObjectOutputStream oos, float[][] playerInfo) {
		this.playerInfo = playerInfo;
		this.server = server; 
        this.ois = ois; 
        this.oos = oos;
	}
	
	public int getPlayerNum() {
		return playerNum;
	}
	
	@Override
    public void run() {
		try {
			playerNum = ois.readInt();
			while (true) {
				System.out.println("Yo.");
				playerInfo = (float[][])ois.readObject();
				if(playerNum == 0) {
					playerInfo[playerNum][2]++;
				}else {
					playerInfo[playerNum][2]--;
				}
				
        		oos.writeObject(playerInfo);
        		oos.flush();
	        }
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
}
