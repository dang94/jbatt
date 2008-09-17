package server.game;

import java.util.Observable;

import server.ClientStruct;
import game.layout.GameBoard;

public class GameStartMonitor extends Observable implements Runnable {
	
	private ClientStruct player1, player2;
	private GameBoard board1, board2;
	
	public GameStartMonitor (Game game) {
		player1 = game.getPlayer1();
		player2 = game.getPlayer2();
		board1 = game.getBoard1();
		board2 = game.getBoard2();
	}
	
	@Override
	public void run() {
		(new Thread () {
			@Override
			public void run() {
				board1 = player1.sendGameStart();
			}
		}).start();
		
		(new Thread () {
			@Override
			public void run() {
				board2 = player2.sendGameStart();
			}
		}).start();

		while (board1 == null || board2 == null);
		
		notifyObservers();
	}
}

