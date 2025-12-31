package request.model;

public enum OutputFormat {
    PEM(true),
    DER(false);

    public final boolean isSupported;

    OutputFormat(boolean isSupported) {
        this.isSupported = isSupported;
    }
}
