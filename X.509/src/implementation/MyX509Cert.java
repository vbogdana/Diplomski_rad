package implementation;

import gui.Constants;

import java.io.IOException;
import java.math.BigInteger;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.SecureRandom;
import java.security.Security;
import java.security.SignatureException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.security.spec.ECGenParameterSpec;
import java.util.Date;
import java.util.Set;

import javax.security.auth.x500.X500Principal;

import org.bouncycastle.asn1.DEROctetString;
import org.bouncycastle.asn1.pkcs.PKCSObjectIdentifiers;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x509.BasicConstraints;
import org.bouncycastle.asn1.x509.Extension;
import org.bouncycastle.asn1.x509.ExtensionsGenerator;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.cert.CertIOException;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.X509v1CertificateBuilder;
import org.bouncycastle.cert.X509v3CertificateBuilder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.jce.ECNamedCurveTable;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.jce.spec.ECParameterSpec;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.bouncycastle.pkcs.PKCS10CertificationRequest;
import org.bouncycastle.pkcs.PKCS10CertificationRequestBuilder;
import org.bouncycastle.pkcs.jcajce.JcaPKCS10CertificationRequestBuilder;

public class MyX509Cert {
	public static final String PROVIDER = "BC";
	
	public static final int V1 = 0, V2 = 1, V3 = 2;
	public static final String DSA = "DSA", RSA = "RSA", GOST = "ECGOST3410", EC = "EC";
	
	// TODO
	public static final String BasicConstraintsOID = "2.5.29.19";
	public static final String KeyUsageOID = "2.5.29.15";
	public static final String IssuerAlternativeName = "2.5.29.18";
	
	
	X509Certificate certificate;
	KeyPair keypair;
	
	int version;
	String serial_number, 
		   algorithm, params[] = new String [2], signature_algorithm, 
		   subject, issuer;
	Date date_not_before, date_not_after;
	
	String subject_ui, issuer_ui;
	
	Extension [] extensions = new Extension [Constants.NUM_OF_EXTENSIONS];;
	int constraint;
	
	/**************************************************************************************
	 										STATIC
	 *************************************************************************************/
	
	public static boolean isCritical(X509Certificate certificate, String oid) {
		Set<String> criticals = certificate.getCriticalExtensionOIDs();
		return (criticals == null ? false : criticals.contains(oid));
	}
	
	public static boolean verify(X509Certificate [] chain) {
		if (isSelfSigned(chain[0]))
           return true;
		
		if (chain.length == 1)
			return false;
		
		if (!isSelfSigned(chain[chain.length - 1]))
			return false;
		
		for (int i = chain.length - 2; i > 0; i--)
			if (isSelfSigned(chain[i]))
				return false;
			else if (!checkSign(chain[i - 1], chain[i]))
				return false;

		return true;		
	}
	
	public static boolean isSelfSigned(X509Certificate certificate) {
		try {
			certificate.checkValidity();
			certificate.verify(certificate.getPublicKey());
			int constraint = certificate.getBasicConstraints();
			if (constraint == -1)
				return false;
			if (!isCritical(certificate, MyX509Cert.BasicConstraintsOID))
				return false;
			return true;
		} catch (InvalidKeyException | CertificateException | NoSuchAlgorithmException | NoSuchProviderException| SignatureException e) {
			return false;
		}
    }
	
	public static boolean checkSign(X509Certificate subject, X509Certificate issuer) {
		try {
			// TODO
			// da li ovde treba proveravati constraint i criticality
			issuer.checkValidity();
			subject.verify(issuer.getPublicKey());
			return true;
		} catch (InvalidKeyException | CertificateException | NoSuchAlgorithmException | NoSuchProviderException| SignatureException e) {
			return false;
		}
	}
	
	public MyX509Cert() {}
	
	public MyX509Cert(X509Certificate cert) throws IOException {
		// TODO Auto-generated constructor stub
		certificate = cert;
		version = cert.getVersion() - 1;
		serial_number = cert.getSerialNumber().toString();
		date_not_before = cert.getNotBefore();
		date_not_after = cert.getNotAfter();
		
		subject = cert.getSubjectX500Principal().getName();
		// if signed dohvati issuer-a
		issuer = cert.getIssuerX500Principal().getName();
		
		algorithm = cert.getPublicKey().getAlgorithm();
		signature_algorithm = cert.getSigAlgName();
				
		if (version > V1) {
			// access.setSubjectUniqueIdentifier(cert.getSubjectUniqueID());
			if (version > V2) {				
				// TODO
				getBasicConstraint();
			}
		}
		/*	
		boolean [] key_usage_arr=cert.getKeyUsage();
		if (key_usage_arr!=null){
			key_usage_0.setSelected(key_usage_arr[0]);
			key_usage_1.setSelected(key_usage_arr[1]);
			key_usage_2.setSelected(key_usage_arr[2]);
			key_usage_3.setSelected(key_usage_arr[3]);
			key_usage_4.setSelected(key_usage_arr[4]);
			key_usage_5.setSelected(key_usage_arr[5]);
			key_usage_6.setSelected(key_usage_arr[6]);
			key_usage_7.setSelected(key_usage_arr[7]);
			key_usage_8.setSelected(key_usage_arr[8]);
		}
		
		try {
			String altern="";
			int counter=0;
			Collection<List<?>> names = cert.getIssuerAlternativeNames();
			if (names!=null){
				for (Iterator i=names.iterator(); i.hasNext(); ){
					List<?> name=(List<?>) i.next();
					if (counter!=0)
						altern+=", ";
					altern+=name.get(1);
					counter++;
				}
			}
			alternative_name.setText(altern);
		} catch (CertificateParsingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		*/
	}

	public void generateKeypair() throws InvalidAlgorithmParameterException, NoSuchAlgorithmException, NoSuchProviderException {
		Security.addProvider(new BouncyCastleProvider());
		
		KeyPairGenerator keyGen = KeyPairGenerator.getInstance(algorithm, PROVIDER);	
		switch (algorithm) {
		case DSA: 		
			keyGen.initialize(Integer.parseInt(params[0]), new SecureRandom());
			keypair = keyGen.generateKeyPair();
			break;
		case RSA: 
			keyGen.initialize(Integer.parseInt(params[0]));
			keypair = keyGen.generateKeyPair();
			break;
		case GOST:
			// TODO
			keyGen.initialize( new ECGenParameterSpec(params[0]) );
			keypair = keyGen.generateKeyPair();
			break;
		case EC: 
			ECParameterSpec ecSpec = ECNamedCurveTable.getParameterSpec(params[1]);
			keyGen.initialize(ecSpec, new SecureRandom());
			keypair = keyGen.generateKeyPair();
			break;
		}
	}
	
	public void generateCertificate() throws OperatorCreationException, CertificateException, CertIOException {
		// TODO sta se stavlja umesto issuer name-a
		X509v1CertificateBuilder certv1;
		X509v3CertificateBuilder certv3;
		X509CertificateHolder holder;
		
		X500Name name = new X500Name(subject);	
		SubjectPublicKeyInfo subPubKeyInfo = SubjectPublicKeyInfo.getInstance(keypair.getPublic().getEncoded());
		ContentSigner sigGen = new JcaContentSignerBuilder(signature_algorithm).setProvider(new BouncyCastleProvider()).build(keypair.getPrivate());
		if (version == V1) {
			certv1 = new X509v1CertificateBuilder(name, new BigInteger(serial_number), date_not_before, date_not_after, name, subPubKeyInfo);
			holder = certv1.build(sigGen);
		} else {
			certv3 = new X509v3CertificateBuilder(name, new BigInteger(serial_number), date_not_before, date_not_after, name, subPubKeyInfo);
			
			//if (subject_ui != null)				cert.setSubjectUniqueID(boolean[] uid);
			if (version > V2) {
				for (int i = 0; i < Constants.NUM_OF_EXTENSIONS; i++)
					if (extensions[i] != null) 			certv3.addExtension(extensions[i]);
			}
			
			holder = certv3.build(sigGen);
		}				
		certificate = new JcaX509CertificateConverter().getCertificate(holder);
	}
	
	public PKCS10CertificationRequest generateCSR(PrivateKey privateKey) throws OperatorCreationException {
		// TODO Auto-generated method stub
		X500Principal subject = certificate.getSubjectX500Principal();		
		ContentSigner signGen = new JcaContentSignerBuilder(certificate.getSigAlgName()).setProvider(new BouncyCastleProvider()).build(privateKey);
		PKCS10CertificationRequestBuilder builder = new JcaPKCS10CertificationRequestBuilder(subject, certificate.getPublicKey());		
		if (version > V1) {
			//if (subject_ui != null)				cert.setSubjectUniqueID(boolean[] uid);
			if (version > V2) {			
				
				ExtensionsGenerator extensionsGenerator = new ExtensionsGenerator();
				for (int i = 0; i < Constants.NUM_OF_EXTENSIONS; i++)
					if (extensions[i] != null) 			extensionsGenerator.addExtension(extensions[i]);
				builder.addAttribute(PKCSObjectIdentifiers.pkcs_9_at_extensionRequest, extensionsGenerator.generate());
			}
		}	
		PKCS10CertificationRequest csr = builder.build(signGen);		
		return csr;
	}
	
	public void generateBasicConstraint(boolean isCritical, boolean isCA, String pathLen) throws IOException{
		// pathLen imaju samo oni sertifikati sa CA true i keyCertSign true
		if (isCA) {
			if (pathLen == null || pathLen.isEmpty())
				extensions[Constants.BC] = new Extension(Extension.basicConstraints, isCritical, new DEROctetString(new BasicConstraints(isCA)));
			else
				extensions[Constants.BC] = new Extension(Extension.basicConstraints, isCritical, new DEROctetString(new BasicConstraints(Integer.parseInt(pathLen))));
		} else
			extensions[Constants.BC] = new Extension(Extension.basicConstraints, isCritical, new DEROctetString(new BasicConstraints(isCA)));
	}
	
	public void getBasicConstraint() throws IOException {
		constraint = certificate.getBasicConstraints();
		boolean critical = isCritical(certificate, MyX509Cert.BasicConstraintsOID);
		// ako je CA true - samo oni koji su i critical sluze za potpisivanje
		
		if (constraint != -1) {
			if (constraint == Integer.MAX_VALUE)
				extensions[Constants.BC] = new Extension(Extension.basicConstraints, critical, new DEROctetString(new BasicConstraints(true)));
			else
				extensions[Constants.BC] = new Extension(Extension.basicConstraints, critical, new DEROctetString(new BasicConstraints(constraint)));
		} else {
			extensions[Constants.BC] = new Extension(Extension.basicConstraints, critical, new DEROctetString(new BasicConstraints(false)));
		}		
	}

}
