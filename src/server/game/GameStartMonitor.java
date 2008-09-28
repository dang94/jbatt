package server.game;

import java.util.Observable;

import server.ClientStruct;
import game.layout.GameBoard;

public class GameStartMonitor extends Observable implements Runnable {
	
	private GameBoard board1, board2;
	private Game game;
	
	public GameStartMonitor (Game game) {
		this.game = game;
	}
	
	@Override
	public void run() {
		(new Thread () {
			@Override
			public void run() {
				board1 = game.getPlayer1().sendGameStart();
			}
		}).start();
		
		(new Thread () {
			@Override
			public void run() {
				board2 = game.getPlayer2().sendGameStart();
			}
		}).start();

		while (board1 == null || board2 == null);
		
		game.setBoard1(board1);
		game.setBoard2(board2);
		System.out.println("IT WORKS!!!");
		System.out.println(board1);
		System.out.println(board2);
		setChanged();
		notifyObservers();
	}
}

