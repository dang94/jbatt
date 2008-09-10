package game.layout;

import java.awt.Color;
import java.awt.Graphics;

public class Ship {
	
	public enum Orientation {
		RIGHT,
		DOWN
	}
	
	private final int length;
	private int x, y;
	private Orientation direction;
	
	private final GameBoard owner;
	
	private boolean sunk;
	private boolean [] hits;
	
	public Ship (int length, GameBoard owner) {
		this.length = length;
		this.owner = owner;
		
		x = y = -1;
		direction = null;
		sunk = false;
		hits = new boolean[length];
		for (int i = 0; i < length; i++)
			hits[i] = false;
	}
	
	public int getLength () {
		return length;
	}
	
	public void setOrientation (Orientation direction) {
		if (direction == null)
			direction = Orientation.RIGHT;
		if (this.direction != direction) {
			if (direction == Orientation.RIGHT) {
				this.direction = direction;
				while (!inBounds(x, y))
					x--;
			} else if (direction == Orientation.DOWN) {
				this.direction = direction;
				while (!inBounds(x, y))
					y--;
			} else if (direction == null) {
				this.direction = null;
			}
		}
	}
	
	public boolean move (int x, int y) {
		if (direction == null)
			direction = Orientation.RIGHT;
		if (inBounds(x, y)) {
			this.x = x;
			this.y = y;
			return true;
		} else
			return false;
	}
	
	public boolean translate (int dx, int dy) {
		if (isPlaced() &&
				inBounds(x + dx, y + dy)) {
			x += dx;
			y += dy;
			return true;
		} else if (!isPlaced()) {
			move(0, 0);
			return false;
			//TODO should be true or false?
		} else
			return false;
	}
	
	public void rotate () {
		setOrientation(direction == Orientation.RIGHT ?
				Orientation.DOWN :
				Orientation.RIGHT);
	}
	
	public boolean isPlaced () {
		return direction != null;
	}
	
	private boolean inBounds (int x, int y) {
		return (direction == Orientation.RIGHT &&
				x + length - 1 < GameBoard.WIDTH &&
				y < GameBoard.HEIGHT) ||
			(direction == Orientation.DOWN &&
				y + length - 1 < GameBoard.HEIGHT &&
				x < GameBoard.WIDTH);
	}
	
	public void paint (Graphics g) {
		paint(g, false);
	}
	
	public void paint (Graphics g, boolean dimmed) {
		if (!dimmed)
			g.setColor(Color.BLACK);
		else
			g.setColor(Color.GRAY);
		if (direction == Orientation.RIGHT) {
			for (int i = 0; i < length; i++)
				g.fillArc((x + i) * GameBoard.GRID_SIZE + owner.LEFT_OFFSET,
						y * GameBoard.GRID_SIZE + owner.TOP_OFFSET,
						GameBoard.GRID_SIZE, GameBoard.GRID_SIZE, 0, 360);
		} else if (direction == Orientation.DOWN) {
			for (int i = 0; i < length; i++)
				g.fillArc(x * GameBoard.GRID_SIZE + owner.LEFT_OFFSET,
						(y + i) * GameBoard.GRID_SIZE + owner.TOP_OFFSET,
						GameBoard.GRID_SIZE, GameBoard.GRID_SIZE, 0, 360);
		} 
	}
	
	public static void paintGhost (Graphics g, boolean dimmed, int left, int top, int length, Orientation direction) {
		if (!dimmed)
			g.setColor(Color.BLACK);
		else
			g.setColor(Color.GRAY);
		if (direction == Orientation.RIGHT) {
			for (int i = 0; i < length; i++)
				g.fillArc(i * GameBoard.GRID_SIZE + left, top,
						GameBoard.GRID_SIZE, GameBoard.GRID_SIZE, 0, 360);
		} else if (direction == Orientation.DOWN) {
			for (int i = 0; i < length; i++)
				g.fillArc(left, i * GameBoard.GRID_SIZE + top,
						GameBoard.GRID_SIZE, GameBoard.GRID_SIZE, 0, 360);
		} 
	}
	
}
