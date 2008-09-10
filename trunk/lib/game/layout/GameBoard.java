package game.layout;

import java.awt.Color;
import java.awt.Graphics;
import java.io.Serializable;
import java.util.Vector;

public class GameBoard implements Serializable{
	
	public static final int GRID_SIZE = 20;
	
	public static final int WIDTH = 10;
	public static final int HEIGHT = 10;
	
	public final int LEFT_OFFSET;
	public final int TOP_OFFSET;
	
	private Vector<Ship> ships;
	private char [][] hitBoard;
	
	public GameBoard (int left, int top, int [] shipLengths) {
		LEFT_OFFSET = left;
		TOP_OFFSET = top;
		hitBoard = new char[WIDTH][HEIGHT];
		ships = new Vector<Ship>();
		for (int i = 0; i < shipLengths.length; i++)
			ships.add(new Ship(shipLengths[i], this));
	}
	
	public Ship getShip (int index) {
		if (index < 0 || index >= ships.size())
			return null;
		else
			return ships.get(index);
	}
	
	public void paint (Graphics g, int placing) {
		g.setColor(Color.BLACK);
		int width = WIDTH * GRID_SIZE;
		int height = HEIGHT * GRID_SIZE;
		for (int x = 0; x <= WIDTH; x++)
			g.drawLine(
					x * GRID_SIZE + LEFT_OFFSET,
					0 + TOP_OFFSET,
					x * GRID_SIZE + LEFT_OFFSET,
					height + TOP_OFFSET);
		for (int y = 0; y <= HEIGHT; y++)
			g.drawLine(
					0 + LEFT_OFFSET,
					y * GRID_SIZE + TOP_OFFSET,
					width + LEFT_OFFSET,
					y * GRID_SIZE + TOP_OFFSET);
		
		for (int i = 0; i < 5; i++)
			if (i < placing)
				getShip(i).paint(g, true);
			else
				getShip(i).paint(g, false);
	}
	
}
