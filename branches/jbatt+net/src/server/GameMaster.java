/**
 * @author Alex Peterson
 * @version 2008SE17
 */

package server;

import java.util.Observable;
import java.util.Observer;
import java.util.Vector;

import server.ClientStruct.ClientStatus;
import server.game.PulseMonitor;
import server.game.Game;
import server.game.Game.GameStatus;
import server.ui.ServerPanel;

public class GameMaster extends Observable implements Runnable, Observer {
	
	/* Fields */
	
	private Server server;
	private Vector<Game> games;
	private Vector<ClientStruct> players;
	private PulseMonitor pm;
	
	/* END Fields */
	
	
	/* Constructors */
	
	/**
	 * Constructs a new GameMaster with a Vector of players.
	 * @param players a Vector containing all eligible players
	 */
	public GameMaster (Server server) {
		players = new Vector<ClientStruct>();
		games = new Vector<Game>();
		this.server = server;
	}
	
	/* END Constructors */
	
	
	/* Visible Methods */
	
	public synchronized void addPlayer (ClientStruct player) {
		players.add(player);
		pm = new PulseMonitor(player);
		pm.addObserver(this);
		(new Thread(pm)).start();
		System.out.println("Server: Player added.");
		notifyObservers();
	}
	
	public synchronized Vector<String> getPlayerStrings () {
		Vector<String> strings = new Vector<String>();
		ClientStruct s;
		for (int i = 0; i < players.size(); i++) {
			s = players.get(i);
			strings.add(s.getURL() + '\t' + s.getStatus().toString());
		}
		return strings;
	}
	
	/* END Visible Methods */
	
	
	/* Local Methods */
	
	private void startGame (ClientStruct player1, ClientStruct player2) {
		System.out.println("start game, notify observers");
		//TODO check that players are still connected first!!!
		setChanged();
		notifyObservers();
		Game g = new Game(player1, player2);
		games.add(g);
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

	public void update (Observable o, Object arg) {
		System.out.println("gm got update");
		if (o == pm) {
			System.out.println("gamemaster will cleanup player");
			ClientStruct player = (ClientStruct)arg;
			if (player.getStatus() == ClientStatus.IN_GAME)
					player.getCurrentGame().stopGame("A player was disconnected");
			if (player.getStatus() == ClientStatus.AWAITING_CLEANUP)
				players.removeElement(player);
			((ServerPanel)server.getContentPane()).refreshPlayers(getPlayerStrings());
		}
		
	}
	
	/* END Runnable Methods */
	
}
