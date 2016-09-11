package gui;

import java.awt.Font;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

@SuppressWarnings("serial")
public class MainFrame extends JFrame {
	
	KeyStorePanel keystore_panel = new KeyStorePanel();					// KEYSTORE PANEL	
	VersionPanel version_panel = new VersionPanel(this);				// VERSION PANEL	
	SerialNumberPanel serial_number_panel = new SerialNumberPanel();	// SERIAL NUMBER PANEL
	InfoPanel issuer_panel = new IssuerPanel();							// ISSUER PANEL
	ValidityPanel validity_panel = new ValidityPanel();					// VALIDITY PANEL
	public InfoPanel subject_panel = new SubjectPanel();						// SUBJECT PANEL
	PublicKeyPanel public_key_panel;									// PUBLIC KEY PANEL
	// Version 3
	ExtensionsPanel extensions_panel = new ExtensionsPanel();			// EXTENSIONS PANEL

	
	public MainFrame() throws CustomException {
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setTitle("X.509 Certificate Manager");
		setSize(1300, 670);
		setLocation(30, 30);
		setFont(Font.getFont("Times New Roman"));
		getContentPane().setLayout(null);
		
		public_key_panel = new PublicKeyPanel(this);
		subject_panel.setValue(InfoPanel.SA, public_key_panel.getSignatureAlgorithm());
		
		getContentPane().add(keystore_panel);
		getContentPane().add(version_panel);
		getContentPane().add(serial_number_panel);
		getContentPane().add(issuer_panel);
		getContentPane().add(validity_panel);
		getContentPane().add(subject_panel);
		getContentPane().add(public_key_panel);
		getContentPane().add(extensions_panel);
		
		setVisible(true);
		
	}
	
	public static void reportError(String err) {
		JOptionPane.showMessageDialog(null, err, "Error", JOptionPane.ERROR_MESSAGE);
	}

}
