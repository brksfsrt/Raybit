package model;

/**
 * Created by brkfsrt on 23/12/16.
 */
public class TableDataItem {
    private String fileName;
    private String filePath;
    private String fileSize;

    public TableDataItem(String fileName,String filePath,String fileSize){
        this.fileName = fileName;
        this.filePath = filePath;
        this.fileSize = fileSize;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getFileSize() {
        return fileSize;
    }

    public void setFileSize(String fileSize) {
        this.fileSize = fileSize;
    }
}
