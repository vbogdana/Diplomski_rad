package x509.v1;

import code.CodeInterface;
import code.GuiException;

public abstract class CodeV1 implements CodeInterface {

	protected GuiV1 access;
	
	public CodeV1(boolean[] conf) throws GuiException {
		this.access = new GuiV1(conf, this);
	}

}
