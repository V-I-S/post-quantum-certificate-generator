package csr;

import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;

import java.security.PrivateKey;

public class ContentSignerFactory {
    /*
        Dilithium3 is supported as a ML-DSA-65 NIST standard.
        https://downloads.bouncycastle.org/java/docs/PQC-Almanac.pdf
     */
    private static final String DILITHIUM3_STANDARD = "ML-DSA-65";

    public static ContentSigner createDilithiumContentSigner(PrivateKey signingKey) throws OperatorCreationException {
        return new JcaContentSignerBuilder(DILITHIUM3_STANDARD).setProvider("BC").build(signingKey);
    }
}
