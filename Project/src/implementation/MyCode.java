package implementation;

import java.io.File;
import java.util.Enumeration;
import java.util.List;

import code.GuiException;
import x509.v3.CodeV3;

public class MyCode extends CodeV3 {

	public MyCode(boolean[] algorithm_conf, boolean[] extensions_conf)
			throws GuiException {
		super(algorithm_conf, extensions_conf);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Enumeration<String> loadLocalKeystore() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void resetLocalKeystore() {
		// TODO Auto-generated method stub

	}

	@Override
	public int loadKeypair(String keypair_name) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean saveKeypair(String keypair_name) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean removeKeypair(String keypair_name) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean importKeypair(String keypair_name, String file,
			String password) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean exportKeypair(String keypair_name, String file,
			String password) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean signCertificate(String issuer, String algorithm) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean importCertificate(File file, String keypair_name) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean exportCertificate(File file, int encoding) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String getIssuer(String keypair_name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getIssuerPublicKeyAlgorithm(String keypair_name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getRSAKeyLength(String keypair_name) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public List<String> getIssuers(String keypair_name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean generateCSR(String keypair_name) {
		// TODO Auto-generated method stub
		return false;
	}

}
