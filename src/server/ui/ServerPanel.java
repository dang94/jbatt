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
	
	public void refreshPlayers (Vector<ClientStruct> list) {
		Vector<String> strings = new Vector<String>();
		for (int i = 0; i < list.size(); i++) {
			ClientStruct s = list.get(i);
			strings.add(s.getURL() + '\t' + s.getStatus().toString());
		}
		System.out.println("strings " + strings.get(0));
		lstClients.setListData(strings);
	}
	
}
