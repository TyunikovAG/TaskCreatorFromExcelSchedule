package ru.tyunikovag.schedule.controller;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;

import javax.swing.*;
import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable {

    public MenuItem menuClose;
    public MenuItem menuSetting;
    public MenuItem menuAbout;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }


    public void closeAction(ActionEvent event) {
        Platform.exit();
    }

    public void aboutAction(ActionEvent event) {
        JOptionPane.showMessageDialog(null, "Пробная версия, возможны баги ¯ \\ _ (ツ) _ / ¯");
    }

    public void settingAction(ActionEvent event) {
        final Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
//        dialog.initOwner(primaryStage);
        VBox dialogVbox = new VBox(20);
        dialogVbox.getChildren().add(new Text("This is a Dialog"));
        Scene dialogScene = new Scene(dialogVbox, 300, 200);
        dialog.setScene(dialogScene);
        dialog.show();
    }
}
