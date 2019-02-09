package Server;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class Server implements Runnable{
	
	static boolean waiting = true;
	
	static int PORT = 8000;
	ServerSocket listener;
	
	static HashSet<PrintWriter> writers = new HashSet<PrintWriter>();
	
	static JFrame frame = new JFrame("Server");
    static JTextField textField = new JTextField(80);
    static JButton startButton = new JButton("Start Game");
    static JTextArea messageArea = new JTextArea(30, 80);
	
	//create the server
	public static void main(String[] args) throws IOException {
		Server server = new Server();
	}
	
	public Server() throws IOException{
    	// Layout GUI
        textField.setEditable(false);
        messageArea.setEditable(false);
        frame.getContentPane().add(startButton, "North");
        frame.getContentPane().add(new JScrollPane(messageArea), "Center");
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        
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
        for(PrintWriter out: writers){
        	out.println("START");
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
                System.out.println("New handler created for client");
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
        private Socket socket;
        private BufferedReader in;
        private PrintWriter out;

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
	                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
	                out = new PrintWriter(socket.getOutputStream(), true);
	                
	                //alert the client of the connection and add to list of writers
	                System.out.println("Connected Player: " + socket.getChannel());
	                messageArea.append(socket.getChannel().toString());
	                out.println("CONNECTED");
	                writers.add(out);
	
	                //broadcast messages from the client
	                while (true) {
	                    String input = in.readLine();
	                    if (input == null) {
	                        return;
	                    }
	                    for (PrintWriter writer : writers) {
	                    	if(writer == out) continue;
	                        writer.println(input);
	                    }
	                }
	            } catch (IOException e) {
	                System.out.println(e);
	            } finally {
	            	//if the client connection has failed, remove the client close
	            	System.out.println("Removing player: " + socket.getChannel());

	                if (out != null) {
	                    writers.remove(out);
	                }
	                try {
	                    socket.close();
	                } catch (IOException e) {
	                }
	            }
	        }
	    }
    }
}
