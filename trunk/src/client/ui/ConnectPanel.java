package client.ui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class ConnectPanel extends JPanel {
	
	private ActionListener listener;
	
	private JTextField txtHost;
	private JButton cmdConnect;
	private JButton cmdQuit;
	
	public ConnectPanel (ActionListener listener) {
		this.listener = listener;
		
		setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		
		c.gridx = 0;
		c.gridy = 0;
		add(new JLabel("Hostname:"), c);
		
		c.gridx = 1;
		add(txtHost = new JTextField(20), c);
		
		c.gridx = 0;
		c.gridy = 1;
		cmdConnect = new JButton("Connect");
		cmdConnect.setActionCommand("Connect");
		cmdConnect.setMnemonic(KeyEvent.VK_C);
		cmdConnect.addActionListener(listener);
		add(cmdConnect, c);
		
		c.gridx = 1;
		cmdQuit = new JButton("Quit");
		cmdQuit.setActionCommand("Quit");
		cmdQuit.setMnemonic(KeyEvent.VK_Q);
		cmdQuit.addActionListener(listener);
		add(cmdQuit, c);
	}
	
	public String getHostname () {
		return txtHost.getText();
	}
	
}
