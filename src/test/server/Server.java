package test.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;


import test.client.Client;
import test.server.ClientStruct.ClientStatus;

public class Server {
	
	private ServerWindow window;
	private Gamemaster gm;
	
	public static void main (String [] args) {
		System.out.println("Server: Starting server.");
		new Server();
	}
	
	public Server () {
		System.out.println("Server: Creating window.");
		window = new ServerWindow();
		gm = new Gamemaster(this);
		gm.start();
		System.out.println("Server: Creating connection listener.");
		ConnectionListener cl = new ConnectionListener();
		cl.start();
		
	}
	
	private void gotConnection (Socket s) {
		System.out.println("Server: Got connection.");
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
				System.out.println("Server: Preparing to listen.");
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
