package server.test;

public class Gamemaster extends Thread {
	
	private Game game;
	
	public Gamemaster (Game game) {
		this.game = game;
	}
	
	@Override
	public void run() {
		game.start();
	}
	
}
