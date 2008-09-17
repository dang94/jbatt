package client.ui;

import game.layout.GameBoard;
import game.layout.Ship;
import game.layout.Ship.Orientation;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JPanel;

public class GamePanel extends JPanel implements KeyListener {

	private GameBoard myBoard, opponentBoard;
	
	public GamePanel (GameBoard myboard) {
		addKeyListener(this);
		setFocusable(true);
		
	}
	
	@Override
	public void paint(Graphics g) {
		g.clearRect(0, 0, getWidth(), getHeight());
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, getWidth(), getHeight());
		
		int count = 0;
		
		myBoard.paint(g);
	}

	public void keyPressed(KeyEvent e) {
		repaint();
	}

	public void keyReleased(KeyEvent e) {}

	public void keyTyped(KeyEvent e) {}
	
}
