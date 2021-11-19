package exceptions;

public class NoFileFoundException extends FileStorageException {
    /**
     * This exception is thrown when the file at the specified path isn't found.
     * @param message the path.
     */
    public NoFileFoundException(String message) {
        super("Nije pronadjen fajl na toj putanji. " + message);
    }
}
