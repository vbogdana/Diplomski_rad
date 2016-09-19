package implementation;

import gui.Constants;
import gui.GuiInterface;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.KeyStore.Entry;
import java.security.KeyStore.PrivateKeyEntry;
import java.security.KeyStore.TrustedCertificateEntry;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.UnrecoverableEntryException;
import java.security.cert.Certificate;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import org.bouncycastle.asn1.DEROctetString;
import org.bouncycastle.asn1.pkcs.CertificationRequestInfo;
import org.bouncycastle.operator.OperatorCreationException;

import sun.misc.BASE64Encoder;
import sun.security.provider.X509Factory;
import code.GuiException;
import x509.v3.CodeV3;

public class MyCodeV3 extends CodeV3 {
	private String current_alias;
	private MyX509Cert current_cert;
	private Entry current_keyentry;
	// TODO current_csr
	// private PKCS10CertificationRequest current_csr;	
	private CertificationRequestInfo current_csr_info;
	
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
	public int loadKey(String keypair_name) {
		boolean signed = false;
		try {
			MyKeyStore.load(MyKeyStore.localKeyStore, MyKeyStore.localPassword);
			X509Certificate cert;
			Entry entry = MyKeyStore.getKey(keypair_name, MyKeyStore.localPassword);
			if (entry == null) {
				entry = MyKeyStore.getCert(keypair_name);
				cert = (X509Certificate) ((TrustedCertificateEntry) entry).getTrustedCertificate();
			} else
				cert = (X509Certificate) ((PrivateKeyEntry) entry).getCertificate();		
			current_cert  = new MyX509Cert(cert);
			current_keyentry = entry;
			current_alias = keypair_name;
			signed = loadCertificateToGui();
		} catch (KeyStoreException | NoSuchAlgorithmException | CertificateException | IOException | UnrecoverableEntryException e) {
			GuiInterface.reportError(e);
			return -1;
		}
		if (current_keyentry instanceof TrustedCertificateEntry)
			return 2;
		return (signed ? 1 : 0);
	}

	@Override
	public boolean saveKey(String keypair_name) {		
		try {
			MyKeyStore.load(MyKeyStore.localKeyStore, MyKeyStore.localPassword);
			if (MyKeyStore.ks.containsAlias(keypair_name)) {
				GuiInterface.reportError("Keystore already contains that alias.");
		    	return false;
			}
			
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
			cert.issuer = cert.subject;
			cert.generateKeypair();
			if (cert.version > Constants.V1) {
				cert.subject_ui = access.getSubjectUniqueIdentifier();
				cert.issuer_ui = cert.subject_ui;
				
				
				if (cert.version > Constants.V2) {
					// TODO save key zbog v2 i v3
					// Basic Constraints
					cert.generateBasicConstraint(access.isCritical(Constants.BC), access.isCA(), access.getPathLen());
					// Authority and subject key identifiers
					if (access.getEnabledKeyIdentifiers()) {
						cert.generateAuthorityKeyIdentifier(access.isCritical(Constants.AKID));
						cert.generateSubjectKeyIdentifier(access.isCritical(Constants.AKID));
					}				
					// Key usage
					cert.generateKeyUsage(access.isCritical(Constants.KU), access.getKeyUsage());
				}
			}					
			cert.generateCertificate(cert.keypair.getPrivate());		
			
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
		// load keypair using a unique alias (unique for keystore)
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
			if (current_keyentry instanceof TrustedCertificateEntry) {
				GuiInterface.reportError("Can not export a trusted certificate as a keypair.");
				return false;
			}				
			MyKeyStore.load(file + ".p12", null);
			MyKeyStore.putChain(keypair_name, ((PrivateKeyEntry) current_keyentry).getPrivateKey(), ((PrivateKeyEntry) current_keyentry).getCertificateChain(), password);
			MyKeyStore.store(file + ".p12", password);
		} catch (KeyStoreException | NoSuchAlgorithmException | CertificateException | IOException e) {
			GuiInterface.reportError(e);
			return false;
		}
		return true;
	}

	@Override
	public boolean signCertificate(String issuer, String algorithm) {
		try {		
			MyKeyStore.load(MyKeyStore.localKeyStore, MyKeyStore.localPassword);
			PrivateKeyEntry entry_issuer = MyKeyStore.getKey(issuer, MyKeyStore.localPassword);
			X509Certificate cert_issuer = (X509Certificate) entry_issuer.getCertificate();
			// TODO zbog CSR-a i UI-a signCertificate			
			if (current_cert.version > Constants.V1) {
				// ovde nesto mozda za ui
				if (current_cert.version > Constants.V2) {
					//current_cert.loadExtensions(current_csr);
					current_cert.loadExtensions(current_csr_info);
					byte [] keyID =  MyX509Cert.extractSubjectKeyIdentifier(cert_issuer);
					if (keyID != null)
						current_cert.extensions[Constants.AKID] = MyX509Cert.generateAuthorityKeyIdentifier(false, keyID, cert_issuer.getSubjectX500Principal().getName(), cert_issuer.getSerialNumber());
				}
			}
					
			//current_cert.subject = current_csr.getSubject().toString();			
			//current_cert.subPubKeyInfo = current_csr.getSubjectPublicKeyInfo();
			current_cert.subject = current_csr_info.getSubject().toString();			
			current_cert.subPubKeyInfo = current_csr_info.getSubjectPublicKeyInfo();
			current_cert.issuer = cert_issuer.getSubjectX500Principal().getName();
			current_cert.signature_algorithm = algorithm;
						
			current_cert.generateCertificate(entry_issuer.getPrivateKey());
				
			Certificate[] chain = (X509Certificate[]) entry_issuer.getCertificateChain();
			ArrayList<X509Certificate> list_array = new ArrayList<X509Certificate> ();
			list_array.add(current_cert.certificate);
			for (int i=0; i < chain.length; i++)
				list_array.add((X509Certificate) chain[i]);
			
			chain = list_array.toArray(new X509Certificate[list_array.size()]);	
			
			MyKeyStore.putChain(current_alias, ((PrivateKeyEntry) current_keyentry).getPrivateKey(), chain, MyKeyStore.localPassword);
			MyKeyStore.store(MyKeyStore.localKeyStore, MyKeyStore.localPassword);
		} catch (KeyStoreException | NoSuchAlgorithmException | CertificateException 
				| UnrecoverableEntryException | OperatorCreationException | IOException e) {
			GuiInterface.reportError(e);
			return false;
		}
		return true;

	}

	@Override
	public boolean importCertificate(File file, String keypair_name) {
		CertificateFactory fact;
		try {
			MyKeyStore.load(MyKeyStore.localKeyStore, MyKeyStore.localPassword);
			if (MyKeyStore.ks.containsAlias(keypair_name)) {
				GuiInterface.reportError("Keystore already contains that alias.");
		    	return false;
			}
			
			fact = CertificateFactory.getInstance("X.509");
			FileInputStream is = new FileInputStream (file.getAbsolutePath());
		    X509Certificate cert = (X509Certificate) fact.generateCertificate(is);
		    
		    MyKeyStore.putCert(keypair_name, cert);
			MyKeyStore.store(MyKeyStore.localKeyStore, MyKeyStore.localPassword);
		} catch (CertificateException | KeyStoreException | NoSuchAlgorithmException | IOException e) {
			GuiInterface.reportError(e);
			return false;
		}
		return true;
	}

	@Override
	public void exportCertificate(File file, int encoding) {	
		FileOutputStream fos;
		try {
			fos = new FileOutputStream(file.getAbsolutePath() + ".cer");
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream( );
			switch (encoding) {
			case Constants.PEM: 
				BASE64Encoder encoder = new BASE64Encoder();		    			    
			    outputStream.write(X509Factory.BEGIN_CERT.getBytes());
			    encoder.encodeBuffer(current_cert.certificate.getEncoded(), outputStream);
			    outputStream.write(X509Factory.END_CERT.getBytes());
				break;
			case Constants.DER:
				outputStream.write(current_cert.certificate.getEncoded());
				break;
			}		
			byte c[] = outputStream.toByteArray();			    
		    fos.write(c);
		    fos.close();
		} catch (IOException | CertificateEncodingException e) {
			GuiInterface.reportError(e);
		}   
	}
		

	@Override
	public String getIssuer(String keypair_name) {
		try {
			MyKeyStore.load(MyKeyStore.localKeyStore, MyKeyStore.localPassword);
			PrivateKeyEntry cert_issuer_key = MyKeyStore.getKey(keypair_name, MyKeyStore.localPassword);
			X509Certificate cert_issuer = (X509Certificate) cert_issuer_key.getCertificate();
			
			String issuer = cert_issuer.getSubjectX500Principal().getName();
			issuer += ",SA=" + cert_issuer.getSigAlgName();
			return issuer;
		} catch (KeyStoreException | NoSuchAlgorithmException | CertificateException | IOException | UnrecoverableEntryException e) {
			GuiInterface.reportError(e);
		}
		return "";
	}
	
	public String getIssuerPublicKeyAlgorithm(String keypair_name) {
		try {
			MyKeyStore.load(MyKeyStore.localKeyStore, MyKeyStore.localPassword);
			PrivateKeyEntry cert_issuer_key = MyKeyStore.getKey(keypair_name, MyKeyStore.localPassword);
			X509Certificate cert_issuer = (X509Certificate) cert_issuer_key.getCertificate();
			
			String algorithm = cert_issuer.getPublicKey().getAlgorithm();
			return algorithm;
		} catch (KeyStoreException | NoSuchAlgorithmException | CertificateException | IOException | UnrecoverableEntryException e) {
			GuiInterface.reportError(e);
		}
		return "";
	}
	
	@Override
	public List<String> getIssuers(String keypair_name) {
		List<String> valid = new ArrayList<String> ();
		try {
			MyKeyStore.load(MyKeyStore.localKeyStore, MyKeyStore.localPassword);
			Enumeration<String> certs = MyKeyStore.ks.aliases();
			
			while (certs.hasMoreElements()){
				String alias = certs.nextElement();
				if (alias.equals(keypair_name))
					continue;
				Entry local_entry = MyKeyStore.getCert(alias);
				if (local_entry != null)
					continue;
				local_entry = MyKeyStore.getKey(alias, MyKeyStore.localPassword);
				if (MyX509Cert.canBeAnIssuer((X509Certificate) ((PrivateKeyEntry) local_entry).getCertificate())) {
					Certificate [] cert_chain = ((PrivateKeyEntry) local_entry).getCertificateChain();
					boolean isValid = MyX509Cert.verify((X509Certificate[]) cert_chain);
					if (isValid)
						valid.add(alias);
				}
			}			
		} catch (KeyStoreException | NoSuchAlgorithmException | CertificateException | IOException | UnrecoverableEntryException e) {
			GuiInterface.reportError(e);
		}
		return valid;
	}

	@Override
	public boolean generateCSR(String keypair_name) {
		// TODO generate csr zbog csra
		/*try {
			current_csr = current_cert.generateCSR(current_keyentry.getPrivateKey());			
		} catch (OperatorCreationException e) {
			GuiInterface.reportError(e);
			return false;
		}*/
		current_csr_info = current_cert.generateCSRInfo();
		return true;
		
	}
	
	public boolean loadCertificateToGui() throws IOException {
		// TODO zbog v2 i v3 load to gui
		boolean signed = false;
		
		access.setVersion(current_cert.version);
		access.setSerialNumber(current_cert.serial_number);
		access.setNotBefore(current_cert.date_not_before);
		access.setNotAfter(current_cert.date_not_after);
		access.setSubject(current_cert.subject);
		access.setSubjectSignatureAlgorithm(current_cert.signature_algorithm);
		
		if (current_keyentry instanceof PrivateKeyEntry)
			signed = MyX509Cert.verify((X509Certificate[]) ((PrivateKeyEntry) current_keyentry).getCertificateChain());
		else
			signed = true;
		if (signed) {
			access.setIssuer(current_cert.issuer);
			access.setIssuerSignatureAlgorithm(current_cert.signature_algorithm);
			// if (current_cert.version > Constants.V1)
				// access.setIssuerUniqueIdentifier(v);
		}
		
		if (current_cert.version > Constants.V1) {
			// access.setSubjectUniqueIdentifier(v);
			if (current_cert.version > Constants.V2) {
				for (int i = 0; i < Constants.NUM_OF_EXTENSIONS; i++)
					if (current_cert.extensions[i] == null)
						access.setCritical(i, false);
					else
						access.setCritical(i, current_cert.extensions[i].isCritical());
				
				// Basic Constraints				
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
				// Key Identifiers
				if (signed && current_cert.extensions[Constants.AKID] != null) {
					access.setAuthorityKeyID(String.valueOf(new DEROctetString(current_cert.akid.getKeyIdentifier())));
					if (current_cert.akid.getAuthorityCertIssuer() != null )
						access.setAuthorityIssuer((current_cert.akid.getAuthorityCertIssuer().getNames())[0].toString());
					if (current_cert.akid.getAuthorityCertSerialNumber() != null)
					access.setAuthoritySerialNumber(String.valueOf(current_cert.akid.getAuthorityCertSerialNumber()));
				}
				if (current_cert.extensions[Constants.SKID] != null && current_cert.skid.getKeyIdentifier() != null)
					access.setSubjectKeyID(String.valueOf(new DEROctetString(current_cert.skid.getKeyIdentifier())));
				// Key Usage
				if (current_cert.extensions[Constants.KU] != null && current_cert.certificate.getKeyUsage() != null) {
					access.setKeyUsage(current_cert.certificate.getKeyUsage());
				}
			}			
		}
		return signed;
	}
}
