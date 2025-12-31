package certificate;

import csr.ContentSignerFactory;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x509.Extension;
import org.bouncycastle.asn1.x509.GeneralName;
import org.bouncycastle.asn1.x509.GeneralNames;
import org.bouncycastle.asn1.x509.KeyPurposeId;
import org.bouncycastle.cert.CertIOException;
import org.bouncycastle.cert.X509v3CertificateBuilder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.cert.jcajce.JcaX509v3CertificateBuilder;
import org.bouncycastle.jce.X509KeyUsage;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.OperatorCreationException;
import request.model.CertificateRequest;

import java.math.BigInteger;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Date;

public class SelfSignDilithiumCertificateIssuer {
    public static final long CERTIFICATE_VALIDITY_PERIOD = 365 * 24 * 60 * 60 * 1000L;

    private final JcaX509CertificateConverter certConverter;
    private final ContentSigner certSigner;
    private final PublicKey publicKey;

    public SelfSignDilithiumCertificateIssuer(KeyPair keyPair) {
        certConverter = new JcaX509CertificateConverter().setProvider("BC");
        certSigner = initializeCertSigner(keyPair.getPrivate());
        publicKey = keyPair.getPublic();
    }

    public X509Certificate issueCertificateOrExit(CertificateRequest request) {
        try {
            return issueCertificate(request);
        } catch (CertIOException e) {
            throw new RuntimeException("Certificate assembly input/output error", e);
        } catch (CertificateException e) {
            throw new RuntimeException("Certificate issuing error", e);
        }
    }

    public X509Certificate issueCertificate(CertificateRequest request) throws CertIOException, CertificateException {
        X500Name issuer = new X500Name(String.format("CN=%s,OU=%s,O=%s,C=%s", request.commonName(),
                request.subject().organizationalUnit(), request.subject().organization(), request.subject().country()));
        X509v3CertificateBuilder certBuiler = new JcaX509v3CertificateBuilder(
                    issuer, BigInteger.valueOf(1), new Date(System.currentTimeMillis()),
                    new Date(System.currentTimeMillis() + CERTIFICATE_VALIDITY_PERIOD), issuer, publicKey)
                .addExtension(Extension.keyUsage, true, new X509KeyUsage(X509KeyUsage.encipherOnly))
                .addExtension(Extension.extendedKeyUsage, true, new DERSequence(KeyPurposeId.anyExtendedKeyUsage));
        request.subjectAlternativeNames()
                .forEach(san -> addSubjectAlternativeName(certBuiler, san));
        return certConverter.getCertificate(certBuiler.build(certSigner));
    }

    private void addSubjectAlternativeName(X509v3CertificateBuilder certBuiler,
                                           CertificateRequest.SubjectAlternativeName san) {
        if ("DNS".equals(san.type())) {
            try {
                certBuiler.addExtension(Extension.subjectAlternativeName, true,
                        new GeneralNames(new GeneralName(GeneralName.dNSName, san.value())));
            } catch (CertIOException e) {
                throw new RuntimeException("Error while defining the SAN: " + san.value(), e);
            }
        }
    }

    private ContentSigner initializeCertSigner(PrivateKey signingKey) {
        try {
            return ContentSignerFactory.createDilithiumContentSigner(signingKey);
        } catch (OperatorCreationException e) {
            throw new RuntimeException("Failed to initialize CSR signer", e);
        }
    }
}
