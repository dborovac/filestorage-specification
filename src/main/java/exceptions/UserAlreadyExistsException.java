package exceptions;

public class UserAlreadyExistsException extends FileStorageException {
    public UserAlreadyExistsException(String message) {
        super(message);
    }
}
