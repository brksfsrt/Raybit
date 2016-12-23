package Controller;

import com.sun.tools.javac.util.Name;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.FileTreeItem;
import model.TableDataItem;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;

import java.io.*;

/**
 * Created by brkfsrt on 29/11/16.
 */
public class BrowseFileScreenController {
    @FXML private TreeView treeView;
    @FXML private Button uploadButton;
    @FXML private Button shareFileButton;
    @FXML private TableView downloadedFileTable;
    @FXML private TableView uploadedFileTable;
    @FXML private TableView sharedFileTable;

    @FXML private TableColumn sfNameColumn;
    @FXML private TableColumn sfSizeColumn;


    private Session session = Session.getInstance();
    FTPClient client = session.getClient();

    private ObservableList<TableDataItem> sharedFileData = FXCollections.observableArrayList();

    public void initialize(){
        FileTreeItem rootItem = new FileTreeItem("/media/usb0",true,"Storage Device");
        treeView.setRoot(rootItem);

        uploadButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                uploadFile(event);
            }
        });
        shareFileButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                shareFile(event, (FileTreeItem) treeView.getSelectionModel().getSelectedItem());
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
                        item.getChildren().clear();
                        item.setExpanded(false);

                    }
                    else if(event.getClickCount() == 2 && !item.isDirectory()){
                        (new Thread(new DownloadTask(client,item.getFileName(),item.getFullPath()))).start();
                    }

                }
            }
        });

        sfNameColumn.setCellValueFactory(new PropertyValueFactory<TableDataItem,String>("fileName"));
        sfSizeColumn.setCellValueFactory(new PropertyValueFactory<TableDataItem,String>("fileSize"));
        sharedFileTable.setItems(sharedFileData);

        
    }

    protected  FTPFile [] expandDirectory(String path){
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

    protected void shareFile(ActionEvent event,FileTreeItem item){

        File downloadedFile = new File(System.getProperty("user.dir")+"/temp-shareFile" );
        String remoteFile = "/media/shared-files.txt";
        OutputStream os = null;
        try {
            os = new BufferedOutputStream(new FileOutputStream(downloadedFile));
            client.retrieveFile(remoteFile,os);
            os.close();

            BufferedReader br = new BufferedReader(new FileReader(downloadedFile));
            String line = br.readLine();
            String[] splitStr = line.split("\\s+");
            while(line!= null) {

                sharedFileData.add(new TableDataItem(splitStr[0], splitStr[1], splitStr[2]));
                line = br.readLine();
            }
            br.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void fillSharedTable(){
        File downloadedFile = new File(System.getProperty("user.dir")+ "/temp-shareFile");
        String remoteFile = "/media/shared-files.txt";
        OutputStream os = null;

        try {
            os = new BufferedOutputStream(new FileOutputStream(downloadedFile));
            client.retrieveFile(remoteFile,os);
            os.close();

            BufferedReader br = new BufferedReader(new FileReader(downloadedFile));
            String line = br.readLine();
            String[] splitStr = line.split("\\s+");
            while(line!= null) {

                sharedFileData.add(new TableDataItem(splitStr[0], splitStr[1], splitStr[2]));
                line = br.readLine();
            }
            br.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
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
            File downloadedFile = new File(currentPath+"/downloads/"+ fileName);

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
        private String uploadPath;
        private FTPClient client;
        private File localFile;

        public UploadTask(FTPClient client,File file){
            this.client = client;
            this.filePath = file.getAbsolutePath();
            this.fileName = file.getName();
            this.localFile = file;
        }


        public void uploadFile(){
            String remoteFile = fileName;
            try {
                System.out.println(fileName);
                InputStream is = new FileInputStream(localFile);
                boolean done = client.storeFile(remoteFile,is);
                is.close();
                if(done){
                    System.out.println("Boo yeah look at it go");
                }
                else{
                    System.out.println("Hata");
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
