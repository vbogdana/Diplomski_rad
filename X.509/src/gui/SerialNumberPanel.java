package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigInteger;
import java.security.SecureRandom;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;

import code.DataException;

@SuppressWarnings("serial")
public class SerialNumberPanel extends JPanel implements ActionListener {
	
	public static final int NUM_BITS = 64;
	
	private JLabel label = new JLabel("Serial Number:");
	private JTextField value = new JTextField(20);
	private JButton button = new JButton("Generate");

	SerialNumberPanel() {
		setBounds(720, 70, 560, 60);
		setLayout(new GridBagLayout());
		Border b = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
        setBorder(BorderFactory.createTitledBorder(b, "Certificate Serial Number"));

		label.setHorizontalAlignment(SwingConstants.RIGHT);
		button.addActionListener(this);
		value.setEnabled(false);
		value.setDisabledTextColor(Color.BLACK);
		
		GridBagConstraints c = new GridBagConstraints();		
		c.gridy = 0; c.gridx = 0;
		c.weightx = 0.3; c.weighty = 1; 	
		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(2, 4, 2, 4);
		add(label, c);
		
		c.gridx = 1;
		c.weightx = 0.5; c.weighty = 1;
		value.setPreferredSize(new Dimension(100, 25));
		add(value, c);
		
		c.gridx = 2;		
		add(button, c);
	}
	
	void resetPanel() {
		value.setText("");
	}
	
	void enablePanel(boolean flag) {
		//setEnabled(flag);
		button.setEnabled(flag);		
	}
	
	void checkData() throws DataException {
		if (value.getText() == null || value.getText().equals(""))
			throw new DataException("A serial number is required.");
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		BigInteger num = new BigInteger(NUM_BITS, new SecureRandom());
		value.setText(num.toString());
	}
	
	// ********************************************************************************************************
	// 											GETTERS AND SETTERS
	// ********************************************************************************************************
	
	String getSerialNumber() {
		return value.getText();
	}
	
	void setSerialNumber(String v) {
		value.setText(v);
	}

}
