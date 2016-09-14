package gui;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;

@SuppressWarnings("serial")
public class ExtensionsPanel extends JPanel {
	
	
	private ExtensionPanel extensions[] = new ExtensionPanel [Constants.NUM_OF_EXTENSIONS];
	
	ExtensionsPanel() {
		setBounds(720, 420, 560, 200);
		setLayout(new BorderLayout());
		Border b = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
        setBorder(BorderFactory.createTitledBorder(b, "Certificate Version 3 Extensions"));
		
		final JPanel panel = new JPanel();
		panel.setLayout(null);
        panel.setPreferredSize(new Dimension(530, 1520));
        
        for (int i = 0; i < Constants.NUM_OF_EXTENSIONS; i++) {
        	extensions[i] = new ExtensionPanel();
        	extensions[i].setBounds(10, i*100 + 10, 510, extensions[i].getH());
            panel.add(extensions[i]);
        }

        final JScrollPane scroll = new JScrollPane(panel);
        scroll.setBorder(null);
     
        add(scroll, BorderLayout.CENTER);

	}
	
	void resetPanel() {
		// TODO
	}
	
	void enablePanel(boolean flag) {
		// TODO
		setEnabled(flag);
	}
}
