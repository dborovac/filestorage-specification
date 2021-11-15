package exceptions;

public class StorageConfigurationException extends FileStorageException {
    public StorageConfigurationException(String message) {
        super("Greska prilikom provere konfiguracije skladista. " + message);
    }
}
