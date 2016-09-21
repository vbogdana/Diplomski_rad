package implementation;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.security.AlgorithmParameters;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.UnrecoverableEntryException;
import java.security.KeyStore.PrivateKeyEntry;
import java.security.KeyStore.TrustedCertificateEntry;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.InvalidParameterSpecException;
import java.security.spec.KeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

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
	
	public static void removeEntry(String keypair_name) throws KeyStoreException {
		ks.deleteEntry(keypair_name);
	}
	
	public static void load(String path, String password) throws KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException {
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
	}
	
	public static void loadAES(String path, String password) throws KeyStoreException, IOException, NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, CertificateException, InvalidAlgorithmParameterException  {
		ks = KeyStore.getInstance(PKCS);		
		File file = new File(path);

		if (file.exists()) {	
		    java.io.FileInputStream fis = null;
		    java.io.ByteArrayInputStream bis = null;
		    try {
		    	byte[] ciphertext = new byte[(int) file.length() - 16];
		    	byte[] iv = new byte[16];
		    	fis = new java.io.FileInputStream(path);
		    	fis.read(iv);
		    	fis.read(ciphertext);
	 		    
		    	byte[] salt = {
		        				(byte)0xc7, (byte)0x73, (byte)0x21, (byte)0x8c,
		        				(byte)0x7e, (byte)0xc8, (byte)0xee, (byte)0x99
		    		  		  };
		    	/* Derive the key, given password and salt. */
		    	SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
		    	KeySpec keySpec = new PBEKeySpec(password.toCharArray(), salt, 65536, 128);
		    	SecretKey tmp = factory.generateSecret(keySpec);
		    	SecretKey secret = new SecretKeySpec(tmp.getEncoded(), "AES");
		    	Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		    	cipher.init(Cipher.DECRYPT_MODE, secret, new IvParameterSpec(iv));				

				/* File decryption */
				byte[] plaintext = cipher.doFinal(ciphertext);
				bis = new ByteArrayInputStream(plaintext);	    			    	
		        ks.load(bis, password.toCharArray());
		    } finally {
		        if (fis != null) {
		            fis.close();
		        }
		    }
		} else {
			ks = KeyStore.getInstance(PKCS);
			ks.load(null, null);
		}
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
	    } finally {
			if (fos != null) {
				fos.close();
	    	}
		}
	}
	
	public static void storeAES(String path, String password) throws KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException, InvalidKeySpecException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, InvalidParameterSpecException  {
		File file = new File(path);
		if (file.exists()) {	
			file.delete();
		}
				
		java.io.FileOutputStream fos = null;
		java.io.ByteArrayOutputStream bos = null;
		
		try {		
			bos = new java.io.ByteArrayOutputStream();
		    fos = new java.io.FileOutputStream(path);
		    ks.store(bos, password.toCharArray());
		    
	    	byte[] salt = {
	        				(byte)0xc7, (byte)0x73, (byte)0x21, (byte)0x8c,
	        				(byte)0x7e, (byte)0xc8, (byte)0xee, (byte)0x99
	    		  		  };
	    	/* Derive the key, given password and salt. */
	    	SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
	    	KeySpec keySpec = new PBEKeySpec(password.toCharArray(), salt, 65536, 128);
	    	SecretKey tmp = factory.generateSecret(keySpec);
	    	SecretKey secret = new SecretKeySpec(tmp.getEncoded(), "AES");
	    	Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
	    	cipher.init(Cipher.ENCRYPT_MODE, secret);
	    	AlgorithmParameters params = cipher.getParameters();
	    	byte[] iv = params.getParameterSpec(IvParameterSpec.class).getIV();
	    	/* File decryption */
	    	byte[] ciphertext = cipher.doFinal(bos.toByteArray());
		    
	    	fos.write(iv);
		    fos.write(ciphertext);
	    } finally {
			if (fos != null) {
				fos.close();
				if (bos != null) {
					bos.close();
		    	}
	    	}
		}		
	}
	
	public static void putKey(String keypair_name, PrivateKey key, Certificate cert, String password) throws KeyStoreException {
		KeyStore.ProtectionParameter protParam = new KeyStore.PasswordProtection(password.toCharArray());
		
		Certificate[] chain = new Certificate[1];
		chain[0] = cert;
		
		KeyStore.PrivateKeyEntry entry = new KeyStore.PrivateKeyEntry(key, chain);
		ks.setEntry(keypair_name, entry, protParam);
	}
	
	public static PrivateKeyEntry getKey(String keypair_name, String password) throws NoSuchAlgorithmException, UnrecoverableEntryException, KeyStoreException{
		KeyStore.ProtectionParameter protParam = new KeyStore.PasswordProtection(password.toCharArray());
		
		if (ks.isKeyEntry(keypair_name))
			return (PrivateKeyEntry) ks.getEntry(keypair_name, protParam);
		
		return null;
	}
	
	public static void putCert(String keypair_name, Certificate cert) throws KeyStoreException{
		ks.setCertificateEntry(keypair_name, cert);
	}
	
	public static TrustedCertificateEntry getCert(String keypair_name) throws NoSuchAlgorithmException, UnrecoverableEntryException, KeyStoreException {
		if (ks.isCertificateEntry(keypair_name))
			return (TrustedCertificateEntry) ks.getEntry(keypair_name, null);
		
		return null;
	}
	
	public static void putChain(String keypair_name, PrivateKey key, Certificate[] chain, String password) throws KeyStoreException{
		KeyStore.ProtectionParameter protParam = new KeyStore.PasswordProtection(password.toCharArray());
		
		KeyStore.PrivateKeyEntry entry = new KeyStore.PrivateKeyEntry(key, chain);
		ks.deleteEntry(keypair_name);
		ks.setEntry(keypair_name, entry, protParam);
	}

}
