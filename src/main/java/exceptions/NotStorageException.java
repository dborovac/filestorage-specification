package exceptions;

public class NotStorageException extends FileStorageException {
    /**
     * This exception is thrown when the specified path doesn't represent a storage.
     * @param message the path.
     */
    public NotStorageException(String message) {
        super("Data putanja nije skladiste. " + message);
    }
}
