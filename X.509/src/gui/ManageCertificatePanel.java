package gui;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.border.EtchedBorder;

@SuppressWarnings("serial")
public class ManageCertificatePanel extends JPanel implements ActionListener {
	
	JButton sign = new JButton ("Sign certificate"),
			import_certificate = new JButton("Import certificate"),
			export_certificate = new JButton("Export certificate");
	ButtonGroup buttonGroup = new ButtonGroup();
	JRadioButton buttons[] = new JRadioButton [2];
	int encoding;
	
	ManageCertificatePanel(ToolbarListener listener) {
		setBounds(15, 425, 300, 170);
		setLayout(null);
		setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
		//setBackground(new Color(204,215,224));
		
		sign.setBounds(65, 10, 170, 25);
		sign.addActionListener(listener);
		add(sign);
		
		JPanel p = new JPanel();
		p.setBounds(10, 45, 280, 105);
		p.setLayout(null);
		p.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
		p.setBackground(new Color(204,215,224));
		add(p);
		
		buttons[Constants.DER] = new JRadioButton("DER encoding");
		buttons[Constants.PEM] = new JRadioButton("PEM encoding");	
		
		for (int i = 0; i < Constants.NUM_OF_ENCODINGS; i++) {
			buttons[i].setBounds(i*130 + 10, 10, 130, 25);
			buttons[i].addActionListener(this);
			buttonGroup.add(buttons[i]);
			p.add(buttons[i]);
		}
		buttons[Constants.DER].setSelected(true);
		encoding = Constants.DER;
		
		import_certificate.setBounds(23, 70, 115, 25);
		export_certificate.setBounds(142, 70, 115, 25);
		
		import_certificate.addActionListener(listener);
		export_certificate.addActionListener(listener);
		
		p.add(import_certificate);
		p.add(export_certificate);
		
		resetPanel();
			
	}
	
	void resetPanel() {
		sign.setEnabled(false);
		buttons[Constants.DER].setSelected(true);
		encoding = Constants.DER;
		import_certificate.setEnabled(true);
		export_certificate.setEnabled(false);		
	}
	
	void enableSignButton(boolean flag) {
		sign.setEnabled(flag);
	}
	
	void enableExportCertificateButton(boolean flag) {
		export_certificate.setEnabled(flag);
	}

	@Override
	public void actionPerformed(ActionEvent ev) {
		// TODO Auto-generated method stub
		for (int i = 0; i < Constants.NUM_OF_EXTENSIONS; i++)
			if (buttons[i].isSelected()) {
				encoding = i;
				break;
			}
	}
}
