package server.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.Vector;

import javax.swing.JList;
import javax.swing.JPanel;

import server.ClientStruct;

public class ServerPanel extends JPanel {
	
	private JList lstClients;
	
	public ServerPanel () {
		setLayout(new BorderLayout());
		add(lstClients = new JList(), BorderLayout.CENTER);
	}
	
	public void refreshPlayers (Vector<String> list) {
		lstClients.setListData(list);
	}
	
}
