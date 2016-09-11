package gui;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

@SuppressWarnings("serial")
public class VersionPanel extends JPanel implements ActionListener {
	MainFrame parent;
	
	int selected = 0;
	ButtonGroup buttonGroup = new ButtonGroup();
	JRadioButton buttons[] = new JRadioButton [3];
	
	public VersionPanel(MainFrame parent) {
		this.parent = parent;
		
		setBounds(720, 10, 550, 50);
		setLayout(new GridLayout(1, 3, 5, 3));
		setBorder(BorderFactory.createTitledBorder("Certificate Version"));
		
		for (int i = 0; i < 3; i++) {
			buttons[i] = new JRadioButton("Version " + (i + 1));
			buttons[i].addActionListener(this);
			buttonGroup.add(buttons[i]);
			add(buttons[i]);
		}
		
		buttons[selected].setSelected(true);			
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		for (int i = 0; i < 3; i++)
			if (((JRadioButton) e.getSource()) == buttons[i]) {
				selected = i;
				break;
			} 
			
			if (selected == 0) {
				parent.subject_panel.labels[InfoPanel.UI].setEnabled(false);
				((SubjectPanel) parent.subject_panel).values[InfoPanel.UI].setEnabled(false);
				parent.issuer_panel.labels[InfoPanel.UI].setEnabled(false);
				((IssuerPanel) parent.issuer_panel).values[InfoPanel.UI].setEnabled(false);
			} else {
				parent.subject_panel.labels[InfoPanel.UI].setEnabled(true);
				((SubjectPanel) parent.subject_panel).values[InfoPanel.UI].setEnabled(true);
				parent.issuer_panel.labels[InfoPanel.UI].setEnabled(true);
				((IssuerPanel) parent.issuer_panel).values[InfoPanel.UI].setEnabled(true);
			}
	}
	
	// ********************************************************************************************************
	// 												GETTERS
	// ********************************************************************************************************
	
	public int getSelected() {
		return selected;
	}

}
