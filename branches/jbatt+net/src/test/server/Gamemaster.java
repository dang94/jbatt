package test.server;

import java.util.Vector;

import test.server.ClientStruct.ClientStatus;

public class Gamemaster extends Thread {
	
	private Server owner;
	private Vector<ClientStruct> players;
	
	public Gamemaster (Server owner) {
		this.owner = owner;
		players = new Vector<ClientStruct>();
	}
	
	@Override
	public void run() {
		while (true) {
			ClientStruct [] currPlayers = new ClientStruct[2];
			boolean done = false;
			int numPlayers = players.size();
			for (int i = 0; i < numPlayers && !done; i++) {
				if (players.get(i).getStatus() == ClientStatus.AWAITING_PLAY) {
					if (currPlayers[0] == null)
						currPlayers[0] = players.get(i);
					else if (currPlayers[1] == null) {
						currPlayers[1]= players.get(i);
						done = true;
					}
				}
			}
			if (done)
				startGame(currPlayers[0], currPlayers[1]);
		}
	}
	
	public synchronized void addPlayer (ClientStruct player) {
		players.add(player);
		System.out.println("Server: Player added.");
		owner.updatePlayerList(players);
	}
	
	private void startGame (ClientStruct player1, ClientStruct player2) {
		player1.setStatus(ClientStatus.IN_GAME);
		player2.setStatus(ClientStatus.IN_GAME);
		owner.updatePlayerList(players);
	}
	
}
