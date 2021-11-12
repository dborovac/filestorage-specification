package exceptions;

public class StorageNotFoundException extends FileStorageException {
    public StorageNotFoundException(String message) {
        super("Skladiste nije pronadjeno. " + message);
    }
}
