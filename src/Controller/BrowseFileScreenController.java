package Controller;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TreeView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;

import java.io.*;

/**
 * Created by brkfsrt on 29/11/16.
 */
public class BrowseFileScreenController {
    @FXML private TreeView treeView;
    @FXML private Button uploadButton;
    private Session session = Session.getInstance();
    FTPClient client = session.getClient();

    public void initialize(){
        FileTreeItem rootItem = new FileTreeItem("/media/usb0",true,"Storage Device");
        treeView.setRoot(rootItem);
        uploadButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                uploadFile(event);
            }
        });
        treeView.setOnMouseClicked(new EventHandler<MouseEvent>() {

            //handles the tree view item mouse on clicked
            @Override
            public void handle(MouseEvent event) {

                if(event.getButton().equals(MouseButton.PRIMARY)){

                    FileTreeItem item = (FileTreeItem) treeView.getSelectionModel().getSelectedItem();

                    if(item.isDirectory() && !item.isExpanded()) {
                        item.setExpanded(true);
                        FTPFile[] files = expandDirectory(item.getFullPath());

                        for(FTPFile file:files){
                            System.out.println(file.toString());
                            FileTreeItem newItem = new FileTreeItem(item.getFullPath()+"/"+file.getName(),file.isDirectory(),file.getName());
                            item.getChildren().add(newItem);
                        }

                    }
                    else if(item.isDirectory() && item.isExpanded()){
                        item.setExpanded(false);
                    }
                    else if(event.getClickCount() == 2 && !item.isDirectory()){
                        (new Thread(new DownloadTask(client,item.getFileName(),item.getFullPath()))).start();
                    }

                }
            }
        });

    }

    public FTPFile [] expandDirectory(String path){
        FTPFile [] files = null;
        try {
            System.out.println(path);
            files = client.listFiles(path);

        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            return files;
        }

    }

    protected void uploadFile(ActionEvent event){
        FileChooser fChooser = new FileChooser();
        File selectedFile = fChooser.showOpenDialog((Stage) (uploadButton.getScene().getWindow()));

        if(selectedFile == null && selectedFile.isDirectory()){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("");
            alert.setHeaderText("No file is selected");
            alert.setContentText("");
            alert.show();
        }
        else{
            (new Thread(new UploadTask(client,selectedFile))).start();
        }
    }

    private static class DownloadTask implements Runnable{
        private String remotePath;
        private String currentPath;
        private FTPClient client;
        private String fileName;

        public DownloadTask(FTPClient client,String fileName, String path) {
            remotePath = path;
            currentPath = System.getProperty("user.dir");
            this.client = client;
            this.fileName = fileName;
        }

        public void downloadFile() throws IOException,FileNotFoundException {
            File downloadedFile = new File(currentPath+"/"+ fileName);

            OutputStream os = new BufferedOutputStream(new FileOutputStream(downloadedFile));
            InputStream is = client.retrieveFileStream( remotePath);

            byte[]  buffer  = new byte[4096];
            int bytesRead = -1;
            while((bytesRead = is.read(buffer)) != -1){
                os.write(buffer,0,bytesRead);
            }

            boolean success = client.completePendingCommand();
            if(success){
                System.out.println("Success");
            }
            os.close();
            is.close();
        }

        @Override
        public void run() {
            try {
                downloadFile();
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("Error Occured");
            }
        }
    }

    //TODO: select where to upload
     private static class UploadTask implements Runnable{
        private String fileName;
        private String filePath;
        private String uploadPath = "/media/usb0";
        private FTPClient client;
        private File localFile;

        public UploadTask(FTPClient client,File file){
            this.client = client;
            this.filePath = file.getAbsolutePath();
            this.fileName = file.getName();
            this.localFile = file;
        }


        public void uploadFile(){
            String remoteFile = uploadPath+"/"+fileName;
            try {
                InputStream is = new FileInputStream(localFile);
                System.out.println(remoteFile);
                OutputStream os = client.storeFileStream(remoteFile);
                byte[] buffer = new byte[4096];
                int read = 0;

                while((read = is.read(buffer)) != -1){
                    os.write(buffer,0,read);
                    System.out.println("asdasdsasda");
                }
                os.close();
                is.close();
                boolean completed = client.completePendingCommand();
                if (completed) {
                    System.out.println("The second file is uploaded successfully.");
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        @Override
        public void run() {
            uploadFile();
        }
    }

}
