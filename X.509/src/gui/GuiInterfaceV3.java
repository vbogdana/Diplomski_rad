package gui;

import code.CodeInterface;
import code.GuiException;

public abstract class GuiInterfaceV3 extends GuiInterfaceV2 {
	
	public GuiInterfaceV3(boolean[] algorithm_conf, int supported_version, CodeInterface code) throws GuiException {
		super(algorithm_conf, supported_version, code);
	}
	
	// V3
	// ********************************************************************************************************
	// 												GETTERS
	// ********************************************************************************************************
	public boolean isCritical(int i) { return window.isCritical(i); }
	public String getPathLen() { return window.getPathLen(); }
	public boolean isCA() { return window.isCA(); }
	public boolean getEnabledKeyIdentifiers() { return window.getEnabledKID(); }
	
	
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

}
