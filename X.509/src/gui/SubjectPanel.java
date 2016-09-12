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

import exceptions.DataException;

@SuppressWarnings("serial")
public class SubjectPanel extends InfoPanel {
	
	private JTextField values[] = new JTextField [8];

	SubjectPanel(MainFrame parent) {
		super(parent);
		
		labels[CN] = new JLabel("Common Name (CN)*:");
		
		setBounds(360, 10, 350, 300);
		setLayout(new GridBagLayout());
		Border b = BorderFactory.createEtchedBorder(EtchedBorder.RAISED);
        setBorder(BorderFactory.createTitledBorder(b, "Certificate Subject"));
		
		for (int i = 0; i < 8; i++) {
			if ((parent.version_panel.getSupportedVersion() < VersionPanel.V2) && (i == UI))
				continue;
			
			values[i] = new JTextField(40);
			
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

		values[SA].setEnabled(false);
		values[SA].setDisabledTextColor(Color.BLACK);
		if (parent.version_panel.getSupportedVersion() >= VersionPanel.V2) {
			labels[UI].setEnabled(false);
			values[UI].setEnabled(false);
		}
	}
	
	void resetPanel() {
		// TODO
		for (int i = 0; i < 8; i++) {
			if ((parent.version_panel.getSupportedVersion() < VersionPanel.V2) && (i == UI))
				continue;
			
			values[i].setText("");
		}
		
		// if (parent.version_panel.getVersion() < VersionPanel.V2)
					//labels[UI].setEnabled(false);
		// da li treba i algoritam da se popuni
		values[SA].setText(parent.public_key_panel.getSignatureAlgorithm());
	}

	void enablePanel(boolean flag) {
		// TODO check zbog v2
		for (int i = 0; i < 8; i++) {
			if ((parent.version_panel.getSupportedVersion() < VersionPanel.V2) && (i == UI))
				continue;
			
			labels[i].setEnabled(flag);
			values[i].setEnabled(flag);
		}
		
		if (flag) {
			values[SA].setEnabled(false);
			values[SA].setDisabledTextColor(Color.BLACK);
			
			 if (parent.version_panel.getVersion() < VersionPanel.V2) {
				 labels[UI].setEnabled(false);
				 values[UI].setEnabled(false);
			 }
		} else {
			values[SA].setDisabledTextColor(Color.LIGHT_GRAY);
		}
	}
	
	void enableV2(boolean flag) {
		labels[InfoPanel.UI].setEnabled(flag);
		values[InfoPanel.UI].setEnabled(flag);
		
		if (!flag)
			values[UI].setText("");
	}
	
	void checkData() throws DataException {
		if (values[CN].getText() == null || values[CN].getText().equals(""))
			throw new DataException("Common Name is required.");
		// TODO
		// is UI in v2 required?
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
		
		if (!values[CN].getText().isEmpty()) 	info += "CN=" + values[CN].getText();
		if (!values[OU].getText().isEmpty())	info += ", OU=" + values[OU].getText();
		if (!values[O].getText().isEmpty())		info += ", O=" + values[O].getText();
		if (!values[L].getText().isEmpty())		info += ", L=" + values[L].getText();
		if (!values[ST].getText().isEmpty())		info += ", ST=" + values[ST].getText();
		if (!values[C].getText().isEmpty())		info += ", C=" + values[C].getText();
		
		return info;
	}

}
