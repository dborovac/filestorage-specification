package exceptions;

public class InsufficientPrivilegesException extends FileStorageException {
    public InsufficientPrivilegesException(String message) {
        super("Nedovoljne privilegije za ovu opciju. " + message);
    }
}
