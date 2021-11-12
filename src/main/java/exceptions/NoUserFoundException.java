package exceptions;

public class NoUserFoundException extends FileStorageException {
    public NoUserFoundException(String message) {
        super("Greska prilokom logovanja. Proverite korisnicko ime i/ili lozinku." + message);
    }

    public NoUserFoundException() {
        super("Greska prilokom logovanja. Proverite korisnicko ime i/ili lozinku.");
    }
}
