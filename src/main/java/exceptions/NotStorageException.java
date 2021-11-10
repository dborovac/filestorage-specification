package exceptions;

public class NotStorageException extends RuntimeException {
    public NotStorageException() {
        super("Data putanja nije skladiste.");
    }
}
