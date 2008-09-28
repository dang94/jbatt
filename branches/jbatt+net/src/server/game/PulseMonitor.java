package server.game;

import java.io.DataInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;
import java.util.Observable;
import java.util.Vector;

import server.ClientStruct;
import server.Server;

public class PulseMonitor extends Observable implements Runnable {
	
	//TODO make connection monitor do monitoring for any type of connection, not just in game
	private ClientStruct player;
	
	public PulseMonitor (ClientStruct player) {
		this.player = player;
	}
	
	@Override
	public void run() {
		DataInputStream in;
		try {
			in = new DataInputStream(player.getPulse().getInputStream());
			boolean done = false;
			while (!done) {
				try {
					player.getPulse().setSoTimeout(200);
					System.out.println("got pulse " + in.readByte());
				} catch (IOException e) {
					e.printStackTrace();
				} catch (Exception e) {
					if (e instanceof EOFException ||
							e instanceof SocketException) {
						done = true;
						setChanged();
						notifyObservers(player);
					}
				}
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
	
}

