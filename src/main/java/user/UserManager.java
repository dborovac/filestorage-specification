package user;

import java.util.ArrayList;
import java.util.List;

public class UserManager {
    private final List<User> users = new ArrayList<>();
    private User currentUser;

    public void registerAndLogin(String credentials) {
        this.currentUser = new User(credentials, new Privileges(PrivilegeTypes.GodMode));
    }

    public void logout() {
        this.currentUser = null;
    }

    public boolean login(String credentials) {
        for (User user : users) {
            if (user.getCredentials().equals(credentials)) {
                this.currentUser = user;
                return true;
            }
        }
        return false;
    }

    public boolean addUser(String credentials, Privileges privilege) {
        if (currentUser != null && currentUser.getPrivileges().isGod()) {
            User user = new User(credentials, privilege);
            this.users.add(user);
            return true;
        }
        return false;
    }

    public User getCurrentUser() {
        return currentUser;
    }
}
