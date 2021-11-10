package exceptions;

public class ParseException extends RuntimeException {
    public ParseException() {
        super("Greska prilikom parsiranja sablona.");
    }
}
