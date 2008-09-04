package server.test;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;

import server.test.ClientStruct.ClientStatus;

public class Server {
	
	private Vector<ClientStruct> clients;
	
	public Server () {
		clients = new Vector<ClientStruct>();
		new ConnectionListener().run();
		
	}
	
	void gotConnection (Socket s) {
		ClientStruct client = new ClientStruct(s);
		client.setStatus(ClientStatus.AWAITING_PLAY)
		clients.add();
		
	}
	
	
	
	private class ConnectionListener extends Thread {
		
		private ServerSocket socket;
		
		public ConnectionListener () {
			try {
				socket = new ServerSocket(1234);
			} catch (IOException e) {
				System.err.println("Server: Cannot create socket (port busy?).");
			}
		}
		
		@Override
		public void run() {
			while (true) {
				try {
					Socket current = socket.accept();
				} catch (IOException e) {
					System.err.println("Server: Cannot connect to client.");
				}
				
			}
		}
	}
}
