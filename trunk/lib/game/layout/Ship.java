package game.layout;

import graphics.AlphaFilter;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.image.ConvolveOp;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageConsumer;
import java.awt.image.ImageFilter;
import java.awt.image.ImageProducer;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Vector;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import sun.awt.image.ToolkitImage;

public class Ship implements Serializable {
	
	private static Vector<BufferedImage> [] images;
	private static Vector<BufferedImage> [] imagesDim;
	
	static {
		images = new Vector[2];
		images[Orientation.RIGHT.INDEX] = new Vector<BufferedImage>();
		images[Orientation.DOWN.INDEX] = new Vector<BufferedImage>();
		imagesDim = new Vector[2];
		imagesDim[Orientation.RIGHT.INDEX] = new Vector<BufferedImage>();
		imagesDim[Orientation.DOWN.INDEX] = new Vector<BufferedImage>();
		
		
		//Preload images
		Toolkit tk = Toolkit.getDefaultToolkit();
		
		for (int i = 2; i <= 5; i++) {
			try {
				images[Orientation.RIGHT.INDEX].add(ImageIO.read(new File("res/" + String.valueOf(i) + Orientation.RIGHT.SYMBOL + ".png")));
				images[Orientation.DOWN.INDEX].add(ImageIO.read(new File("res/" + String.valueOf(i) + Orientation.DOWN.SYMBOL + ".png")));
			} catch (IOException e) {}
		}
		
		/*for (int i = 2; i <= 5; i++) {
			horizImages.add(new ImageIcon("res/" + String.valueOf(i) + "h.png"));
			vertImages.add(tk.createImage("res/" + String.valueOf(i) + "v.png"));
		}*/
		
		//Create and preload dimmed images
		
		for (int i = 0; i < 4; i++)
			for (int n = 0; n <= 1; n++) {
				BufferedImage src = images[n].get(i);
				BufferedImage dest = new BufferedImage(src.getWidth(), src.getHeight(), BufferedImage.TYPE_INT_ARGB);
		        Graphics2D g2 = dest.createGraphics();
		        AlphaComposite ac = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f);
		        g2.setComposite(ac);
		        g2.drawImage(src, null, 0, 0);
		        g2.dispose();
		        imagesDim[n].add(dest);
			}
		
		/*AlphaFilter filter = new AlphaFilter(135);
		for (int i = 0; i < 4; i++) {
			horizImagesDim.add(tk.createImage(new FilteredImageSource(horizImages.get(i).getSource(), filter)));
			vertImagesDim.add(tk.createImage(new FilteredImageSource(vertImages.get(i).getSource(), filter)));
		}*/
	}
	
	public static enum Orientation implements Serializable {
		RIGHT(0, 'h'),
		DOWN(1, 'v');
		
		public final int INDEX;
		public final char SYMBOL;
		
		private Orientation (int index, char symbol) {
			INDEX = index;
			SYMBOL = symbol;
		}
		
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
		//TODO try cloning images to fix drawing problems
		System.out.println("painting ship of length: " + length + ", dimmed: " + dimmed);
		if (!dimmed) {
			g.drawImage(images[direction.INDEX].get(length - 2),
					x * GameBoard.GRID_SIZE + owner.LEFT_OFFSET,
					y * GameBoard.GRID_SIZE + owner.TOP_OFFSET,
				null);
		} else {
			g.drawImage(imagesDim[direction.INDEX].get(length - 2),
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
		if (!dimmed)
			g.drawImage(images[direction.INDEX].get(length - 2), left, top, null);
		else
			g.drawImage(imagesDim[direction.INDEX].get(length - 2), left, top, null);
		
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
