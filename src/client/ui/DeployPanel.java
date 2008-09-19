package client.ui;

import game.layout.GameBoard;
import game.layout.Ship;
import game.layout.GameBoard.BoardButton;
import game.layout.Ship.Orientation;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JLayeredPane;
import javax.swing.JPanel;

public class DeployPanel extends JLayeredPane {
	
	private GameBoard board;
	private int placing;
	
	public DeployPanel () {
		LocalListener listener = new LocalListener();
		addKeyListener(listener);
		setFocusable(true);
		
		int [] shipLengths = {2, 3, 3, 4, 5};
		board = new GameBoard(130, 100, shipLengths, this);
		board.placeButtons(this, listener);
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
	
	private class LocalListener implements KeyListener, ActionListener, MouseListener {
		
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
					break;
			}
			repaint();
		}
	
		public void keyReleased(KeyEvent e) {}
	
		public void keyTyped(KeyEvent e) {}

		public void actionPerformed(ActionEvent e) {
			String cmd = e.getActionCommand();
			System.out.println(cmd);
			int comma;
			if ((comma = cmd.indexOf(",")) >= 1) {
				int x = Integer.parseInt(cmd.substring(0, comma));
				int y = Integer.parseInt(cmd.substring(comma + 1));
				board.fire(x, y);
			}
		}

		public void mouseClicked(MouseEvent e) {
			int x =((BoardButton)e.getComponent()).getX();
			int y =((BoardButton)e.getComponent()).getY();
			System.out.println("click " + x + ", " + y);
			board.fire(x, y);
		}

		public void mouseEntered(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		public void mouseExited(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		public void mousePressed(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		public void mouseReleased(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}
	
	}
}
