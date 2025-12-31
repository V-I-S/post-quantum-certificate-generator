package csr;

import format.BytesFormatter;
import org.bouncycastle.openssl.jcajce.JcaPEMWriter;
import org.bouncycastle.pkcs.PKCS10CertificationRequest;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class CsrOutputWriter {
    private final PKCS10CertificationRequest csr;

    public CsrOutputWriter(PKCS10CertificationRequest csr) {
        this.csr = csr;
    }

    public File writeCsr(String directory, String name) {
        File outputFile = new File(directory, name + ".csr");
        try (JcaPEMWriter writer = new JcaPEMWriter(new FileWriter(outputFile))) {
            writer.writeObject(csr);
        } catch (IOException e) {
            throw new RuntimeException("Certificate sign request PEM writing error", e);
        }
        return outputFile;
    }

    public String toHex() {
        try {
            return BytesFormatter.toHex(csr.getEncoded()).toUpperCase();
        } catch (IOException e) {
            throw new RuntimeException("Error while encoding the CSR", e);
        }
    }


}
