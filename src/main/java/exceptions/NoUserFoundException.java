package exceptions;

public class NoUserFoundException extends RuntimeException {
    public NoUserFoundException() {
        super("Greska prilikom logovanja. Proverite korisnicko ime i lozinku.");
    }
}
