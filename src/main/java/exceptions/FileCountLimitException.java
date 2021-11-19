package exceptions;

public class FileCountLimitException extends StorageConfigurationException {
    /**
     * This exception is thrown when making or moving files if the directory does not allow a certain number of files.
     * @param message the max number of files in the directory.
     */
    public FileCountLimitException(String message) {
        super(message);
    }
}
