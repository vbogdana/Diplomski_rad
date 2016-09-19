package gui;

import java.awt.Font;
import java.util.Date;
import java.util.Enumeration;

import javax.swing.JFrame;

import code.CodeInterface;

@SuppressWarnings("serial")
public class MainFrame extends JFrame {
	int supported_version;
	
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
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setTitle("X.509 Certificate Manager");
		setSize(1310, 670);
		setLocation(30, 30);
		setFont(Font.getFont("Times New Roman"));
		getContentPane().setLayout(null);
		
		this.supported_version = supported_version;
		
		// configuration constructors
		toolbar_panel = new ToolbarPanel(this, code);
		version_panel = new VersionPanel(this, supported_version);
		serial_number_panel = new SerialNumberPanel();
		issuer_panel = new IssuerPanel(this);
		subject_panel = new SubjectPanel(this);
		validity_panel = new ValidityPanel();
		public_key_panel = new PublicKeyPanel(this, algorithm_conf);
		// V3
		if (supported_version >= Constants.V3)
			extensions_panel = new ExtensionsPanel(this);
		
		getContentPane().add(toolbar_panel);
		getContentPane().add(version_panel);
		getContentPane().add(serial_number_panel);
		getContentPane().add(issuer_panel);
		getContentPane().add(validity_panel);
		getContentPane().add(subject_panel);
		getContentPane().add(public_key_panel);
		// V3
		if (supported_version >= Constants.V3)
			getContentPane().add(extensions_panel);
		
		loadKeystore(code.loadKeystore());
	
		setVisible(true);
	}
	
	void resetPanel() {
		// toolbar_panel.resetPanel();
		version_panel.resetPanel();
		serial_number_panel.resetPanel();
		issuer_panel.resetPanel();
		subject_panel.resetPanel();
		validity_panel.resetPanel();
		public_key_panel.resetPanel();
		if (supported_version >= Constants.V3)
			extensions_panel.resetPanel();
	}
	
	void enablePanel(boolean flag) {
		// toolbar_panel.enablePanel(flag);
		version_panel.enablePanel(flag);
		serial_number_panel.enablePanel(flag);
		issuer_panel.enablePanel(flag);
		subject_panel.enablePanel(flag);
		validity_panel.enablePanel(flag);
		public_key_panel.enablePanel(flag);
		if (supported_version >= Constants.V3)
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
	
	// V3
	boolean isCritical(int i) { return ((supported_version >= Constants.V3) ? extensions_panel.getCritical(i) : false); }
	String getPathLen() { return ((supported_version >= Constants.V3) ? extensions_panel.basic_constraints_panel.getPathLen() : ""); }
	boolean isCA() { return ((supported_version >= Constants.V3) ? extensions_panel.basic_constraints_panel.isCertificateAuthority() : false); }
	boolean getEnabledKID() { return extensions_panel.key_identifiers_panel.getIsEnabled(); }
	boolean [] getKeyUsage() {
		boolean [] key_usage = new boolean [Constants.NUM_OF_KU];
		for (int i = 0; i < Constants.NUM_OF_KU; i++)
			key_usage[i] = extensions_panel.key_usage_panel.getKeyUsage(i);
		return key_usage;
	}
	
	// ********************************************************************************************************
	// 												SETTERS
	// ********************************************************************************************************
	
	void enableSignButton(boolean flag) { toolbar_panel.manage_panel.enableSignButton(flag); }
	void enableExportCertificateButton(boolean flag) { toolbar_panel.manage_panel.enableExportCertificateButton(flag); }

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
	
	// V3
	void setCritical(int i, boolean v) { if (supported_version >= Constants.V3) extensions_panel.setCritical(i, v); }
	void setPathLen(String v) { if (supported_version >= Constants.V3) extensions_panel.basic_constraints_panel.setPathLen(v); }
	void setCA(boolean v) { if (supported_version >= Constants.V3) extensions_panel.basic_constraints_panel.setCertificateAuthority(v); }
	void setEnabledKID(boolean v) { extensions_panel.key_identifiers_panel.setIsEnabled(v); }
	void setAuthorityKeyID(String authorityKeyID) { extensions_panel.key_identifiers_panel.setAuthorityKeyID(authorityKeyID); }
	void setAuthorityIssuer(String authorityIssuer) { extensions_panel.key_identifiers_panel.setAuthorityIssuer(authorityIssuer); }
	void setAuthoritySerialNumber(String authoritySerialNumber) { extensions_panel.key_identifiers_panel.setAuthoritySerialNumber(authoritySerialNumber); }
	void setSubjectKeyID(String subjectKeyID) { extensions_panel.key_identifiers_panel.setSubjectKeyID(subjectKeyID); }
	void setKeyUsage(boolean [] key_usage) {
		for (int i = 0; i < Constants.NUM_OF_KU; i++)
			extensions_panel.key_usage_panel.setKeyUsage(i, key_usage[i]);
	}
}
