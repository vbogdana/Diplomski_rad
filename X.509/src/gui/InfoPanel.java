package gui;

import javax.swing.JLabel;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public abstract class InfoPanel extends JPanel {
	// UI - unique identifiers V2+
	public static final int C = 0, S = 1, L = 2, O = 3, OU = 4, CN = 5, SA = 6, UI = 7;	
	JLabel labels[] = new JLabel [8];	

	public InfoPanel() {
		super();
		
		labels[C] = new JLabel("Country (C):");
		labels[S] = new JLabel("State (ST):");
		labels[L] = new JLabel("Locality (L):");
		labels[O] = new JLabel("Organization (O):");
		labels[OU] = new JLabel("Organization Unit (OU):");
		// Common Name label initialized in extended class constructor
		labels[SA] = new JLabel("Signature Algorithm:");
		labels[UI] = new JLabel("Unique identifier:"); // V2+		
		
	}
	
	// ********************************************************************************************************
	// 												GETTERS
	// ********************************************************************************************************
	
	public abstract String getValue(int i);
	public abstract void setValue(int i, String s);
}
