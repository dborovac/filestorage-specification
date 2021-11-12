package exceptions;

public class UnsupportedOperationException extends FileStorageException {
    public UnsupportedOperationException(String message) {
        super("Operacija nije podrzana. " + message);
    }
}
