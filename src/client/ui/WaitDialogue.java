package client.ui;

import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class WaitDialogue extends JDialog {
	
	public WaitDialogue (JFrame owner) {
		this(owner, "Please wait...");
	}
	
	public WaitDialogue (JFrame owner, String message) {
		super(owner, true);
		setLayout(new FlowLayout());
		add(new JLabel(message));
		setPreferredSize(new Dimension(100, 100));
		pack();
		setVisible(true);
	}
	
}
