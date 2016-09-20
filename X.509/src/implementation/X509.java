package implementation;

import gui.GuiInterface;
import code.CodeInterface;
import code.GuiException;

// this class should be given to the students
// along with the jar GUI file for the selected version
public class X509 {

	public static void main(String[] args) {
		try {
			// configuration array for supported public key algorithms
			// sorted by their array index - DSA, RSA, GOST, EC
			// true - enabled / false - disabled
			boolean algorithm_conf[] = { true, true, false, true };
			
			// class MyCode is implemented by students
			// it should extend class CodeVX for jar file (X depends of a x509 supported version, it can be 1 or 3)
			// class CodeVX has a field access which represents an access point to the GUI using getters/setters
			@SuppressWarnings("unused")
			CodeInterface my_code = new MyCode(algorithm_conf);
		} catch (GuiException e) {
			// Handles frame construction exceptions
			GuiInterface.reportError(e);
		}
	}

}
