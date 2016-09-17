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
	
	
	
	// ********************************************************************************************************
	// 												SETTERS
	// ********************************************************************************************************
	public void setCritical(int i, boolean v) { window.setCritical(i, v); }
	public void setPathLen(String v) { window.setPathLen(v); }
	public void setCA(boolean v) { window.setCA(v); }

}