package x509.v3;

import code.CodeInterface;
import code.GuiException;
import gui.Constants;
import gui.GuiInterfaceV3;

public class GuiV3 extends GuiInterfaceV3 {

	public GuiV3(boolean[] algorithm_conf, boolean[] extensions_conf, CodeInterface code) throws GuiException {
		super(algorithm_conf, Constants.V3, extensions_conf, code);
	}

}
