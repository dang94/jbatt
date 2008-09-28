package server.game;

import java.io.ObjectOutputStream;
import java.util.Observable;
import java.util.Observer;

import com.sun.corba.se.impl.corba.ContextListImpl;

import game.layout.GameBoard;
import game.net.action.FiredShot;
import game.net.outcome.Outcome;
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
	
	private final ClientStruct [] players;
	private GameBoard [] boards;
	private GameStatus status;
	private boolean done;
	
	/* END Fields */
	
	
	/* Constructors */
	
	public Game (ClientStruct player1, ClientStruct player2) {
		players = new  ClientStruct[2];
		players[0] = player1;
		players[1] = player2;
	}
	
	/* END Constructors */
	
	/* Accessors */
	
	public GameStatus getStatus () {
		return status;
	}
	
	public ClientStruct getPlayer1 () {
		return players[0];
	}
	
	public ClientStruct getPlayer2 () {
		return players[1];
	}
	
	public GameBoard getBoard1 () {
		return boards[0];
	}
	
	public GameBoard getBoard2 () {
		return boards[1];
	}
	
	/* END Accessors */
	
	
	/* Mutators */
	
	public void setBoard1 (GameBoard board) {
		boards[0] = board;
	}
	
	public void setBoard2 (GameBoard board) {
		boards[1] = board;
	}
	
	/* END Mutators */
	
	
	/* Runnable Methods */
	
	@Override
	public void run () {
		boolean done = false;
		ClientStruct winner;
		int going = 0, waiting = 1;
		while (!done) {
			//
			FiredShot shot = players[going].requestMove();
			Outcome o = boards[waiting].fire(shot.getX(), shot.getY());
			if (!players[waiting].sendFiredShot(shot))
				System.out.println("Server: could not send data to player " + waiting);
			if (!players[going].sendOutcome(o))
				System.out.println("Server: did not get expected response from client.");
			
			//TODO check for win
			if (!boards[waiting].isAlive()) {
				done = true;
				winner = players[going];
			}
			
			//switch player
			int temp = going;
			going = waiting;
			waiting = temp;
		}
		
	}
	
	/* Runnable Methods */
	
	
	public void stopGame (String reason) {
		System.out.println("Server: Game stopped: \"" + reason + "\"");
		if (players[0].isConnected())
			players[0].sendGameStop(reason);
		if (players[1].isConnected())
			players[1].sendGameStop(reason);
		status = GameStatus.ENDED;
	}
	
	
	/* Observer Methods */
	
	public void update(Observable o, Object arg) {
		if (o instanceof GameStartMonitor) {
			//game can start now
			(new Thread(this)).start();
		}
	}	
	
	/* Observer Methods */
	
}
