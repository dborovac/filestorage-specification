package fs;

import java.util.*;

public class StorageConfiguration {
    private long maxSize;
    private List<String> restrictedTypes;
    private Map<String, Integer> maxFilesPerFolder;

    /**
     * @param maxSize maximum size (in bytes) of the storage.
     * @param restrictedTypes a list of restricted extensions as strings.
     * @param maxFilesPerFolder a map with the keys being directories and values representing the maximum number of files in that directory.
     */
    public StorageConfiguration(int maxSize, List<String> restrictedTypes, Map<String, Integer> maxFilesPerFolder) {
        this.maxSize = maxSize;
        this.restrictedTypes = restrictedTypes;
        this.maxFilesPerFolder = maxFilesPerFolder;
    }

    /**
     * Default constructor.
     * Sets the maximum size of the storage to 500 bytes.
     * Initializes the restricted types as an empty {@code ArrayList}.
     * Initializes the max files per folder map as an empty {@code HashMap}.
     */
    public StorageConfiguration() {
        this.maxSize = 500;
        this.restrictedTypes = new ArrayList<>();
        this.maxFilesPerFolder = new HashMap<>();
    }

    public long getMaxSize() {
        return maxSize;
    }

    public void setMaxSize(long maxSize) {
        this.maxSize = maxSize;
    }

    public List<String> getRestrictedTypes() {
        return restrictedTypes;
    }

    public void setRestrictedTypes(List<String> restrictedTypes) {
        this.restrictedTypes = restrictedTypes;
    }

    /**
     * Adds a new restricted type to the storages' configuration if the type isn't already added and if the type isn't an empty string.
     * @param type the type.
     * @return true if the addition was successful or false otherwise.
     */
    public boolean addRestrictedType(String type) {
        if (!restrictedTypes.contains(type) && !type.isEmpty()) {
            restrictedTypes.add(type);
            return true;
        }
        return false;
    }

    /**
     * Removes a restricted type from the storages' configuration if the type exists and if it isn't an empty string.
     * @param type the type.
     * @return true if the removal was successful or false otherwise.
     */
    public boolean removeRestrictedType(String type) {
        if (restrictedTypes.contains(type) && !type.isEmpty()) {
            restrictedTypes.remove(type);
            return true;
        }
        return false;
    }

    public Map<String, Integer> getMaxFilesPerFolder() {
        return maxFilesPerFolder;
    }

    public void setMaxFilesPerFolder(Map<String, Integer> maxFilesPerFolder) {
        this.maxFilesPerFolder = maxFilesPerFolder;
    }

    /**
     * Adds a file count restriction to a directory if the directory doesn't already have a count restriction specified.
     * @param folder the directory path.
     * @param number the max file count.
     * @return true if the addition was successful, false otherwise.
     */
    public boolean addFileCountRestriction(String folder, int number) {
        if (!maxFilesPerFolder.containsKey(folder)) {
            maxFilesPerFolder.put(folder, number);
            return true;
        }
        return false;
    }

    /**
     * Updates a directories' file count restriction if the directory has a count restriction specified.
     * @param folder the directory path.
     * @param number the max file count.
     * @return true if the update was successful, false otherwise.
     */
    public boolean updateFileCountRestriction(String folder, int number) {
        if (maxFilesPerFolder.containsKey(folder)) {
            maxFilesPerFolder.replace(folder, number);
            return true;
        }
        return false;
    }

    /**
     * Removes a directories' file count restriction if the directory specified has a count restriction specified.
     * @param folder the directory path.
     * @return true if the removal was successful, false otherwise.
     */
    public boolean removeFileCountRestriction(String folder) {
        if (maxFilesPerFolder.remove(folder) == null) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "maxSize=" + maxSize + "B, restrictedTypes=" + restrictedTypes + ", maxFilesPerFolder=" + maxFilesPerFolder;
    }
}
