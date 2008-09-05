package server.test;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;

public class ServerWindow extends JFrame {
	
	private JList lstClients;
	
	public ServerWindow () {
		JPanel content = new JPanel(new BorderLayout());
		content.add(lstClients = new JList(), BorderLayout.CENTER);
		setContentPane(content);
		setPreferredSize(new Dimension(200, 200));
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		pack();
		setVisible(true);
	}
	
	void updatePlayerList (Vector<ClientStruct> list) {
		lstClients.setListData(structToString(list));
	}
	
	private synchronized Vector<String> structToString (Vector<ClientStruct> structs) {
		Vector<String> strings = new Vector<String>();
		for (int i = 0; i < structs.size(); i++) {
			ClientStruct s = structs.get(i);
			strings.add(s.getURL() + '\t' + s.getStatus().toString());
		}
		System.out.println("strings " + strings.get(0));
		return strings;
	}
}
