package gui;

import javax.swing.JLabel;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public abstract class InfoPanel extends JPanel {
	protected MainFrame parent;
	// UI - unique identifiers V2+
	public static final int C = 0, ST = 1, L = 2, O = 3, OU = 4, CN = 5, SA = 6, UI = 7;	
	protected JLabel labels[] = new JLabel [8];

	InfoPanel(MainFrame parent) {
		super();
		
		this.parent = parent;
		
		labels[C] = new JLabel("Country (C):");
		labels[ST] = new JLabel("State (ST):");
		labels[L] = new JLabel("Locality (L):");
		labels[O] = new JLabel("Organization (O):");
		labels[OU] = new JLabel("Organization Unit (OU):");
		labels[SA] = new JLabel("Signature Algorithm:");		
		// TODO
		if (parent.version_panel.getSupportedVersion() >= VersionPanel.V2)
			labels[UI] = new JLabel("Unique identifier:");		
		
	}
	
	abstract void enableV2(boolean flag);
	abstract void resetPanel();
	abstract void enablePanel(boolean flag);
	// TODO
	//public abstract void checkData();
	
	// ********************************************************************************************************
	// 												GETTERS
	// ********************************************************************************************************
	
	abstract String getValue(int i);
	abstract void setValue(int i, String s);
	abstract String getInfo();
}
