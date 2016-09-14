package gui;

import java.awt.Font;
import java.util.Date;
import java.util.Enumeration;

import javax.swing.JFrame;

import code.CodeInterface;

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
		setSize(1310, 670);
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
		if (supported_version >= Constants.V3)
			extensions_panel = new ExtensionsPanel();
		
		getContentPane().add(toolbar_panel);
		getContentPane().add(version_panel);
		getContentPane().add(serial_number_panel);
		getContentPane().add(issuer_panel);
		getContentPane().add(validity_panel);
		getContentPane().add(subject_panel);
		getContentPane().add(public_key_panel);
		// V3 TODO
		if (supported_version >= Constants.V3)
			getContentPane().add(extensions_panel);
		
		loadKeystore(code.loadKeystore());
	
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
		if (version_panel.getSupportedVersion() >= Constants.V3)
			extensions_panel.resetPanel();
	}
	
	void enablePanel(boolean flag) {
		//toolbar_panel.enablePanel(flag);
		version_panel.enablePanel(flag);
		serial_number_panel.enablePanel(flag);
		issuer_panel.enablePanel(flag);
		subject_panel.enablePanel(flag);
		validity_panel.enablePanel(flag);
		public_key_panel.enablePanel(flag);
		if (version_panel.getSupportedVersion() >= Constants.V3)
			extensions_panel.enablePanel(flag);
	}

	// ********************************************************************************************************
	// 												GETTERS
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
			if (public_key_panel.enabled[Constants.EC] && (public_key_panel.getAlgorithmIndex() == Constants.EC))
				return public_key_panel.getAlgorithmParameter(1);
			else
				return "EC Algorithm is not selected.";
			}
		}
	String getPublicKeySignatureAlgorithm() { return public_key_panel.getSignatureAlgorithm(); }
	
	// ********************************************************************************************************
	// 												SETTERS
	// ********************************************************************************************************
	
	void enableSignButton(boolean flag) { toolbar_panel.manage_panel.enableSignButton(flag); }

	void addKeypair(String name) { toolbar_panel.keystore_panel.addKeypair(name); }
	void loadKeystore(Enumeration<String> certificates) { toolbar_panel.keystore_panel.loadKeystore(certificates); }
	
	void setVersion(int i) { version_panel.setVersion(i); }
	void setSerialNumber(String v) { serial_number_panel.setSerialNumber(v); }
	
	void setIssuerInfo(int i, String v) { issuer_panel.setValue(i, v); }
	void setIssuer(String v) { issuer_panel.setInfo(v); }
	
	void setValidity(int i, Date d) { validity_panel.setDate(i, d); }
	
	void setSubjectInfo(int i, String v) { subject_panel.setValue(i, v); }
	void setSubject(String v) { subject_panel.setInfo(v); }
	
	void setPublicKeyAlgorithm(String v) { public_key_panel.setAlgorithm(v); }
	void setPublicKeyParameter(int i, String v) { 
		if (i == 0)
			public_key_panel.setAlgorithmParameter(i, v);
		else if (public_key_panel.enabled[Constants.EC] && (public_key_panel.getAlgorithmIndex() == Constants.EC))
			public_key_panel.setAlgorithmParameter(i, v);
	}
	void setPublicKeySignatureAlgorithm(String v) { public_key_panel.setSignatureAlgorithm(v); }
	
	
}