package gui;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;

import code.DataException;

@SuppressWarnings("serial")
public class SubjectPanel extends InfoPanel {
	
	private JTextField values[] = new JTextField [8];

	SubjectPanel(MainFrame parent) {
		super(parent);
		
		labels[Constants.CN] = new JLabel("Common Name (CN)*:");
		
		setBounds(360, 10, 350, 300);
		setLayout(new GridBagLayout());
		Border b = BorderFactory.createEtchedBorder(EtchedBorder.RAISED);
        setBorder(BorderFactory.createTitledBorder(b, "Certificate Subject"));
		
		for (int i = 0; i < 8; i++) {
			if ((parent.supported_version < Constants.V2) && (i == Constants.UI))
				continue;
			
			values[i] = new JTextField(40);
			values[i].setDisabledTextColor(Color.BLACK);
			
			GridBagConstraints c = new GridBagConstraints();		
			c.gridy = i; c.gridx = 0;
			c.insets = new Insets(2, 4, 2, 4);
			c.anchor = GridBagConstraints.LINE_END;
			add(labels[i], c);
			
			c.gridx = 1;
			c.weightx = 1.0; c.weighty = 1.0; 
			c.fill = GridBagConstraints.BOTH;
			c.insets = new Insets(4, 4, 4, 4);
			add(values[i], c);
			
		}

		values[Constants.SA].setEnabled(false);
		// because at the start selected version is 1
		if (parent.supported_version >= Constants.V2) {
			labels[Constants.UI].setEnabled(false);
			values[Constants.UI].setEnabled(false);
		}
	}
	
	void resetPanel() {
		// TODO
		for (int i = 0; i < 8; i++) {
			if ((parent.supported_version < Constants.V2) && (i == Constants.UI))
				continue;
			
			values[i].setText("");
		}

		// da li treba i algoritam da se popuni
		values[Constants.SA].setText(parent.public_key_panel.getSignatureAlgorithm());
	}

	void enablePanel(boolean flag) {
		// TODO check zbog v2
		for (int i = 0; i < 8; i++) {
			if ((parent.supported_version < Constants.V2) && (i == Constants.UI))
				continue;
			
			values[i].setEnabled(flag);
		}

		if (flag) {
			values[Constants.SA].setEnabled(false);
			if (parent.supported_version < Constants.V2)
				return;

			if (parent.version_panel.getVersion() < Constants.V2)
				enableV2(false);
			else
				enableV2(true);
		} else {
			if (parent.supported_version < Constants.V2)
				return;

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
	
	void checkData() throws DataException {
		if (values[Constants.CN].getText() == null || values[Constants.CN].getText().equals(""))
			throw new DataException("Common Name is required.");
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

		if (!values[Constants.CN].getText().isEmpty()) 		info += "CN=" + values[Constants.CN].getText();
		if (!values[Constants.OU].getText().isEmpty())		info += ", OU=" + values[Constants.OU].getText();
		if (!values[Constants.O].getText().isEmpty())		info += ", O=" + values[Constants.O].getText();
		if (!values[Constants.L].getText().isEmpty())		info += ", L=" + values[Constants.L].getText();
		if (!values[Constants.ST].getText().isEmpty())		info += ", ST = " + values[Constants.ST].getText();
		if (!values[Constants.C].getText().isEmpty())		info += ", C=" + values[Constants.C].getText();
		
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
			}
		}
	}

}
