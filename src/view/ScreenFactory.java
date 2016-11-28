package view;

import Controller.ButtonEventHandler;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import view.LoginScreen;
import javafx.scene.Parent;

import java.io.IOException;
import Exception.*;
/**
 * Created by brkfsrt on 28/11/16.
 */
public class ScreenFactory {
    public LoginScreen makeLoginScreen(){
        Parent root = constructLoginScreen();
        return new LoginScreen(root,600,400);
    }
    //public BrowseFileScreen makeBrowseFileScreen(){
    //
   //}
    public GridPane constructLoginScreen(){
        GridPane grid = new GridPane();

        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        TextField userTextField = new TextField();
        userTextField.setPromptText("Username");
        grid.add(userTextField,0,1);

        PasswordField passField = new PasswordField();
        passField.setPromptText("Password");
        grid.add(passField,0,2);

        Button loginButton = new Button("Login");
        loginButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    new ButtonEventHandler().loginRaybit(userTextField.getText(),passField.getText());
                    System.out.println(userTextField.getText().trim()+ " "+ passField.getText().trim());
                } catch (ConnectionException e) {
                    e.printStackTrace();

                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("Connection cannot established");
                    alert.show();
                } catch (AuthorizationException e) {
                    e.printStackTrace();

                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setContentText("Wrong username or password");
                    System.out.println(userTextField.getText()+ " "+ passField.getText());
                    alert.show();
                } catch (SessionAlreadyExistException e) {
                    e.printStackTrace();

                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setContentText("Another session exist");
                    alert.show();
                }
            }
        });

        HBox hbox = new HBox();
        hbox.setAlignment(Pos.CENTER_RIGHT);
        hbox.getChildren().add(loginButton);
        grid.add(hbox,0,3);
        return grid;
    }
}
