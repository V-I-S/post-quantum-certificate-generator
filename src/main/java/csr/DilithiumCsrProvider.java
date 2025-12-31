package csr;

import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.pkcs.PKCS10CertificationRequest;
import org.bouncycastle.pkcs.jcajce.JcaPKCS10CertificationRequestBuilder;
import request.model.CertificateRequest;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;

public class DilithiumCsrProvider {
    private final ContentSigner csrSigner;
    private final PublicKey publicKey;

    public DilithiumCsrProvider(KeyPair keyPair) {
        csrSigner = initializeCsrSigner(keyPair.getPrivate());
        publicKey = keyPair.getPublic();
    }

    public PKCS10CertificationRequest provideCsr(CertificateRequest request) {
        String subjectValue = makeSubjectValue(request.commonName(), request.subject());
        X500Name subject = new X500Name(subjectValue);
        return new JcaPKCS10CertificationRequestBuilder(subject, publicKey).build(csrSigner);
    }

    private ContentSigner initializeCsrSigner(PrivateKey signingKey) {
        try {
            return ContentSignerFactory.createDilithiumContentSigner(signingKey);
        } catch (OperatorCreationException e) {
            throw new RuntimeException("Failed to initialize CSR signer", e);
        }
    }

    private String makeSubjectValue(String commonName, CertificateRequest.Subject subject) {
        return String.format("CN=%s,OU=%s,O=%s,C=%s", commonName, subject.organizationalUnit(),
                subject.organization(), subject.country());
    }
}
