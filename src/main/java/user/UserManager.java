package user;

import exceptions.NoUserFoundException;

import java.util.ArrayList;
import java.util.List;

/**
 * <h1>This class contains a list of users in the storage and a current user field.</h1>
 */

public class UserManager {
    private final List<User> users = new ArrayList<>();
    private User currentUser;

    /**
     * This method registers a new user with god-mode privileges and adds him to the list.
     * @param credentials the users' credentials.
     */
    public void registerAndLogin(Credentials credentials) {
        User user = new User(credentials, new Privileges(PrivilegeTypes.GodMode));
        this.currentUser = user;
        users.add(user);
    }

    /**
     * Sets the current user to null.
     */
    public void logout() {
        this.currentUser = null;
    }

    /**
     * Tries to login (set the current user to the user with specified credentials).
     * @param credentials the users' credentials.
     * @return true if the credentials match with a user object already in the list, false otherwise.
     * @throws NoUserFoundException if no user with specified credentials is found.
     */
    public boolean login(Credentials credentials) throws NoUserFoundException {
        for (User user : users) {
            if (user.getCredentials().equals(credentials)) {
                this.currentUser = user;
                return true;
            }
        }
        throw new NoUserFoundException();
    }

    /**
     * Adds a new user with specified credentials and privileges.
     * @param credentials the users' credentials.
     * @param privileges the users' privileges.
     * @return true if the user was successfully added, false if the user already exists.
     */
    public boolean addUser(Credentials credentials, Privileges privileges) {
        User user = new User(credentials, privileges);
        for (User aUser : users) {
            if (aUser.getCredentials().equals(credentials)) {
                return false;
            }
        }
        users.add(user);
        return true;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public List<User> getUsers() {
        return users;
    }

    /**
     * Tries to find a user with the specified credentials and privileges.
     * @param credentials the users' credentials.
     * @param privileges the users' privileges.
     * @return the requested user.
     * @throws NoUserFoundException if requested user wasn't found.
     */
    public User findUser(Credentials credentials, PrivilegeTypes privileges) throws NoUserFoundException {
        for (User user : users) {
            if (user.getCredentials().equals(credentials) && user.getPrivileges().getType().equals(privileges)) {
                return user;
            }
        }
        throw new NoUserFoundException();
    }
}
