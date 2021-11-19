package exceptions;

public class UserAlreadyExistsException extends FileStorageException {
    /**
     * This exception is thrown when adding new users if the user list in that storage already contains a user with the same credentials.
     * @param message the credentials.
     */
    public UserAlreadyExistsException(String message) {
        super(message);
    }
}
