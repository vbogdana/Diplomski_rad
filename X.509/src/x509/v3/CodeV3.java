package x509.v3;

import code.CodeInterface;
import code.GuiException;

public abstract class CodeV3 implements CodeInterface {
	protected GuiV3 access;
	
	public CodeV3(boolean[] algorithm_conf, boolean[] extensions_conf) throws GuiException {
		this.access = new GuiV3(algorithm_conf, extensions_conf, this);
	}
}
