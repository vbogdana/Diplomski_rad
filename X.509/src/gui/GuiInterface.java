package gui;

import java.util.Date;

import javax.swing.JOptionPane;

import code.CodeInterface;
import code.GuiException;

public abstract class GuiInterface {
	
	protected MainFrame window;
	protected boolean algorithm_conf[];
	
	protected GuiInterface(boolean algorithm_conf[], int supported_version, CodeInterface code) throws GuiException {
		this.algorithm_conf = algorithm_conf;
		int algorithm = -1;
		for (int i = 0; i < Constants.NUM_OF_ALGORITHMS; i++)
			if (algorithm_conf[i]) {
				algorithm = i;
				break;
			}
		
		if (algorithm == -1)
			throw new GuiException("At least one public key algorithm must be enabled.");
		
		window = new MainFrame(algorithm_conf, supported_version, code);
	}
	
	public static void reportError(Exception e) {
		JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
	}
	
	public static void reportError(String msg) {
		JOptionPane.showMessageDialog(null, msg, "Error", JOptionPane.ERROR_MESSAGE);
	}
	
	// ********************************************************************************************************
	// 												GETTERS
	// ********************************************************************************************************
	
	public String getSubject() { return window.getSubject(); }
	public String getSubjectCountry() { return window.getSubjectInfo(Constants.C); }
	public String getSubjectState() { return window.getSubjectInfo(Constants.ST); }
	public String getSubjectLocality() { return window.getSubjectInfo(Constants.L); }
	public String getSubjectOrganization() { return window.getSubjectInfo(Constants.O); }
	public String getSubjectOrganizationUnit() { return window.getSubjectInfo(Constants.OU); }
	public String getSubjectCommonName() { return window.getSubjectInfo(Constants.CN); }
	
	public String getIssuer() { return window.getIssuer(); }
	public String getIssuerSignatureAlgorithm() { return window.getIssuerInfo(Constants.SA); }
	
	public int getVersion() { return window.getVersion(); }
	public String getSerialNumber() { return window.getSerialNumber(); }
	public Date getNotBefore() { return window.getValidity(Constants.NOT_BEFORE); }
	public Date getNotAfter() { return window.getValidity(Constants.NOT_AFTER); }
	
	public String getPublicKeyAlgorithm() { return window.getPublicKeyAlgorithm(); }
	public String getPublicKeyParameter() { return window.getPublicKeyParameter(0); }
	public String getPublicKeySignatureAlgorithm() { return window.getPublicKeySignatureAlgorithm(); }
	// in case that EC algorithm is enabled
	public String getPublicKeyECCurve() { return window.getPublicKeyParameter(1); }
	
	// ********************************************************************************************************
	// 												SETTERS
	// ********************************************************************************************************
	
	public void enableSignButton(boolean flag) { window.enableSignButton(flag); }
	public void enableExportButton(boolean flag) { window.enableExportCertificateButton(flag); }
	
	public void addKeypair(String name) { window.addKeypair(name); }
	
	public void setSubject(String v) { window.setSubject(v); }
	public void setSubjectCountry(String v) { window.setSubjectInfo(Constants.C, v); }
	public void setSubjectState(String v) { window.setSubjectInfo(Constants.ST, v); }
	public void setSubjectLocality(String v) { window.setSubjectInfo(Constants.L, v); }
	public void setSubjectOrganization(String v) { window.setSubjectInfo(Constants.O, v); }
	public void setSubjectOrganizationUnit(String v) { window.setSubjectInfo(Constants.OU, v); }
	public void setSubjectCommonName(String v) { window.setSubjectInfo(Constants.CN, v); }
	public void setSubjectSignatureAlgorithm(String v) { window.setSubjectInfo(Constants.SA, v); }
	
	public void setIssuer(String v) { window.setIssuer(v); }
	public void setIssuerSignatureAlgorithm(String v) { window.setIssuerInfo(Constants.SA, v); }
	
	public void setVersion(int i) { window.setVersion(i); }
	public void setSerialNumber(String v) { window.setSerialNumber(v); }
	public void setNotBefore(Date d) { window.setValidity(Constants.NOT_BEFORE, d); }
	public void setNotAfter(Date d) { window.setValidity(Constants.NOT_AFTER, d); }
	
	public void setPublicKeyAlgorithm(String v) { window.setPublicKeyAlgorithm(v); }
	public void setPublicKeyParameter(String v) { window.setPublicKeyParameter(0, v); }
	public void setPublicKeySignatureAlgorithm(String v) { window.setPublicKeySignatureAlgorithm(v); }
	// in case that EC algorithm is enabled
	public void setPublicKeyECCurve(String v) { window.setPublicKeyParameter(1, v); }

}
