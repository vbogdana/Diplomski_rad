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
	
	private JCheckBox isCA;
	private JTextField pathLen;
	private JLabel label;


	BasicConstraintsPanel(MainFrame mainFrame) {
		super(mainFrame, "Basic Constraints", Constants.BC);
		setBounds(10, 10, 510, 110);
		
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
					mainFrame.extensions_panel.setCritical(Constants.AKID, false);
					mainFrame.extensions_panel.key_identifiers_panel.setIsEnabled(true);
					mainFrame.extensions_panel.key_identifiers_panel.isCritical.setEnabled(false);
					mainFrame.extensions_panel.key_identifiers_panel.isEnabled.setEnabled(false);
				} else {
					mainFrame.extensions_panel.key_identifiers_panel.resetPanel();
					mainFrame.extensions_panel.key_identifiers_panel.enablePanel(true);
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
			pathLen.setEnabled(flag);
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

}
