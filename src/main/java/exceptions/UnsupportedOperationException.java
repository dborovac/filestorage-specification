package exceptions;

public class UnsupportedOperationException extends FileStorageException {
    /**
     * This exception is to be thrown by the implementations of this specification if an operation isn't supported.
     * @param message the operation description.
     */
    public UnsupportedOperationException(String message) {
        super("Operacija nije podrzana. " + message);
    }
}
