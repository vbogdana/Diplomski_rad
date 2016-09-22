package gui;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JTextField;

import code.DataException;

@SuppressWarnings("serial")
public class BasicConstraintsPanel extends ExtensionPanel {
	
	JCheckBox isCA;
	JTextField pathLen;
	JLabel label;


	BasicConstraintsPanel(MainFrame mainFrame) {
		super(mainFrame, "Basic Constraints", Constants.BC);
		//setBounds(10, 10, 510, 110);
		
		panel.setBounds(10, 50, 490, 50);		
		
		label = new JLabel("Path length:");
		label.setBounds(10, 10, 80, 25);
		label.setEnabled(false);
		panel.add(label);
		
		pathLen = new JTextField(20);
		pathLen.setBounds(90, 10, 100, 25);
		pathLen.setEnabled(false);
		panel.add(pathLen);
		
		isCA = new JCheckBox("is certificate authority");
		isCA.setBounds(300, 10, 150, 25);
		isCA.setSelected(false);
		isCA.setEnabled(false);
		isCA.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (isCA.isSelected()) {
					// Path length
					pathLen.setEnabled(true);
					// Key IDs
					if (mainFrame.extensions_panel.key_identifiers_panel != null) {
						mainFrame.extensions_panel.setCritical(Constants.AKID, false);
						mainFrame.extensions_panel.key_identifiers_panel.setIsEnabled(true);
						mainFrame.extensions_panel.key_identifiers_panel.isCritical.setEnabled(false);
						mainFrame.extensions_panel.key_identifiers_panel.isEnabled.setEnabled(false);	
					}										
					// Certificate policies
					if (mainFrame.extensions_panel.certificate_policies_panel != null) {
						mainFrame.extensions_panel.certificate_policies_panel.setAnyPolicy(true);
						mainFrame.extensions_panel.certificate_policies_panel.anyPolicy.setEnabled(false);
						mainFrame.extensions_panel.certificate_policies_panel.cpsUri.setEnabled(true);
					}
					// Subject directory attributes
					if (mainFrame.extensions_panel.subject_directory_attributes_panel != null) {
						mainFrame.extensions_panel.setCritical(Constants.SDA, false);
						mainFrame.extensions_panel.subject_directory_attributes_panel.isCritical.setEnabled(false);
					}
					// Inhibit any policy
					if (mainFrame.getInhibitAnyPolicy()) {
						mainFrame.setCritical(Constants.IAP, true);
						mainFrame.extensions_panel.inhibit_any_policy_panel.isCritical.setEnabled(false);
					}
				} else {
					// Path length
					pathLen.setText("");
					pathLen.setEnabled(false);
					isCritical.setEnabled(true);
					isCritical.setSelected(false);
					// Key IDs
					if (mainFrame.extensions_panel.key_identifiers_panel != null) {
						mainFrame.extensions_panel.key_identifiers_panel.resetPanel();
						mainFrame.extensions_panel.key_identifiers_panel.enablePanel(true);
					}
					// Key usage
					if (mainFrame.extensions_panel.key_usage_panel != null) {
						mainFrame.extensions_panel.key_usage_panel.setKeyUsage(Constants.KEY_CERT_SIGN, false);
						mainFrame.extensions_panel.key_usage_panel.key_usage[Constants.KEY_CERT_SIGN].setEnabled(true);
						mainFrame.extensions_panel.key_usage_panel.uncheckIsCritical();
					}
					// Certificate policies
					if (mainFrame.extensions_panel.certificate_policies_panel != null)
						mainFrame.extensions_panel.certificate_policies_panel.anyPolicy.setEnabled(true);
					// Subject directory attributes
					if (mainFrame.extensions_panel.subject_directory_attributes_panel != null)
						mainFrame.extensions_panel.subject_directory_attributes_panel.isCritical.setEnabled(true);
					// Inhibit any policy
					if (mainFrame.extensions_panel.inhibit_any_policy_panel != null)
						mainFrame.extensions_panel.inhibit_any_policy_panel.isCritical.setEnabled(true);
				}				
			}			
		});
		panel.add(isCA);
		
	}
	
	void resetPanel() {
		resetExtensionPanel();
		pathLen.setText("");
		pathLen.setDisabledTextColor(Color.LIGHT_GRAY);
		isCA.setSelected(false);		
	}
	
	void enablePanel(boolean flag) {
		enableExtensionPanel(flag);
		
		if (mainFrame.version_panel.getVersion() < Constants.V2) {
			isCA.setEnabled(false);
			label.setEnabled(false);
			pathLen.setEnabled(false);
			resetPanel();
		} else {
			isCA.setEnabled(flag);
			label.setEnabled(true);
			if (isCA.isSelected() && flag)
				pathLen.setEnabled(flag);
			else
				pathLen.setEnabled(false);
			pathLen.setDisabledTextColor(Color.BLACK);
		}
	}
	
	void checkData() throws DataException {
		if (pathLen != null && !pathLen.getText().equals("")) {
			try { 
		        Integer.parseInt(pathLen.getText()); 
		    } catch(NumberFormatException ex) { 
		    	throw new DataException("Path Length field must be a number.");
		    }		
			if (Integer.parseInt(pathLen.getText()) < 0)
				throw new DataException("Path Length field must be a non-negative number.");
		}
	}
	
	void setPathLen(String v) { pathLen.setText(v); }
	String getPathLen() { return pathLen.getText(); }
	boolean isCertificateAuthority() { return isCA.isSelected(); }
	void setCertificateAuthority(boolean v) { isCA.setSelected(v); }

	@Override
	int getH() { return 110; }

	@Override
	void setY(int y) { setBounds(10, y, 510, 110); }

}
