import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class Client implements Runnable{

    BufferedReader in;
    PrintWriter out;
    Socket socket;
    
    boolean active = true;
    
    public Client(){
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
    	String serverAddress = "128.237.172.91";
        socket = new Socket(serverAddress, 8000);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);
        
        //setup connection with the server
        String line = "";
        while(!line.startsWith("CONNECTED")){
			try {
				line = in.readLine();
			}catch(IOException e){
				e.printStackTrace();
			}
        }
        
        while(!line.startsWith("START")){
        	line = in.readLine();
        }
    }
    
    public void sendData(String data){
    	out.println(data);
    }

	public void run() {
		while(active){
			//receive input
			try {
				System.out.println(in.readLine());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}