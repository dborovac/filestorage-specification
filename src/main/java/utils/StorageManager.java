package utils;

import fs.FileStorage;

public class StorageManager {
    private static FileStorage _storage;

    public static void registerStorage(FileStorage storage) {
        _storage = storage;
    }

    public static FileStorage getStorage() {
        return _storage;
    }
}
