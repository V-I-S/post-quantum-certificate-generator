package request.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import request.model.CertificateRequest;

import java.io.File;
import java.io.IOException;

public class RequestParser {
    private final File requestfile;

    public RequestParser(File request) {
        this.requestfile = request;
    }

    public CertificateRequest parseOrExit() {
        try {
            return parse();
        } catch (IOException ex) {
            throw new RuntimeException("Error while parsing the request file", ex);
        }
    }

    public CertificateRequest parse() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(requestfile, CertificateRequest.class);
    }
}
