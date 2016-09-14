package x509.v2;

import code.CodeInterface;
import code.GuiException;
import gui.Constants;
import gui.GuiInterfaceV2;

public class GuiV2 extends GuiInterfaceV2 {

	public GuiV2(boolean[] algorithm_conf, CodeInterface code) throws GuiException {
		super(algorithm_conf, Constants.V2, code);
	}

}
