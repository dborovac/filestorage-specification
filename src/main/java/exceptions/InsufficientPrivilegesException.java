package exceptions;

public class InsufficientPrivilegesException extends RuntimeException {
    public InsufficientPrivilegesException() {
        super("Nedovoljne privilegije za ovu opciju.");
    }
}
