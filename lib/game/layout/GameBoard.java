package game.layout;

import java.io.Serializable;

public class GameBoard implements Serializable{
	
	public static final int WIDTH = 10;
	public static final int HEIGHT = 10;
	
	private char [][] board;
	
	public GameBoard () {
		board = new char[WIDTH][HEIGHT];
	}
	
	/*public char getLocation (int x, int y) throws IndexOutOfBoundsException {
		if (x >= WIDTH || y >= HEIGHT)
			throw new IndexOutOfBoundsException();
		
	}*/
	
}
