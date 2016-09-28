package code;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

import implementation.MyCode;
import gui.Constants;
import gui.GuiInterfaceV1;

// this class should be given to the students
// inside the jar GUI file for the selected version
// it is up to the teacher to edit this file depending on the desired GUI
public class X509 {
	static boolean [] algorithm_conf;
	static boolean [] extensions_conf;
	
	public static void main(String[] args) {
		try {
			
			if (args.length == 0) {
				// configuration array for supported public key algorithms		
				// true - enabled / false - disabled
				// sorted by their array index - DSA, RSA, GOST (not implemented), EC
				boolean [] algorithm = { true, true, false, true };
				algorithm_conf = algorithm;
				
				// configuration array for supported v3 extensions			
				// true - enabled / false - disabled
				// sorted by their array index
				boolean [] extensions = { 
										  true, 		// Authority Key ID
										  true, 		// Subject Key ID (goes together with authority key id)
										  true, 		// Key Usage
										  true, 		// Certificate policies 
										  false, 		// Policy mappings (not implemented)
										  true,  		// Subject Alternative Names
										  true, 		// Issuer Alternative Names 
										  true, 		// Subject Directory Attributes 
										  true, 		// Basic constraints
										  false, 		// Name constraints	(not implemented)
										  false, 		// Policy constraints (not implemented)
										  true, 		// Extended key usage
										  false, 		// CRL Distribution Points (not implemented)
										  true, 		// Inhibit anyPolicy 
										  false 		// Freshest CRL (a.k.a. Delta CRL Distribution Point) (not implemented)
										};
				extensions_conf = extensions;
			} else 
				X509.readConfigurationFromFile(args[0]);
			
			// class MyCode is implemented by students
			// it should be in the package "implementation"
			// it should extend class CodeVX from jar file (X depends of a x509 supported version, it can be 1 or 3)
			// class CodeVX has a field "access" which represents an access point to the GUI using getters/setters
			@SuppressWarnings("unused")
			CodeInterface my_code = new MyCode(algorithm_conf, extensions_conf);
			
		} catch (GuiException | IOException e) {
			// Handles frame construction exceptions
			GuiInterfaceV1.reportError(e);
		}
	}

	private static void readConfigurationFromFile(String path) throws GuiException, IOException {		
		File file = new File(path);       
		if (file.exists()) {
			String line;
	        boolean [] algorithm = new boolean [Constants.NUM_OF_ALGORITHMS];
	        boolean [] extensions = new boolean [Constants.NUM_OF_EXTENSIONS];
		    java.io.FileInputStream fis = null;
		    try {
		        fis = new java.io.FileInputStream(path);	   
		        InputStreamReader isr = new InputStreamReader(fis);
		        BufferedReader br = new BufferedReader(isr);
		        
		        while ((line = br.readLine()) != null) {		            
		        	switch (line.toLowerCase()) {
		        	// algorithms
		        	case "dsa": algorithm[Constants.DSA] = true; break;
		        	case "rsa": algorithm[Constants.RSA] = true; break;
		        	case "ec": algorithm[Constants.EC] = true; break;
		        	// extensions
		        	case "key identifiers": extensions[Constants.AKID] = true; extensions[Constants.SKID] = true; break;
		        	case "key usage": extensions[Constants.KU] = true; break;
		        	case "certificate policies": extensions[Constants.CP] = true; break;
		        	case "subject alternative name": extensions[Constants.SAN] = true; break;
		        	case "issuer alternative name": extensions[Constants.IAN] = true; break;
		        	case "subject directory attributes": extensions[Constants.SDA] = true; break;
		        	case "basic constraints": extensions[Constants.BC] = true; break;
		        	case "extended key usage": extensions[Constants.EKU] = true; break;
		        	case "inhibit any policy": extensions[Constants.IAP] = true; break;
		        	// unrecognized
		        	default: GuiInterfaceV1.reportError("Unsupported algorithm or extension: " + line); break;
		        	}
		        }
		        if (br != null)
		        	br.close();       
		    } finally {
		    	extensions_conf = extensions;
		    	algorithm_conf = algorithm;
		        if (fis != null)
		            fis.close();	        
		    }
		} else
			throw new GuiException("Configuration file doesn't exist.");
	}

}
