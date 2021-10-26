package fs;

import user.UserManager;
import utils.SortOrder;
import utils.UserInput;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public abstract class FileStorage {
    protected UserManager userManager;
    protected String currentPath;

    public final void init(String path) {
        if (!isStorage(currentPath)) {
            this.currentPath = path;
            String credentials = UserInput.inputUsernameAndPassword();
            userManager.registerAndLogin(credentials);
            connect();
        }
    }

    public final void login(String path) {
        String credentials = UserInput.inputUsernameAndPassword();
        if (isStorage(path) && authenticate(path) && userManager.login(credentials)) {
            this.currentPath = path;
            connect();
        }
    }

    public final void logout() {
        if (userManager.getCurrentUser() != null) {
            userManager.logout();
        }
    }

    public final List<String> ls() {
        if (userManager.getCurrentUser().getPrivileges().hasReadPrivilege()) {
            return ls(this.currentPath).stream().map(File::toString).collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    public final List<String> lsFiles() {
        if (userManager.getCurrentUser().getPrivileges().hasReadPrivilege()) {
            return ls(this.currentPath).stream().filter(File::isFile).map(File::toString).collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    public final List<String> lsDirectories() {
        if (userManager.getCurrentUser().getPrivileges().hasReadPrivilege()) {
            return ls(this.currentPath).stream().filter(File::isDirectory).map(File::toString).collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    public final List<String> lsByType(String type) {
        if (userManager.getCurrentUser().getPrivileges().hasReadPrivilege()) {
            return ls(this.currentPath).stream().filter(file -> file.getType().equals(type)).map(File::toString).collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    public final List<String> lsSortedByName(String path, SortOrder order) {
        if (userManager.getCurrentUser().getPrivileges().hasReadPrivilege()) {
            return ls(this.currentPath).stream().sorted(Comparator.comparing(File::getFileName)).map(File::toString).collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    public final List<String> lsSortedByDate(String path, SortOrder order) {
        return null;
    }

    public final List<String> lsSortedByLastModified(String path, SortOrder order) {
        return null;
    }

    abstract protected void connect();

    abstract protected boolean authenticate(String path);

    abstract protected boolean isStorage(String path);

    abstract protected List<File> ls(String path);

}
