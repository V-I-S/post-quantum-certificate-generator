package format;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;

public class Digester {

    public static String hexDigestRepresentation(byte[] data) {
        return hexDigest(data).orElse("<ERROR>");
    }

    public static Optional<String> hexDigest(byte[] data) {
        try {
            return Optional.of(
                    BytesFormatter.toHex(
                            BytesFormatter.sha256(data)));
        } catch (NoSuchAlgorithmException e) {
            return Optional.empty();
        }
    }
}
