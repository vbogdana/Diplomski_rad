package implementation;

import java.math.BigInteger;
import java.security.InvalidAlgorithmParameterException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.security.Security;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.security.spec.ECGenParameterSpec;
import java.util.Date;

import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
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

public class MyX509Cert {
	public static final String PROVIDER = "BC";
	
	public static final int V1 = 0, V2 = 1, V3 = 2;
	public static final String DSA = "DSA", RSA = "RSA", GOST = "ECGOST3410", EC = "EC";
	
	// TODO
	public static final String BasicConstraintsOID = "2.5.29.19";
	public static final String KeyUsageOID = "2.5.29.15";
	public static final String IssuerAlternativeName = "2.5.29.18";
	
	
	private X509Certificate certificate;
	private KeyPair keypair;
	
	private int version;
	private String algorithm, params[] = new String [2], signature_algorithm, 
				   subject, subject_ui,
				   issuer, issuer_ui;
	String serial_number;
	private Date date_from, date_to;
	
	public MyX509Cert() {}
	
	public MyX509Cert(X509Certificate cert) {
		// TODO Auto-generated constructor stub
		certificate = cert;
		version = cert.getVersion() - 1;
		serial_number = cert.getSerialNumber().toString();
		date_from = cert.getNotBefore();
		date_to = cert.getNotAfter();
		
		subject = cert.getSubjectX500Principal().getName();
		issuer = cert.getIssuerX500Principal().getName();
		
		algorithm = cert.getPublicKey().getAlgorithm();
		/*
		switch (algorithm) {
		case DSA: 		
			DSAPublicKey keyDSA =  (DSAPublicKey) cert.getPublicKey();
			//params[0] = String.valueOf(key);
			break;
		case RSA: 
			RSAPublicKey keyRSA =  (RSAPublicKey) cert.getPublicKey();
			params[0] = String.valueOf(keyRSA.getModulus().bitLength());
			break;
		case GOST:
			// TODO
			GOSTPublicKey
			break;
		case EC: 
			
			break;
		}
		*/
		signature_algorithm = cert.getSigAlgName();
		
		if (cert.getVersion() != V1) {
			// access.setSubjectUniqueIdentifier(cert.getSubjectUniqueID());
			// dalje ekstenzije
		}
		/*
		Set<String> crit=cert.getCriticalExtensionOIDs();
		if(crit.contains(MyX509Cert.BasicConstraintsOID))
			basic_constraint_critical.setSelected(true);
		else
			basic_constraint_critical.setSelected(false);
		if(crit.contains(MyX509Cert.KeyUsageOID))
			key_usage_critical.setSelected(true);
		else
			key_usage_critical.setSelected(false);
		if(crit.contains(MyX509Cert.IssuerAlternativeName))
			alternative_name_critical.setSelected(true);
		else
			alternative_name_critical.setSelected(false);
		
		int basic_constr=cert.getBasicConstraints();
		if (basic_constr==Integer.MAX_VALUE)
			basic_constraint_ca.setSelected(true);
		else if (basic_constr!=-1){
			path_length.setText(basic_constr+"");
			basic_constraint_ca.setSelected(true);
		}
		
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
	
	public void generateCertificate() throws OperatorCreationException, CertificateException {
		// TODO
		X509v1CertificateBuilder certv1;
		X509v3CertificateBuilder certv3;
		X509CertificateHolder holder;
		
		X500Name name = new X500Name(subject);	
		SubjectPublicKeyInfo subPubKeyInfo = SubjectPublicKeyInfo.getInstance(keypair.getPublic().getEncoded());
		ContentSigner sigGen = new JcaContentSignerBuilder(signature_algorithm).setProvider(new BouncyCastleProvider()).build(keypair.getPrivate());
		if (version == V1) {
			certv1 = new X509v1CertificateBuilder(name, new BigInteger(serial_number), date_from, date_to, name, subPubKeyInfo);
			holder = certv1.build(sigGen);
		} else {
			certv3 = new X509v3CertificateBuilder(name, new BigInteger(serial_number), date_from, date_to, name, subPubKeyInfo);
			
			//if (subject_ui != null)				cert.setSubjectUniqueID(boolean[] uid);
			//if (basicConstraint != null) 			cert.addExtension(basicConstraint);
			//if (keyUsage != null)					cert.addExtension(keyUsage);
			//if (issuerAlternativeName != null)	cert.addExtension(issuerAlternativeName);
			
			holder = certv3.build(sigGen);
		}				
		certificate = new JcaX509CertificateConverter().getCertificate(holder);
	}
	
	// ********************************************************************************************************
	// 										GETTERS AND SETTERS
	// ********************************************************************************************************

	public X509Certificate getCertificate() {
		return certificate;
	}

	public void setCertificate(X509Certificate certificate) {
		this.certificate = certificate;
	}

	public KeyPair getKeypair() {
		return keypair;
	}

	public void setKeypair(KeyPair keypair) {
		this.keypair = keypair;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public String getSerial_number() {
		return serial_number;
	}

	public void setSerial_number(String serial_number) {
		this.serial_number = serial_number;
	}

	public Date getDate_from() {
		return date_from;
	}

	public void setDate_from(Date date_from) {
		this.date_from = date_from;
	}

	public Date getDate_to() {
		return date_to;
	}

	public void setDate_to(Date date_to) {
		this.date_to = date_to;
	}

	public String getAlgorithm() {
		return algorithm;
	}

	public void setAlgorithm(String algorithm) {
		this.algorithm = algorithm;
	}

	public String getParam(int i) {
		return params[i];
	}

	public void setParam(int i, String param) {
		this.params[i] = param;
	}

	public String getSignature_algorithm() {
		return signature_algorithm;
	}

	public void setSignature_algorithm(String signature_algorithm) {
		this.signature_algorithm = signature_algorithm;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getSubject_ui() {
		return subject_ui;
	}

	public void setSubject_ui(String subject_ui) {
		this.subject_ui = subject_ui;
	}

	public String getIssuer() {
		return issuer;
	}

	public void setIssuer(String issuer) {
		this.issuer = issuer;
	}

	public String getIssuer_ui() {
		return issuer_ui;
	}

	public void setIssuer_ui(String issuer_ui) {
		this.issuer_ui = issuer_ui;
	}	
	
}
