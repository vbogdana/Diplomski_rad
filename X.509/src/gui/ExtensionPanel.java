package gui;


import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public abstract class ExtensionPanel extends JPanel {
	
	protected MainFrame mainFrame;
	protected JPanel panel;
	protected JCheckBox isCritical;
	
	ExtensionPanel(MainFrame mainFrame, String title, int i) {
		this.mainFrame = mainFrame;
		setLayout(null);
		setBorder(BorderFactory.createTitledBorder(title));
		
		isCritical = new JCheckBox("is critical");
		isCritical.setBounds(10, 20, 490, 25);
		isCritical.setBackground(new Color(204,215,224));
		isCritical.setSelected(false);
		isCritical.setEnabled(false);
		isCritical.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				mainFrame.extensions_panel.setIsCritical(i, isCritical.isSelected());
			}
		});
		add(isCritical);
		
		panel = new JPanel();
		panel.setLayout(null);
		panel.setBorder(BorderFactory.createLineBorder(new Color(204,215,224)));
		add(panel);
	}
	
	protected void resetExtensionPanel() {
		isCritical.setSelected(false);		
	}
	
	protected void enableExtensionPanel(boolean flag) {
		if (mainFrame.version_panel.getVersion() < Constants.V2) {
			setEnabled(false);
			isCritical.setEnabled(false);
		} else {
			setEnabled(true);
			isCritical.setEnabled(flag);
		}
	}
	
	abstract void resetPanel();
	abstract void enablePanel(boolean flag);
	
	boolean isCritical() { return isCritical.isSelected(); }
	void setCritical(boolean flag) { isCritical.setSelected(flag); }
	

}
