package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Observable;

import comm.Ports;

public class ConnectionListener extends Observable implements Runnable {
	
	private ServerSocket socket;
	private ServerSocket pulse;
	
	public ConnectionListener () {
		try {
			socket = new ServerSocket(Ports.COMM_PORT);
			pulse = new ServerSocket(Ports.PULSE_PORT);
			System.out.println("Server: Preparing to listen.");
		} catch (IOException e) {
			//FIX was failing here
			System.err.println("Server: Cannot create socket (port busy?).");
		}
	}
	
	@Override
	public void run() {
		while (true) {
			try {
				System.out.println("Server: Listening...");
				Socket current = socket.accept();
				Socket currPulse = pulse.accept();
				System.out.println("Server: Preparing to handle connection");
				System.out.println();
				if (current != null && currPulse != null) {
					setChanged();
					notifyObservers(new ClientStruct(current, currPulse));
				}
			} catch (IOException e) {
				System.err.println("Server: Cannot connect to client.");
			}
			
		}
	}
	
}
