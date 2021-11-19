package exceptions;

public class StorageNotFoundException extends FileStorageException {
    /**
     * This exception is thrown when the path specified doesn't represent a storage.
     * @param message the path.
     */
    public StorageNotFoundException(String message) {
        super("Skladiste nije pronadjeno. " + message);
    }
}
