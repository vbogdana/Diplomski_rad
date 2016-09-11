package gui;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JLabel;

@SuppressWarnings("serial")
public class IssuerPanel extends InfoPanel {
	
	JLabel values[] = new JLabel [8];

	public IssuerPanel() {
		super();
		
		labels[CN] = new JLabel("Common Name (CN):");
		
		setBounds(360, 320, 350, 300);
		setLayout(new GridBagLayout());
		setBorder(BorderFactory.createTitledBorder("Issued by"));
		
		for (int i = 0; i < 8; i++) {		
			values[i] = new JLabel(" ");

			GridBagConstraints c = new GridBagConstraints();
			values[i] = new JLabel(" ");
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
