package code;

import java.io.File;
import java.util.Enumeration;
import java.util.List;

public interface CodeInterface {
	public abstract Enumeration<String> loadLocalKeystore();
	public abstract void resetLocalKeystore();
	// loadKey must return: -1 error, 0 not signed, 1 signed, 2 in case trusted certificate
	public abstract int loadKeypair(String keypair_name);		
	public abstract boolean saveKeypair(String keypair_name);
	public abstract boolean removeKeypair(String keypair_name);	
	public abstract boolean importKeypair(String keypair_name, String file, String password);
	public abstract boolean exportKeypair(String keypair_name, String file, String password);
	public abstract boolean signCertificate(String issuer, String algorithm);
	public abstract boolean importCertificate(File file, String keypair_name);
	public abstract boolean exportCertificate(File file, int encoding);
	public abstract String getIssuer(String keypair_name);
	public abstract String getIssuerPublicKeyAlgorithm(String keypair_name);
	// getRSAKeyLength returns the length of a RSA public key in bits, if RSA algorithm is supported
	public abstract int getRSAKeyLength(String keypair_name); 
	public abstract List<String> getIssuers(String keypair_name);
	public abstract boolean generateCSR(String keypair_name);
}
