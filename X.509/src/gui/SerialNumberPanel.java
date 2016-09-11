package gui;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

@SuppressWarnings("serial")
public class SerialNumberPanel extends JPanel implements ActionListener {
	
	private JLabel label = new JLabel("Serial Number:");
	private JTextField value = new JTextField(20);
	private JButton button = new JButton("Generate");

	public SerialNumberPanel() {
		setBounds(720, 70, 550, 60);
		setLayout(new GridLayout(1, 3, 5, 3));
		setBorder(BorderFactory.createTitledBorder("Certificate Serial Number"));
		
		button.addActionListener(this);
		label.setHorizontalAlignment(SwingConstants.RIGHT);
		value.setEnabled(false);
		value.setDisabledTextColor(new Color(0));
		
		add(label);
		add(value);
		add(new JLabel(""));
		add(button);
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		value.setText("generisano");
	}
	
	// ********************************************************************************************************
	// 												GETTERS
	// ********************************************************************************************************
	
	public String getSerialNumber() {
		return value.getText();
	}

}
