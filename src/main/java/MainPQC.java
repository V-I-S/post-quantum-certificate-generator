// Post Quantum Cryptography Certificate Issuing Tool Demo
// Patryk Stopyra
// patrykstopyra+pqc@gmail.com

import certificate.CertificateOutputWriter;
import certificate.SelfSignDilithiumCertificateIssuer;
import csr.CsrOutputWriter;
import csr.DilithiumCsrProvider;
import csr.SignatureVerifier;
import format.Digester;
import keypair.DilithiumKeyPairProvider;
import keypair.KeyOutputWriter;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.pkcs.PKCS10CertificationRequest;
import org.bouncycastle.pqc.jcajce.provider.BouncyCastlePQCProvider;
import request.model.CertificateRequest;
import request.service.RequestParser;
import request.service.ResponseWriter;

import java.io.File;
import java.security.KeyPair;
import java.security.Security;
import java.security.cert.X509Certificate;

public class MainPQC {
    public static void main(String[] args) {
        preConfigure();
        validateInput(args);

        File requestFile = new File(args[0]);
        System.out.println(" > Reading request file: " + requestFile.getAbsolutePath());

        CertificateRequest request = new RequestParser(requestFile).parseOrExit();
        System.out.println(" > Certificate request: " + request);
        System.out.println(">>>REQUEST: " + request.options());

        KeyPair keyPair = new DilithiumKeyPairProvider().provideKeyPair();
        System.out.println(" > Key Pair: generated with standard " + keyPair.getPrivate().getAlgorithm());
        System.out.println(" > Private Key (SHA-256 fingerprint): " + Digester.hexDigestRepresentation(keyPair.getPrivate().getEncoded()) +
                "\n                            (length): " + keyPair.getPrivate().getEncoded().length + " B");
        System.out.println(" > Public Key  (SHA-256 fingerprint): " +  Digester.hexDigestRepresentation(keyPair.getPublic().getEncoded())  +
                "\n                            (length): " + keyPair.getPublic().getEncoded().length + " B");

        PKCS10CertificationRequest csr =  new DilithiumCsrProvider(keyPair).provideCsr(request);
        CsrOutputWriter csrOutputWriter = new CsrOutputWriter(csr);
        System.out.println(" > Certificate sign request: " + csrOutputWriter.toHex());

        System.out.println(" > Is CSR signature valid: " + new SignatureVerifier().isSignatureValid(csr));

        X509Certificate certificate = new SelfSignDilithiumCertificateIssuer(keyPair).issueCertificateOrExit(request);
        System.out.println(" > Certificate issued: \n" + certificate);

        String outputDir = args[1];
        String requestFilename = requestFile.getName().substring(0, requestFile.getName().lastIndexOf('.'));
        KeyOutputWriter keyOutputWriter = new KeyOutputWriter(keyPair);

        File privateKeyFile = keyOutputWriter.writePrivateKey(outputDir, requestFilename);
        System.out.println(" > Stored the generated private key: " + privateKeyFile);

        File publicKeyFile = keyOutputWriter.writePublicKey(outputDir, requestFilename);
        System.out.println(" > Stored the generated public key: " + publicKeyFile);

        File csrFile = csrOutputWriter.writeCsr(outputDir, requestFilename);
        System.out.println(" > Stored the generated csr: " + csrFile);

        File certFile = new CertificateOutputWriter(certificate).writeCertificate(outputDir, requestFilename);
        System.out.println(" > Stored the issued certificate: " + certFile);

        ResponseWriter responseWriter = new ResponseWriter(request);
        File responseFile = responseWriter
                .includeCsr(csrFile)
                .includeCert(certFile)
                .includePublicKey(publicKeyFile)
                .includePrivateKey(privateKeyFile)
                .writeResponse(outputDir, requestFilename);
        System.out.println(" > Stored the JSON response: " + responseFile);

        System.out.println("Execution complete. 1 certificate issued with accompanying csr & key pair");
    }

    private static void preConfigure() {
        Security.addProvider(new BouncyCastleProvider());
        Security.addProvider(new BouncyCastlePQCProvider());
    }

    private static void validateInput(String[] args) {
        if (args.length != 2) {
            System.err.println("Wrong number of arguments! Application intakes 2 arguments:" +
                    "\n    - path of the json request file " +
                    "\n    - path of the output directory");
            System.err.println("Usage: k");
            System.exit(1);
        }
    }
}