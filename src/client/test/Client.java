package client.test;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {
	
	private Socket socket;
	
	public Client () {
		
	}
	
	public void connect (String url) {
		try {
			socket = new Socket("localhost", 1234);
		} catch (UnknownHostException e) {
		System.err.println("Client: ");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
