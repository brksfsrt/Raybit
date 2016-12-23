package Controller;

import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.*;
import java.nio.file.Files;

import Exception.*;
import org.apache.commons.net.ftp.FTPClient;
import View.ScreenFactory;


/**
 * Created by brkfsrt on 28/11/16.
 */
public class LoginEventController {
    private boolean isRoot = true;

    //TODO:authentication process will be changed, this is just for the demo
    public void loginRaybit(String username, String password,Event event) throws ConnectionException, AuthorizationException, SessionAlreadyExistException {
        String server = "10.0.0.1";
        int port = 21;

        FTPClient client = new FTPClient();
        client.setControlEncoding("UTF-8");


        Session session = Session.getInstance();
        try {
            client.connect(server,port);
            if (!login(client,username,password)) {
                client.disconnect();
                throw new AuthorizationException("Wrong username or password");
            }
            else {
                if (session.isSessionExist()) {
                    throw new SessionAlreadyExistException("Session already exist");
                }
                else {
                    System.out.println(client.printWorkingDirectory());
                    session.setClient(client);
                    session.start();
                    session.setUserName(username);
                    session.setPath("/media/usb0");
                    session.setIsRoot(isRoot);
                }
            }

            System.out.println("Everything is fine, lets party.");
            FXMLLoader loader = null;
            Parent root= null;

            if(isRoot) {
                loader = new FXMLLoader(getClass().getResource("/View/BrowseFileScreen.fxml"));
                root = loader.load(getClass().getResource("/View/BrowseFileScreen.fxml"));
            }
            else{
                loader = new FXMLLoader(getClass().getResource("/View/GuestFileScreen.fxml"));
                root = loader.load(getClass().getResource("/View/GuestFileScreen.fxml"));
            }
            Scene scene = new Scene(root,800,600);
            Node node = (Node ) event.getSource();
            Stage stage = (Stage) node.getScene().getWindow();
            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            throw new ConnectionException("Cannot connect to server");
        }
    }

    //TODO:Bu kod gecici
    public boolean login(FTPClient client, String handle,String pass) throws IOException {
        if(!client.login("pi","raspberry")){
            client.disconnect();
            return false;
        }
        else{
            File downloadedFile = new File(System.getProperty("user.dir")+"/asdfsdf" );
            String remoteFile = "/media/auth-rules.txt";
            OutputStream os = new BufferedOutputStream(new FileOutputStream(downloadedFile));
            client.retrieveFile(remoteFile,os);
            os.close();

            BufferedReader br = new BufferedReader(new FileReader(downloadedFile));
            String line = br.readLine();

            while(line != null){
                String a = line.substring(0,line.indexOf(":"));
                String b = line.substring(line.indexOf(":")+1);
                if(a.equals(handle) && b.equals(pass)){
                    br.close();
                    Files.deleteIfExists(downloadedFile.toPath());
                    if(a.equals("guest"))
                        isRoot = false;
                    return true;
                }
                line = br.readLine();
            }
            return false;
        }
    }}
