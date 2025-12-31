package request.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record CertificateResponse(
        @JsonProperty("request") CertificateRequest request,
        @JsonProperty("csr") String csr,
        @JsonProperty("certificate") String cert,
        @JsonProperty("private_key") String privateKey,
        @JsonProperty("public_key") String publicKey
) {
    @Override
    public String toString() {
        return "CertificateResponse{" +
                "request=" + request +
                ", csr='" + csr + '\'' +
                ", cert='" + cert + '\'' +
                ", privateKey='" + privateKey + '\'' +
                ", publicKey='" + publicKey + '\'' +
                '}';
    }
}