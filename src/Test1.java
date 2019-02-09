import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Test1 {
	volatile static float[][][] playerInfo;
	public static void main(String[] args) {
		playerInfo = new float[2][2][10];
		for(int i = 0; i < 2; i++) {
			for(int j = 0; j < 2; j++) {
				for(int k = 0; k < 10; k++) {
					if(i == 0) {
						playerInfo[i][j][k] = 0;
					}else {
						playerInfo[i][j][k] = 1;
					}
				}
			}
		}
		for(int i = 0; i < 2; i++) {
			try {
				Socket sock = new Socket("127.0.0.1", 8000);
				ObjectOutputStream oos = new ObjectOutputStream(sock.getOutputStream());
				ObjectInputStream ois = new ObjectInputStream(sock.getInputStream());
		        
		        ClientThread ct = new ClientThread(sock, ois, oos, playerInfo[i]);
		        ct.start();
		        
			}catch(Exception e) {
				e.printStackTrace();
			}
		}
		
		long time = System.currentTimeMillis();
		while(time - System.currentTimeMillis() < 10000) {
			if((time - System.currentTimeMillis())%1000 == 0) {
				//System.out.println("Player 0: " + playerInfo[0][0][2] + " Player 1: " + playerInfo[1][1][2]);
			}
		}
		playerInfo[0][0][0] = -1;
	}

}
