package ru.tyunikovag.schedule.controller;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.MenuItem;

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
        JOptionPane.showMessageDialog(null, "В разработке");
    }
}
