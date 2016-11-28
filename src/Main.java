import javafx.application.Application;
import javafx.stage.Stage;
import view.ScreenFactory;

/**
 * Created by brkfsrt on 28/11/16.
 */
public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Raybit");
        primaryStage.setScene(new ScreenFactory().makeLoginScreen());
        primaryStage.show();
    }
    public static void main(String args[]){
        launch(args);
    }
}
