package gui.version3;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class ExtensionPanel extends JPanel {
	
	public ExtensionPanel() {
		// TODO
		setBorder(BorderFactory.createTitledBorder("Extension"));

		
	}
	
	public void resetPanel() {
		// TODO
	}
	
	public void enablePanel(boolean flag) {
		// TODO
		setEnabled(flag);
	}
	
	public int getH() {
		//TODO da bude abstract i da vraca visinu u zavisnosti od extenzije
		return 100;
	}

}
