package client.ui;

import game.layout.*;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowListener;
import java.awt.event.WindowStateListener;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JFrame;
import javax.swing.JLayeredPane;

import comm.ObservableParent;

public class GameWindow extends JFrame implements ObservableParent {
	
	private static final int MY_BOARD_LEFT = 30;
	private static final int MY_BOARD_TOP = 100;
	private static final int OPP_BOARD_LEFT = 240;
	private static final int OPP_BOARD_TOP = 100;
	
	private LocalCommunicator comm;
	
	private GameBoard myBoard, opponentBoard;
	
	public GameWindow (GameBoard myBoard) {
		addKeyListener(comm);
		
		setFocusable(true);
		this.myBoard = new GameBoard(myBoard, MY_BOARD_LEFT, MY_BOARD_TOP);
		opponentBoard = new GameBoard(OPP_BOARD_LEFT, OPP_BOARD_TOP);
		
	}
	
	/* Accessors */
	
	public GameBoard getMyBoard () {
		return myBoard;
	}
	
	
	public GameBoard getOpponentBoard () {
		return opponentBoard;
	}
	
	/* END Accessors */
	
	public void addObserver (Observer o) {
		comm.addObserver(o);
	}
	
	@Override
	public void paint (Graphics g) {
		g.clearRect(0, 0, getWidth(), getHeight());
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, getWidth(), getHeight());
		
		myBoard.paint(g);
		opponentBoard.paint(g);
		
	}
	
	/* LocalCommunicator Class */
	
	private class LocalCommunicator extends Observable implements KeyListener {
		
		/* KeyListener Methods */
		
		public void keyPressed(KeyEvent e) {
			repaint();
		}
	
		public void keyReleased(KeyEvent e) {}
	
		public void keyTyped(KeyEvent e) {}
		
		/* END KeyListener Methods */
		
	}
}
