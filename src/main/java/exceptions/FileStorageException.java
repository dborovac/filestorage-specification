package exceptions;

public class FileStorageException extends Exception {
    /**
     * This is the base exception for all file storage related exceptions.
     * @param message message provided by sub-exceptions.
     */
    public FileStorageException(String message) {
        super(message);
    }
}
