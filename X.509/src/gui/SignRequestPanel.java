package gui;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;

import code.CodeInterface;

@SuppressWarnings({"serial", "rawtypes", "unchecked"})
public class SignRequestPanel extends JDialog  implements ActionListener {
	MainFrame parent;
	CodeInterface code;
	
	SubjectPanel subject_panel;
	IssuerPanel issuer_panel;
	JPanel info_panel, authorities_panel;
	JLabel version, serial_number, not_before, not_after;
	JComboBox<String> authorities,digest_algorithms;
	DefaultComboBoxModel<String> model[] = new DefaultComboBoxModel [Constants.NUM_OF_ALGORITHMS];
	JButton sign_button;
	
	SignRequestPanel(MainFrame parent, CodeInterface code) {
		this.parent = parent;
		this.code = code;
		
		setModal(true);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setTitle("Certificate Signing Request");
		setSize(745, 520);
		setLocation(300, 150);
		setFont(Font.getFont("Arial"));
		getContentPane().setLayout(null);
		
		subject_panel = new SubjectPanel(parent);
		subject_panel.setLocation(10, 170);
		subject_panel.enablePanel(false);
		
		issuer_panel = new IssuerPanel(parent);
		issuer_panel.setLocation(370, 170);
		issuer_panel.enablePanel(false);
		
		generateInfoPanel();
		generateAuthoritiesPanel();
		
		getContentPane().add(subject_panel);
		getContentPane().add(issuer_panel);
		getContentPane().add(info_panel);
		getContentPane().add(authorities_panel);
					
		setVisible(true);
	}
	
	private void generateInfoPanel() {
		info_panel = new JPanel();
		info_panel.setBounds(10, 10, 350, 150);
		info_panel.setLayout(null);
		Border b = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
		info_panel.setBorder(BorderFactory.createTitledBorder(b, "Info"));
		
		SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
		version = new JLabel("Version " + (parent.getVersion() + 1));		
		serial_number = new JLabel("Serial number: " + parent.getSerialNumber());		
		not_before = new JLabel("Not before: " + sdf.format(parent.getValidity(Constants.NOT_BEFORE)));		
		not_after = new JLabel("Not after: " + sdf.format(parent.getValidity(Constants.NOT_AFTER)));
		
		version.setBounds(10, 20, 100, 25);
		serial_number.setBounds(10, 55, 250, 25);
		not_before.setBounds(10, 85, 200, 25);
		not_after.setBounds(10, 110, 200, 25);
		
		info_panel.add(version);
		info_panel.add(serial_number);
		info_panel.add(not_before);
		info_panel.add(not_after);
		
		for (int i = 0; i < Constants.NUM_OF_INFO; i++)
			if (parent.supported_version > Constants.V1 || i != Constants.UI) {
				subject_panel.setValue(i, parent.getSubjectInfo(i));
				// issuer_panel.setValue(i, parent.getIssuerInfo(i));
			}	
	}
	
	private void generateAuthoritiesPanel() {
		authorities_panel = new JPanel();
		authorities_panel.setBounds(370, 10, 350, 150);
		authorities_panel.setLayout(null);
		Border b = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
		authorities_panel.setBorder(BorderFactory.createTitledBorder(b, "Choose issuer"));
		
		JLabel label = new JLabel("Choose a CA: ");
		label.setBounds(10, 20, 80, 25);		
		authorities = new JComboBox();
		authorities.setBounds(100, 20, 240, 25);
		authorities.addActionListener(this);
		
		digest_algorithms = new JComboBox();
		digest_algorithms.setBounds(100, 55, 240, 25);
		digest_algorithms.setEnabled(false);		
		
		sign_button = new JButton("Sign");
		sign_button.setBounds(100, 100, 150, 25);
		sign_button.setEnabled(false);
		sign_button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {

				if (code.signCertificate((String) authorities.getSelectedItem(), (String) digest_algorithms.getSelectedItem()))
					dispose();
			}			
		});
		
		populateComboboxes();
		
		authorities_panel.add(label);
		authorities_panel.add(authorities);;
		authorities_panel.add(digest_algorithms);
		authorities_panel.add(sign_button);
		
		
	}
	
	private void populateComboboxes() {
		for (int i = 0; i < Constants.NUM_OF_ALGORITHMS; i++) {
			model[i] = new DefaultComboBoxModel<> ();
			for (int j = 0; j < PublicKeyPanel.hashes[i].length; j++)
				model[i].addElement(PublicKeyPanel.hashes[i][j]);
		}
		
		List<String> certs = code.getIssuers(parent.toolbar_panel.keystore_panel.getSelectedValue());
		authorities.addItem("Choose Certificate");
		for (String alias : certs)
			authorities.addItem(alias);
		
		authorities.setSelectedIndex(0);
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		if (authorities.getSelectedIndex() == 0) {
			issuer_panel.resetPanel();
			digest_algorithms.setEnabled(false);
			sign_button.setEnabled(false);
			return;
		}
		
		sign_button.setEnabled(true);
		String keypair_name = (String) authorities.getSelectedItem();
		String issuer = code.getIssuer(keypair_name);
		issuer_panel.setInfo(issuer);
			
		digest_algorithms.setEnabled(true);
		String issuer_algorithm = code.getIssuerPublicKeyAlgorithm(keypair_name);
		switch (issuer_algorithm) {
		case "DSA": digest_algorithms.setModel(model[Constants.DSA]); break;
		case "RSA": digest_algorithms.setModel(model[Constants.RSA]); break;
		case "EC": digest_algorithms.setModel(model[Constants.EC]); break;
		}
		
	}

}
