package code;

import java.io.File;
import java.util.Enumeration;
import java.util.List;

public interface CodeInterface {
	public abstract Enumeration<String> loadKeystore();
	public abstract int loadKey(String keypair_name);		// must return: -1 error, 0 not signed, >0 signed
	public abstract boolean saveKey(String keypair_name);
	public abstract void resetLocalKeyStore();
	public abstract boolean importKeypair(String keypair_name, String file, String password);
	public abstract boolean exportKeypair(String keypair_name, String file, String password);
	public abstract boolean signCertificate(String issuer);
	public abstract void importCertificate();
	public abstract void exportCertificate(File file, String format);
	public abstract String getIssuer(String keypair_name);
	public abstract List<String> getIssuers();
	public abstract boolean generateCSR(String keypair_name);
	

}
