package fs;

import exceptions.ExtensionNotAllowedException;
import exceptions.FileCountLimitException;
import exceptions.StorageConfigurationException;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

public class StorageConfigurationChecker {
    public static void checkRestrictedExtensions(List<String> restrictedExtensions, List<MyFile> myFiles) throws StorageConfigurationException {
        List<String> matchedExtensions = matchRestrictedExtensions(restrictedExtensions, myFiles);
        if (!restrictedExtensions.isEmpty()) {
            throw new ExtensionNotAllowedException(matchedExtensions.toString());
        }
    }

    private static List<String> matchRestrictedExtensions(List<String> restrictedExtensions, List<MyFile> myFiles) {
        List<String> matchedRestrictedExtensions = new ArrayList<>();
        ListIterator<MyFile> myFileIterator = myFiles.listIterator();
        while (myFileIterator.hasNext()) {
            String type = myFileIterator.next().getType();
            if (restrictedExtensions.contains(type) && !matchedRestrictedExtensions.contains(type)) {
                myFileIterator.remove();
                matchedRestrictedExtensions.add(type);
            }
        }
        return matchedRestrictedExtensions;
    }

    public static void checkMaxFilesRestriction(List<MyFile> myFiles, int maxFilesPerFolder, int currentNumberOfFiles) throws FileCountLimitException {
        if (currentNumberOfFiles + myFiles.size() > maxFilesPerFolder) {
            throw new FileCountLimitException("Maksimalan broj fajlova u ovom folderu je: " + maxFilesPerFolder);
        }
    }
}
