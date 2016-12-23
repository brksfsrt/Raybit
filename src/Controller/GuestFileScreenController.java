package Controller;

import com.sun.tools.javac.util.Name;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import model.TableDataItem;
import org.apache.commons.net.ftp.FTPClient;

import java.io.*;

/**
 * Created by brkfsrt on 22/12/16.
 */
public class GuestFileScreenController {
    private final String sharedFiletxt = "shared-files.txt";
    private Session session = Session.getInstance();
    private FTPClient client = session.getClient();
    private ObservableList<TableDataItem> data = FXCollections.observableArrayList();

    @FXML private TableView tableView;
    @FXML private Button logoutButton;
    @FXML private TableColumn nameColumn,sizeColumn,pathColumn;

    public void initialize(){
        nameColumn.setCellValueFactory(new PropertyValueFactory<TableDataItem,String>("fileName"));
        sizeColumn.setCellValueFactory(new PropertyValueFactory<TableDataItem,String>("filePath"));
        pathColumn.setCellValueFactory(new PropertyValueFactory<TableDataItem,String>("fileSize"));

        tableView.setItems(data);
    }
    public void fillValue(ObservableList<TableDataItem> data){
        File downloadedFile = new File(System.getProperty("user.dir")+"/asdfsdf" );
        String remoteFile = "/media/auth-rules.txt";
        OutputStream os = null;
        try {
            os = new BufferedOutputStream(new FileOutputStream(downloadedFile));
            client.retrieveFile(remoteFile,os);
            os.close();

            BufferedReader br = new BufferedReader(new FileReader(downloadedFile));
            String line = br.readLine();
            String[] splitStr = line.split("\\s+");
            while(line!= null) {
                data.add(new TableDataItem(splitStr[0], splitStr[1], splitStr[2]));
                line = br.readLine();
            }
            br.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
