package gui;

import javax.swing.JCheckBox;
import javax.swing.JLabel;

@SuppressWarnings("serial")
public class KeyIdentifiersPanel extends ExtensionPanel {
	JCheckBox isEnabled;
	JLabel authorityKeyID, authorityIssuer, authoritySerialNumber, subjectKeyID;

	KeyIdentifiersPanel(MainFrame mainFrame) {
		super(mainFrame, "Key Identifiers", Constants.AKID);
		//setBounds(10, 130, 510, 195);
		
		panel.setBounds(10, 50, 490, 135);
		
		isEnabled = new JCheckBox("is enabled");
		isEnabled.setBounds(10, 10, 85, 25);
		isEnabled.setSelected(false);
		isEnabled.setEnabled(false);
		panel.add(isEnabled);
		
		authorityKeyID = new JLabel("Authority key id: ");
		authorityKeyID.setBounds(110, 10, 370, 25);
		authorityKeyID.setEnabled(false);
		authorityIssuer = new JLabel("issuer: ");
		authorityIssuer.setBounds(165, 40, 315, 25);
		authorityIssuer.setEnabled(false);
		authoritySerialNumber = new JLabel("serial number: ");
		authoritySerialNumber.setBounds(165, 70, 315, 25);
		authoritySerialNumber.setEnabled(false);
		
		subjectKeyID = new JLabel("Subject key id: ");
		subjectKeyID.setBounds(120, 100, 360, 25);
		subjectKeyID.setEnabled(false);
		
		panel.add(authorityKeyID);
		panel.add(authorityIssuer);
		panel.add(authoritySerialNumber);
		panel.add(subjectKeyID);
	}

	@Override
	void resetPanel() {
		resetExtensionPanel();
		isEnabled.setSelected(false);
		authorityKeyID.setText("Authority key id: ");
		authorityIssuer.setText("issuer: ");
		authoritySerialNumber.setText("serial number: ");
		subjectKeyID.setText("Subject key id: ");
	}

	@Override
	void enablePanel(boolean flag) {
		enableExtensionPanel(flag);
		if (mainFrame.version_panel.getVersion() < Constants.V2) {
			isEnabled.setEnabled(false);
			authorityKeyID.setEnabled(false);
			authorityIssuer.setEnabled(false);
			authoritySerialNumber.setEnabled(false);
			subjectKeyID.setEnabled(false);
			resetPanel();
		} else {
			isEnabled.setEnabled(flag);
			authorityKeyID.setEnabled(true);
			authorityIssuer.setEnabled(true);
			authoritySerialNumber.setEnabled(true);
			subjectKeyID.setEnabled(true);
		}		
	}
	
	boolean getIsEnabled() { return isEnabled.isSelected(); }
	void setIsEnabled(boolean flag) { isEnabled.setSelected(flag); }

	void setAuthorityKeyID(String authorityKeyID) {
		this.authorityKeyID.setText(this.authorityKeyID.getText() + " " + authorityKeyID);
	}

	void setAuthorityIssuer(String authorityIssuer) {
		this.authorityIssuer.setText(this.authorityIssuer.getText() + " " + authorityIssuer);
	}

	void setAuthoritySerialNumber(String authoritySerialNumber) {
		this.authoritySerialNumber.setText(this.authoritySerialNumber.getText() + " " + authoritySerialNumber);
	}

	void setSubjectKeyID(String subjectKeyID) {
		this.subjectKeyID.setText(this.subjectKeyID.getText() + " " + subjectKeyID);
	}

	@Override
	int getH() { return 195; }

	@Override
	void setY(int y) { setBounds(10, y, 510, 195); }
	

}
