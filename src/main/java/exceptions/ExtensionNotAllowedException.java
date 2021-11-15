package exceptions;

public class ExtensionNotAllowedException extends StorageConfigurationException {
    public ExtensionNotAllowedException(String message) {
        super("Nedozvoljene ekstenzije: " + message);
    }
}
