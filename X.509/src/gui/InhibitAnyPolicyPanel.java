package gui;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JTextField;

import code.DataException;

@SuppressWarnings("serial")
public class InhibitAnyPolicyPanel extends ExtensionPanel {
	JCheckBox inhibitAnyPolicy;
	JTextField skipCerts;
	JLabel label;

	public InhibitAnyPolicyPanel(MainFrame mainFrame) {
		super(mainFrame, "Inhibit anyPolicy", Constants.IAP);

		panel.setBounds(10, 50, 490, 50);		
		
		label = new JLabel("Skip certs:");
		label.setBounds(10, 10, 80, 25);
		label.setEnabled(false);
		panel.add(label);
		
		skipCerts = new JTextField(20);
		skipCerts.setBounds(90, 10, 100, 25);
		skipCerts.setEnabled(false);
		panel.add(skipCerts);
		
		inhibitAnyPolicy = new JCheckBox("inhibit anyPolicy");
		inhibitAnyPolicy.setBounds(300, 10, 150, 25);
		inhibitAnyPolicy.setSelected(false);
		inhibitAnyPolicy.setEnabled(false);
		inhibitAnyPolicy.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (inhibitAnyPolicy.isSelected()) {
					// Skip certs
					skipCerts.setEnabled(true);
					// Basic Constraints
					if (mainFrame.isCA()) {
						isCritical.setSelected(true);
						isCritical.setEnabled(false);
					}
				} else {
					// Skip certs
					skipCerts.setText("");
					skipCerts.setEnabled(false);
					isCritical.setEnabled(true);
					isCritical.setSelected(false);
				}				
			}
		});
		panel.add(inhibitAnyPolicy);
	}

	@Override
	void resetPanel() {
		resetExtensionPanel();
		skipCerts.setText("");
		skipCerts.setDisabledTextColor(Color.LIGHT_GRAY);
		inhibitAnyPolicy.setSelected(false);
	}

	@Override
	void enablePanel(boolean flag) {
		enableExtensionPanel(flag);		
		if (mainFrame.version_panel.getVersion() < Constants.V2) {
			inhibitAnyPolicy.setEnabled(false);
			label.setEnabled(false);
			skipCerts.setEnabled(false);
			resetPanel();
		} else {
			inhibitAnyPolicy.setEnabled(flag);
			label.setEnabled(true);
			if (inhibitAnyPolicy.isSelected() && flag)
				skipCerts.setEnabled(flag);
			else
				skipCerts.setEnabled(false);
			skipCerts.setDisabledTextColor(Color.BLACK);
		}
	}
	
	void checkData() throws DataException {
		if (skipCerts != null && !skipCerts.getText().equals("")) {
			try { 
		        Integer.parseInt(skipCerts.getText()); 
		    } catch(NumberFormatException ex) { 
		    	throw new DataException("Skip certs field must be a number.");
		    }		
			if (Integer.parseInt(skipCerts.getText()) < 0)
				throw new DataException("Skip certs field must be a non-negative number.");
		}
	}

	void setSkipCerts(String v) { skipCerts.setText(v); }
	String getSkipCerts() { return skipCerts.getText(); }
	boolean getInhibitAnyPolicy() { return inhibitAnyPolicy.isSelected(); }
	void setInhibitAnyPolicy(boolean v) { inhibitAnyPolicy.setSelected(v); }

	@Override
	int getH() { return 110; }

	@Override
	void setY(int y) { setBounds(10, y, 510, 110); }

}
