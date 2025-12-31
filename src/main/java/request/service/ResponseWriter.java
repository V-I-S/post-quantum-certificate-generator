package request.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.bouncycastle.openssl.jcajce.JcaPEMWriter;
import request.model.CertificateRequest;
import request.model.CertificateResponse;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;

public class ResponseWriter {
    private final CertificateRequest request;
    private String csr;
    private String cert;
    private String privateKey;
    private String publicKey;


    public ResponseWriter(CertificateRequest request) {
        this.request = request;
    }

    public ResponseWriter includeCsr(File csrFile) {
        this.csr = read(csrFile);
        return this;
    }

    public ResponseWriter includeCert(File certFile) {
        this.cert = read(certFile);
        return this;
    }

    public ResponseWriter includePrivateKey(File keyFile) {
        this.privateKey = read(keyFile);
        return this;
    }

    public ResponseWriter includePublicKey(File keyFile) {
        this.publicKey = read(keyFile);
        return this;
    }

    public File writeResponse(String directory, String name) {
        File outputFile = new File(directory, name + ".json");
        try (FileWriter writer = new FileWriter(outputFile)) {
            String jsonResponse = makeJsonResponse();
            writer.write(jsonResponse);
        } catch (IOException e) {
            throw new RuntimeException("Exception occurred while writing the response JSON", e);
        }
        return outputFile;
    }

    public String makeJsonResponse() throws JsonProcessingException {
        CertificateResponse response = new CertificateResponse(request, csr, cert, privateKey, publicKey);
        return new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(response);
    }

    private String read(File file) {
        try {
            return Files.readString(file.toPath())
                    .replace("\n", "")
                    .replace("\r", "");
        } catch (IOException e) {
            throw new RuntimeException("Could not read file to prepare a JSON response: " + file, e);
        }
    }
}
