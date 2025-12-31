package request.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record CertificateRequest(
        @JsonProperty("common_name") String commonName,
        @JsonProperty("key_type") String keyType,
        @JsonProperty("key_size") String keySize,
        @JsonProperty("signature_algorithm") String signatureAlgorithm,
        @JsonProperty("signature_oid") String signatureOid,
        Subject subject,
        @JsonProperty("subject_alternative_names") List<SubjectAlternativeName> subjectAlternativeNames,
        @JsonProperty(value = "options") Options options
) {

    @Override
    public String toString() {
        return "CertificateRequest{" +
                "commonName='" + commonName + '\'' +
                ", keyType='" + keyType + '\'' +
                ", keySize='" + keySize + '\'' +
                ", signatureAlgorithm='" + signatureAlgorithm + '\'' +
                ", signatureOid='" + signatureOid + '\'' +
                ", subject=" + subject +
                ", subjectAlternativeNames=" + subjectAlternativeNames +
                '}';
    }

    public record Subject(
            String organization,
            @JsonProperty("organizational_unit") String organizationalUnit,
            String country,
            String state,
            String locality,
            String email
    ) {}

    public record SubjectAlternativeName(
            String type,
            String value
    ) {}

    public record Options(
            @JsonProperty(value = "output_format", defaultValue = "DER") OutputFormat outputFormat,
            @JsonProperty(value = "bare_content") Boolean bareContent
    ) {}
}