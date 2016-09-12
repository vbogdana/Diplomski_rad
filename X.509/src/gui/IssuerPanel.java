package gui;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;

@SuppressWarnings("serial")
public class IssuerPanel extends InfoPanel {
	
	private JLabel values[] = new JLabel [8];

	IssuerPanel(MainFrame parent) {
		super(parent);
		
		labels[CN] = new JLabel("Common Name (CN):");
		
		setBounds(360, 320, 350, 300);
		setLayout(new GridBagLayout());
		Border b = BorderFactory.createEtchedBorder(EtchedBorder.RAISED);
        setBorder(BorderFactory.createTitledBorder(b, "Issued by"));
		
		for (int i = 0; i < 8; i++) {	
			if ((parent.version_panel.getSupportedVersion() < VersionPanel.V2) && (i == UI))
				continue;
			
			values[i] = new JLabel("");

			GridBagConstraints c = new GridBagConstraints();
			values[i].setPreferredSize(new Dimension(270, 30));
			
			c.gridy = i; c.gridx = 0;
			c.weightx = 0.1; c.weighty = 0.2; 
			c.insets = new Insets(2, 4, 2, 4);
			c.anchor = GridBagConstraints.LINE_END;
			add(labels[i], c);
			
			c.gridx = 1;
			c.weightx = 1.0; c.weighty = 1.0; 
			c.fill = GridBagConstraints.BOTH;		
			add(values[i], c);
		}
		
		if (parent.version_panel.getSupportedVersion() >= VersionPanel.V2) {
			labels[UI].setEnabled(false);
			values[UI].setEnabled(false);
		}
	}
	
	void resetPanel() {
		for (int i = 0; i < 8; i++) {
			if ((parent.version_panel.getSupportedVersion() < VersionPanel.V2) && (i == UI))
				continue;
			
			values[i].setText("");
		}
	}
	
	void enablePanel(boolean flag) {
		// TODO check
		for (int i = 0; i < 8; i++) {
			if ((parent.version_panel.getSupportedVersion() < VersionPanel.V2) && (i == UI))
				continue;
			
			labels[i].setEnabled(flag);
			values[i].setEnabled(flag);
		}
		
		if (flag) {
			if (parent.version_panel.getVersion() < VersionPanel.V2) {
				 labels[UI].setEnabled(false);
				 values[UI].setEnabled(false);
			 }	
		}		
	}
	
	void enableV2(boolean flag) {
		labels[InfoPanel.UI].setEnabled(flag);
		values[InfoPanel.UI].setEnabled(flag);
		
		if (!flag)
			values[UI].setText("");
	}
	
	// ********************************************************************************************************
	// 										GETTERS AND SETTERS
	// ********************************************************************************************************
	
	String getValue(int i) {
		return values[i].getText();
	}
	
	void setValue(int i, String s) {
		values[i].setText(s);
	}
	
	String getInfo() {
		// TODO
		String info = "";
		
		return info;
	}

}
