package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JTextField;

@SuppressWarnings("serial")
public class SubjectPanel extends InfoPanel {
	
	JTextField values[] = new JTextField [8];

	public SubjectPanel() {
		super();
		
		labels[CN] = new JLabel("Common Name (CN)*:");
		
		setBounds(360, 10, 350, 300);
		setLayout(new GridBagLayout());
		setBorder(BorderFactory.createTitledBorder("Certificate Subject"));
		
		//GridBagConstraints c = new GridBagConstraints();
		for (int i = 0; i < 8; i++) {
			GridBagConstraints c = new GridBagConstraints();
			values[i] = new JTextField(40);
			values[i].setPreferredSize(new Dimension(270, 30));
			
			c.gridy = i; c.gridx = 0;
			c.weightx = 0.1; c.weighty = 0.2; 
			c.insets = new Insets(2, 4, 2, 4);
			c.anchor = GridBagConstraints.LINE_END;
			add(labels[i], c);
			
			c.gridx = 1;
			c.weightx = 1.0; c.weighty = 1.0; 
			c.fill = GridBagConstraints.BOTH;		
			add(values[i], c);
		}

		values[SA].setEnabled(false);
		values[SA].setDisabledTextColor(new Color(0));
		labels[UI].setEnabled(false);
		values[UI].setEnabled(false);
	}
	
	// ********************************************************************************************************
	// 												GETTERS
	// ********************************************************************************************************
	
	public String getValue(int i) {
		return values[i].getText();
	}
	
	public void setValue(int i, String s) {
		values[i].setText(s);
	}

}
