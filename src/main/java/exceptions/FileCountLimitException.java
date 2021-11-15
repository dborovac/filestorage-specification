package exceptions;

public class FileCountLimitException extends StorageConfigurationException {
    public FileCountLimitException(String message) {
        super(message);
    }
}
