package implementation;

import java.util.Enumeration;
import java.util.List;

import code.GuiException;
import x509.v1.CodeV1;

public class MyCodeV1 extends CodeV1 {

	public MyCodeV1(boolean[] conf) throws GuiException {
		super(conf);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Enumeration<String> loadKeystore() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean loadKey(String keypair_name) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean saveKey(String keypair_name) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void resetLocalKeyStore() {
		// TODO Auto-generated method stub
		
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
	public void signCertificate() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void importCertificate() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void exportCertificate() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getIssuer(String keypair_name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> getIssuers() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean generateCSR(String keypair_name) {
		// TODO Auto-generated method stub
		return false;
		
	}

}
