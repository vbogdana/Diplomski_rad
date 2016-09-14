package code;

import java.util.Enumeration;

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
	

}
