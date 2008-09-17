package server.game;

import java.util.Observable;
import java.util.Observer;

import game.layout.GameBoard;
import server.ClientStruct;

public class Game implements Runnable, Observer {
	
	/* Enumerators */
	
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
	
	/* END Enumerators */
	
	
	/* Fields */
	
	private ClientStruct player1, player2;
	private GameBoard board1, board2;
	private GameStatus status;
	
	/* END Fields */
	
	
	/* Constructors */
	
	public Game (ClientStruct player1, ClientStruct player2) {
		this.player1 = player1;
		this.player2 = player2;
		(new Thread(new ConnectionMonitor(this))).start();
	}
	
	/* END Constructors */
	
	
	/* Accessors */
	
	public GameStatus getStatus () {
		return status;
	}
	
	public ClientStruct getPlayer1 () {
		return player1;
	}
	
	public ClientStruct getPlayer2 () {
		return player2;
	}
	
	public GameBoard getBoard1 () {
		return board1;
	}
	
	public GameBoard getBoard2 () {
		return board2;
	}
	
	/* END Accessors */
	
	
	/* Runnable Methods */
	
	@Override
	public void run () {
		//TODO figure out how to make a game into a thread
	}
	
	/* Runnable Methods */
	
	
	/* Observer Methods */
	
	public void update(Observable o, Object arg) {
		if (o instanceof GameStartMonitor) {
			//game can start now
			(new Thread(this)).start();
		} else if (o instanceof ConnectionMonitor) {
			//a player is disconnected
		}
	}	
	
	/* Observer Methods */
	
}
