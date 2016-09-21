package gui;

import javax.swing.JCheckBox;

@SuppressWarnings("serial")
public class ExtendedKeyUsagePanel extends ExtensionPanel {
	JCheckBox key_usage[] = new JCheckBox [Constants.NUM_OF_EKU];
	
	public ExtendedKeyUsagePanel(MainFrame mainFrame) {
		super(mainFrame, "Extended key usage", Constants.EKU);
		
		panel.setBounds(10, 50, 490, 100);
		
		key_usage[0] = new JCheckBox("Any extended key usage");
		key_usage[1] = new JCheckBox("Server authentication");
		key_usage[2] = new JCheckBox("Client authentication");
		key_usage[3] = new JCheckBox("Code signing");
		key_usage[4] = new JCheckBox("Email protection");
		key_usage[5] = new JCheckBox("Time stamping");
		key_usage[6] = new JCheckBox("OCSP signing");
		
		key_usage[0].setBounds(10, 10, 210, 25);
		for (int j = 0; j < 2; j++) {
			key_usage[j*3 + 1].setBounds(10, j*25+35, 150, 25);
			key_usage[j*3 + 2].setBounds(170, j*25+35, 150, 25);
			key_usage[j*3 + 3].setBounds(330, j*25+35, 150, 25);
		}
		
		for (int j = 0; j < Constants.NUM_OF_EKU; j++) {
			panel.add(key_usage[j]);
			key_usage[j].setEnabled(false);
		}
		
	}

	@Override
	void resetPanel() {
		resetExtensionPanel();
		for (int i = 0; i < Constants.NUM_OF_EKU; i++) 
			key_usage[i].setSelected(false);

	}

	@Override
	void enablePanel(boolean flag) {
		enableExtensionPanel(flag);
		if (mainFrame.version_panel.getVersion() < Constants.V2) {
			for (int i = 0; i < Constants.NUM_OF_EKU; i++) 
				key_usage[i].setEnabled(false);
			resetPanel();
		} else {
			for (int i = 0; i < Constants.NUM_OF_EKU; i++) 
				key_usage[i].setEnabled(flag);
		}

	}
	
	boolean getKeyUsage(int i) { return key_usage[i].isSelected(); }
	void setKeyUsage(int i, boolean flag) { key_usage[i].setSelected(flag); }

	@Override
	int getH() { return 160;}

	@Override
	void setY(int y) { setBounds(10, y, 510, 160); }

}
