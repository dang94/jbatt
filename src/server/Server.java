package server;

import java.awt.Dimension;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JFrame;

import server.ui.ServerPanel;

public class Server extends JFrame {
	
	private LocalListener listener;
	private ServerPanel contentPane;
	private GameMaster gm;
	
	public static void main (String [] args) {
		System.out.println("Server: Starting server.");
		new Server();
	}
	
	public Server () {
		System.out.println("Server: Creating window.");
		listener = new LocalListener();
		buildWindow();
		gm = new GameMaster(this);
		gm.addObserver(listener);
		(new Thread(gm)).start();
		System.out.println("Server: Creating connection listener.");
		ConnectionListener cl = new ConnectionListener();
		cl.addObserver(listener);
		(new Thread(cl)).start();
		
	}
	
	private void buildWindow () {
		setContentPane(contentPane = new ServerPanel());
		setPreferredSize(new Dimension(200, 200));
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		pack();
		setVisible(true);
	}
	
	private class LocalListener implements Observer {
		
		public void update(Observable o, Object arg) {
			System.out.println("update called");
			if (arg instanceof ClientStruct) {
				System.out.println("Server: Got connection.");
				ClientStruct cs = (ClientStruct)arg;
				gm.addPlayer(cs);
				contentPane.refreshPlayers(gm.getPlayerStrings());
			} else if (o == gm) {
				System.out.println("will refresh");
				contentPane.refreshPlayers(gm.getPlayerStrings());
			}
		}
	}
}
