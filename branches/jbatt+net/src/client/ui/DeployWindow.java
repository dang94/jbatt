package client.ui;

import comm.*;
import game.layout.*;
import game.layout.GameBoard.*;
import game.layout.Ship.*;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.*;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLayeredPane;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class DeployWindow extends JFrame implements ObservableParent {
	
	private LocalCommunicator comm;
	
	private JPanel content;
	
	private GameBoard board;
	private int placing;
	
	private JButton cmdOK, cmdClear;
	
	public DeployWindow () {
		comm = new LocalCommunicator();
		
		content = new JPanel();
		
		content.addKeyListener(comm);
		content.setFocusable(true);
		
		cmdOK = new JButton("OK");
		cmdOK.setEnabled(false);
		cmdOK.addActionListener(comm);
		content.add(cmdOK, new Integer(1));
		
		cmdClear = new JButton("Clear");
		cmdClear.addActionListener(comm);
		content.add(cmdClear, new Integer(1));
		
		setPreferredSize(new Dimension(600, 400));
		//setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		pack();
		//setVisible(true);
		//repaint();
		
		createGameBoard();
	}
	
	private void createGameBoard () {
		int [] shipLengths = {2, 3, 3, 4, 5};
		String [] shipNames = {"destroyer", "battleship", "battleship", "submarine", "carrier"};
		board = new GameBoard(130, 100, shipLengths, shipNames);
		placing = 0;
		board.getShip(placing).move(0, 0);
	}
	
	public void addObserver (Observer o) {
		comm.addObserver(o);
	}
	
	@Override
	public void paint(Graphics g) {
		g.clearRect(0, 0, getWidth(), getHeight());
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, getWidth(), getHeight());
		
		int count = 0;
		for (int i = 0; i < 5; i++)
			if (i > placing) {
				Ship.paintGhost(g, true, 10, 10 + count * (GameBoard.GRID_SIZE + 2), board.getShip(i).getLength(), Orientation.RIGHT);
				count++;
			}
		
		board.paint(g, placing);
	}
	
	
	/* LocalCommunicator Class */
	
	private class LocalCommunicator extends Observable implements KeyListener, ActionListener {
		
		/* KeyListener Methods */
		
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
					if (!board.overlaps(placing)) {
						board.getShip(placing).makeFinal();
						board.getShip(++placing).move(0, 0);
						
					}
					//break;
			}
			content.repaint();
		}
	
		public void keyReleased(KeyEvent e) {}
	
		public void keyTyped(KeyEvent e) {}
		
		/* KeyListener Methods */
		
		
		/* ActionListener Methods */
		
		public void actionPerformed (ActionEvent e) {
			if (e.getSource() == cmdOK) {
				notifyObservers(board);
			} else if (e.getSource() == cmdClear) {
				JOptionPane.showConfirmDialog(content, "Are you sure you want to clear your gameboard?");
				createGameBoard();
			}
		}
		
		/* END ActionListener Methods */
		
	}
	
	/* END LocalCommunicator Class */
	
}
