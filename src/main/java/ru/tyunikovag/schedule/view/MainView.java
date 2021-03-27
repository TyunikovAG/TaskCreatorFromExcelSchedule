package ru.tyunikovag.schedule.view;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;

import javax.swing.*;
import java.net.URL;
import java.util.ResourceBundle;

public class MainView implements Initializable {

    public MenuItem menuClose;
    public MenuItem menuSetting;
    public MenuItem menuAbout;
    @FXML
    private BorderPane taskWindow;
    @FXML
    private TaskView taskWindowController;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println(111);
    }


    public void closeAction(ActionEvent event) {
        Platform.exit();
    }

    public void aboutAction(ActionEvent event) {
        JOptionPane.showMessageDialog(null, "Пробная версия, возможны баги ¯ \\ _ (ツ) _ / ¯");
    }

    public void settingAction(ActionEvent event) {
        JOptionPane.showMessageDialog(null, "В разработке");
    }

    public TaskView getTaskView() {
        return taskWindowController;
    }
}
