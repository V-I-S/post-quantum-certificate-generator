package keypair;

import org.bouncycastle.openssl.jcajce.JcaPEMWriter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;

public class KeyOutputWriter {
    private final PrivateKey privateKey;
    private final PublicKey publicKey;

    public KeyOutputWriter(KeyPair keyPair) {
        privateKey = keyPair.getPrivate();
        publicKey = keyPair.getPublic();
    }

    public File writePrivateKey(String directory, String name) {
        File outputFile = new File(directory, name + ".key");
        try (JcaPEMWriter writer = new JcaPEMWriter(new FileWriter(outputFile))) {
            writer.writeObject(privateKey);
        } catch (IOException e) {
            throw new RuntimeException("Private key PEM writing error", e);
        }
        return outputFile;
    }

    public File writePublicKey(String directory, String name) {
        File outputFile = new File(directory, name + ".pub");
        try (JcaPEMWriter writer = new JcaPEMWriter(new FileWriter(outputFile))) {
            writer.writeObject(publicKey);
        } catch (IOException e) {
            throw new RuntimeException("Public key PEM writing error", e);
        }
        return outputFile;
    }
}
