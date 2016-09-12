package gui;

import java.awt.Font;
import java.util.Date;

import javax.swing.JFrame;

import code.CodeInterface;
import gui.version3.ExtensionsPanel;

@SuppressWarnings("serial")
public class MainFrame extends JFrame {
	
	ToolbarPanel toolbar_panel;											// KEYSTORE PANEL	
	VersionPanel version_panel;											// VERSION PANEL	
	SerialNumberPanel serial_number_panel;								// SERIAL NUMBER PANEL
	InfoPanel issuer_panel;												// ISSUER PANEL
	ValidityPanel validity_panel;										// VALIDITY PANEL
	InfoPanel subject_panel;											// SUBJECT PANEL
	PublicKeyPanel public_key_panel;									// PUBLIC KEY PANEL
	// V3
	ExtensionsPanel extensions_panel;									// EXTENSIONS PANEL

	
	MainFrame(boolean algorithm_conf[], int supported_version, CodeInterface code) {
		// TODO
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setTitle("X.509 Certificate Manager");
		setSize(1300, 670);
		setLocation(30, 30);
		setFont(Font.getFont("Times New Roman"));
		getContentPane().setLayout(null);
		
		// configuration constructors
		toolbar_panel = new ToolbarPanel(this, code);		
		version_panel = new VersionPanel(this, supported_version);
		serial_number_panel = new SerialNumberPanel();
		issuer_panel = new IssuerPanel(this);
		subject_panel = new SubjectPanel(this);
		validity_panel = new ValidityPanel();
		public_key_panel = new PublicKeyPanel(this, algorithm_conf);
		// V3 TODO
		if (supported_version >= VersionPanel.V3)
			extensions_panel = new ExtensionsPanel();
		
		getContentPane().add(toolbar_panel);
		getContentPane().add(version_panel);
		getContentPane().add(serial_number_panel);
		getContentPane().add(issuer_panel);
		getContentPane().add(validity_panel);
		getContentPane().add(subject_panel);
		getContentPane().add(public_key_panel);
		// V3 TODO
		if (supported_version >= VersionPanel.V3)
			getContentPane().add(extensions_panel);
	
		setVisible(true);
	}
	
	void resetPanel() {
		toolbar_panel.resetPanel();
		version_panel.resetPanel();
		serial_number_panel.resetPanel();
		issuer_panel.resetPanel();
		subject_panel.resetPanel();
		validity_panel.resetPanel();
		public_key_panel.resetPanel();
		if (version_panel.getSupportedVersion() >= VersionPanel.V3)
			extensions_panel.resetPanel();
	}

	// ********************************************************************************************************
	// 										GETTERS AND SETTERS
	// ********************************************************************************************************
	
	int getVersion() { return version_panel.getVersion(); }
	String getSerialNumber() { return serial_number_panel.getSerialNumber(); }
	
	String getIssuerInfo(int i) { return issuer_panel.getValue(i); }
	String getIssuer() { return issuer_panel.getInfo(); }
	
	Date getValidity(int i) { return validity_panel.getDate(i); }
	
	String getSubjectInfo(int i) { return subject_panel.getValue(i); }
	String getSubject() { return subject_panel.getInfo(); }
	
	String getPublicKeyAlgorithm() { return public_key_panel.getAlgorithm(); }
	String getPublicKeyParameter(int i) { 
		if (i == 0)
			return public_key_panel.getAlgorithmParameter(i);
		else {
			if (public_key_panel.enabled[PublicKeyPanel.EC] && (public_key_panel.getAlgorithmIndex() == PublicKeyPanel.EC))
				return public_key_panel.getAlgorithmParameter(1);
			else
				return "EC Algorithm is not selected.";
			}
		}
	String getPublicKeySignatureAlgorithm() { return public_key_panel.getSignatureAlgorithm(); }
	
}
