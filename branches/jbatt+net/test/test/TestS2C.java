package test;

import server.Server;
import client.Client;

public class TestS2C {

	/**
	 * @param args
	 */
	public static void main (String [] args) {
		new Thread() {
			public void run () {
				new Server();
			}
		}.start();
		
		new Thread() {
			public void run () {
				new Client();
			}
		}.start();
		
		new Thread() {
			public void run () {
				new Client();
			}
		}.start();
	}

}
