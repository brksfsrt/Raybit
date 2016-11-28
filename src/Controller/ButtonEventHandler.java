package Controller;

import javafx.event.Event;
import javafx.event.EventHandler;
import org.apache.commons.net.ftp.FTP;

import java.io.IOException;

/**
 * Created by brkfsrt on 28/11/16.
 */
public class ButtonEventHandler{

    public void loginRaybit(String username, String password) {
        String server = "10.0.0.1";
        int port = 21;

        FTP client = new FTP();
        try {
            client.connect(server,port);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
