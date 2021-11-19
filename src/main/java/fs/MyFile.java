package fs;

/**
 * <h1>This class represents an abstract file with attributes that are shared for files across all platforms and environments.</h1>
 */

public class MyFile {
    private final String fileName;
    private String date;
    private String lastModified;
    private final boolean isFile;
    private final boolean isDirectory;
    private final String type;

    public MyFile(String fileName, String date, String lastModified, boolean isFile, boolean isDirectory, String type) {
        this.fileName = fileName;
        this.date = date;
        this.lastModified = lastModified;
        this.isFile = isFile;
        this.isDirectory = isDirectory;
        this.type = type;
    }

    public MyFile(String fileName, boolean isFile, String type) {
        this.fileName = fileName;
        this.isFile = isFile;
        this.isDirectory = !isFile;
        this.type = type;
    }

    @Override
    public String toString() {
        return fileName + " " + date + " " + lastModified;
    }

    public boolean isFile() {
        return isFile;
    }

    public boolean isDirectory() {
        return isDirectory;
    }

    public String getFileName() {
        return fileName;
    }

    public String getDate() {
        return date;
    }

    public String getLastModified() {
        return lastModified;
    }

    public String getType() {
        return type;
    }
}
