package gui;

import code.CodeInterface;
import code.GuiException;

public abstract class GuiInterfaceV2 extends GuiInterface {

	public GuiInterfaceV2(boolean[] algorithm_conf, int supported_version, CodeInterface code) throws GuiException {
		super(algorithm_conf, supported_version, code);
	}
	
	// V2+
	public String getSubjectUniqueIdentifier() { return window.getSubjectInfo(Constants.UI); }
	public void setSubjectUniqueIdentifier(String v) { window.setSubjectInfo(Constants.UI, v); }
	
	public String getIssuerUniqueIdentifier() { return window.getIssuerInfo(Constants.UI); }
	public void setIssuerUniqueIdentifier(String v) { window.setIssuerInfo(Constants.UI, v); }

}
