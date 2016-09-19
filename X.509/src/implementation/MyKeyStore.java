package implementation;

import java.io.File;
import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.UnrecoverableEntryException;
import java.security.KeyStore.PrivateKeyEntry;
import java.security.KeyStore.TrustedCertificateEntry;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;

public class MyKeyStore {
	
	public static final String PKCS = "PKCS12";
	public static final String localKeyStore = "localKeyStore.jks";
	public static final String localPassword = "localKeyStorePassword";
	
	public static KeyStore ks;
	
	public static void delete(String path) {
		File file = new File(path);
		if (file.exists()) {	
			file.delete();
		}
	}
	
	public static KeyStore load(String path, String password) throws KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException {
		ks = KeyStore.getInstance(PKCS);
		
		File file = new File(path);
		if (file.exists()) {	
		    java.io.FileInputStream fis = null;
		    try {
		        fis = new java.io.FileInputStream(path);
		        ks.load(fis, password.toCharArray());
		    } finally {
		        if (fis != null) {
		            fis.close();
		        }
		    }
		} else {
			ks = KeyStore.getInstance(PKCS);
			ks.load(null, null);
		}
		
		return ks;
	}
	
	public static void store(String path, String password) throws IOException, KeyStoreException, NoSuchAlgorithmException, CertificateException {
		File file = new File(path);
		
		if (file.exists()) {	
			file.delete();
		}

		java.io.FileOutputStream fos = null;
		try {		
			fos = new java.io.FileOutputStream(path);
		    ks.store(fos, password.toCharArray());
		    ks = null;
	    } 
		finally {
			if (fos != null) {
				fos.close();
	    	}
		}
	}
	
	public static void putKey(String keypair_name, PrivateKey key, Certificate cert, String password) throws KeyStoreException {
		/*
		SecureRandom random = new SecureRandom();
		byte[] ivBytes = new byte[16];
		random.nextBytes(ivBytes);
		IvParameterSpec iv = new IvParameterSpec(ivBytes);
		KeyStore.ProtectionParameter protParam = new KeyStore.PasswordProtection(password.toCharArray(), "AES", iv);
		*/
		KeyStore.ProtectionParameter protParam = new KeyStore.PasswordProtection(password.toCharArray());
		
		Certificate[] chain = new Certificate[1];
		chain[0] = cert;
		
		KeyStore.PrivateKeyEntry entry = new KeyStore.PrivateKeyEntry(key, chain);
		ks.setEntry(keypair_name, entry, protParam);
	}
	
	public static PrivateKeyEntry getKey(String keypair_name, String password) throws NoSuchAlgorithmException, UnrecoverableEntryException, KeyStoreException{
		KeyStore.ProtectionParameter protParam = new KeyStore.PasswordProtection(password.toCharArray());
		//KeyStore.ProtectionParameter protParam = new KeyStore.PasswordProtection(password.toCharArray(), "AES", null);
		
		if (ks.isKeyEntry(keypair_name))
			return (PrivateKeyEntry) ks.getEntry(keypair_name, protParam);
		
		return null;
	}
	
	public static void putCert(String keypair_name, Certificate cert, String password) throws KeyStoreException{
		KeyStore.ProtectionParameter protParam = new KeyStore.PasswordProtection(password.toCharArray());
		//KeyStore.ProtectionParameter protParam = new KeyStore.PasswordProtection(password.toCharArray(), "AES", null);
		
		KeyStore.TrustedCertificateEntry entry = new KeyStore.TrustedCertificateEntry(cert);
		ks.setEntry(keypair_name, entry, protParam);
	}
	
	public static TrustedCertificateEntry getCert(String keypair_name, String password) throws NoSuchAlgorithmException, UnrecoverableEntryException, KeyStoreException{
		KeyStore.ProtectionParameter protParam = new KeyStore.PasswordProtection(password.toCharArray());
		//KeyStore.ProtectionParameter protParam = new KeyStore.PasswordProtection(password.toCharArray(), "AES", null);
		
		if (ks.isCertificateEntry(keypair_name))
			return (TrustedCertificateEntry) ks.getEntry(keypair_name, protParam);
		
		return null;
	}
	
	public static void putChain(String keypair_name, PrivateKey key, Certificate[] chain, String password) throws KeyStoreException{
		/*
		SecureRandom random = new SecureRandom();
		byte[] ivBytes = new byte[16];
		random.nextBytes(ivBytes);
		IvParameterSpec iv = new IvParameterSpec(ivBytes);
		KeyStore.ProtectionParameter protParam = new KeyStore.PasswordProtection(password.toCharArray(), "AES", iv);
		*/
		KeyStore.ProtectionParameter protParam = new KeyStore.PasswordProtection(password.toCharArray());
		
		KeyStore.PrivateKeyEntry entry = new KeyStore.PrivateKeyEntry(key, chain);
		ks.deleteEntry(keypair_name);
		ks.setEntry(keypair_name, entry, protParam);
	}

}
