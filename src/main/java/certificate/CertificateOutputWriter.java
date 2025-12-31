package certificate;

import org.bouncycastle.openssl.jcajce.JcaPEMWriter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.security.cert.X509Certificate;

public class CertificateOutputWriter {
    private final X509Certificate certificate;

    public CertificateOutputWriter(X509Certificate certificate) {
        this.certificate = certificate;
    }

    public File writeCertificate(String directory, String name) {
        File outputFile = new File(directory, name + ".crt");
        try (JcaPEMWriter writer = new JcaPEMWriter(new FileWriter(outputFile))) {
            writer.writeObject(certificate);
        } catch (IOException e) {
            throw new RuntimeException("Certificate PEM writing error", e);
        }
        return outputFile;
    }
}
