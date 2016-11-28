import Controller.ButtonEventHandler;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import view.LoginScreen;
import javafx.scene.Parent;

import java.io.IOException;

/**
 * Created by brkfsrt on 28/11/16.
 */
public class ScreenFactory {
    public LoginScreen makeLoginScreen(){
        return new LoginScreen(null,600,400);
    }
    //public BrowseFileScreen makeBrowseFileScreen(){
    //
   //}
}
