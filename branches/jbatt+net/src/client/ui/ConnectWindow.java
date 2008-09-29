/**
 * @author Alex Peterson
 * @version 2008SE22
 */

package client.ui;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.Observable;
import java.util.Observer;

import javax.swing.*;

import comm.ObservableParent;

/**
 * A JLayeredPane for connecting the client to the server.
 */
public class ConnectWindow extends JFrame implements ObservableParent {
	
	/* Constants */
	
	private static final long serialVersionUID = -2008240741719453504L;
	
	/* END Constants */
	
	
	/* Fields */
	
	private LocalCommunicator comm;
	
		/* Components */
		
		private JTextField txtHost, txtUsername;
		private JButton cmdConnect, cmdQuit;
		
		/* END Components */
	
	/* END Fields */
	
	
	/* Constructors */
	
	/**
	 * Constructs a new ConnectPanel.
	 */
	public ConnectWindow () {
		comm = new LocalCommunicator();
		
		JPanel content = new JPanel(new GridBagLayout());
		
		setContentPane(content);
		GridBagConstraints c = new GridBagConstraints();
		
		c.gridx = 0;
		c.gridy = 0;
		content.add(new JLabel("Hostname:"), c);
		
		c.gridx = 1;
		content.add(txtHost = new JTextField("localhost"), c);
		
		c.gridx = 0;
		c.gridy = 1;
		content.add(new JLabel("Username:"), c);
		
		c.gridx = 1;
		content.add(txtUsername = new JTextField(10), c);
		
		c.gridx = 0;
		c.gridy = 2;
		cmdConnect = new JButton("Connect");
		cmdConnect.setMnemonic(KeyEvent.VK_C);
		cmdConnect.addActionListener(comm);
		content.add(cmdConnect, c);
		
		c.gridx = 1;
		cmdQuit = new JButton("Quit");
		cmdQuit.setMnemonic(KeyEvent.VK_Q);
		cmdQuit.addActionListener(comm);
		content.add(cmdQuit, c);
		
		setPreferredSize(new Dimension(400, 400));
		//setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		pack();
		//setVisible(true);
		//repaint();
	}
	
	/* END Constructors */
	
	
	/* Accessors */
	
	/**
	 * Returns the observable part of this ConnectPanel.
	 * @return an obervable object
	 *//*
	public Observable getObservable () {
		return comm;
	}*/
	
	/**
	 * Returns the hostname as a String.
	 * @return the hostname
	 */
	public String getHostname () {
		return txtHost.getText();
	}
	
	/**
	 * Returns the username as a String.
	 * @return the username
	 */
	public String getUsername () {
		return txtUsername.getText();
	}
	
	/* END Accessors */
	
	public void addObserver (Observer o) {
		comm.addObserver(o);
	}
	
	/* LocalCommunicator Class */
	
	private class LocalCommunicator extends Observable implements ActionListener {
		
		/* ActionListener Methods */
		
		public void actionPerformed (ActionEvent e) {
			if (e.getSource() == cmdConnect) { 
				setChanged();
				notifyObservers("connect");
			} else if (e.getSource() == cmdQuit) {
				System.out.println("notifying dispose");
				System.out.println(countObservers());
				setChanged();
				notifyObservers("dispose");
			}
		}
		
		/* END ActionListener Methods */
		
	}
	
	/* END LocalCommunicator Class */
	
}
