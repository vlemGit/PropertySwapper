package com.vlemgit;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;


public class App extends Application {

    @Override
    public void start(Stage mainStage) {
        try {
            FXMLLoader loader = new FXMLLoader(App.class.getResource("/view/main.fxml"));
            BorderPane root = loader.load();

            Scene scene = new Scene(root, 1200, 800);
            mainStage.setScene(scene);
            mainStage.setTitle("Property Editor");
            mainStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch();
    }

}