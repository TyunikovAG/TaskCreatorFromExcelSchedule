package ru.tyunikovag.schedule;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ru.tyunikovag.schedule.controller.TaskController;
import ru.tyunikovag.schedule.view.MainView;
import ru.tyunikovag.schedule.view.TaskView;

public class Main extends Application {

    public static void main(String[] args) {
        Application.launch();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

//        FXMLLoader loader = new FXMLLoader();
//        loader.setLocation(Main.class.getResource("task.fxml"));
        //Parent mainLayout = loader.load();

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("MainWindow.fxml"));
//        Parent fxmlRoot = fxmlLoader.load(getClass().getClassLoader().getResource("MainWindow.fxml"));
        Parent fxmlRoot = fxmlLoader.load();
        MainView mainView = fxmlLoader.getController();
        TaskView view = mainView.getTaskView();
        Scene scene = new Scene(fxmlRoot);
        primaryStage.setScene(scene);
        primaryStage.setTitle("TyunikovAG inc.");

        TaskController taskController = new TaskController(view);
        view.setController(taskController);
        view.addTeam(null);

        primaryStage.show();
    }
}
