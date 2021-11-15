package exceptions;

public class MoveFileException extends FileStorageException {
    public MoveFileException(String message) {
        super("Greska pri premestanju fajla. " + message);
    }
}
