package Server;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;

public class Server implements Runnable{
	
	static boolean waiting = true;
	
	static int PORT = 8001;
	ServerSocket listener;
	
	static ArrayList<ObjectOutputStream> streams = new ArrayList<ObjectOutputStream>();
	
	static JFrame frame = new JFrame("Server");
    static JButton startButton = new JButton("Start Game");
	
	//create the server
	public static void main(String[] args) throws IOException {
		new Server();
	}
	
	public Server() throws IOException{
    	// Layout GUI
        frame.getContentPane().add(startButton, "Center");
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        frame.setSize(800,400);
        
        startButton.addActionListener(new ActionListener(){
        	public void actionPerformed(ActionEvent e){
        		System.out.println("Starting Game");
        		waiting = false;
        	}
        });
        
        //create the server
        listener = new ServerSocket(PORT);
        frame.setTitle("Server: " + InetAddress.getLocalHost() + "on port" + PORT);
        
        //add clients
        Thread addNewClients = new Thread(this);
        addNewClients.start();
        while(waiting) { System.out.print(""); }
        
        //start all players
        for(int s = 0; s < streams.size(); s++){
        	streams.get(s).writeInt(-1);
        	streams.get(s).writeInt(streams.size());
        	streams.get(s).writeInt(s);
        	streams.get(s).flush();
        }
        while(true);
	}

	@Override
	//add new clients
	public void run() {
		try {
            while (waiting) {
            	//create a new handler for each new client
                new Handler(listener.accept()).start();
            }
        } catch (IOException e) {
			e.printStackTrace();
		} finally {
            try {
				listener.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
        }
	}
	
	//thread for handling one client
    private static class Handler extends Thread {
        Socket socket = null;
        ObjectInputStream in = null;
        ObjectOutputStream out = null;

        //create handler with socket from the new client
        public Handler(Socket socket) {
            this.socket = socket;
        }
        
        //start the loop for the client
        public void run() {
        	//check to make sure new clients are being accepted
        	if(waiting){
	            try {
	                // Create character streams for the socket.
	            	out = new ObjectOutputStream(socket.getOutputStream());
	                in = new ObjectInputStream(socket.getInputStream());
	                
	                //alert the client of the connection and add to list of writers
	                System.out.println("Connected Player: " + socket.getLocalSocketAddress());
	                streams.add(out);
	
	                //broadcast messages from the client
	                while (true) {
	                    float[] input = (float[])in.readObject();
	                    if (input == null) {
	                        return;
	                    }
	                    for (ObjectOutputStream stream : streams) {
	                    	if(stream == out) continue;
	                    	stream.writeObject(input);
	                    	stream.flush();
	                    }
	                }
	            } catch (IOException | ClassNotFoundException e) {
					e.printStackTrace();
				} finally {
	            	//if the client connection has failed, remove the client close
	            	System.out.println("Removing player: " + socket.getChannel());

	                if (out != null) {
	                    streams.remove(out);
	                }
	                try {
	                	out.close();
	                    socket.close();
	                } catch (IOException e) {
	                }
	            }
	        }
	    }
    }
}
