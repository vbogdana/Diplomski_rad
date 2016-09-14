package gui;

import javax.swing.JLabel;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public abstract class InfoPanel extends JPanel {
	protected MainFrame parent;	
	protected JLabel labels[] = new JLabel [8];

	InfoPanel(MainFrame parent) {
		super();
		
		this.parent = parent;
		
		labels[Constants.C] = new JLabel("Country (C):");
		labels[Constants.ST] = new JLabel("State (ST):");
		labels[Constants.L] = new JLabel("Locality (L):");
		labels[Constants.O] = new JLabel("Organization (O):");
		labels[Constants.OU] = new JLabel("Organization Unit (OU):");
		labels[Constants.SA] = new JLabel("Signature Algorithm:");		
		// TODO
		if (parent.version_panel.getSupportedVersion() >= VersionPanel.V2)
			labels[Constants.UI] = new JLabel("Unique identifier:");		
		
	}
	
	abstract void enableV2(boolean flag);
	abstract void resetPanel();
	abstract void enablePanel(boolean flag);
	
	// ********************************************************************************************************
	// 												GETTERS
	// ********************************************************************************************************
	
	abstract String getValue(int i);
	abstract void setValue(int i, String s);
	abstract String getInfo();
	abstract void setInfo(String v);
}
