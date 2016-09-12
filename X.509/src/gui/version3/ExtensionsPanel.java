package gui.version3;

import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;

@SuppressWarnings("serial")
public class ExtensionsPanel extends JPanel {
	public static final int NUM_OF_EXTENSIONS = 15;
	
	private ExtensionPanel extensions[] = new ExtensionPanel [NUM_OF_EXTENSIONS];
	
	public ExtensionsPanel() {
		setBounds(720, 420, 550, 200);
		setLayout(new BorderLayout());
		Border b = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
        setBorder(BorderFactory.createTitledBorder(b, "Certificate Version 3 Extensions"));
		
		final JPanel panel = new JPanel();
		panel.setLayout(null);
        panel.setPreferredSize(new Dimension(520, 1520));
        
        for (int i = 0; i < NUM_OF_EXTENSIONS; i++) {
        	extensions[i] = new ExtensionPanel();
        	extensions[i].setBounds(10, i*100 + 10, 500, extensions[i].getH());
            panel.add(extensions[i]);
        }

        final JScrollPane scroll = new JScrollPane(panel);
        scroll.setBorder(null);
     
        add(scroll, BorderLayout.CENTER);

	}
	
	public void resetPanel() {
		// TODO
	}
	
	public void enablePanel(boolean flag) {
		// TODO
		setEnabled(flag);
	}
}
