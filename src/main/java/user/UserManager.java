package user;

import exceptions.NoUserFoundException;

import java.util.ArrayList;
import java.util.List;

public class UserManager {
    private final List<User> users = new ArrayList<>();
    private User currentUser;

    public void registerAndLogin(Credentials credentials) {
        User user = new User(credentials, new Privileges(PrivilegeTypes.GodMode));
        this.currentUser = user;
        users.add(user);
    }

    public void logout() {
        this.currentUser = null;
    }

    public boolean login(Credentials credentials) throws NoUserFoundException {
        for (User user : users) {
            if (user.getCredentials().equals(credentials)) {
                this.currentUser = user;
                return true;
            }
        }
        throw new NoUserFoundException();
    }

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

    public User findUser(Credentials credentials, PrivilegeTypes privileges) throws NoUserFoundException {
        for (User user : users) {
            if (user.getCredentials().equals(credentials) && user.getPrivileges().getType().equals(privileges)) {
                return user;
            }
        }
        throw new NoUserFoundException();
    }
}
