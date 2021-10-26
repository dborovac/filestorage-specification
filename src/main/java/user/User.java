package user;

public class User {
    private String credentials;
    private Privileges privileges;

    public User(String credentials, Privileges privileges) {
        this.credentials = credentials;
        this.privileges = privileges;
    }

    public String getCredentials() {
        return credentials;
    }

    public Privileges getPrivileges() {
        return privileges;
    }
}
