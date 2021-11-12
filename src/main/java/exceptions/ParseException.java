package exceptions;

public class ParseException extends FileStorageException {
    public ParseException(String message) {
        super("Greska prilikom parsiranja sablona. " + message);
    }
}
