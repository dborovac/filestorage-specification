package fs;

import exceptions.InsufficientPrivilegesException;
import exceptions.NoFileFoundException;
import exceptions.NoUserFoundException;
import exceptions.NotStorageException;
import exceptions.UnsupportedOperationException;
import user.PrivilegeTypes;
import user.UserManager;
import utils.PatternParser;
import utils.SortOrder;

import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public abstract class FileStorage {
    protected String currentPath;
    private UserManager userManager;

    public final void init(String path, String credentials) {
        currentPath = path;
        init();
        userManager = getCurrentStorage().getUserManager();
        userManager.registerAndLogin(credentials);
        addUserToJson(credentials, PrivilegeTypes.GodMode);
    }

    public final void login(String path, String credentials) {
        if (!isStorage(path)) {
            throw new NotStorageException("Putanja: " + path);
        }
        UserManager oldUserManager = null;
        if (getCurrentStorage() != null) oldUserManager = getCurrentStorage().getUserManager();
        connect(path);
        userManager = getCurrentStorage().getUserManager();
        if (!userManager.login(credentials) || !authenticate(path)) {
            disconnect();
            userManager = oldUserManager;
            throw new NoUserFoundException();
        }
        currentPath = path;
    }

    public final void addUser(String credentials, PrivilegeTypes privileges) {
        if (!userManager.getCurrentUser().getPrivileges().isGod()) {
            throw new InsufficientPrivilegesException("Niste vlasnik ovog skladista.");
        }
        userManager.addUser(credentials, privileges);
        addUserToJson(credentials, privileges);
    }

    public final void logout() {
        if (userManager.getCurrentUser() != null) {
            userManager.logout();
            disconnect();
        }
    }

    public final List<MyFile> ls() {
        if (!userManager.getCurrentUser().getPrivileges().hasReadPrivilege()) {
            throw new InsufficientPrivilegesException("Nemate privilegiju za citanje.");
        }
        return ls(this.currentPath);
    }

    public final List<MyFile> lsFiles() {
        return lsFiltered(MyFile::isFile);
    }

    public final List<MyFile> lsDirectories() {
        return lsFiltered(MyFile::isDirectory);
    }

    public final List<MyFile> lsByType(String type) {
        return lsFiltered(file -> file.getType().equals(type));
    }

    private List<MyFile> lsFiltered(Predicate<MyFile> predicate) {
        if (!userManager.getCurrentUser().getPrivileges().hasReadPrivilege()) {
            throw new InsufficientPrivilegesException("Nemate privilegiju za citanje.");
        }
        return ls(this.currentPath).stream()
                .filter(predicate)
                .collect(Collectors.toList());
    }

    public final List<MyFile> lsSortedByName(SortOrder order) {
        return lsSorted(order, Comparator.comparing(MyFile::getFileName));
    }

    public final List<MyFile> lsSortedByDate(SortOrder order) {
        return lsSorted(order, Comparator.comparing(MyFile::getDate));
    }

    public final List<MyFile> lsSortedByLastModified(SortOrder order) {
        return lsSorted(order, Comparator.comparing(MyFile::getLastModified));
    }

    private List<MyFile> lsSorted(SortOrder order, Comparator<MyFile> comparing) {
        if (!userManager.getCurrentUser().getPrivileges().hasReadPrivilege()) {
            throw new InsufficientPrivilegesException("Nemate privilegiju za citanje.");
        }
        if (order == SortOrder.DESC) {
            return ls(this.currentPath).stream()
                    .sorted(comparing.reversed())
                    .collect(Collectors.toList());
        }
        return ls(this.currentPath).stream()
                .sorted(comparing)
                .collect(Collectors.toList());
    }

    public final void makeFiles(String pattern, String path) {
        if (!userManager.getCurrentUser().getPrivileges().hasWritePrivilege()) {
            throw new InsufficientPrivilegesException("Nemate privilegiju za pisanje.");
        }
        List<String> fileNames = PatternParser.parse(pattern);
        if (!fileNames.isEmpty() && fileNames.get(0).equals("mkdir")) {
            fileNames.remove(0);
            makeFiles(path, fileNames, true);
        } else {
            fileNames.remove(0);
            makeFiles(path, fileNames, false);
        }
    }

    public final void deleteFile(String path) {
        if (!userManager.getCurrentUser().getPrivileges().hasDeletePrivilege()) {
            throw new InsufficientPrivilegesException("Nemate privilegiju za brisanje.");
        }
        removeFile(path);
    }

    public final void downloadFile(String path) {
        if (!userManager.getCurrentUser().getPrivileges().hasDownloadPrivilege()) {
            throw new InsufficientPrivilegesException("Nemate privilegiju za preuzimanje.");
        }
        getFile(path);
    }

    abstract protected void init();

    abstract protected void connect(String path);

    abstract protected void disconnect();

    abstract protected Storage getCurrentStorage();

    abstract protected boolean authenticate(String path);

    abstract protected boolean isStorage(String path);

    abstract protected void addUserToJson(String credentials, PrivilegeTypes privileges);

    abstract protected List<MyFile> ls(String path);

    abstract protected void makeFiles(String path, List<String> names, boolean isDirectory);

    abstract protected void removeFile(String path);

    abstract protected void getFile(String path);
}
