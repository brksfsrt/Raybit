package Controller;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.TreeItem;
import org.apache.commons.net.ftp.FTPFile;

/**
 * Created by brkfsrt on 29/11/16.
 */
public class FileTreeItem extends TreeItem<String> {
    private String fullPath;
    private String fileName;
    public String getFullPath(){return(this.fullPath);}

    private boolean isDirectory;
    public boolean isDirectory(){return(this.isDirectory);}
    public String getFileName(){return(this.fileName);}

    public FileTreeItem(String path, boolean isDirectory, String fileName){
        super(fileName);
        fullPath = path;
        this.isDirectory = isDirectory;
        this.fileName = fileName;
    }


}
