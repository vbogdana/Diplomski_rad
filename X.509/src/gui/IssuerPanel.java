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
		
		labels[Constants.CN] = new JLabel("Common Name (CN):");
		
		setBounds(360, 320, 350, 300);
		setLayout(new GridBagLayout());
		Border b = BorderFactory.createEtchedBorder(EtchedBorder.RAISED);
        setBorder(BorderFactory.createTitledBorder(b, "Issued by"));
		
		for (int i = 0; i < 8; i++) {	
			if ((parent.version_panel.getSupportedVersion() < Constants.V2) && (i == Constants.UI))
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
		
		// because at the start selected version is 1
		if (parent.version_panel.getSupportedVersion() >= Constants.V2) {
			labels[Constants.UI].setEnabled(false);
			values[Constants.UI].setEnabled(false);
		}
	}
	
	void resetPanel() {
		for (int i = 0; i < 8; i++) {
			if ((parent.version_panel.getSupportedVersion() < Constants.V2) && (i == Constants.UI))
				continue;
			
			values[i].setText("");
		}
	}
	
	void enablePanel(boolean flag) {
		// TODO check zbog v2
		/*
		for (int i = 0; i < 8; i++) {
			if ((parent.version_panel.getSupportedVersion() < Constants.V2) && (i == Constants.UI))
				continue;
			
			labels[i].setEnabled(flag);
			values[i].setEnabled(flag);
		}
		*/
		if (parent.version_panel.getSupportedVersion() < Constants.V2)
			return;
		
		if (flag) {
			if (parent.version_panel.getVersion() < Constants.V2)
				 enableV2(false);
			 else
				 enableV2(true);
		} else {
			if (parent.version_panel.getVersion() < Constants.V2)
				labels[Constants.UI].setEnabled(false);
			else
				labels[Constants.UI].setEnabled(true);
		}
	}
	
	void enableV2(boolean flag) {
		labels[Constants.UI].setEnabled(flag);
		values[Constants.UI].setEnabled(flag);
		
		if (!flag)
			values[Constants.UI].setText("");
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
	
	void setInfo(String info) {
		resetPanel();
		String arr[] = info.split(",");
		for (int i = 0; i < arr.length; i++){
			String attr = arr[i];
			String key_val[] = attr.split("=");
			switch(key_val[0]) {
				case "C": setValue(Constants.C, key_val[1]); break;
				case "ST": setValue(Constants.ST, key_val[1]); break;
				case "L": setValue(Constants.L, key_val[1]); break;
				case "O": setValue(Constants.O, key_val[1]); break;
				case "OU": setValue(Constants.OU, key_val[1]); break;
				case "CN": setValue(Constants.CN, key_val[1]); break;
				case "SA": setValue(Constants.SA, key_val[1]); break;
			}
		}
	}

}
