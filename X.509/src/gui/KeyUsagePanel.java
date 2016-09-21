package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JCheckBox;

@SuppressWarnings("serial")
public class KeyUsagePanel extends ExtensionPanel {
	JCheckBox key_usage[] = new JCheckBox [Constants.NUM_OF_KU];
	

	KeyUsagePanel(MainFrame mainFrame) {
		super(mainFrame, "Key usage", Constants.KU);
		//setBounds(10, 335, 510, 160);
		
		panel.setBounds(10, 50, 490, 100);
		
		key_usage[0] = new JCheckBox("Digital Signature");
		key_usage[1] = new JCheckBox("Content Commitment");
		key_usage[2] = new JCheckBox("Key Encipherment");
		key_usage[3] = new JCheckBox("Data Encipherment");
		key_usage[4] = new JCheckBox("Key Agreement");
		key_usage[5] = new JCheckBox("Certificate Signing");
		key_usage[6] = new JCheckBox("CRL Signing");
		key_usage[7] = new JCheckBox("Encipher Only");
		key_usage[8] = new JCheckBox("Decipher Only");
		
		for (int j = 0; j < 3; j++) {
			key_usage[j*3].setBounds(10, j*25+10, 150, 25);
			key_usage[j*3+1].setBounds(170, j*25+10, 150, 25);
			key_usage[j*3+2].setBounds(330, j*25+10, 150, 25);
		}
		
		for (int j = 0; j < Constants.NUM_OF_KU; j++) {
			key_usage[j].addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					if (((JCheckBox) e.getSource()).isSelected() && !isCritical.isSelected()) {
						isCritical.setSelected(true);
					} else if (!((JCheckBox) e.getSource()).isSelected() && isCritical.isSelected())
						uncheckIsCritical();
				}				
			});
		}
		
		key_usage[Constants.KEY_CERT_SIGN].addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO check nullove panela
				if (key_usage[Constants.KEY_CERT_SIGN].isSelected()) {
					// Key usage
					isCritical.setSelected(true);					
					// Basic Constraints
					if (mainFrame.extensions_panel.basic_constraints_panel != null) {
						mainFrame.extensions_panel.setCritical(Constants.BC, true);
						mainFrame.extensions_panel.basic_constraints_panel.isCritical.setEnabled(false);
						mainFrame.extensions_panel.basic_constraints_panel.setCertificateAuthority(true);
						mainFrame.extensions_panel.basic_constraints_panel.pathLen.setEnabled(true);
					}
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
				} else {
					// Key usage
					uncheckIsCritical();
					// Basic Constraints
					if (mainFrame.extensions_panel.basic_constraints_panel != null) {
						mainFrame.extensions_panel.basic_constraints_panel.isCritical.setEnabled(true);
					}					
				}
			}			
		});
		
		for (int i = 0; i < Constants.NUM_OF_KU; i++) {
			panel.add(key_usage[i]);
			key_usage[i].setEnabled(false);
		}
	}

	@Override
	void resetPanel() {
		resetExtensionPanel();
		for (int i = 0; i < Constants.NUM_OF_KU; i++) 
			key_usage[i].setSelected(false);
	}

	@Override
	void enablePanel(boolean flag) {
		enableExtensionPanel(flag);
		isCritical.setEnabled(false);
		if (mainFrame.version_panel.getVersion() < Constants.V2) {
			for (int i = 0; i < Constants.NUM_OF_KU; i++) 
				key_usage[i].setEnabled(false);
			resetPanel();
		} else {
			for (int i = 0; i < Constants.NUM_OF_KU; i++) 
				key_usage[i].setEnabled(flag);
		}
	}

	void uncheckIsCritical() {
		for (int i = 0; i < Constants.NUM_OF_KU; i++) 
			if (key_usage[i].isSelected())
				return;
		isCritical.setSelected(false);
	}
	
	boolean getKeyUsage(int i) { return key_usage[i].isSelected(); }
	void setKeyUsage(int i, boolean flag) { key_usage[i].setSelected(flag); }

	@Override
	int getH() { return 160; }

	@Override
	void setY(int y) { setBounds(10, y, 510, 160); }

}
