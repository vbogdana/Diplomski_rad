package x509;

import code.CodeInterface;
import exceptions.GuiException;
import gui.GuiInterfaceV2;

public class GuiV3 extends GuiInterfaceV2 {

	public GuiV3(boolean[] algorithm_conf, CodeInterface code) throws GuiException {
		super(algorithm_conf, V3, code);
	}

}
