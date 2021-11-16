package fs;

import exceptions.*;
import user.Credentials;
import user.Privileges;
import user.UserManager;
import utils.PatternParser;
import utils.SortOrder;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.ListIterator;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public abstract class FileStorage {
    private UserManager userManager;

    public final void init(String path, Credentials credentials) {
        init(path);
        getCurrentStorage().setUserManager(new UserManager());
        this.userManager = getCurrentStorage().getUserManager();
        getCurrentStorage().getUserManager().registerAndLogin(credentials);
        updateUsersJson();
        getCurrentStorage().setStorageConfiguration(new StorageConfiguration());
    }

    public final void login(String path, Credentials credentials) throws FileStorageException {
        UserManager oldUserManager = null;
        if (getCurrentStorage() != null) {
            oldUserManager = getCurrentStorage().getUserManager();
        }
        connect(path);
        this.userManager = getCurrentStorage().getUserManager();
        if (!userManager.login(credentials)) {
            disconnect();
            this.userManager = oldUserManager;
            throw new NoUserFoundException();
        }
        if (!isStorage(path)) {
            throw new NotStorageException("Putanja: " + path);
        }
        if (!authenticate(path)) {
            throw new NoUserFoundException("Korisnik nije pronadjen u users.json");
        }
    }

    public final void addUser(Credentials credentials, Privileges privileges) throws FileStorageException {
        if (!userManager.getCurrentUser().getPrivileges().isGod()) {
            throw new InsufficientPrivilegesException("Niste vlasnik ovog skladista.");
        }
        if (!userManager.addUser(credentials, privileges)) {
            throw new UserAlreadyExistsException("Korisnik " + credentials.toString() + " vec postoji.");
        }
        updateUsersJson();
    }

    public final void logout() {
        if (userManager.getCurrentUser() != null) {
            userManager.logout();
            disconnect();
        }
    }

    public final void configureStorage(StorageConfiguration storageConfiguration) throws FileStorageException {
        if (!userManager.getCurrentUser().getPrivileges().isGod()) {
            throw new InsufficientPrivilegesException("Nemate privilegiju za konfigurisanje skladista.");
        }
        getCurrentStorage().setStorageConfiguration(storageConfiguration);
    }

    public final List<MyFile> lsAll(String path) throws InsufficientPrivilegesException {
        if (!userManager.getCurrentUser().getPrivileges().hasReadPrivilege()) {
            throw new InsufficientPrivilegesException("Nemate privilegiju za citanje.");
        }
        return ls(path);
    }

    public final List<MyFile> lsFiles(String path) throws InsufficientPrivilegesException {
        return lsFiltered(path, MyFile::isFile);
    }

    public final List<MyFile> lsDirectories(String path) throws InsufficientPrivilegesException {
        return lsFiltered(path, MyFile::isDirectory);
    }

    public final List<MyFile> lsByType(String path, String type) throws InsufficientPrivilegesException {
        return lsFiltered(path, file -> file.getType().equals(type));
    }

    private List<MyFile> lsFiltered(String path, Predicate<MyFile> predicate) throws InsufficientPrivilegesException {
        if (!userManager.getCurrentUser().getPrivileges().hasReadPrivilege()) {
            throw new InsufficientPrivilegesException("Nemate privilegiju za citanje.");
        }
        return ls(path).stream()
                .filter(predicate)
                .collect(Collectors.toList());
    }

    public final List<MyFile> lsSortedByName(String path, SortOrder order) throws InsufficientPrivilegesException {
        return lsSorted(path, order, Comparator.comparing(MyFile::getFileName));
    }

    public final List<MyFile> lsSortedByDate(String path, SortOrder order) throws InsufficientPrivilegesException {
        return lsSorted(path, order, Comparator.comparing(MyFile::getDate));
    }

    public final List<MyFile> lsSortedByLastModified(String path, SortOrder order) throws InsufficientPrivilegesException {
        return lsSorted(path, order, Comparator.comparing(MyFile::getLastModified));
    }

    private List<MyFile> lsSorted(String path, SortOrder order, Comparator<MyFile> comparing) throws InsufficientPrivilegesException {
        if (!userManager.getCurrentUser().getPrivileges().hasReadPrivilege()) {
            throw new InsufficientPrivilegesException("Nemate privilegiju za citanje.");
        }
        if (order == SortOrder.DESC) {
            return ls(path).stream()
                    .sorted(comparing.reversed())
                    .collect(Collectors.toList());
        }
        return ls(path).stream()
                .sorted(comparing)
                .collect(Collectors.toList());
    }

    public final void makeFiles(String pattern, String path) throws FileStorageException {
        if (!userManager.getCurrentUser().getPrivileges().hasWritePrivilege()) {
            throw new InsufficientPrivilegesException("Nemate privilegiju za pisanje.");
        }
        List<MyFile> myFiles = PatternParser.parse(pattern);
        List<String> restrictedExtensions = getRestrictedExtensions(myFiles);
        if (!restrictedExtensions.isEmpty()) {
            if (!myFiles.isEmpty()) makeFiles(path, myFiles);
            throw new ExtensionNotAllowedException(restrictedExtensions.toString());
        }
        StorageConfiguration currentStorageConfiguration = getCurrentStorage().getStorageConfiguration();
        int maxFilesPerFolder = currentStorageConfiguration.getMaxFilesPerFolder().getOrDefault(path, 999);
        int currentNumberOfFiles = getNumberOfFilesInDirectory(path);
        if (currentNumberOfFiles + myFiles.size() > maxFilesPerFolder) {
            throw new FileCountLimitException("Maksimalan broj fajlova u ovom folderu je: " + maxFilesPerFolder);
        }
        makeFiles(path, myFiles);
    }

    private List<String> getRestrictedExtensions(List<MyFile> myFiles) {
        List<String> restrictedExtensions = new ArrayList<>();
        ListIterator<MyFile> myFileIterator = myFiles.listIterator();
        while (myFileIterator.hasNext()) {
            String type = myFileIterator.next().getType();
            if (getCurrentStorage().getStorageConfiguration().getRestrictedTypes().contains(type) && !restrictedExtensions.contains(type)) {
                myFileIterator.remove();
                restrictedExtensions.add(type);
            }
        }
        return restrictedExtensions;
    }

    public final void deleteFile(String path) throws FileStorageException {
        if (!userManager.getCurrentUser().getPrivileges().hasDeletePrivilege()) {
            throw new InsufficientPrivilegesException("Nemate privilegiju za brisanje.");
        }
        removeFile(path);
    }

    public final void downloadFile(String path) throws FileStorageException {
        if (!userManager.getCurrentUser().getPrivileges().hasDownloadPrivilege()) {
            throw new InsufficientPrivilegesException("Nemate privilegiju za preuzimanje.");
        }
        getFile(path);
    }

    public final void moveFile(String source, String destination) throws FileStorageException {
        if (!userManager.getCurrentUser().getPrivileges().hasWritePrivilege()) {
            throw new InsufficientPrivilegesException("Nemate privilegiju za premestanje fajlova.");
        }
        move(source, destination);
    }

    abstract protected void init(String path);

    abstract protected void connect(String path) throws FileStorageException;

    abstract protected void disconnect();

    abstract protected Storage getCurrentStorage();

    abstract protected boolean authenticate(String path);

    abstract protected boolean isStorage(String path) throws FileStorageException;

    abstract protected void updateUsersJson();

    abstract protected List<MyFile> ls(String path);

    abstract protected void makeFiles(String path, List<MyFile> names);

    abstract protected void removeFile(String path) throws FileStorageException;

    abstract protected void getFile(String path) throws FileStorageException;

    abstract protected void move(String source, String destination) throws FileStorageException;

    abstract protected int getNumberOfFilesInDirectory(String path);
}
