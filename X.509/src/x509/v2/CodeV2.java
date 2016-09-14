package x509.v2;

import code.CodeInterface;
import code.GuiException;

abstract class CodeV2 implements CodeInterface {
	protected GuiV2 access;
	
	public CodeV2(boolean[] conf) throws GuiException {
		this.access = new GuiV2(conf, this);
	}
}
