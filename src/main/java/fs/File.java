package fs;

public class File {
    private String fileName;
    private String date;
    private String lastModified;
    private boolean isFile;
    private boolean isDirectory;
    private String type;

    public File(String fileName, String date, String lastModified, boolean isFile, boolean isDirectory) {
        this.fileName = fileName;
        this.date = date;
        this.lastModified = lastModified;
        this.isFile = isFile;
        this.isDirectory = isDirectory;
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
