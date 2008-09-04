package server.test;

public class Game {
	
	ClientStruct player1;
	ClientStruct player2;
	
	public Game (ClientStruct player1, ClientStruct player2) {
		this.player1 = player1;
		this.player2 = player2;
	}
	
	public void start () {
		player1.sendGameStart();
		player2.sendGameStart();
	}
}
