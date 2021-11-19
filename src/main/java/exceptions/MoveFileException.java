package exceptions;

public class MoveFileException extends FileStorageException {
    /**
     * This exception is thrown when moving files if the file does not exist.
     * @param message name of the file.
     */
    public MoveFileException(String message) {
        super("Greska pri premestanju fajla. " + message);
    }
}
