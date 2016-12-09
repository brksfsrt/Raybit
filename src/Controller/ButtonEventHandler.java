package Controller;

import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import Exception.*;
import org.apache.commons.net.ftp.FTPClient;
import View.ScreenFactory;


/**
 * Created by brkfsrt on 28/11/16.
 */
public class ButtonEventHandler{

    public void loginRaybit(String username, String password,Event event) throws ConnectionException, AuthorizationException, SessionAlreadyExistException {
        String server = "10.0.0.1";
        int port = 21;

        FTPClient client = new FTPClient();
        client.setControlEncoding("UTF-8");


        Session session = Session.getInstance();
        try {
            client.connect(server,port);
            if (!client.login(username,password)) {
                client.disconnect();
                throw new AuthorizationException("Wrong username or password");
            }
            else {
                if (session.isSessionExist()) {
                    throw new SessionAlreadyExistException("Session already exist");
                }
                else {
                    session.start();
                    session.setUserName(username);
                    session.setPath("/media/usb0");
                    session.setClient(client);
                }
            }
            System.out.println("Everything is fine, lets party.");
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/BrowseFileScreen.fxml"));

            Parent root = loader.load(getClass().getResource("/View/BrowseFileScreen.fxml"));
            Scene scene = new Scene(root,800,600);
            Node node = (Node ) event.getSource();
            Stage stage = (Stage) node.getScene().getWindow();
            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            throw new ConnectionException("Cannot connect to server");
        }
    }
}
