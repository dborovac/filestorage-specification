package exceptions;

public class NotStorageException extends FileStorageException {
    public NotStorageException(String message) {
        super("Data putanja nije skladiste. " + message);
    }
}
