package ru.tyunikovag.schedule;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    public static void main(String[] args) {
        Application.launch();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        Parent fxmlRoot = new FXMLLoader().load(getClass().getClassLoader().getResource("MainWindow.fxml"));

        Scene scene = new Scene(fxmlRoot);
        primaryStage.setScene(scene);
        primaryStage.setTitle("TyunikovAG inc.");
        primaryStage.show();
    }
}
