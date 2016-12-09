package View;

import javafx.beans.NamedArg;
import javafx.scene.Parent;
import javafx.scene.Scene;

/**
 * Created by brkfsrt on 28/11/16.
 */
public class LoginScreen extends Scene{
    public LoginScreen(@NamedArg("root") Parent root, @NamedArg("width") double width, @NamedArg("height") double height) {
        super(root,600,400);
    }

}
