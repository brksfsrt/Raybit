package Controller;

import javafx.event.Event;
import javafx.event.EventHandler;
import org.apache.commons.net.ftp.FTP;

import java.io.IOException;
import Exception.*;
import org.apache.commons.net.ftp.FTPClient;

/**
 * Created by brkfsrt on 28/11/16.
 */
public class ButtonEventHandler{

    public void loginRaybit(String username, String password) throws ConnectionException, AuthorizationException, SessionAlreadyExistException {
        String server = "10.0.0.1";
        int port = 21;

        FTPClient client = new FTPClient();
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
            System.out.println("Everything is fine");
        } catch (IOException e) {
            throw new ConnectionException("Cannot connect to server");
        }
    }
}
