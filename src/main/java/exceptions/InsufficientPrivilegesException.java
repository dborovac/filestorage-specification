package exceptions;

public class InsufficientPrivilegesException extends FileStorageException {
    /**
     * This exception is thrown when the current user attempts an operation without the required privileges.
     * @param message the operation attempted.
     */
    public InsufficientPrivilegesException(String message) {
        super("Nedovoljne privilegije za ovu opciju. " + message);
    }
}
