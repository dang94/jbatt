package server.game;

import server.ClientStruct;

public class Game {
	
	public enum GameStatus {
		IN_PROGRESS("In Progress"),
		ENDED("Ended");
		
		private String str;
		
		private GameStatus (String str) {
			this.str = str;
		}
		
		public String toString () {
			return str;
		}
	}
	
	private ClientStruct player1;
	private ClientStruct player2;
	private GameStatus status;
	
	public Game (ClientStruct player1, ClientStruct player2) {
		this.player1 = player1;
		this.player2 = player2;
		(new ConnectionMonitor()).start();
	}
	
	public GameStatus getStatus () {
		return status;
	}
	
	public ClientStruct getPlayer1 () {
		return player1;
	}
	
	public ClientStruct getPlayer2 () {
		return player2;
	}
	
	public void start () {
		player1.sendGameStart();
		player2.sendGameStart();
	}
	
	private class ConnectionMonitor extends Thread {
		
		@Override
		public void run() {
			boolean allConnected = true;
			while (allConnected) {
				if (!player1.getSocket().isConnected() ||
						!player2.getSocket().isConnected()) {
					System.err.println("Server: A player has been connected.");
					allConnected = false;
				}
			}
		}
		
	}
	
}
