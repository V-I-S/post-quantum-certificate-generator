package request.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class RequestReader {
    private final Path filePath;

    public RequestReader(String filePath) {
        this.filePath = Paths.get(filePath);
    }

    public String readOrExit() {
        try {
            return read();
        } catch (IOException ex) {
            System.err.println("Error reading the request file: " + ex.getMessage());
            System.exit(1);
        }
        return null;
    }

    public String read() throws IOException {
        StringBuilder sb = new StringBuilder();
        Files.readAllLines(filePath).forEach(sb::append);
        return sb.toString();
    }
}
