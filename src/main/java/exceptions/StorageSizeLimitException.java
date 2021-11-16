package exceptions;

public class StorageSizeLimitException extends StorageConfigurationException {
    public StorageSizeLimitException(String message) {
        super(message);
    }
}
