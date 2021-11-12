package exceptions;

public class NoFileFoundException extends FileStorageException {
    public NoFileFoundException(String message) {
        super("Nije pronadjen fajl na toj putanji. " + message);
    }
}
