package fs;

import java.util.*;

public class StorageConfiguration {
    private long maxSize;
    private List<String> restrictedTypes;
    private Map<String, Integer> maxFilesPerFolder;

    public StorageConfiguration(int maxSize, List<String> restrictedTypes, Map<String, Integer> maxFilesPerFolder) {
        this.maxSize = maxSize;
        this.restrictedTypes = restrictedTypes;
        this.maxFilesPerFolder = maxFilesPerFolder;
    }

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

    public boolean addRestrictedType(String type) {
        if (!restrictedTypes.contains(type) && !type.isEmpty()) {
            restrictedTypes.add(type);
            return true;
        }
        return false;
    }

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

    public boolean addFileCountRestriction(String folder, int number) {
        if (!maxFilesPerFolder.containsKey(folder)) {
            maxFilesPerFolder.put(folder, number);
            return true;
        }
        return false;
    }

    public boolean updateFileCountRestriction(String folder, int number) {
        if (maxFilesPerFolder.containsKey(folder)) {
            maxFilesPerFolder.replace(folder, number);
            return true;
        }
        return false;
    }

    public boolean removeFileCountRestriction(String folder) {
        if (maxFilesPerFolder.remove(folder) == null) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "maxSize=" + maxSize + ", restrictedTypes=" + restrictedTypes + ", maxFilesPerFolder=" + maxFilesPerFolder;
    }
}
