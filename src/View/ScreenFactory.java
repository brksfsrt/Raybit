package View;

import Controller.ButtonEventHandler;
import Controller.FileTreeItem;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.Parent;

import Exception.*;
/**
 * Created by brkfsrt on 28/11/16.
 */
public class ScreenFactory {
    public LoginScreen makeLoginScreen(){
        Parent root = constructLoginScreen();
        return new LoginScreen(root,600,400);
    }
    public BrowseFileScreen makeBrowseFileScreen(){
        Parent root = constructBrowseScreen();
        return new BrowseFileScreen(root,600,400);
    }
    private GridPane constructLoginScreen(){
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
                    new ButtonEventHandler().loginRaybit(userTextField.getText(),passField.getText(),event);
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

    public VBox constructBrowseScreen(){
        VBox treeBox = new VBox();
        treeBox.setPadding(new Insets(10,10,10,10));
        treeBox.setSpacing(10);
        TreeView<String > treeView;
        FileTreeItem rootNode = new FileTreeItem("/media/usb0",true,"directory");
        treeView = new TreeView<>(rootNode);
        treeBox.setVgrow(treeView, Priority.ALWAYS);
        return treeBox;

    }
}
