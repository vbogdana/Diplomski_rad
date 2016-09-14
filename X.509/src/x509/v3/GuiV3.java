package x509.v3;

import code.CodeInterface;
import code.GuiException;
import gui.Constants;
import gui.GuiInterfaceV2;

public class GuiV3 extends GuiInterfaceV2 {

	public GuiV3(boolean[] algorithm_conf, CodeInterface code) throws GuiException {
		super(algorithm_conf, Constants.V3, code);
	}

}
