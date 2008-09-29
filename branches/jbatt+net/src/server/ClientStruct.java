package server;

import game.layout.GameBoard;
import game.net.action.Confirmation;
import game.net.action.FiredShot;
import game.net.outcome.Outcome;
import game.net.request.Request;
import game.net.request.Request.RequestType;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;

import com.sun.org.apache.regexp.internal.recompile;

import server.game.Game;

public class ClientStruct {
	
	public enum ClientStatus {
		AWAITING_PLAY("Waiting"),
		IN_GAME("Playing"),
		LEAVING_GAME("Confirmation"),
		AWAITING_CLEANUP("Disconnecting");
		
		private String str;
		
		private ClientStatus (String str) {
			this.str = str;
		}
		
		public String toString () {
			return str;
		}
	}
	
	private final Socket socket;
	private final Socket pulse;
	private Game currentGame;
	private ClientStatus status;
	private boolean connected;
	private final String username;
	
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

	public ClientStruct (Socket socket, Socket pulse) {
		status = ClientStatus.AWAITING_PLAY;
		this.socket = socket;
		this.pulse = pulse;
		connected = true;
		String tempname;
		try {
			ObjectInputStream ois = new ObjectInputStream(this.socket.getInputStream());
			tempname = (String)ois.readObject();
		} catch (Exception e) {
			e.printStackTrace();
			tempname = "Unknown";
		}
		username = tempname;
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
	
	public synchronized Socket getPulse () {
		return pulse;
	}
	
	public synchronized String getURL () {
		return socket.getLocalAddress().toString();
	}
	
	public synchronized void disconnected () {
		connected = false;
	}
	
	/**
	 * @param currentGame the currentGame to set
	 */
	public synchronized void setCurrentGame (Game currentGame) {
		setCurrentGame(currentGame, ClientStatus.AWAITING_PLAY);
	}
	
	public synchronized void setCurrentGame (Game currentGame, ClientStatus status) {
		this.currentGame = currentGame;
		this.status = status;
		//TODO why did I do this?
		currentGame = null;
	}
	
	public GameBoard sendGameStart () {
		status = ClientStatus.IN_GAME;
		
		try {
			ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
			oos.writeObject(new Request(RequestType.GAMEBOARD_REQUEST));
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
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
	
	public FiredShot requestMove () {
		
		Object recieved;
		try {
			ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
			oos.writeObject(new Request(RequestType.MOVE_REQUEST));
			recieved = new ObjectInputStream(socket.getInputStream()).readObject();
			if (!(recieved instanceof FiredShot)) {
				System.err.println("Server: Did not receive expected response from client (GameBoard);");
				return null;
			} else {
				return (FiredShot)recieved;
			}
		} catch (IOException e) {
			System.err.println("Server: Cannot contact client (GameBoard request).");
			return null;
		} catch (ClassNotFoundException e) {
			System.err.println("Server: ClassNotFoundException.");
			return null;
		}
	}
	
	public boolean sendFiredShot (FiredShot shot) {
		
		Object recieved;
		try {
			ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
			oos.writeObject(shot);
			recieved = new ObjectInputStream(socket.getInputStream()).readObject();
			if (!(recieved instanceof Confirmation)) {
				System.err.println("Server: Did not receive expected response from client (GameBoard);");
				return false;
			} else {
				return true;
			}
		} catch (IOException e) {
			System.err.println("Server: Cannot contact client (GameBoard request).");
			return false;
		} catch (ClassNotFoundException e) {
			System.err.println("Server: ClassNotFoundException.");
			return false;
		}
	}
	
	public boolean sendOutcome (Outcome outcome) {
		
		Object recieved;
		try {
			ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
			oos.writeObject(outcome);
			recieved = new ObjectInputStream(socket.getInputStream()).readObject();
			if (!(recieved instanceof Confirmation)) {
				System.err.println("Server: Did not receive expected response from client (GameBoard);");
				return false;
			} else {
				return true;
			}
		} catch (IOException e) {
			System.err.println("Server: Cannot contact client (GameBoard request).");
			return false;
		} catch (ClassNotFoundException e) {
			System.err.println("Server: ClassNotFoundException.");
			return false;
		}
	}
	
	public boolean isConnected () {
		return connected;
	}
	
	public void sendGameStop (String message) {
		if (!isConnected()) {
			status = ClientStatus.AWAITING_CLEANUP;
		} else {
			try {
				ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
				oos.writeObject(new Request(RequestType.CONFIRMATION_REQUEST));
				ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
				Object result = ois.readObject();
				//FIX I don't know if this will work 
				if (result != null)
					status = ClientStatus.AWAITING_PLAY;
				else
					status = ClientStatus.AWAITING_CLEANUP;
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
}
