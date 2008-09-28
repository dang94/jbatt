/**
 * @author Alex Peterson
 * @version 2008SE22
 */

package game.layout;

import game.layout.Ship.*;
import game.net.outcome.*;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseListener;
import java.io.Serializable;
import java.util.Vector;

import javax.swing.AbstractButton;
import javax.swing.JLayeredPane;

/**
 * A gameboard in battleship.  This keeps track of
 * hits and misses and positions and statuses of ships.
 */
public class GameBoard implements Serializable {
	
	/* Constants */
	
	private static final long serialVersionUID = 8129433227461288450L;

	/**
	 * The size (height and width) of a grid square.
	 */
	public static final int GRID_SIZE = 20;
	
	/**
	 * The width of the gameboard in grid squares.
	 */
	public static final int WIDTH = 10;
	/**
	 * The height of the gameboard in grid squares.
	 */
	public static final int HEIGHT = 10;
	
	/**
	 * The color to paint a hit on the gameboard.
	 */
	public static final Color HIT_COLOUR = Color.RED;
	/**
	 * The color to paint a miss on the gameboard.
	 */
	public static final Color MISS_COLOUR = Color.WHITE;
	
	/**
	 * The position of the left-most side of the gameboard.
	 */
	public final int LEFT_OFFSET;
	/**
	 * The position of the top-most side of the gameboard.
	 */
	public final int TOP_OFFSET;
	
	/* END Constants */
	
	
	/* Enumerators */
	
	/**
	 * Represents the different states an element of the hit board can be in.
	 */
	public static enum ShotBoard {
		/**
		 * A state where this location has been fired upon and hit a ship.
		 */
		HIT,
		/**
		 * A state where this location has been fired upon and the shot missed. 
		 */
		MISS,
		/**
		 * A state where a shot has never been fired upon this location.
		 */
		NO_SHOT
	}
	
	/* END Enumerators */
	
	
	/* Fields */
	
	private Vector<Ship> ships;
	private ShotBoard [][] hitBoard;
	
	/* END Fields */
	
	
	/* Constructors */
	
	/**
	 * Constructs a GameBoard from a previous GameBoard
	 * but moves the board to a new position.
	 * @param board the old GameBoard
	 * @param left the new left offset
	 * @param top the new top offset
	 */
	public GameBoard (GameBoard board, int left, int top) {
		ships = board.ships;
		hitBoard = board.hitBoard;
		LEFT_OFFSET = left;
		TOP_OFFSET = top;
	}
	
	/**
	 * Constructs a new GameBoard with ships and specified left and top offsets.
	 * @param left the left offset
	 * @param top the top offset
	 * @param shipLengths an array which holds the lengths of the ships to create
	 * @param shipNames an array which holds the names of the ships to create
	 */
	public GameBoard (int left, int top, int [] shipLengths, String [] shipNames) {
		this(left, top);
		for (int i = 0; i < shipLengths.length; i++)
			ships.add(new Ship(shipLengths[i], shipNames[i], this));
	}
	
	/**
	 * Constructs a new GameBoard with no ships and
	 * specified left and top offsets.
	 * @param left the left offset 
	 * @param top the top offset
	 */
	public GameBoard (int left, int top) {
		LEFT_OFFSET = left;
		TOP_OFFSET = top;
		hitBoard = new ShotBoard[WIDTH][HEIGHT];
		for (int y = 0; y < HEIGHT; y++)
			for (int x = 0; x < WIDTH; x++)
				hitBoard[x][y] = ShotBoard.NO_SHOT;
		ships = new Vector<Ship>();
	}
	
	/* END Constructors */
	
	
	/* Accessors */
	
	/**
	 * Returns the ship with a specified index.
	 * @param index the index of the ship to return
	 * @return the ship with the specified index
	 */
	public Ship getShip (int index) {
		if (index < 0 || index >= ships.size())
			return null;
		return ships.get(index);
	}
	
	/* END Accessors */
	
	
	/* Methods */
	
	/**
	 * Checks if this GameBoard still contains ships
	 * which have not been sunk and returns true if
	 * any are found.
	 * @return true if the player is still alive
	 */
	public boolean isAlive () {
		for (int i = 0; i < ships.size(); i++)
			if (!ships.get(i).isSunk())
				return true;
		return false;
	}
	
	/**
	 * Updates the GameBoard with a specified Outcome. 
	 * @param o the outcome to update the board with
	 */
	public void update (Outcome o) {
		if (o instanceof Hit || o instanceof Sunk)
			hitBoard[o.getX()][o.getY()] = ShotBoard.HIT;
		else if (o instanceof Miss)
			hitBoard[o.getX()][o.getY()] = ShotBoard.MISS;
	}
	
	/**
	 * Fires a shot at position (x, y) and returns
	 * the outcome of that shot.
	 * @param x the x coordinate
	 * @param y the y coordinate
	 * @return the Outcome of the shot
	 */
	public Outcome fire (int x, int y) {
		for (int i = 0; i < ships.size(); i++)
			if (ships.get(i).isFinal())
				for (int n = 0; n < ships.get(i).getLength(); n++)
					if (ships.get(i).getPoint(n).x == x &&
							ships.get(i).getPoint(n).y == y) {
						hitBoard[x][y] = ShotBoard.HIT;
						System.out.println("hitboard " + hitBoard[x][y]);
						if (ships.get(i).isSunk(n))
							return new Sunk(x, y, ships.get(i));
						return new Hit(x, y);
					}
		hitBoard[x][y] = ShotBoard.MISS;
		System.out.println("hitboard " + hitBoard[x][y]);
		return new Miss(x, y);
	}
	
	/**
	 * Checks to see if the ship with index placing is
	 * overlapping any other ships currently on the
	 * board and returns true if it is.
	 * @param placing the index of the ship to check the placement of
	 * @return true if the ship with index placing overlaps any other ship
	 */
	public boolean overlaps (int placing) {
		boolean [][] placed = new boolean[WIDTH][HEIGHT];
		for (int i = 0; i < placing && i < ships.size(); i++) {
			int x = ships.get(i).getX();
			int y = ships.get(i).getY();
			if (ships.get(i).getOrientation() == Orientation.RIGHT)
				for (int n = 0; n < ships.get(i).getLength(); n++)
					placed[x + n][y] = true;
			else //Orientation.DOWN
				for (int n = 0; n < ships.get(i).getLength(); n++)
					placed[x][y + n] = true;
		}
		
		if (placing >= 0 && placing < ships.size()) {
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
		}
		return false;
	}
	
	/**
	 * Places grid buttons on the specified panel and
	 * adds a MouseListener to those buttons.
	 * @param panel the panel to add the grid buttons to
	 * @param listener the MouseListener to add to the buttonss
	 */
	public void placeButtons (JLayeredPane panel, MouseListener listener) {
		int count = 1;
		for (int y = 0; y < HEIGHT; y++)
			for (int x = 0; x < WIDTH; x++) {
				AbstractButton button = new BoardButton(x, y);
				button.addMouseListener(listener);
				panel.add(button, new Integer(count++));
				button.setBounds(x * GRID_SIZE + LEFT_OFFSET,
						y * GRID_SIZE + TOP_OFFSET,
						GRID_SIZE, GRID_SIZE);
				button.repaint();
				}
	}

	
	
	/**
	 * Paints the game board on the specified Graphics object.
	 * @param g the Graphics object with which to paint the GameBoard
	 */
	public void paint (Graphics g) {
		paint(g, -1);
	}
	
	/**
	 * Paints the game board on the specified Graphics object and
	 * dims the images of any ships withan index less than placing.
	 * @param g the Graphics object with which to paint the GameBoard
	 * @param placing any ships with an index less than this value will be painted dimmed
	 */
	
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
		
		for (int i = 0; i < ships.size(); i++)
			if (getShip(i).isPlaced()) {
				if (i < placing)
					getShip(i).paint(g, true);
				else
					getShip(i).paint(g, false);
			}
		
		for (int y = 0; y < HEIGHT; y++)
			for (int x = 0; x < WIDTH; x++) {
				if (hitBoard[x][y] == ShotBoard.HIT || hitBoard[x][y] == ShotBoard.MISS) {
					if (hitBoard[x][y] == ShotBoard.HIT)
						g.setColor(HIT_COLOUR);
					else if (hitBoard[x][y] == ShotBoard.MISS)
						g.setColor(MISS_COLOUR);
					g.fillArc(x * GRID_SIZE + LEFT_OFFSET + 5, y * GRID_SIZE + TOP_OFFSET + 5, GRID_SIZE - 10, GRID_SIZE - 10, 0, 360);
				}
			}
	}
	
	/* END Methods */
	
	
	/* BoardButton Class */
	
	
	/* END Methods */
	
	
	/* BoardButton Class */
	
	/**
	 * An AbstractButton which also stores information about
	 * where it is located (with (x, y) coordinates) on a GameBoard.
	 */
	public class BoardButton extends AbstractButton {
		
		/* Constants */
		
		private static final long serialVersionUID = 7287814718002806990L;
		
		/* END Constants */
		
		/* Fields */
		
		private int x;
		private int y;
		
		/* END Fields */
		
		
		/* Constructors */
		
		/**
		 * Constructs a BoardButton with a specified x and y value.
		 * @param x the x value
		 * @param y the y value
		 */
		public BoardButton (int x, int y) {
			this.x = x;
			this.y = y;
			setFocusable(false);
		}
		
		/* END Constructors */
		
		
		/* Accessors */
		
		/**
		 * Gets the x value.
		 * @return the x value
		 */
		public int getX () {
			return x;
		}
		
		/**
		 * Gets the y value.
		 * @return the y value
		 */
		public int getY () {
			return y;
		}
		
		/* END Accessors */
		
	}

	
	/* END BoardButton Class */
}
