/**
 * @author Alex Peteron
 * @version 2008SE17
 */

package client;

import client.ui.*;
import game.layout.*;
import game.net.action.*;
import game.net.outcome.*;
import game.net.request.*;
import game.net.request.Request.*;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Observable;
import java.util.Observer;

import javax.swing.*;

import comm.ObservableParent;
import comm.Ports;

/**
 * The main client program.
 */
public class Client {
	
	/* Main Method */
	
	/**
	 * Starts the Client.
	 * @param args arguments
	 */
	public static void main (String [] args) {
		new Client();
	}
	
	/* END Main Method */
	
	
	/* Constants */
	
	private static final long serialVersionUID = 6190349844718271471L;
	
	/* END Constants */
	
	
	/* Fields */
	
	private WaitDialogue wait;
	private Socket socket;
	private Socket pulse;
	private JFrame window;
	private LocalListener listener;
	private WindowListener wl;
	private PulseGenerator pg;
	
	/* END Fields */
	
	
	/* Constructors */
	
	/**
	 * Constructs a new Client.
	 */
	public Client () {
		listener = new LocalListener();
		wl = listener;
		
		switchWindow(new ConnectWindow());
	}
	
	/* END Constructors */
	
	private void switchWindow (JFrame newWindow) {
		if (window != null)
			window.dispose();
		window = newWindow;
		((ObservableParent)window).addObserver(listener);
		window.addWindowListener(wl);
		window.setVisible(true);
		window.repaint();
	}
	
	private void buildWindow () {

		//TODO this will be removed; for testing ONLY
		/*{
			int [] shipLengths = {2, 3, 3, 4, 5};
			String [] shipNames = {"destroyer", "battleship", "battleship", "submarine", "carrier"};
			GameBoard board = new GameBoard(130, 100, shipLengths, shipNames);
			//board.placeButtons(this, listener);
			setContentPane(contentPane = new GamePanel(board));
		}*/
		

	}
	
	private void doWait () {
		if (wait != null)
			wait.dispose();
		wait = new WaitDialogue (window, "Waiting for game to start...");
	}
	
	private void doMove () {
		unwait();
		//TODO make gamepanel active (already is) and handle click as shot
	}
	
	private void unwait () {
		if (wait != null)
			wait.dispose();
	}
	
	private void doUpdateOpponent (Outcome o) {
		unwait();
		if (o instanceof Hit || o instanceof Miss) {
			((GameWindow)window).getOpponentBoard().update(o);
		}
		JOptionPane.showConfirmDialog(window, o.getOppMessage());
		doWait();
	}
	
	private void doUpdateSelf (FiredShot f) {
		unwait();
		Outcome o = ((GameWindow)window).getMyBoard().fire(f.getX(), f.getY());
		//TODO print result
		JOptionPane.showConfirmDialog(window, o.getSelfMessage());
		doWait();
	}
	
	/*private void doConfirmation (String message) {
		if (wait != null)
			wait.dispose();
		JOptionPane.showConfirmDialog(this, message);
	}*/
	
	private void connect (String url, String username) {
		try {
			socket = new Socket(url, Ports.COMM_PORT);
			pulse = new Socket(url, Ports.PULSE_PORT);
			pg = new PulseGenerator();
			(new Thread(pg)).start();
			//(new ObjectOutputStream(socket.getOutputStream())).writeObject(username);
			(new Thread(new NetworkMonitor(socket, listener))).start();
			System.out.println("Client: Connected.");
		} catch (UnknownHostException e) {
			System.err.println("Client: Cannot find host.");
			JOptionPane.showMessageDialog(window, "Cannot find host.", "jbatt client", JOptionPane.ERROR_MESSAGE);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/* PulseGenerator Class */
	
	private class PulseGenerator implements Runnable {
		
		private boolean done;
		
		public PulseGenerator () {
			done = false;
		}
		
		public void stop () {
			done = true;
		}
		
		public void run () {
			try {
				DataOutputStream dos = new DataOutputStream(pulse.getOutputStream());
				while (!done) {
					try {
						dos.writeByte(1);
						Thread.sleep(100);
					} catch (InterruptedException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	/* END PulseGenerator Class */
	
	
	/* LocalListener Class */
	
	private class LocalListener implements Observer, WindowListener {
		
		/* Observer Methods */
		
		@Override
		public void update (Observable o, Object arg) {
			System.out.println("contentpane " + window.getClass().getName());
			if (window instanceof ConnectWindow) {
				System.out.println("connect arg " + arg);
				if ("connect".equals(arg)) {
					ConnectWindow cp = (ConnectWindow)window; 
					connect(cp.getHostname(), cp.getUsername());
					switchWindow(new DeployWindow());
				} else if ("dispose".equals(arg)) {
					System.out.println("disposing");
					System.exit(0);
				}
			} else if (window instanceof DeployWindow) {
				if (arg instanceof GameBoard)
					try {
						GameBoard board = (GameBoard)arg;
						(new ObjectOutputStream(socket.getOutputStream())).writeObject(board);
						switchWindow(new GameWindow(board));
					} catch (IOException e) {
						e.printStackTrace();
					}
			}
			
			// TODO recieved net object
			if (o instanceof NetworkMonitor) {
				if (arg instanceof Request) {
					Request r = (Request)arg;
					if (r.getType() == RequestType.GAMEBOARD_REQUEST) {
						unwait();
					} else if (r.getType() == RequestType.MOVE_REQUEST)
						doMove();
					/*else if (r.getType() == RequestType.CONFIRMATION_REQUEST)
						doConfirmation();*/
				} else if (arg instanceof Outcome) {
					doUpdateOpponent((Outcome)arg);
				} else if (arg instanceof FiredShot) {
					doUpdateSelf((FiredShot)arg);
				}
			}
		}
		
		/* END Observer Methods */
		
		
		/* WindowListener Methods */
		
		public void windowClosing (WindowEvent e) {
			System.out.println("closing");	
			try {
				if (socket != null) {
					//socket.getInputStream().close();
					//socket.getOutputStream().close();
					socket.close();
					pg.stop();
					System.exit(0);
				}
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		
		public void windowActivated (WindowEvent e) {}

		public void windowClosed (WindowEvent e) {}

		public void windowDeactivated (WindowEvent e) {}

		public void windowDeiconified (WindowEvent e) {}

		public void windowIconified (WindowEvent e) {}

		public void windowOpened (WindowEvent e) {}
		
		/* END WindowListener Methods */
		
	}
	/* LocalListener class */

	
	
}
