package gui;

import code.CodeInterface;
import code.GuiException;

public abstract class GuiInterfaceV3 extends GuiInterfaceV2 {
	private boolean [] extensions_conf;
	public GuiInterfaceV3(boolean[] algorithm_conf, int supported_version, boolean[] extensions_conf, CodeInterface code) throws GuiException {
		super(algorithm_conf, supported_version, extensions_conf, code);
		this.extensions_conf = extensions_conf;
	}
	
	public boolean isSupported(int i) { return extensions_conf[i]; }
	// V3
	// ********************************************************************************************************
	// 												GETTERS
	// ********************************************************************************************************
	public boolean isCritical(int i) { return window.isCritical(i); }
	public String getPathLen() { return window.getPathLen(); }
	public boolean isCA() { return window.isCA(); }
	public boolean getEnabledKeyIdentifiers() { return window.getEnabledKID(); }
	public boolean [] getKeyUsage() { return window.getKeyUsage(); }
	public String getCpsUri() { return window.getCpsUri(); }
	public boolean getAnyPolicy() { return window.getAnyPolicy(); }
	public String[] getAlternativeName(int i) { return window.getAlternativeName(i); }
	public String getSubjectDirectoryAttribute(int i) { return window.getSubjectDirectoryAttribute(i); }
	public String getGender() { return window.getGender(); }
	public String getDateOfBirth() { return window.getDateOfBirth(); }
	public boolean [] getExtendedKeyUsage() { return window.getExtendedKeyUsage(); }
	
	// ********************************************************************************************************
	// 												SETTERS
	// ********************************************************************************************************
	public void setCritical(int i, boolean v) { window.setCritical(i, v); }
	public void setPathLen(String v) { window.setPathLen(v); }
	public void setCA(boolean v) { window.setCA(v); }
	public void setEnabledKeyIdentifiers(boolean v) { window.setEnabledKID(v); }
	public void setAuthorityKeyID(String authorityKeyID) { window.setAuthorityKeyID(authorityKeyID); }
	public void setAuthorityIssuer(String authorityIssuer) { window.setAuthorityIssuer(authorityIssuer); }
	public void setAuthoritySerialNumber(String authoritySerialNumber) { window.setAuthoritySerialNumber(authoritySerialNumber); }
	public void setSubjectKeyID(String subjectKeyID) { window.setSubjectKeyID(subjectKeyID); }
	public void setKeyUsage( boolean [] key_usage) { window.setKeyUsage(key_usage); }
	public void setCpsUri(String v) { window.setCpsUri(v); }
	public void setAnyPolicy(boolean v) { window.setAnyPolicy(v); }
	public void setAlternativeName(int i, String v) { window.setAlternativeName(i, v); }
	public void setSubjectDirectoryAttribute(int i, String v) { window.setSubjectDirectoryAttribute(i, v); }
	public void setGender(String v) { window.setGender(v); }
	public void setDateOfBirth(String v) { window.setDateOfBirth(v); }
	public void setExtendedKeyUsage( boolean [] key_usage) { window.setExtendedKeyUsage(key_usage); }
	
}
