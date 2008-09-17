package server;

import java.awt.Dimension;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Observable;
import java.util.Observer;
import java.util.Vector;

import javax.swing.JFrame;

import server.ui.ServerPanel;


public class Server extends JFrame implements Observer {
	
	private ServerPanel contentPane;
	private GameMaster gm;
	private Vector<ClientStruct> players;
	
	public static void main (String [] args) {
		System.out.println("Server: Starting server.");
		new Server();
	}
	
	public Server () {
		System.out.println("Server: Creating window.");
		buildWindow();
		players = new Vector<ClientStruct>();
		gm = new GameMaster(players);
		gm.addObserver(this);
		(new Thread(gm)).start();
		System.out.println("Server: Creating connection listener.");
		ConnectionListener cl = new ConnectionListener();
		cl.addObserver(this);
		(new Thread(cl)).start();
		
	}
	
	private void buildWindow () {
		setContentPane(contentPane = new ServerPanel());
		setPreferredSize(new Dimension(200, 200));
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		pack();
		setVisible(true);
	}
	
	public void update(Observable o, Object arg) {
		if (arg instanceof Socket) {
			System.out.println("Server: Got connection.");
			gm.addPlayer(new ClientStruct((Socket)arg));
		} else if (o instanceof GameMaster) {
			contentPane.refreshPlayers((Vector<ClientStruct>)players.clone());
		}
	}
	
}
