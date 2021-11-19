package exceptions;

public class StorageSizeLimitException extends StorageConfigurationException {
    /**
     * This exception is a configuration related exception thrown when moving files if the size limit of the storage is exceeded.
     * @param message the storages' max size in bytes.
     */
    public StorageSizeLimitException(String message) {
        super(message);
    }
}
