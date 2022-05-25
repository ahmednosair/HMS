package HMS;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class Main extends Application {


    @Override
    public void start(Stage primaryStage) throws Exception {
        System.setProperty("prism.text", "t2k");
        System.setProperty("prism.lcdtext", "false");
        Parent root = FXMLLoader.load(getClass().getResource("View/logIn.fxml"));
        primaryStage.setTitle("نظام ادارة المستشفي");
        primaryStage.setScene(new Scene(root));
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

}
