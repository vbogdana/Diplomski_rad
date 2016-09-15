package gui;

import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.border.EtchedBorder;

import code.CodeInterface;

@SuppressWarnings("serial")
public class ToolbarPanel extends JPanel {
	
	//private MainFrame parent;
	KeyStorePanel keystore_panel;
	ManageCertificatePanel manage_panel;
	ToolbarListener listener;
	
	ToolbarPanel(MainFrame parent, CodeInterface code) {
		//this.parent = parent;
		
		setBounds(10, 10, 330, 610);
		setLayout(null);
		setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
		setBackground(new Color(204,215,224));
		
		listener = new ToolbarListener(parent, code);
		keystore_panel = new KeyStorePanel(listener);
		manage_panel = new ManageCertificatePanel(listener);
		
		add(keystore_panel);
		add(manage_panel);
	}
	
	void resetPanel() {
		keystore_panel.resetPanel();
		manage_panel.resetPanel();
	}
	

}
