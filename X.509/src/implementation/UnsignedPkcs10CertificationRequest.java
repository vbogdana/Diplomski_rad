package implementation;

import java.io.IOException;

import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.DERBitString;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.pkcs.CertificationRequestInfo;

public class UnsignedPkcs10CertificationRequest {
	 
    private CertificationRequestInfo certificationRequestInfo;
 
    public UnsignedPkcs10CertificationRequest(CertificationRequestInfo certificationRequestInfo) {
        this.certificationRequestInfo = certificationRequestInfo;
    }
     
    public byte[] getEncoded() {
        try {
            ASN1EncodableVector v = new ASN1EncodableVector();
            v.add(certificationRequestInfo);
            ASN1ObjectIdentifier oid = new ASN1ObjectIdentifier("0.0");
            v.add(new DERSequence(oid));
            v.add(new DERBitString(new byte[] {}));
            byte[] encoded = new DERSequence(v).getEncoded();
            return encoded;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
