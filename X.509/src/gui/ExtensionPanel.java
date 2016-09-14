package gui;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class ExtensionPanel extends JPanel {
	
	ExtensionPanel() {
		// TODO
		setBorder(BorderFactory.createTitledBorder("Extension"));

		
	}
	
	void resetPanel() {
		// TODO
	}
	
	void enablePanel(boolean flag) {
		// TODO
		setEnabled(flag);
	}
	
	int getH() {
		//TODO da bude abstract i da vraca visinu u zavisnosti od extenzije
		return 100;
	}

}
