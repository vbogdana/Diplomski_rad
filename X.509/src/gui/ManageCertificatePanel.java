package gui;

import java.awt.Color;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.border.EtchedBorder;

@SuppressWarnings("serial")
public class ManageCertificatePanel extends JPanel {
	
	JButton sign = new JButton ("Sign certificate"),
			import_certificate = new JButton("Import certificate"),
			export_certificate = new JButton("Export certificate");
	
	ManageCertificatePanel(ToolbarListener listener) {
		setBounds(15, 425, 300, 170);
		setLayout(null);
		setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
		//setBackground(new Color(204,215,224));
		
		sign.setBounds(65, 10, 170, 25);
		sign.addActionListener(listener);
		add(sign);
		
		JPanel p = new JPanel();
		p.setBounds(10, 50, 280, 100);
		p.setLayout(null);
		p.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
		p.setBackground(new Color(204,215,224));
		add(p);
	
		
		import_certificate.setBounds(23, 50, 115, 25);
		export_certificate.setBounds(142, 50, 115, 25);
		
		import_certificate.addActionListener(listener);
		export_certificate.addActionListener(listener);
		
		p.add(import_certificate);
		p.add(export_certificate);
		
		resetPanel();
			
	}
	
	void resetPanel() {
		sign.setEnabled(false);
		import_certificate.setEnabled(true);
		export_certificate.setEnabled(false);		
	}

}
