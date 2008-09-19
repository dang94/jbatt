package game.layout;

import game.layout.Ship.Orientation;
import game.net.outcome.Hit;
import game.net.outcome.Miss;
import game.net.outcome.Outcome;
import game.net.outcome.Sunk;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import java.beans.PropertyChangeListener;
import java.io.Serializable;
import java.util.Vector;

import javax.swing.AbstractButton;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;

public class GameBoard implements Serializable {
	
	public static final int GRID_SIZE = 20;
	
	public static final int WIDTH = 10;
	public static final int HEIGHT = 10;
	
	public static final Color HIT_COLOUR = Color.RED;
	public static final Color MISS_COLOUR = Color.WHITE;
	
	public final int LEFT_OFFSET;
	public final int TOP_OFFSET;
	
	public static enum ShotBoard {
		HIT,
		MISS,
		NO_SHOT
	}
	
	private Vector<Ship> ships;
	private ShotBoard [][] hitBoard;
	private final JLayeredPane buttonPanel;
	
	public GameBoard (int left, int top, int [] shipLengths, String [] shipNames, JLayeredPane buttonPanel) {
		this.buttonPanel = buttonPanel;
		LEFT_OFFSET = left;
		TOP_OFFSET = top;
		hitBoard = new ShotBoard[WIDTH][HEIGHT];
		for (int y = 0; y < HEIGHT; y++)
			for (int x = 0; x < WIDTH; x++)
				hitBoard[x][y] = ShotBoard.NO_SHOT;
		ships = new Vector<Ship>();
		for (int i = 0; i < shipLengths.length; i++)
			ships.add(new Ship(shipLengths[i], shipNames[i], this));
	}
	
	public boolean isAlive () {
		for (int i = 0; i < ships.size(); i++)
			if (!ships.get(i).isSunk())
				return true;
		return false;
	}
	
	public void update (Outcome o) {
		if (o instanceof Hit || o instanceof Sunk)
			hitBoard[o.getX()][o.getY()] = ShotBoard.HIT;
		else if (o instanceof Miss)
			hitBoard[o.getX()][o.getY()] = ShotBoard.MISS;
	}
	
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
						else
							return new Hit(x, y);
					}
		hitBoard[x][y] = ShotBoard.MISS;
		System.out.println("hitboard " + hitBoard[x][y]);
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
	
	public void placeButtons (JLayeredPane panel, MouseListener listener) {
		int count = 1;
		for (int y = 0; y < HEIGHT; y++)
			for (int x = 0; x < WIDTH; x++) {
				AbstractButton button = new BoardButton(x, y);
				//button.setActionCommand(x + "," + y);
				button.addMouseListener(listener);
				panel.add(button, new Integer(count++));
				button.setBounds(x * GRID_SIZE + LEFT_OFFSET,
						y * GRID_SIZE + TOP_OFFSET,
						GRID_SIZE, GRID_SIZE);
				button.repaint();
				}
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
			if (getShip(i).isPlaced()) {
				if (i < placing)
					getShip(i).paint(g, true);
				else
					getShip(i).paint(g, false);
			}
		
		for (int y = 0; y < HEIGHT; y++)
			for (int x = 0; x < WIDTH; x++) {
				if (hitBoard[x][y] == ShotBoard.HIT)
					g.setColor(HIT_COLOUR);
				else if (hitBoard[x][y] == ShotBoard.MISS)
					g.setColor(MISS_COLOUR);
				switch (hitBoard[x][y]) {
					case HIT: System.out.println("paint hb " + x + ", " + y + " hit"); break;
					case MISS: System.out.println("paint hb " + x + ", " + y + " miss"); break;
					case NO_SHOT: System.out.println("paint hb " + x + ", " + y + " none");
				}
				
				if (hitBoard[x][y] != ShotBoard.NO_SHOT)
					g.fillArc(width + LEFT_OFFSET + 5, height + LEFT_OFFSET + 5, GRID_SIZE - 10, GRID_SIZE - 10, 0, 360);
			}
	}
	
	public class BoardButton extends AbstractButton {
		
		private int x;
		private int y;
		
		public BoardButton (int x, int y) {
			this.x = x;
			this.y = y;
			setFocusable(false);
		}
		
		public int getX () {
			return x;
		}
		
		public int getY () {
			return y;
		}
		
	}
	
	public class BoardClickAction implements Action {
		
		private Point coordinates;
		
		public BoardClickAction (int x, int y) {
			coordinates = new Point(x, y);
		}
		
		public void addPropertyChangeListener (PropertyChangeListener listener) {}

		public Object getValue (String key) {
			if (key.equals("coordinates"))
				return coordinates;
			else
				return null;
		}

		public boolean isEnabled () {
			return true;
		}

		public void putValue (String key, Object value) {}

		public void removePropertyChangeListener (PropertyChangeListener listener) {}

		public void setEnabled (boolean b) {}

		public void actionPerformed (ActionEvent e) {}
		
	}
	
}
