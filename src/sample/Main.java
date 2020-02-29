package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.awt.*;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("sample.fxml"));
        Parent root =loader.load();
        Controller controller = loader.getController();
        controller.initStage(primaryStage);


        Scene parentRoot = new Scene(root);
        primaryStage.setTitle("Similarity Checker");
        primaryStage.setScene(parentRoot);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
