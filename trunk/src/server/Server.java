package server;

import java.awt.Dimension;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;

import javax.swing.JFrame;

import client.test.Client;

import server.ClientStruct.ClientStatus;
import server.game.Game;
import server.game.Game.GameStatus;
import server.ui.ServerPanel;

public class Server extends JFrame {
	
	private ServerPanel contentPane;
	private Gamemaster gm;
	private Vector<ClientStruct> players;
	
	public static void main (String [] args) {
		System.out.println("Server: Starting server.");
		new Server();
	}
	
	public Server () {
		System.out.println("Server: Creating window.");
		BuildWindow();
		gm = new Gamemaster();
		gm.start();
		System.out.println("Server: Creating connection listener.");
		(new ConnectionListener()).start();
		
	}
	
	private void BuildWindow () {
		setContentPane(contentPane = new ServerPanel());
		setPreferredSize(new Dimension(200, 200));
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		pack();
		setVisible(true);
	}
	
	private void gotConnection (Socket s) {
		System.out.println("Server: Got connection.");
		gm.addPlayer(new ClientStruct(s));
	}
	
	private void refreshPlayers () {
		contentPane.refreshPlayers((Vector<ClientStruct>)players.clone());
	}
	
	private class ConnectionListener extends Thread {
		
		private ServerSocket socket;
		
		public ConnectionListener () {
			try {
				socket = new ServerSocket(1234);
				System.out.println("Server: Preparing to listen.");
			} catch (IOException e) {
				System.err.println("Server: Cannot create socket (port busy?).");
			}
		}
		
		@Override
		public void run() {
			while (true) {
				try {
					System.out.println("Server: Listening...");
					Socket current = socket.accept();
					if (current != null)
						gotConnection(current);
				} catch (IOException e) {
					System.err.println("Server: Cannot connect to client.");
				}
				
			}
		}
	}
	
	private class Gamemaster extends Thread {
		
		private Vector<Game> games;
		
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
		
		public synchronized void addPlayer (ClientStruct player) {
			players.add(player);
			System.out.println("Server: Player added.");
			refreshPlayers();
		}
		
		private void startGame (ClientStruct player1, ClientStruct player2) {
			//TODO check that players are still connected first!!!
			player1.setStatus(ClientStatus.IN_GAME);
			player2.setStatus(ClientStatus.IN_GAME);
			refreshPlayers();
			games.add(new Game(player1, player2));
		}
	}
}
