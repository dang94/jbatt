package game.layout;

import game.layout.Ship.Orientation;
import game.net.outcome.Hit;
import game.net.outcome.Miss;
import game.net.outcome.Outcome;
import game.net.outcome.Sunk;

import java.awt.Color;
import java.awt.Graphics;
import java.io.Serializable;
import java.util.Vector;

public class GameBoard implements Serializable {
	
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
	
	public Outcome fire (int x, int y) {
		for (int i = 0; i < ships.size(); i++)
			if (ships.get(i).isFinal())
				for (int n = 0; n < ships.get(i).getLength(); n++)
					if (ships.get(i).getPoint(n).x == x &&
							ships.get(i).getPoint(n).y == y) {
						if (ships.get(i).isSunk(n))
							return new Sunk(x, y, ships.get(i));
						else
							return new Hit(x, y);
					}
		return new Miss(x, y);
	}
	
	public boolean overlaps (int placing) {
		boolean [][] placed = new boolean[WIDTH][HEIGHT];
		for (int i = 0; i < placing; i++) {
			int x = ships.get(i).getX();
			int y = ships.get(i).getY();
			if (ships.get(i).getOrientation() == Orientation.RIGHT)
				for (int n = 0; n < ships.get(i).getLength(); n++)
					placed[x + n][y] = true;
			else //Orientation.DOWN
				for (int n = 0; n < ships.get(i).getLength(); n++)
					placed[x][y + n] = true;
		}
		
		int x = ships.get(placing).getX();
		int y = ships.get(placing).getY();
		if (ships.get(placing).getOrientation() == Orientation.RIGHT) {
			for (int n = 0; n < ships.get(placing).getLength(); n++)
				if (placed[x + n][y])
					return true;
		} else { //Orientation.DOWN
			for (int n = 0; n < ships.get(placing).getLength(); n++)
				if (placed[x][y + n])
					return true;
		}
		return false;
	}
	
	public Ship getShip (int index) {
		if (index < 0 || index >= ships.size())
			return null;
		else
			return ships.get(index);
	}
	
	public void paint (Graphics g, int placing) {
		int width = WIDTH * GRID_SIZE;
		int height = HEIGHT * GRID_SIZE;
		
		g.setColor(new Color(29, 183, 239));
		g.fillRect(LEFT_OFFSET, TOP_OFFSET, width, height);
		
		g.setColor(Color.BLACK);
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
