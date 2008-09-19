package client.ui;

import java.awt.FlowLayout;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class WaitDialogue extends JDialog {
	
	public WaitDialogue (JFrame owner) {
		super(owner, true);
		setLayout(new FlowLayout());
		add(new JLabel("Please wait..."));
		pack();
		setVisible(true);
	}
	
}
