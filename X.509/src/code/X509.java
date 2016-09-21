package code;

import implementation.MyCode;
import gui.GuiInterface;

// this class should be given to the students
// along with the jar GUI file for the selected version
// it is up to the teacher to edit this file depending on the desired GUI
public class X509 {

	public static void main(String[] args) {
		try {
			// configuration array for supported public key algorithms		
			// true - enabled / false - disabled
			// sorted by their array index - DSA, RSA, GOST (not implemented), EC
			boolean algorithm_conf[] = { true, true, false, true };
			// configuration array for supported v3 extensions			
			// true - enabled / false - disabled
			// sorted by their array index
			boolean extensions_conf[] = { true, 		// Authority Key ID
										  true, 		// Subject Key ID (goes together with authority key id)
										  true, 		// Key Usage
										  true, 		// Certificate policies 
										  true, 		// Policy mappings (not implemented)
										  true,  		// Subject Alternative Names
										  true, 		// Issuer Alternative Names 
										  true, 		// Subject Directory Attributes 
										  true, 		// Basic constraints
										  true, 		// Name constraints
										  true, 		// Policy constraints 
										  true, 		// Extended key usage
										  true, 		// CRL Distribution Points
										  true, 		// Inhibit anyPolicy 
										  true 			// Freshest CRL (a.k.a. Delta CRL Distribution Point)
										};
			// class MyCode is implemented by students
			// it should extend class CodeVX for jar file (X depends of a x509 supported version, it can be 1 or 3)
			// class CodeVX has a field "access" which represents an access point to the GUI using getters/setters
			@SuppressWarnings("unused")
			CodeInterface my_code = new MyCode(algorithm_conf, extensions_conf);
		} catch (GuiException e) {
			// Handles frame construction exceptions
			GuiInterface.reportError(e);
		}
	}

}
