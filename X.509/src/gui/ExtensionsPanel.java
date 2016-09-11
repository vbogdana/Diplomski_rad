package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneLayout;

@SuppressWarnings("serial")
public class ExtensionsPanel extends JPanel {
	
	public ExtensionsPanel() {
		setBounds(720, 420, 550, 200);
		setLayout(new BorderLayout());
		setBorder(BorderFactory.createTitledBorder("Certificate Version 3 Extensions"));

		
		final JPanel panel = new JPanel();
		panel.setLayout(new GridBagLayout());
        //panel.setBorder(BorderFactory.createLineBorder(Color.gray));
        panel.setPreferredSize(new Dimension(260, 600));
        
        for (int i = 0; i < 3; i++) {
        	JPanel p = new JPanel();
            p.setBorder(BorderFactory.createTitledBorder("title"));
            //p.setPreferredSize(new Dimension(100, 150));
            
            GridBagConstraints c = new GridBagConstraints();
			c.gridx = 0; c.gridy = i;
			c.weightx = 1; c.weighty = 1;
			c.fill = GridBagConstraints.BOTH;
			c.insets = new Insets(2, 4, 2, 4);
            panel.add(p, c);
        }

        final JScrollPane scroll = new JScrollPane(panel);
        scroll.setBorder(null);

        
        add(scroll, BorderLayout.CENTER);

	}
}
