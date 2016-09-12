package x509;

import code.CodeInterface;
import exceptions.GuiException;
import gui.GuiInterfaceV2;

public class GuiV2 extends GuiInterfaceV2 {

	public GuiV2(boolean[] algorithm_conf, CodeInterface code) throws GuiException {
		super(algorithm_conf, V2, code);
	}

}
