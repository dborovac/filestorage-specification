package exceptions;

public class ParseException extends FileStorageException {
    /**
     * This exception is thrown when the specified pattern is unsuccessfully parsed.
     * @param message the pattern.
     */
    public ParseException(String message) {
        super("Greska prilikom parsiranja sablona. " + message);
    }
}
