package client.test;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {
	
	public static void main (String [] args) {
		new Client();
	}
	
	private Socket socket;
	
	public Client () {
		connect("localhost");
	}
	
	public void connect (String url) {
		try {
			socket = new Socket("localhost", 1234);
			System.out.println("Client: Connected.");
		} catch (UnknownHostException e) {
		System.err.println("Client: ");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
