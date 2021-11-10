package fs;

import user.UserManager;

public class Storage {
    private Object rootFile;
    private Object usersFile;
    private Object configFile;
    private UserManager userManager;

    public Object getRootFile() {
        return rootFile;
    }

    public void setRootFile(Object rootFile) {
        this.rootFile = rootFile;
    }

    public Object getUsersFile() {
        return usersFile;
    }

    public void setUsersFile(Object usersFile) {
        this.usersFile = usersFile;
    }

    public Object getConfigFile() {
        return configFile;
    }

    public void setConfigFile(Object configFile) {
        this.configFile = configFile;
    }

    public UserManager getUserManager() {
        return userManager;
    }

    public void setUserManager(UserManager userManager) {
        this.userManager = userManager;
    }
}
