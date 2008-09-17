package client.ui;

import game.layout.GameBoard;
import game.layout.Ship;
import game.layout.Ship.Orientation;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JPanel;

public class DeployPanel extends JPanel implements KeyListener {
	
	private GameBoard board;
	private int placing;
	
	public DeployPanel () {
		addKeyListener(this);
		setFocusable(true);
		
		int [] shipLengths = {2, 3, 3, 4, 5};
		board = new GameBoard(130, 100, shipLengths);
		placing = 0;
		board.getShip(placing).move(0, 0);
	}
	
	@Override
	public void paint(Graphics g) {
		g.clearRect(0, 0, getWidth(), getHeight());
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, getWidth(), getHeight());
		
		int count = 0;
		for (int i = 0; i < 5; i++)
			if (i > placing) {
				System.out.println("drew index " + i);
				Ship.paintGhost(g, true, 10, 10 + count * (board.GRID_SIZE + 2), board.getShip(i).getLength(), Orientation.RIGHT);
				count++;
			}
		
		board.paint(g, placing);
	}

	public void keyPressed(KeyEvent e) {
		int code = e.getKeyCode();
		switch (code) {
			case KeyEvent.VK_UP:
				board.getShip(placing).translate(0, -1);
				break;
			case KeyEvent.VK_DOWN:
				board.getShip(placing).translate(0, 1);
				break;
			case KeyEvent.VK_LEFT:
				board.getShip(placing).translate(-1, 0);
				break;
			case KeyEvent.VK_RIGHT:
				board.getShip(placing).translate(1, 0);
				break;
			case KeyEvent.VK_SPACE:
				board.getShip(placing).rotate();
				break;
			case KeyEvent.VK_ENTER:
				if (!board.overlaps(placing))
					board.getShip(++placing).move(0, 0);
				break;
		}
		repaint();
	}

	public void keyReleased(KeyEvent e) {}

	public void keyTyped(KeyEvent e) {}
}
