package code;

import java.util.Enumeration;
import java.util.List;

public interface CodeInterface {
	public abstract Enumeration<String> loadKeystore();
	public abstract boolean loadKey(String keypair_name);
	public abstract boolean saveKey(String keypair_name);
	public abstract void resetLocalKeyStore();
	public abstract boolean importKeypair(String keypair_name, String file, String password);
	public abstract boolean exportKeypair(String keypair_name, String file, String password);
	public abstract void signCertificate();
	public abstract void importCertificate();
	public abstract void exportCertificate();
	public abstract String getIssuer(String keypair_name);
	public abstract List<String> getIssuers();
	public abstract boolean generateCSR(String keypair_name);
	

}
