package exceptions;

public class ExtensionNotAllowedException extends StorageConfigurationException {
    /**
     * This exception is thrown when making new files with extensions that are not allowed.
     * @param message the restricted extensions.
     */
    public ExtensionNotAllowedException(String message) {
        super("Nedozvoljene ekstenzije: " + message);
    }
}
