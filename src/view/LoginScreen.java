package view;

import javafx.beans.NamedArg;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;

/**
 * Created by brkfsrt on 28/11/16.
 */
public class LoginScreen extends Scene{
    public LoginScreen(@NamedArg("root") Parent root, @NamedArg("width") double width, @NamedArg("height") double height) {
        super(root, width, height);

    }

    public void changeRoot(Parent root){
        this.setRoot(root);
    }
}
