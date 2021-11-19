package exceptions;

public class StorageConfigurationException extends FileStorageException {
    /**
     * This exception is the base class for all configuration related exceptions.
     * @param message message provided by the sub-exceptions.
     */
    public StorageConfigurationException(String message) {
        super("Greska prilikom provere konfiguracije skladista. " + message);
    }
}
