package view;

import Controller.ButtonEventHandler;
import javafx.beans.NamedArg;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

import Exception.*;

/**
 * Created by brkfsrt on 28/11/16.
 */
public class LoginScreen extends Scene{
    public LoginScreen(@NamedArg("root") Parent root, @NamedArg("width") double width, @NamedArg("height") double height) {
        super(root, width, height);
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
                } catch (ConnectionException e) {
                    e.printStackTrace();
                } catch (AuthorizationException e) {
                    e.printStackTrace();
                } catch (SessionAlreadyExistException e) {
                    e.printStackTrace();
                }
            }
        });

        HBox hbox = new HBox();
        hbox.setAlignment(Pos.CENTER_RIGHT);
        hbox.getChildren().add(loginButton);
        grid.add(hbox,0,3);
        this.setRoot(grid);

    }

    public void changeRoot(Parent root){
        this.setRoot(root);
    }
}
