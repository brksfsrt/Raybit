package view;

import Controller.ButtonEventHandler;
import javafx.beans.NamedArg;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
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
        super(root,600,400);
    }

    public void changeRoot(Parent root){
        this.setRoot(root);
    }
}
