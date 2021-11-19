package fs;

import exceptions.*;
import user.Credentials;
import user.Privileges;
import user.UserManager;
import utils.PatternParser;
import utils.SortOrder;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * <h1>This is the main FileStorage abstract class whose abstract methods are to be implemented.</h1>
 * @author Dario Borovac
 * @author Bojan Goretić
 * @version 1.0
 */
public abstract class FileStorage {
    private UserManager userManager;

    /**
     * This method is used to initialize and configure a new storage. It does not require any privileges.
     * It creates a new super-user who becomes the owner of the storage.
     * @param path the path at which the storage should be initialized.
     * @param credentials username and password of the super-user.
     * @throws FileStorageException because of the call to configureStorage()
     */
    public final void init(String path, Credentials credentials) throws FileStorageException {
        init(path);
        getCurrentStorage().setUserManager(new UserManager());
        this.userManager = getCurrentStorage().getUserManager();
        getCurrentStorage().getUserManager().registerAndLogin(credentials);
        updateUsersJson();
        configureStorage(new StorageConfiguration());
    }

    /**
     * This method is used to login to an existing storage at the specified path and with the specified credentials.
     * @param path the path of the storage you wish to login.
     * @param credentials username and password of an existing user in the storage.
     * @throws FileStorageException if the storage or user isn't found.
     */
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

    /**
     * Adds a user to the current storage if and only if the current user is also the super-user of the storage.
     * @param credentials username and password of the user to be added.
     * @param privileges privileges of the user to be added.
     * @throws FileStorageException if privileges are insufficient or the user already exists.
     */
    public final void addUser(Credentials credentials, Privileges privileges) throws FileStorageException {
        if (!userManager.getCurrentUser().getPrivileges().isGod()) {
            throw new InsufficientPrivilegesException("Niste vlasnik ovog skladista.");
        }
        if (!userManager.addUser(credentials, privileges)) {
            throw new UserAlreadyExistsException("Korisnik " + credentials.toString() + " vec postoji.");
        }
        updateUsersJson();
    }

    /**
     * Logs out of the current storage.
     */
    public final void logout() {
        if (userManager.getCurrentUser() != null) {
            userManager.logout();
            disconnect();
        }
    }

    /**
     * Configures a storage if the current user is also the super-user.
     * @param storageConfiguration the configuration object containing all the properties.
     * @throws FileStorageException if privileges are insufficient.
     */
    public final void configureStorage(StorageConfiguration storageConfiguration) throws FileStorageException {
        if (!userManager.getCurrentUser().getPrivileges().isGod()) {
            throw new InsufficientPrivilegesException("Nemate privilegiju za konfigurisanje skladista.");
        }
        getCurrentStorage().setStorageConfiguration(storageConfiguration);
        updateConfigJson();
    }

    /**
     * Gets the current storage configuration if the current user is also the super-user.
     * @return current storage configuration.
     * @throws InsufficientPrivilegesException if privileges of the current user are insufficient.
     */
    public final StorageConfiguration getCurrentStorageConfiguration() throws InsufficientPrivilegesException {
        if (!userManager.getCurrentUser().getPrivileges().isGod()) {
            throw new InsufficientPrivilegesException("Nemate privilegiju za dobijanje trenutne konfiguracije skladista.");
        }
        return getCurrentStorage().getStorageConfiguration();
    }

    /**
     * Lists all the files in the directory at the specified path.
     * @param path the path of the directory.
     * @return a list of MyFile objects with the following attributes:
     * name, created time, modified time, if the file is a directory and its extension.
     * @throws InsufficientPrivilegesException if privileges of the current user are insufficient.
     */
    public final List<MyFile> lsAll(String path) throws InsufficientPrivilegesException {
        if (!userManager.getCurrentUser().getPrivileges().hasReadPrivilege()) {
            throw new InsufficientPrivilegesException("Nemate privilegiju za citanje.");
        }
        return ls(path);
    }

    /**
     * Lists only files (not directories) at the specified path.
     * @param path the path of the directory.
     * @return a list of MyFile objects with the following attributes:
     * name, created time, modified time, if the file is a directory and its extension.
     * @throws InsufficientPrivilegesException if privileges of the current user are insufficient.
     */
    public final List<MyFile> lsFiles(String path) throws InsufficientPrivilegesException {
        return lsFiltered(path, MyFile::isFile);
    }

    /**
     * Lists only directories (not files) at the specified path.
     * @param path the path of the directory.
     * @return a list of MyFile objects with the following attributes:
     * name, created time, modified time, if the file is a directory and its extension.
     * @throws InsufficientPrivilegesException if privileges of the current user are insufficient.
     */
    public final List<MyFile> lsDirectories(String path) throws InsufficientPrivilegesException {
        return lsFiltered(path, MyFile::isDirectory);
    }

    /**
     * Lists files and directories which are of a certain type.
     * @param path the path of the directory.
     * @param type the type (extension) to be filtered by.
     * @return a list of MyFile objects with the following attributes:
     * name, created time, modified time, if the file is a directory and its extension.
     * @throws InsufficientPrivilegesException if privileges of the current user are insufficient.
     */
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

    /**
     * Lists all the files sorted lexicographically by their name.
     * @param path the path of the directory.
     * @param order the order (ascending, descending).
     * @return a list of MyFile objects with the following attributes:
     * name, created time, modified time, if the file is a directory and its extension.
     * @throws InsufficientPrivilegesException if privileges of the current user are insufficient.
     */
    public final List<MyFile> lsSortedByName(String path, SortOrder order) throws InsufficientPrivilegesException {
        return lsSorted(path, order, Comparator.comparing(MyFile::getFileName));
    }

    /**
     * Lists all the files sorted by date of creation.
     * @param path the path of the directory.
     * @param order the order (ascending, descending).
     * @return a list of MyFile objects with the following attributes:
     * name, created time, modified time, if the file is a directory and its extension.
     * @throws InsufficientPrivilegesException if privileges of the current user are insufficient.
     */
    public final List<MyFile> lsSortedByDate(String path, SortOrder order) throws InsufficientPrivilegesException {
        return lsSorted(path, order, Comparator.comparing(MyFile::getDate));
    }

    /**
     * Lists all the files sorted by date of last modification.
     * @param path the path of the directory.
     * @param order the order (ascending, descending).
     * @return a list of MyFile objects with the following attributes:
     * name, created time, modified time, if the file is a directory and its extension.
     * @throws InsufficientPrivilegesException if privileges of the current user are insufficient.
     */
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

    /**
     * Creates empty files or directories based on the provided pattern that this method parses.
     * @param pattern pattern based on which the method makes files.
     * @param path path of the directory in which to make files.
     * @throws FileStorageException if privileges are insufficient or if storage configuration settings don't allow the operation.
     */
    public final void makeFiles(String pattern, String path) throws FileStorageException {
        if (!userManager.getCurrentUser().getPrivileges().hasWritePrivilege()) {
            throw new InsufficientPrivilegesException("Nemate privilegiju za pisanje.");
        }
        List<MyFile> myFiles = PatternParser.parse(pattern);
        StorageConfiguration currentStorageConfiguration = getCurrentStorageConfiguration();
        StorageConfigurationChecker.checkRestrictedExtensions(currentStorageConfiguration.getRestrictedTypes(), myFiles);

        int maxFilesPerFolder = currentStorageConfiguration.getMaxFilesPerFolder().getOrDefault(path, 999);
        int currentNumberOfFiles = getNumberOfFilesInDirectory(path);
        StorageConfigurationChecker.checkMaxFilesRestriction(myFiles, maxFilesPerFolder, currentNumberOfFiles);

        if (!myFiles.isEmpty()) {
            makeFiles(path, myFiles);
        }
    }

    /**
     * Deletes the file or directory at the specified path.
     * @param path path of the file or directory.
     * @throws FileStorageException if privileges are insufficient.
     */
    public final void deleteFile(String path) throws FileStorageException {
        if (!userManager.getCurrentUser().getPrivileges().hasDeletePrivilege()) {
            throw new InsufficientPrivilegesException("Nemate privilegiju za brisanje.");
        }
        removeFile(path);
    }

    /**
     * Downloads the file or directory at the specified path and places it in the users' download folder.
     * @param path path of the file or directory.
     * @throws FileStorageException if privileges are insufficient.
     */
    public final void downloadFile(String path) throws FileStorageException {
        if (!userManager.getCurrentUser().getPrivileges().hasDownloadPrivilege()) {
            throw new InsufficientPrivilegesException("Nemate privilegiju za preuzimanje.");
        }
        getFile(path);
    }

    /**
     * Moves the source file/directory to the destination directory.
     * @param source source file or directory.
     * @param destination destination directory.
     * @throws FileStorageException if privileges are insufficient.
     */
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

    abstract protected void updateConfigJson();

    abstract protected List<MyFile> ls(String path);

    abstract protected void makeFiles(String path, List<MyFile> names);

    abstract protected void removeFile(String path) throws FileStorageException;

    abstract protected void getFile(String path) throws FileStorageException;

    abstract protected void move(String source, String destination) throws FileStorageException;

    abstract protected int getNumberOfFilesInDirectory(String path);
}
