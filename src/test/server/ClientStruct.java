package test.server;

import game.layout.GameBoard;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

public class ClientStruct {
	
	public enum ClientStatus {
		AWAITING_PLAY("Waiting"),
		IN_GAME("Playing"),
		LEAVING_GAME("Disconnecting");
		
		private String str;
		
		private ClientStatus (String str) {
			this.str = str;
		}
		
		public String toString () {
			return str;
		}
	}
	
	private final Socket socket;
	private Game currentGame;
	private ClientStatus status;
	
	/**
	 * @return the status
	 */
	public synchronized ClientStatus getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public synchronized void setStatus(ClientStatus status) {
		this.status = status;
	}

	public ClientStruct (Socket socket) {
		status = ClientStatus.AWAITING_PLAY;
		this.socket = socket;
	}
	
	/**
	 * @return the currentGame
	 */
	public synchronized Game getCurrentGame() {
		return currentGame;
	}
	/**
	 * @return the socket
	 */
	public synchronized Socket getSocket () {
		return socket;
	}
	
	public synchronized String getURL () {
		return socket.getLocalAddress().toString();
	}
	/**
	 * @param currentGame the currentGame to set
	 */
	public synchronized void setCurrentGame(Game currentGame) {
		this.currentGame = currentGame;
		status = ClientStatus.AWAITING_PLAY;
		currentGame = null;
	}
	
	public GameBoard sendGameStart () {
		
		Object recieved;
		try {
			recieved = new ObjectInputStream(socket.getInputStream()).readObject();
			if (!(recieved instanceof GameBoard)) {
				System.err.println("Server: Did not receive expected response from client (GameBoard);");
				return null;
			} else {
				return (GameBoard)recieved;
			}
		} catch (IOException e) {
			System.err.println("Server: Cannot contact client (GameBoard request).");
			return null;
		} catch (ClassNotFoundException e) {
			System.err.println("Server: ClassNotFoundException.");
			return null;
		}
	}
	
}
