package client.test;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client extends Thread {
	
	public static void main (String [] args) {
		new Client();
	}
	
	private Socket socket;
	
	public Client () {
		start();
	}
	
	public void connect (String url) {
		try {
			socket = new Socket(url, 1234);
			System.out.println("Client: Connected.");
		} catch (UnknownHostException e) {
			System.err.println("Client: Cannot find host.");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	public void run() {
		connect("localhost");
		while (true) {}
	}
	
}
