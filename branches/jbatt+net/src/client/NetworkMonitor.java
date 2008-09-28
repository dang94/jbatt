/**
 * @author Alex Peterson
 * @version 2008SE19
 */

package client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.Observable;
import java.util.Observer;

/**
 * An Observable thread which monitors the
 * network connection of a socket.
 */
public class NetworkMonitor extends Observable implements Runnable {
	
	/* Fields */
	
	private Socket socket;
	
	/* END Fields */
	
	
	/* Constructors */
	
	/**
	 * Constructs a new NetworkMonitor with a socket to
	 * monitor and an observer to notify.
	 * @param socket the socket to monitor for an active connection
	 * @param observer the observer to notify if the socket has lost its connection
	 */
	public NetworkMonitor (Socket socket, Observer observer) {
		this.socket = socket;
		addObserver(observer);
	}
	
	/* END Constructors */
	
	
	/* Runnable Methods */
	
	/**
	 * Runs the thread.
	 */
	@Override
	public void run() {
		try {
			final ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
			while (true) {
				//blocking statement
				final Object received = ois.readObject();
				notifyObservers(received);
			}
		} catch (final IOException e) {
			e.printStackTrace();
		} catch (final ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	/* END Runnable Methods */
}
