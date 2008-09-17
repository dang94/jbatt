package server.game;

import java.util.Observable;

import server.ClientStruct;

public class ConnectionMonitor extends Observable implements Runnable {
	
	private ClientStruct player1, player2;
	
	public ConnectionMonitor (Game game) {
		player1 = game.getPlayer1();
		player2 = game.getPlayer2();
	}
	
	@Override
	public void run() {
		boolean allConnected = true;
		while (allConnected) {
			if (!player1.getSocket().isConnected()) {
				System.err.println("Server: Player 1 has been connected.");
				notifyObservers(player1);
				allConnected = false;
			} else if (!player2.getSocket().isConnected()) {
				System.err.println("Server: Player 2 has been connected.");
				notifyObservers(player2);
				allConnected = false;
			}
		}
	}
	
}

