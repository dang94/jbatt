package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Observable;

public class ConnectionListener extends Observable implements Runnable {
	
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
					notifyObservers(current);
			} catch (IOException e) {
				System.err.println("Server: Cannot connect to client.");
			}
			
		}
	}
	
}
