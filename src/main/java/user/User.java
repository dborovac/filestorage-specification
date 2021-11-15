package user;

public class User {
    private Credentials credentials;
    private Privileges privileges;

    public User(Credentials credentials, Privileges privileges) {
        this.credentials = credentials;
        this.privileges = privileges;
    }

    public Credentials getCredentials() {
        return credentials;
    }

    public Privileges getPrivileges() {
        return privileges;
    }
}
