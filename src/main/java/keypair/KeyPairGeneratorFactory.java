package keypair;

import org.bouncycastle.jcajce.spec.MLDSAParameterSpec;
import org.bouncycastle.pqc.jcajce.spec.DilithiumParameterSpec;

import java.security.*;
import java.security.spec.AlgorithmParameterSpec;

public class KeyPairGeneratorFactory {
    /*
        Dilithium3 is supported as a ML-DSA-65 NIST standard.
        https://downloads.bouncycastle.org/java/docs/PQC-Almanac.pdf
     */
    private static final String DILITHIUM3_STANDARD = "ML-DSA-65";
    private static final AlgorithmParameterSpec DILITHIUM3_ALG_PARAM_SPEC = MLDSAParameterSpec.ml_dsa_65;

    public static KeyPairGenerator createDilithiumKeyPairGenerator()
            throws NoSuchAlgorithmException, InvalidAlgorithmParameterException, NoSuchProviderException {
        KeyPairGenerator generator = KeyPairGenerator.getInstance(DILITHIUM3_STANDARD, "BC");
        generator.initialize(DILITHIUM3_ALG_PARAM_SPEC, SecureRandom.getInstanceStrong());
        return generator;
    }
}
