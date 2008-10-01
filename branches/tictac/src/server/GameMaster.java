package server;

import java.util.Observable;
import java.util.Vector;

import server.ClientStruct.ClientStatus;
import server.game.Game;
import server.game.Game.GameStatus;

public class GameMaster extends Observable implements Runnable {
	
	/* Fields */
	
	private Vector<Game> games;
	private Vector<ClientStruct> players;
	
	/* END Fields */
	
	
	/* Constructors */
	
	public GameMaster (Vector<ClientStruct> players) {
		this.players = players;
	}
	
	/* END Constructors */
	
	
	/* Visible Methods */
	
	public synchronized void addPlayer (ClientStruct player) {
		players.add(player);
		System.out.println("Server: Player added.");
		notifyObservers();
	}
	
	/* END Visible Methods */
	
	
	/* Local Methods */
	
	private void startGame (ClientStruct player1, ClientStruct player2) {
		//TODO check that players are still connected first!!!
		player1.setStatus(ClientStatus.IN_GAME);
		player2.setStatus(ClientStatus.IN_GAME);
		notifyObservers();
		Game g = new Game(player1, player2);
		games.add(g);
		(new Thread(g)).start();
	}
	
	/* END Local Methods */
	
	
	/* Runnable Methods */
	
	@Override
	public void run() {
		while (true) {
			//look for ended games
			Vector<Game> currGames = (Vector<Game>)games.clone();
			for (int i = 0; i < currGames.size(); i++)
				if (currGames.get(i).getStatus() == GameStatus.ENDED)
					games.removeElement(currGames.get(i));
			
			ClientStruct player1 = null, player2 = null;
			boolean done = false;
			Vector<ClientStruct> currPlayers = (Vector<ClientStruct>) players.clone();
			for (int i = 0; i < currPlayers.size() && !done; i++) {
				if (currPlayers.get(i).getStatus() == ClientStatus.AWAITING_PLAY) {
					if (player1 == null)
						player1 = currPlayers.get(i);
					else if (player2 == null) {
						player2 = currPlayers.get(i);
						done = true;
					}
				}
			}
			if (done)
				startGame(player1, player2);
		}
	}
	
	/* END Runnable Methods */
	
}
