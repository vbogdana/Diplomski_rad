package gui;

import java.awt.Color;

import javax.swing.JLabel;
import javax.swing.JTextField;

@SuppressWarnings("serial")
public class AlternativeNamePanel extends ExtensionPanel {
	JLabel label;
	JTextField alternative_names;

	AlternativeNamePanel(MainFrame mainFrame, String title, int i) {
		super(mainFrame, title, i);
		
		panel.setBounds(10, 50, 490, 50);
		
		label = new JLabel(title + "s:");
		label.setBounds(10, 10, 190, 25);
		panel.add(label);
		
		alternative_names = new JTextField(300);
		alternative_names.setBounds(210, 10, 270, 25);
		panel.add(alternative_names);
	}

	@Override
	void resetPanel() {
		resetExtensionPanel();
		alternative_names.setText("");
		alternative_names.setDisabledTextColor(Color.LIGHT_GRAY);

	}

	@Override
	void enablePanel(boolean flag) {
		enableExtensionPanel(flag);
		if (mainFrame.version_panel.getVersion() < Constants.V2) {
			label.setEnabled(false);
			alternative_names.setEnabled(false);
			resetPanel();
		} else {
			label.setEnabled(true);
			alternative_names.setEnabled(flag);
			alternative_names.setDisabledTextColor(Color.BLACK);
		}
	}
	
	String [] getAlternativeNames() {
		String [] names = { };
		if (!alternative_names.getText().isEmpty())
			names = alternative_names.getText().split(",");
		return names;
	}	
	void setAlternativeNames(String v) { alternative_names.setText(v); }

	@Override
	int getH() { return 110; }

	@Override
	void setY(int y) { setBounds(10, y, 510, 110); }

}
