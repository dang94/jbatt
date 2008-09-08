package client.ui;

import game.layout.GameBoard;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class CreateGameBoardPanel extends JPanel {
	
	public static final int GRID_SIZE = 20;
	
	public CreateGameBoardPanel () {
	}
	
	@Override
	public void paint(Graphics g) {
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, getWidth(), getHeight());
		
		g.setColor(Color.BLACK);
		int width = GameBoard.WIDTH * GRID_SIZE;
		int height = GameBoard.HEIGHT * GRID_SIZE;
		for (int x = 0; x <= GameBoard.WIDTH; x++)
			g.drawLine(x * GRID_SIZE, 0, x * GRID_SIZE, height);
		for (int y = 0; y <= GameBoard.HEIGHT; y++)
			g.drawLine(0, y * GRID_SIZE, width, y * GRID_SIZE);
		
	}
}
