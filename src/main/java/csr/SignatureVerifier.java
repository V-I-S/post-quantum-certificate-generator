package csr;

import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.operator.ContentVerifierProvider;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.operator.jcajce.JcaContentVerifierProviderBuilder;
import org.bouncycastle.pkcs.PKCS10CertificationRequest;
import org.bouncycastle.pkcs.PKCSException;

public class SignatureVerifier {

    public boolean isSignatureValid(PKCS10CertificationRequest csr) {
        try {
            ContentVerifierProvider verifier = buildContentVerifier(csr.getSubjectPublicKeyInfo());
            return csr.isSignatureValid(verifier);
        } catch (OperatorCreationException e) {
            throw new RuntimeException("Could not retrieve the CSR content verifier", e);
        } catch (PKCSException e) {
            throw new RuntimeException("Could not validate the CSR signature", e);
        }
    }

    private ContentVerifierProvider buildContentVerifier(SubjectPublicKeyInfo pubKeyInfo) throws OperatorCreationException {
        return new JcaContentVerifierProviderBuilder().setProvider("BC").build(pubKeyInfo);
    }
}
