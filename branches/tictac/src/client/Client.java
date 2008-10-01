package client;

import game.net.action.FiredShot;
import game.net.outcome.Hit;
import game.net.outcome.Miss;
import game.net.outcome.Outcome;
import game.net.request.Request;
import game.net.request.Request.RequestType;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JFrame;
import javax.swing.JLayeredPane;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import sun.net.www.content.audio.wav;

import com.sun.org.apache.regexp.internal.recompile;

import client.ui.ConnectPanel;
import client.ui.DeployPanel;
import client.ui.GamePanel;
import client.ui.WaitDialogue;

public class Client extends JFrame implements Observer {
	
	public static void main (String [] args) {
		new Client();
	}
	
	private WaitDialogue wait;
	private Socket socket;
	private JLayeredPane contentPane;
	private LocalListener listener;
	
	public Client () {
		listener = new LocalListener();
		buildWindow();
	}
	
	private void buildWindow () {
		//setContentPane(contentPane = new ConnectPanel(listener));
		setContentPane(contentPane = new DeployPanel());
		setPreferredSize(new Dimension(400, 400));
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		pack();
		setVisible(true);
		contentPane.repaint();
	}
	
	private void doWait () {
		if (wait != null)
			wait.dispose();
		wait = new WaitDialogue (this);
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
			((GamePanel)contentPane).getOpponentBoard().update(o);
		}
		JOptionPane.showConfirmDialog(this, o.getOppMessage());
		doWait();
	}
	
	private void doUpdateSelf (FiredShot f) {
		unwait();
		Outcome o = ((GamePanel)contentPane).getMyBoard().fire(f.getX(), f.getY());
		//TODO print result
		JOptionPane.showConfirmDialog(this, o.getSelfMessage());
		doWait();
	}
	
	/*private void doConfirmation (String message) {
		if (wait != null)
			wait.dispose();
		JOptionPane.showConfirmDialog(this, message);
	}*/
	
	private void connect (String url) {
		try {
			socket = new Socket(url, 1234);
			(new Thread(new NetworkMonitor(this, socket))).start();
			System.out.println("Client: Connected.");
		} catch (UnknownHostException e) {
			System.err.println("Client: Cannot find host.");
			JOptionPane.showMessageDialog(this, "Cannot find host.", "jbatt client", JOptionPane.ERROR_MESSAGE);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private class LocalListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if (e.getActionCommand().equals("Connect")) {
				if (contentPane instanceof ConnectPanel)
					connect(((ConnectPanel)contentPane).getHostname());
			} else if (e.getActionCommand().equals("Quit")) {
				System.exit(0);
			}
		}
	}

	public void update(Observable o, Object arg) {
		// TODO recieved net object
		if (o instanceof NetworkMonitor) {
			if (arg instanceof Request) {
				Request r = (Request)arg;
				if (r.getType() == RequestType.MOVE_REQUEST)
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
	
}
