package x509.v1;

import code.CodeInterface;
import code.GuiException;
import gui.Constants;
import gui.GuiInterface;

public class GuiV1 extends GuiInterface {

	public GuiV1(boolean[] algorithm_conf, CodeInterface code) throws GuiException {
		super(algorithm_conf, Constants.V1, null, code);
	}


}
