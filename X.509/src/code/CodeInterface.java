package code;

import x509.GuiV1;
import x509.GuiV2;
import x509.GuiV3;

public interface CodeInterface {
	
	public abstract void saveKey();
	public abstract void resetLocalKeyStore();
	public abstract void importKeypair();
	public abstract void exportKeypair();
	public abstract void signCertificate();
	public abstract void importCertificate();
	public abstract void exportCertificate();

}

abstract class CodeV1 implements CodeInterface {
	GuiV1 access;
	
	public CodeV1(GuiV1 access) {
		this.access = access;
	}
}

abstract class CodeV2 implements CodeInterface {
	GuiV2 access;
	
	public CodeV2(GuiV2 access) {
		this.access = access;
	}
}

abstract class CodeV3 implements CodeInterface {
	GuiV3 access;
	
	public CodeV3(GuiV3 access) {
		this.access = access;
	}
}
