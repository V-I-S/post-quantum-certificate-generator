package keypair;

import java.security.*;

public class DilithiumKeyPairProvider {

    public KeyPair provideKeyPair() {
        KeyPairGenerator generator;
        try {
            generator = KeyPairGeneratorFactory.createDilithiumKeyPairGenerator();
        } catch (NoSuchAlgorithmException | InvalidAlgorithmParameterException | NoSuchProviderException e) {
            throw new RuntimeException("Could not load the key pair generator", e);
        }
        return generator.generateKeyPair();
    }
}
