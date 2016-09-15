package implementation;

import gui.Constants;
import gui.GuiInterface;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.KeyStore.PrivateKeyEntry;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.UnrecoverableEntryException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Enumeration;

import org.bouncycastle.operator.OperatorCreationException;

import code.GuiException;
import x509.v3.CodeV3;

public class MyCodeV3 extends CodeV3 {
	
	private MyX509Cert current_cert;
	private PrivateKeyEntry current_keyentry;
	
	public MyCodeV3(boolean[] conf) throws GuiException {
		super(conf);		
	}
	
	@Override
	public Enumeration<String> loadKeystore() {
		Enumeration<String> certificates = null;
		try {
			MyKeyStore.load(MyKeyStore.localKeyStore, MyKeyStore.localPassword);
			certificates = MyKeyStore.ks.aliases();
		} catch (KeyStoreException | NoSuchAlgorithmException | CertificateException | IOException e) {
			GuiInterface.reportError(e);
		}
		
		return certificates;
	}
	
	@Override
	public boolean loadKey(String keypair_name) {
		try {
			MyKeyStore.load(MyKeyStore.localKeyStore, MyKeyStore.localPassword);
			PrivateKeyEntry entry = MyKeyStore.getKey(keypair_name, MyKeyStore.localPassword);
			X509Certificate cert = (X509Certificate) entry.getCertificate();
			current_keyentry = entry;
			loadCertificate(cert);
		} catch (KeyStoreException | NoSuchAlgorithmException | CertificateException | IOException | UnrecoverableEntryException e) {
			GuiInterface.reportError(e);
			return false;
		}
		
		return true;
	}

	@Override
	public boolean saveKey(String keypair_name) {
		// TODO Auto-generated method stub
		MyX509Cert cert = new MyX509Cert();
		cert.version = access.getVersion();
		cert.serial_number = access.getSerialNumber();
		cert.date_not_before = access.getNotBefore();
		cert.date_not_after = access.getNotAfter();
		cert.algorithm = access.getPublicKeyAlgorithm();
		cert.params[0] = access.getPublicKeyParameter();	
		cert.params[1] = access.getPublicKeyECCurve();		// will be needed in case of EC algorithm
		cert.signature_algorithm = access.getPublicKeySignatureAlgorithm();
		cert.subject = access.getSubject();
				
		try {
			if (cert.version > Constants.V1) {
				cert.subject_ui = access.getSubjectUniqueIdentifier();
				if (cert.version > Constants.V2) {
					// TODO
					cert.generateBasicConstraint(access.isCritical(Constants.BC), access.isCA(), access.getPathLen());
				}
			}
			
			cert.generateKeypair();
			cert.generateCertificate();
			
			MyKeyStore.load(MyKeyStore.localKeyStore, MyKeyStore.localPassword);
			MyKeyStore.putKey(keypair_name, cert.keypair.getPrivate(), cert.certificate, MyKeyStore.localPassword);
			MyKeyStore.store(MyKeyStore.localKeyStore, MyKeyStore.localPassword);
		} catch (InvalidAlgorithmParameterException | NoSuchAlgorithmException
				| NoSuchProviderException | OperatorCreationException | CertificateException 
				| KeyStoreException | IOException e) {
			GuiInterface.reportError(e);
			return false;
		}
		
		return true;
	}

	@Override
	public void resetLocalKeyStore() {
		MyKeyStore.delete(MyKeyStore.localKeyStore);
	}
	

	@Override
	public boolean importKeypair(String keypair_name, String file, String password) {
		// ucitavamo keypair pod nekim novim imenom
		try {
			MyKeyStore.load(file, password);
			Enumeration<String> aliases = MyKeyStore.ks.aliases();
			String file_name = "";
		    int i = 0;
		    while(aliases.hasMoreElements()){
		    	file_name = aliases.nextElement();
		    	i++;
		    }
		    
		    if (i != 1) {
		    	GuiInterface.reportError("File corrupted");
		    	return false;
		    }	    	
		    
			PrivateKeyEntry imported = MyKeyStore.getKey(file_name, password);
			
			MyKeyStore.load(MyKeyStore.localKeyStore, MyKeyStore.localPassword);
			if (MyKeyStore.ks.containsAlias(keypair_name)) {
				GuiInterface.reportError("Keystore already contains that alias.");
		    	return false;
			}
			MyKeyStore.putKey(keypair_name, imported.getPrivateKey(), imported.getCertificate(), MyKeyStore.localPassword);
			MyKeyStore.store(MyKeyStore.localKeyStore, MyKeyStore.localPassword);
		} catch (KeyStoreException | NoSuchAlgorithmException | CertificateException | IOException | UnrecoverableEntryException e) {
			GuiInterface.reportError(e);
			return false;
		}
				
		return true;
	}

	@Override
	public boolean exportKeypair(String keypair_name, String file, String password) {
		try {		
			MyKeyStore.load(file + ".p12", null);
			MyKeyStore.putChain(keypair_name, current_keyentry.getPrivateKey(), current_keyentry.getCertificateChain(), password);
			MyKeyStore.store(file + ".p12", password);
		} catch (KeyStoreException | NoSuchAlgorithmException | CertificateException | IOException e) {
			GuiInterface.reportError(e);
			return false;
		}
		return true;
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
		
	public void loadCertificate(X509Certificate cert) throws IOException {
		current_cert  = new MyX509Cert(cert);
	
		access.setVersion(current_cert.version);
		access.setSerialNumber(current_cert.serial_number);
		access.setNotBefore(current_cert.date_not_before);
		access.setNotAfter(current_cert.date_not_after);
		access.setSubject(current_cert.subject);
		
		// ako je potpisan moras neki info da vratis i tada ucistavas issuera
		// access.setIssuer(certificate.getIssuer());
		// ako nije potpisan omogucavas sign
		// access.enableSignButton(signed);
		// access.enableExportButton(signed);

		access.setSubjectSignatureAlgorithm(current_cert.signature_algorithm);
		
		if (current_cert.version > Constants.V1) {
			// access.setSubjectUniqueIdentifier(v);
			// access.setIssuerUniqueIdentifier(v);
			if (current_cert.version > Constants.V2) {
				// Basic Constraints
				access.setCritical(Constants.BC, current_cert.extensions[Constants.BC].isCritical());
				if (current_cert.constraint == -1) {
					access.setCA(false);
					access.setPathLen("");
				} else {
					access.setCA(true);
					if (current_cert.constraint == Integer.MAX_VALUE)
						access.setPathLen("");
					else
						access.setPathLen(String.valueOf(current_cert.constraint));
						
				}
			}
			
		}
		

	}
}
