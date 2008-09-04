package server.test;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;

import client.test.Client;

import server.test.ClientStruct.ClientStatus;

public class Server {
	
	private ServerWindow window;
	private Gamemaster gm;
	
	public static void main (String [] args) {
		new Server();
	}
	
	public Server () {
		window = new ServerWindow();
		gm = new Gamemaster(this);
		new ConnectionListener();
		
	}
	
	private void gotConnection (Socket s) {
		gm.addPlayer(new ClientStruct(s));
	}
	
	void updatePlayerList (Vector<ClientStruct> list) {
		window.updatePlayerList(list);
	}
	
	private class ConnectionListener extends Thread {
		
		private ServerSocket socket;
		
		public ConnectionListener () {
			try {
				socket = new ServerSocket(1234);
				run();
			} catch (IOException e) {
				System.err.println("Server: Cannot create socket (port busy?).");
			}
		}
		
		@Override
		public void run() {
			while (true) {
				try {
					System.out.println("Server: Listening...");
					Socket current = socket.accept();
					if (current != null)
						gotConnection(current);
				} catch (IOException e) {
					System.err.println("Server: Cannot connect to client.");
				}
				
			}
		}
	}
}
