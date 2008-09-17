package game.layout;

import graphics.AlphaFilter;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.image.ConvolveOp;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageConsumer;
import java.awt.image.ImageFilter;
import java.awt.image.ImageProducer;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Vector;

import sun.awt.image.ToolkitImage;

public class Ship implements Serializable {
	
	private static Vector<Image> horizImages;
	private static Vector<Image> horizImagesDim;
	private static Vector<Image> vertImages;
	private static Vector<Image> vertImagesDim;
	
	static {
		horizImages = new Vector<Image>();
		horizImagesDim = new Vector<Image>();
		vertImages = new Vector<Image>();
		vertImagesDim = new Vector<Image>();
		
		//Preload images
		Toolkit tk = Toolkit.getDefaultToolkit();
		
		horizImages.add(tk.getImage("res/2h.png"));
		horizImages.add(tk.getImage("res/3h.png"));
		horizImages.add(tk.getImage("res/4h.png"));
		horizImages.add(tk.getImage("res/5h.png"));
		vertImages.add(tk.getImage("res/2v.png"));
		vertImages.add(tk.getImage("res/3v.png"));
		vertImages.add(tk.getImage("res/4v.png"));
		vertImages.add(tk.getImage("res/5v.png"));
		
		//Create and preload dimmed images
		AlphaFilter filter = new AlphaFilter(135);
		for (int i = 0; i < 4; i++) {
			horizImagesDim.add(tk.createImage(new FilteredImageSource(horizImages.get(i).getSource(), filter)));
			vertImagesDim.add(tk.createImage(new FilteredImageSource(vertImages.get(i).getSource(), filter)));
		}
	}
	
	public enum Orientation implements Serializable {
		RIGHT,
		DOWN
	}
	
	private final int length;
	private int x, y;
	private Orientation direction;
	
	private final GameBoard owner;
	
	private boolean isFinal;
	private Point [] points;
	private boolean sunk;
	private boolean [] hits;
	
	public Ship (int length, GameBoard owner) {
		this.length = length;
		this.owner = owner;
		
		x = y = -1;
		direction = null;
		sunk = false;
		isFinal = false;
	}	
	
	public boolean makeFinal () {
		if (!isPlaced())
			return false;
		hits = new boolean[length];
		for (int i = 0; i < length; i++)
			hits[i] = false;
		points = new Point[length];
		if (direction == Orientation.RIGHT)
			for (int i = 0; i < length; i++)
				points[i] = new Point(x + i, y);
		else //Orientation.DOWN
			for (int i = 0; i < length; i++)
				points[i] = new Point(x, y + i);
		return isFinal = true;
	}
	
	public int getX () {
		return x;
	}
	
	public int getY () {
		return y;
	}
	
	public Orientation getOrientation () {
		return direction;
	}
	
	public int getLength () {
		return length;
	}
	
	public boolean isSunk () {
		return sunk;
	}
	
	public boolean isSunk (int hit) {
		hits[hit] = true;
		for (int i = 0; i < length; i++)
			if (hits[i] == false)
				return false;
		return sunk = true;
	}
	
	public Point getPoint (int i) {
		if (!isFinal)
			return null;
		return points[i];
	}
	
	public void setOrientation (Orientation direction) {
		if (!isFinal) {
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
	}
	
	public boolean move (int x, int y) {
		if (isFinal)
			return false;
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
		if (isFinal)
			return false;
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
		if (!isFinal)
			setOrientation(direction == Orientation.RIGHT ?
				Orientation.DOWN :
				Orientation.RIGHT);
	}
	
	public boolean isFinal () {
		return isFinal;
	}
	
	public boolean isPlaced () {
		return direction != null;
	}
	
	private boolean inBounds (int x, int y) {
		return ((direction == Orientation.RIGHT &&
				x + length - 1 < GameBoard.WIDTH &&
				y < GameBoard.HEIGHT) ||
			(direction == Orientation.DOWN &&
				y + length - 1 < GameBoard.HEIGHT &&
				x < GameBoard.WIDTH)) &&
				x >= 0 && y >= 0;
	}
	
	public void paint (Graphics g) {
		paint(g, false);
	}
	
	public void paint (Graphics g, boolean dimmed) {
		System.out.println("painting ship of length: " + length + ", dimmed: " + dimmed);
		if (!dimmed) {
			if (direction == Orientation.RIGHT)
				g.drawImage(horizImages.get(length - 2),
						x * GameBoard.GRID_SIZE + owner.LEFT_OFFSET,
						y * GameBoard.GRID_SIZE + owner.TOP_OFFSET,
						null);
			else //Orientation.DOWN)
				g.drawImage(vertImages.get(length - 2),
						x * GameBoard.GRID_SIZE + owner.LEFT_OFFSET,
						y * GameBoard.GRID_SIZE + owner.TOP_OFFSET,
						null);
		} else {
			if (direction == Orientation.RIGHT)
				g.drawImage(horizImagesDim.get(length - 2),
						x * GameBoard.GRID_SIZE + owner.LEFT_OFFSET,
						y * GameBoard.GRID_SIZE + owner.TOP_OFFSET,
						null);
			else //Orientation.DOWN)
				g.drawImage(vertImagesDim.get(length - 2),
						x * GameBoard.GRID_SIZE + owner.LEFT_OFFSET,
						y * GameBoard.GRID_SIZE + owner.TOP_OFFSET,
						null);
		}
		
		/*if (direction == Orientation.RIGHT) {
			for (int i = 0; i < length; i++)
				g.fillArc((x + i) * GameBoard.GRID_SIZE + owner.LEFT_OFFSET,
						y * GameBoard.GRID_SIZE + owner.TOP_OFFSET,
						GameBoard.GRID_SIZE, GameBoard.GRID_SIZE, 0, 360);
		} else if (direction == Orientation.DOWN) {
			for (int i = 0; i < length; i++)
				g.fillArc(x * GameBoard.GRID_SIZE + owner.LEFT_OFFSET,
						(y + i) * GameBoard.GRID_SIZE + owner.TOP_OFFSET,
						GameBoard.GRID_SIZE, GameBoard.GRID_SIZE, 0, 360);
		}*/ 
	}
	
	public static void paintGhost (Graphics g, boolean dimmed, int left, int top, int length, Orientation direction) {
		if (!dimmed) {
			if (direction == Orientation.RIGHT)
				g.drawImage(horizImages.get(length - 2),
						left, top, null);
			else //Orientation.DOWN)
				g.drawImage(vertImages.get(length - 2),
						left, top, null);
		} else {
			if (direction == Orientation.RIGHT)
				g.drawImage(horizImagesDim.get(length - 2),
						left, top, null);
			else //Orientation.DOWN)
				g.drawImage(vertImagesDim.get(length - 2),
						left, top, null);
		}
		
		/*if (direction == Orientation.RIGHT) {
			for (int i = 0; i < length; i++)
				g.fillArc(i * GameBoard.GRID_SIZE + left, top,
						GameBoard.GRID_SIZE, GameBoard.GRID_SIZE, 0, 360);
		} else if (direction == Orientation.DOWN) {
			for (int i = 0; i < length; i++)
				g.fillArc(left, i * GameBoard.GRID_SIZE + top,
						GameBoard.GRID_SIZE, GameBoard.GRID_SIZE, 0, 360);
		}*/ 
	}
	
}
