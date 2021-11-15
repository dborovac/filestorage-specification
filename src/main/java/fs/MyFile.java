package fs;

public class MyFile {
    private String fileName;
    private String date;
    private String lastModified;
    private boolean isFile;
    private boolean isDirectory;
    private String type;

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
