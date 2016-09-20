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

import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.ASN1OctetString;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.ASN1Set;
import org.bouncycastle.asn1.DEROctetString;
import org.bouncycastle.asn1.DERSet;
import org.bouncycastle.asn1.pkcs.Attribute;
import org.bouncycastle.asn1.pkcs.CertificationRequestInfo;
import org.bouncycastle.asn1.pkcs.PKCSObjectIdentifiers;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x509.AuthorityKeyIdentifier;
import org.bouncycastle.asn1.x509.BasicConstraints;
import org.bouncycastle.asn1.x509.CertificatePolicies;
import org.bouncycastle.asn1.x509.Extension;
import org.bouncycastle.asn1.x509.Extensions;
import org.bouncycastle.asn1.x509.ExtensionsGenerator;
import org.bouncycastle.asn1.x509.GeneralName;
import org.bouncycastle.asn1.x509.GeneralNames;
import org.bouncycastle.asn1.x509.KeyUsage;
import org.bouncycastle.asn1.x509.PolicyInformation;
import org.bouncycastle.asn1.x509.SubjectKeyIdentifier;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.cert.CertIOException;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.X509v1CertificateBuilder;
import org.bouncycastle.cert.X509v3CertificateBuilder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.cert.jcajce.JcaX509ExtensionUtils;
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
	public static final ASN1ObjectIdentifier [] asn1 = { Extension.basicConstraints,
														 Extension.authorityKeyIdentifier,
														 Extension.subjectKeyIdentifier,
														 Extension.keyUsage,
														 Extension.certificatePolicies,
														 Extension.policyMappings,
														 Extension.subjectAlternativeName,
														 Extension.issuerAlternativeName,
														 Extension.subjectDirectoryAttributes,
														 Extension.nameConstraints,
														 Extension.policyConstraints,
														 Extension.extendedKeyUsage,
														 Extension.cRLDistributionPoints,
														 Extension.inhibitAnyPolicy,
														 Extension.freshestCRL 
														};		
	X509Certificate certificate;
	KeyPair keypair;
	SubjectPublicKeyInfo subPubKeyInfo;
	
	int version;
	String serial_number, 
		   algorithm, params[] = new String [2], signature_algorithm, 
		   subject, issuer = "";
	Date date_not_before, date_not_after;
	
	String subject_ui, issuer_ui;
	
	Extension [] extensions = new Extension [Constants.NUM_OF_EXTENSIONS];;
	int constraint;
	AuthorityKeyIdentifier akid;
	SubjectKeyIdentifier skid;
	
	/**************************************************************************************
	 										STATIC
	 *************************************************************************************/
	
	public static boolean isCritical(X509Certificate certificate, String oid) {
		Set<String> criticals = certificate.getCriticalExtensionOIDs();
		return (criticals == null ? false : criticals.contains(oid));
	}
	
	public static boolean verify(X509Certificate [] chain) {
		// if certificate is a CA
		if (isSelfSigned(chain[0]))
           return true;
		
		// if there are no more certificates in the chain
		if (chain.length == 1)
			return false;
		
		// if the root certificate isn't CA return false
		if (!isSelfSigned(chain[chain.length - 1]))
			return false;
		
		// check for every certificate in the chain except for the first, end entity, certificate
		for (int i = chain.length - 2; i > 0; i--)
			// if the certificate is self signed or he didn't sign his predecessor in the chain return false 
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
			// only certificates with critical basic constraints can sign other certificates
			if (!isCritical(certificate, asn1[Constants.BC].getId()))
				return false;
			return true;
		} catch (InvalidKeyException | CertificateException | NoSuchAlgorithmException | NoSuchProviderException| SignatureException e) {
			return false;
		}
    }
	
	public static boolean checkSign(X509Certificate subject, X509Certificate issuer) {
		try {
			issuer.checkValidity();
			subject.verify(issuer.getPublicKey());
			return true;
		} catch (InvalidKeyException | CertificateException | NoSuchAlgorithmException | NoSuchProviderException| SignatureException e) {
			return false;
		}
	}
	
	public static boolean canBeAnIssuer(X509Certificate certificate) throws IOException {
		int constraint = certificate.getBasicConstraints();
		boolean critical = isCritical(certificate, asn1[Constants.BC].getId());
		boolean keyCertSign = false, key_usage[];
		if ((key_usage = certificate.getKeyUsage()) != null)
			keyCertSign = key_usage[Constants.KEY_CERT_SIGN];
		if (constraint != -1 && critical && keyCertSign)
			return true;
		else
			return false;
	}
	
	public static byte[] extractSubjectKeyIdentifier(X509Certificate certificate) throws IOException {
		byte[] extvalue = getCoreExtValue(certificate, asn1[Constants.SKID]);
		if (extvalue != null) {
			SubjectKeyIdentifier skid = SubjectKeyIdentifier.getInstance(extvalue);
			return skid.getKeyIdentifier();
		}
		return null;
	}
	
	public static Extension generateAuthorityKeyIdentifier(boolean critical, byte[] keyID, String issuer, BigInteger serial_number) throws NoSuchAlgorithmException, IOException {
		GeneralNames issuerName = new GeneralNames(new GeneralName(GeneralName.directoryName, issuer));
		AuthorityKeyIdentifier akid = new AuthorityKeyIdentifier(keyID, issuerName, serial_number);
		return new Extension(asn1[Constants.AKID], critical, new DEROctetString(akid));
		
	}
	
	/*****************************************************************************************************
	 										NON STATICS
	 ****************************************************************************************************/
	
	public MyX509Cert() {}
	
	public MyX509Cert(X509Certificate cert) throws IOException {
		certificate = cert;
		version = cert.getVersion() - 1;
		serial_number = cert.getSerialNumber().toString();
		date_not_before = cert.getNotBefore();
		date_not_after = cert.getNotAfter();
		
		subject = cert.getSubjectX500Principal().getName();
		issuer = cert.getIssuerX500Principal().getName();
		
		algorithm = cert.getPublicKey().getAlgorithm();
		signature_algorithm = cert.getSigAlgName();
				
		if (version > V1) {
			// subject_ui = cert.getSubjectUniqueID();
			// issuer_ui = cert.getIssuerUniqueID();
			if (version > V2) {				
				// TODO zbog V3 i V2
				getBasicConstraint();
				getAuthorityKeyIdentifier();
				getSubjectKeyIdentifier();
				getKeyUsage();
			}
		}
		/*	
		
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
			e.printStackTrace();
		}
		*/
	}
	
	public void loadExtensions(PKCS10CertificationRequest csr) {
		Attribute[] attributesAsn1Set = csr.getAttributes();
		Extensions certificateRequestExtensions = null;
		for (int i = 0; i < attributesAsn1Set.length; ++i)
		{
			Attribute attr = attributesAsn1Set[i];
		    if (attr.getAttrType().equals( PKCSObjectIdentifiers.pkcs_9_at_extensionRequest ))
		    {
		    	final ASN1Set attributeValues = attr.getAttrValues();
		    	if (attributeValues.size() >= 1)
		    	{
		    		certificateRequestExtensions =(Extensions) attributeValues.getObjectAt(0);
		    		break;
		    	}
		   	}
	    }
		
		for (int i = 0 ; i < Constants.NUM_OF_EXTENSIONS; i++)
			extensions[i] = certificateRequestExtensions.getExtension(asn1[i]);
	}
	
	public void loadExtensions(CertificationRequestInfo info) {
		ASN1Set attrs = info.getAttributes();
		Extensions e = null;
	    for (int i = 0; i < attrs.size(); i++) {
	        Attribute attr = Attribute.getInstance(attrs.getObjectAt(i));
	        if (PKCSObjectIdentifiers.pkcs_9_at_extensionRequest.equals(attr.getAttrType())) {
	            e = Extensions.getInstance(attr.getAttributeValues()[0]);
	            break;
	        }
	    }
	    if (e != null)
	    	for (int i = 0; i < Constants.NUM_OF_EXTENSIONS; i++)
	    		extensions[i] = e.getExtension(asn1[i]);

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
			// TODO ecgost generate keypair
			keyGen.initialize( new ECGenParameterSpec(params[0]) );
			keypair = keyGen.generateKeyPair();
			break;
		case EC: 
			ECParameterSpec ecSpec = ECNamedCurveTable.getParameterSpec(params[1]);
			keyGen.initialize(ecSpec, new SecureRandom());
			keypair = keyGen.generateKeyPair();
			break;
		}
		subPubKeyInfo = SubjectPublicKeyInfo.getInstance(keypair.getPublic().getEncoded());
	}
	
	public void generateCertificate(PrivateKey private_key) throws OperatorCreationException, CertificateException, CertIOException {
		X509v1CertificateBuilder certv1;
		X509v3CertificateBuilder certv3;
		X509CertificateHolder holder;
		
		X500Name name_subject = new X500Name(subject);
		X500Name name_issuer = new X500Name(issuer);
		
		ContentSigner sigGen = new JcaContentSignerBuilder(signature_algorithm).setProvider(new BouncyCastleProvider()).build(private_key);
		if (version == V1) {
			certv1 = new X509v1CertificateBuilder(name_issuer, new BigInteger(serial_number), date_not_before, date_not_after, name_subject, subPubKeyInfo);
			holder = certv1.build(sigGen);
		} else {
			certv3 = new X509v3CertificateBuilder(name_issuer, new BigInteger(serial_number), date_not_before, date_not_after, name_subject, subPubKeyInfo);
			// TODO zbog uia generate cert
			// if (subject_ui != null)				cert.setSubjectUniqueID(boolean[] uid);
			// if (issuer_ui != null)				cert.setIssuerUniqueID(boolean[] uid);
			if (version > V2) {
				for (int i = 0; i < Constants.NUM_OF_EXTENSIONS; i++)
					if (extensions[i] != null) 			certv3.addExtension(extensions[i]);
			}
			
			holder = certv3.build(sigGen);
		}				
		certificate = new JcaX509CertificateConverter().getCertificate(holder);
	}
	
	public PKCS10CertificationRequest generateCSR(PrivateKey privateKey) throws OperatorCreationException {
		X500Principal subject = certificate.getSubjectX500Principal();	
		// TODO zbog uia generate csr 
		ContentSigner signGen = new JcaContentSignerBuilder(certificate.getSigAlgName()).setProvider(new BouncyCastleProvider()).build(privateKey);
		PKCS10CertificationRequestBuilder builder = new JcaPKCS10CertificationRequestBuilder(subject, certificate.getPublicKey());		
		if (version > V1) {
			// if (subject_ui != null)				cert.setSubjectUniqueID(boolean[] uid);
			// if (issuer_ui != null)				cert.setIssuerUniqueID(boolean[] uid);
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
	
	public CertificationRequestInfo generateCSRInfo() {
		//  TODO zbog uia generate csr info
		X500Name subject = new X500Name(certificate.getSubjectX500Principal().getName());
		subPubKeyInfo = SubjectPublicKeyInfo.getInstance(certificate.getPublicKey().getEncoded());
		CertificationRequestInfo info = null;
		if (version > V1) {
			// if (subject_ui != null)				cert.setSubjectUniqueID(boolean[] uid);
			// if (issuer_ui != null)				cert.setIssuerUniqueID(boolean[] uid);
			if (version > V2) {			
				
				ExtensionsGenerator extensionsGenerator = new ExtensionsGenerator();
				
				for (int i = 0; i < Constants.NUM_OF_EXTENSIONS; i++)
					if (extensions[i] != null) 			extensionsGenerator.addExtension(extensions[i]);
				Attribute attr = new Attribute(PKCSObjectIdentifiers.pkcs_9_at_extensionRequest, new DERSet(extensionsGenerator.generate()));
				info = new CertificationRequestInfo(subject, subPubKeyInfo, new DERSet(attr));
			}
		} else
			info = new CertificationRequestInfo(subject, subPubKeyInfo, new DERSet());
		return info;
	}
	
	/************************************************************************************************************
	 							TODO		EXTENSIONS
	 ***********************************************************************************************************/

	private static byte[] getCoreExtValue(X509Certificate cert, ASN1ObjectIdentifier type) {
		byte[] fullExtValue = cert.getExtensionValue(type.getId());
		if (fullExtValue == null) {
			return null;
		} else
			return ASN1OctetString.getInstance(fullExtValue).getOctets();
	}

	 public void generateBasicConstraint(boolean critical, boolean isCA, String pathLen) throws IOException{
		if (isCA) {
			if (pathLen == null || pathLen.isEmpty())
				extensions[Constants.BC] = new Extension(asn1[Constants.BC], critical, new DEROctetString(new BasicConstraints(isCA)));
			else
				extensions[Constants.BC] = new Extension(asn1[Constants.BC], critical, new DEROctetString(new BasicConstraints(Integer.parseInt(pathLen))));
		} else
			extensions[Constants.BC] = new Extension(asn1[Constants.BC], critical, new DEROctetString(new BasicConstraints(isCA)));
	}
	
	public void getBasicConstraint() throws IOException {
		constraint = certificate.getBasicConstraints();
		boolean critical = isCritical(certificate, asn1[Constants.BC].getId());		
		if (constraint != -1) {
			if (constraint == Integer.MAX_VALUE)
				extensions[Constants.BC] = new Extension(asn1[Constants.BC], critical, new DEROctetString(new BasicConstraints(true)));
			else
				extensions[Constants.BC] = new Extension(asn1[Constants.BC], critical, new DEROctetString(new BasicConstraints(constraint)));
		} else {
			extensions[Constants.BC] = new Extension(asn1[Constants.BC], critical, new DEROctetString(new BasicConstraints(false)));
		}		
	}
	
	public void generateAuthorityKeyIdentifier(boolean critical) throws NoSuchAlgorithmException, IOException {
		JcaX509ExtensionUtils utils;
		utils = new JcaX509ExtensionUtils();
		GeneralNames issuerName = new GeneralNames(new GeneralName(GeneralName.directoryName, issuer));
		AuthorityKeyIdentifier akid = utils.createAuthorityKeyIdentifier(keypair.getPublic());
		akid = new AuthorityKeyIdentifier(akid.getKeyIdentifier(), issuerName, new BigInteger(serial_number));
		extensions[Constants.AKID] = new Extension(asn1[Constants.AKID], critical, new DEROctetString(akid));
		
	}
	
	public void getAuthorityKeyIdentifier() throws IOException {
		boolean critical = isCritical(certificate, asn1[Constants.AKID].getId()); 
		byte[] extvalue = getCoreExtValue(certificate, asn1[Constants.AKID]);
		if (extvalue != null) {
			akid = AuthorityKeyIdentifier.getInstance(extvalue);
			extensions[Constants.AKID] = new Extension(asn1[Constants.AKID], critical, new DEROctetString(akid));
		}
	}
	
	public void generateSubjectKeyIdentifier(boolean critical) throws NoSuchAlgorithmException, IOException {
		JcaX509ExtensionUtils utils;
		utils = new JcaX509ExtensionUtils();
		SubjectKeyIdentifier skid = utils.createSubjectKeyIdentifier(keypair.getPublic());
		extensions[Constants.SKID] = new Extension(asn1[Constants.SKID], critical, new DEROctetString(skid));		
	}
	
	public void getSubjectKeyIdentifier() throws IOException {
		boolean critical = isCritical(certificate, asn1[Constants.SKID].getId());
		byte[] extvalue = getCoreExtValue(certificate, asn1[Constants.SKID]);
		if (extvalue != null) {
			skid = SubjectKeyIdentifier.getInstance(extvalue);
			extensions[Constants.SKID] = new Extension(asn1[Constants.SKID], critical, new DEROctetString(skid));
		}	
	}
	
	public int generateUsage(boolean [] key_usage) {
		int usage = 0;
		
		if (key_usage[0])
			usage |= KeyUsage.digitalSignature;
		if (key_usage[1])
			usage |= KeyUsage.nonRepudiation;
		if (key_usage[2])
			usage |= KeyUsage.keyEncipherment;
		if (key_usage[3])
			usage |= KeyUsage.dataEncipherment;
		if (key_usage[4])
			usage |= KeyUsage.keyAgreement;
		if (key_usage[5])
			usage |= KeyUsage.keyCertSign;
		if (key_usage[6])
			usage |= KeyUsage.cRLSign;
		if (key_usage[7])
			usage |= KeyUsage.encipherOnly;
		if (key_usage[8])
			usage |= KeyUsage.decipherOnly;
		
		return usage;
	}
	
	public void generateKeyUsage(boolean critical, boolean [] key_usage) throws IOException {
		int usage = generateUsage(key_usage);
		
		if (usage != 0) {
			extensions[Constants.KU] = new Extension(asn1[Constants.KU], critical, new DEROctetString(new KeyUsage(usage)));
		}
	}
	
	public void getKeyUsage() throws IOException {
		boolean [] key_usage = certificate.getKeyUsage();
		if (key_usage == null)
			return;
		
		int usage = generateUsage(key_usage);
		
		if (usage == 0)
			return;

		boolean critical= isCritical(certificate, asn1[Constants.KU].getId());		
		extensions[Constants.KU] = new Extension(asn1[Constants.KU], critical, new DEROctetString(new KeyUsage(usage)));
	}

	public void generateCertificatePolicies(boolean critical, String [] identifiers, String [] qualifiers) throws IOException {
		PolicyInformation [] policies = new PolicyInformation [identifiers.length];
		for (PolicyInformation p : policies) {
			p = new PolicyInformation(new ASN1ObjectIdentifier(identifiers[i]), new DERSet(qualifiers[i]));
		}
		extensions[Constants.PI] = new Extension(asn1[Constants.PI], critical, new DEROctetString(new CertificatePolicies(policies)));
	}
}
