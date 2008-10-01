package client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.Observable;
import java.util.Observer;

public class NetworkMonitor extends Observable implements Runnable {
	
	private Observer observer;
	private Socket socket;
	
	public NetworkMonitor (Observer observer, Socket socket) {
		this.socket = socket;
		addObserver(observer);
	}
	
	public void run() {
		try {
			ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
			while (true) {
				//blocking statement
				Object received = ois.readObject();
				notifyObservers(received);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

}
