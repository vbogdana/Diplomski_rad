package gui;

import code.CodeInterface;
import exceptions.GuiException;

public abstract class GuiInterfaceV2 extends GuiInterface {

	public GuiInterfaceV2(boolean[] algorithm_conf, int supported_version, CodeInterface code) throws GuiException {
		super(algorithm_conf, supported_version, code);
	}
	
	//V2+
	public String getSubjectUniqueIdentifier() { return window.getSubjectInfo(UI); }

}
