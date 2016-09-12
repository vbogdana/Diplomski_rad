package gui;

import java.util.Date;

import javax.swing.JOptionPane;

import code.CodeInterface;
import exceptions.GuiException;

public abstract class GuiInterface {
	
	// SUBJECT OID'S
	public static final int C = 0, ST = 1, L = 2, O = 3, OU = 4, CN = 5, SA = 6, UI = 7;
	public static final int V1 = 0, V2 = 1, V3 = 2;
	public static final int NOT_BEFORE = 0, NOT_AFTER = 1;
	public static final int DSA = 0, RSA = 1, GOST = 2, EC = 3, NUM_OF_ALGORITHMS = 4;	
	
	protected MainFrame window;
	private boolean algorithm_conf[];
	
	protected GuiInterface(boolean algorithm_conf[], int supported_version, CodeInterface code) throws GuiException {
		this.algorithm_conf = algorithm_conf;
		int algorithm = -1;
		for (int i = 0; i < NUM_OF_ALGORITHMS; i++)
			if (algorithm_conf[i]) {
				algorithm = i;
				break;
			}
		
		if (algorithm == -1)
			throw new GuiException("At least one public key algorithm must be enabled.");
		
		window = new MainFrame(algorithm_conf, supported_version, code);		
	}
	
	public static void reportError(Exception e) {
		JOptionPane.showMessageDialog(null, e.toString(), "Error", JOptionPane.ERROR_MESSAGE);
	}
	
	// ********************************************************************************************************
	// 										GETTERS AND SETTERS
	// ********************************************************************************************************
	
	public String getSubjectInfo() { return window.getSubject(); }
	public String getSubjectCountry() { return window.getSubjectInfo(C); }
	public String getSubjectState() { return window.getSubjectInfo(ST); }
	public String getSubjectLocality() { return window.getSubjectInfo(L); }
	public String getSubjectOrganization() { return window.getSubjectInfo(O); }
	public String getSubjectOrganizationUnit() { return window.getSubjectInfo(OU); }
	public String getSubjectCommonName() { return window.getSubjectInfo(CN); }
	// public String getSubjectSignatureAlgorithm() { return window.getSubjectInfo(SA); }
	
	public int getVersion() { return window.getVersion(); }
	public String getSerialNumber() { return window.getSerialNumber(); }
	public Date getNotBefore() { return window.getValidity(NOT_BEFORE); }
	public Date getNotAfter() { return window.getValidity(NOT_AFTER); }
	
	public String getPublicKeyAlgorithm() { return window.getPublicKeyAlgorithm(); }
	public String getPublicKeyParameter() { return window.getPublicKeyParameter(0); }
	public String getPublicKeySignatureAlgorithm() { return window.getPublicKeySignatureAlgorithm(); }
	// in case that EC algorithm is enabled
	public String getPublicKeyECCurve() { return window.getPublicKeyParameter(1); }

}
