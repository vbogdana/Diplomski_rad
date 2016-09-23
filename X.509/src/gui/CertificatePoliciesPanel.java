package gui;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JTextField;

import code.DataException;

@SuppressWarnings("serial")
public class CertificatePoliciesPanel extends ExtensionPanel {
	JCheckBox anyPolicy;
	JLabel label;
	JTextField cpsUri;

	CertificatePoliciesPanel(MainFrame mainFrame) {
		super(mainFrame, "Certificate policies", Constants.CP);
		//setBounds(10, 505, 510, 110);
		
		panel.setBounds(10, 50, 490, 50);
		
		label = new JLabel("CPS URI:");
		label.setBounds(10, 10, 80, 25);
		label.setEnabled(false);
		panel.add(label);
		
		cpsUri = new JTextField(100);
		cpsUri.setBounds(90, 10, 200, 25);
		cpsUri.setEnabled(false);
		panel.add(cpsUri);
		
		anyPolicy = new JCheckBox("any policy");
		anyPolicy.setBounds(300, 10, 150, 25);
		anyPolicy.setSelected(false);
		anyPolicy.setEnabled(false);
		anyPolicy.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (anyPolicy.isSelected()) {
					cpsUri.setEnabled(true);
				} else {
					cpsUri.setText("");
					cpsUri.setEnabled(false);
				}
			}			
		});
		panel.add(anyPolicy);
	}

	@Override
	void resetPanel() {
		resetExtensionPanel();
		cpsUri.setText("");
		cpsUri.setDisabledTextColor(Color.LIGHT_GRAY);
		anyPolicy.setSelected(false);

	}

	@Override
	void enablePanel(boolean flag) {
		enableExtensionPanel(flag);
		
		if (mainFrame.version_panel.getVersion() < Constants.V2) {
			anyPolicy.setEnabled(false);
			label.setEnabled(false);
			cpsUri.setEnabled(false);
			resetPanel();
		} else {
			anyPolicy.setEnabled(flag);
			label.setEnabled(true);
			if (anyPolicy.isSelected() && flag)
				cpsUri.setEnabled(flag);
			else
				cpsUri.setEnabled(false);
			cpsUri.setDisabledTextColor(Color.BLACK);
		}

	}
	
	void checkData() throws DataException {
		if (anyPolicy.isSelected()) {
			if (cpsUri.getText() != null && !cpsUri.getText().equals(""))  {
				Pattern p = Pattern.compile("(@)?(href=')?(HREF=')?(HREF=\")?(href=\")?(http://)?[a-zA-Z_0-9\\-]+(\\.\\w[a-zA-Z_0-9\\-]+)+(/[#&\\n\\-=?\\+\\%/\\.\\w]+)?");  
			    Matcher m = p.matcher(cpsUri.getText()); 
			    if (!m.matches())
			    	throw new DataException("CPS URI is not valid.");
			}
		}
	}

	boolean getAnyPolicy() { return anyPolicy.isSelected(); }
	void setAnyPolicy(boolean v) { anyPolicy.setSelected(v); }
	String getCpsUri() { return cpsUri.getText(); }
	void setCpsUri(String v) { cpsUri.setText(v); }

	@Override
	int getH() { return 110; }

	@Override
	void setY(int y) { setBounds(10, y, 510, 110); }
	
	

}
