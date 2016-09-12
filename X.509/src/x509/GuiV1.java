package x509;

import code.CodeInterface;
import exceptions.GuiException;
import gui.GuiInterface;

public class GuiV1 extends GuiInterface {

	public GuiV1(boolean[] algorithm_conf, CodeInterface code) throws GuiException {
		super(algorithm_conf, V1, code);
	}


}
