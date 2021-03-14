package ru.tyunikovag.schedule.view;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import ru.tyunikovag.schedule.controller.TaskController;
import ru.tyunikovag.schedule.providers.DnDProvider;
import ru.tyunikovag.schedule.ExelCreator;
import ru.tyunikovag.schedule.providers.PropertyProvider;
import ru.tyunikovag.schedule.controller.SettingController;
import ru.tyunikovag.schedule.model.Shift;
import ru.tyunikovag.schedule.model.Task;
import ru.tyunikovag.schedule.model.Team;
import ru.tyunikovag.schedule.model.Worker;
import ru.tyunikovag.schedule.util.Util;

import javax.swing.*;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class TaskView implements Initializable {

    private TaskController controller;
    private SettingController settingController;
    private PropertyProvider propertyProvider = PropertyProvider.getInstance();

    @FXML
    private VBox leftBox;
    @FXML
    public VBox mainLeftBox;
    @FXML
    private VBox centerBox;
    @FXML
    private Button btnAddTeam;
    @FXML
    private Button btnSendToBlank;
    @FXML
    private VBox rightBox;
    @FXML
    private DatePicker datePicker;
    @FXML
    private ComboBox<String> cmbShift;
    @FXML
    private ComboBox<String> cmbTaskAuthor;
    @FXML
    private Button buttonMain;
    @FXML
    private Label lblFileScheduleName;
    @FXML
    private Label lblChangeScheduleFile;
    @FXML
    private Label lblChangeBlankFile;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("init TaskView");

        cmbShift.getItems().addAll("Ночь", "Утро", "Вечер");
        cmbTaskAuthor.getItems().addAll(controller.getAuthors());

        datePicker.setValue(LocalDate.now());
//        datePicker.setValue(NOW_LOCAL_DATE());
        lblFileScheduleName.setText("Файл графика: " + controller.getScheduleFileName());
        leftBox.setOnDragOver(DnDProvider::onLeftBoxDragOver);
        leftBox.setOnDragDropped(DnDProvider::onLeftBoxDragDropped);
        DnDProvider.setTaskView(this);
        setImage();
        onBtnAddTeam(new ActionEvent());
    }

    private static LocalDate NOW_LOCAL_DATE() {
        String date = new SimpleDateFormat("dd-MM-yyyy").format(Calendar.getInstance().getTime());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        return LocalDate.parse(date, formatter);
    }

    private void setImage() {
        Image image = new Image("members.jpg");
        BackgroundSize backgroundSize = new BackgroundSize(300, 300, true, true, true, false);
        BackgroundPosition bottom = new BackgroundPosition(Side.LEFT, 0, false,
                Side.BOTTOM, 0, false);
        BackgroundImage backgroundImage = new BackgroundImage(image, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, bottom, backgroundSize);
        BackgroundFill fill = new BackgroundFill(Color.LIGHTGRAY, null, null);

        Background background0 = new Background(new BackgroundFill[]{fill}, new BackgroundImage[]{backgroundImage});
        mainLeftBox.setBackground(background0);
    }

    private void fillLeftBoxByWorkers() {

        deleteAllFromLeftBox();
        Shift shift = getSelectedShift();
        LocalDate localDate = datePicker.getValue();
        List<Worker> workers = controller.getEmployeesOnShift(localDate.getDayOfMonth(), shift);

        for (Worker worker : workers
        ) {
            addWorkerLabelToLeftBox(worker);
        }
    }

    public void addWorkerLabelToLeftBox(Worker worker) {
        Label label = new Label(worker.getFio());
        label.setOnDragDetected(DnDProvider::labelDragDetected);
        leftBox.getChildren().add(label);
    }

    private Shift getSelectedShift() {
        String str = cmbShift.getValue();
        switch (str) {
            case "Утро": {
                return Shift.MORNING;
            }
            case "Вечер": {
                return Shift.EVENING;
            }
            case "Ночь": {
                return Shift.NIGHT;
            }
        }
        return null;
    }

    private void deleteAllFromLeftBox() {
        List<Node> list = leftBox.getChildren();
        while (list.size() > 1) {
            list.remove(1);
        }
    }

    @FXML
    void OnMainButtonAction(ActionEvent event) {
        Label label = (Label) leftBox.getChildren().get(0);
        label.setText(datePicker.getValue().toString());
        if (cmbShift.getValue() == null || cmbShift.getValue().equals("Укажите смену")) {
            JOptionPane.showMessageDialog(null,
                    "Не указана смена", "InfoBox", JOptionPane.INFORMATION_MESSAGE);

        } else {
            clearCentralBox();
            fillLeftBoxByWorkers();
            onBtnAddTeam(new ActionEvent());
        }
    }

    private void clearCentralBox() {
        List<Node> centerList = centerBox.getChildren();
        int count = centerList.size() - 2;
        for (int i = 0; i < count; i++) {
            centerList.remove(centerList.size() - 3);
        }
    }

    @FXML
    void onBtnAddTeam(ActionEvent event) {
        int teamNumber = controller.addTeam();
        VBox teamBox = new VBox();
        teamBox.setId("teamBox#" + teamNumber);
        teamBox.setSpacing(10);
        Label lblTeamNumber = new Label("Звено №" + teamNumber);
        lblTeamNumber.setFont(Font.font(24));
        lblTeamNumber.setPadding(new Insets(0, 0, 5, 15));
        FlowPane teamMembersFlowPane = new FlowPane();
        teamMembersFlowPane.setId("teamMembers#" + teamNumber);

        TextArea teamTask = new TextArea();
        teamTask.setId("teamTask#" + teamNumber);
        teamTask.setFont(new Font(16));
        teamTask.setOnDragOver(DnDProvider::onTeamTaskAreaDragOver);
        teamTask.setOnDragDropped(DnDProvider::onTeamTaskAreaDragDropped);

        teamBox.getChildren().addAll(lblTeamNumber, teamMembersFlowPane, teamTask);

        centerBox.getChildren().add(teamNumber - 1, teamBox);
    }

    @FXML
    void onBtnDeleteTeam(ActionEvent event) {
        List<Node> ceneterList = centerBox.getChildren();
        if (ceneterList.size() > 3) {
            VBox teamBox = (VBox) ceneterList.get(ceneterList.size() - 3);
            List<Node> workers = ((FlowPane) teamBox.getChildren().get(1)).getChildren();
            for (Node worker : workers) {
                Label additionalLabel = new Label(((Label) worker).getText());
                additionalLabel.setOnDragDetected(DnDProvider::labelDragDetected);
                leftBox.getChildren().add(additionalLabel);
            }
            ceneterList.remove(ceneterList.size() - 3);
            controller.deleteOneTeam();
        }
    }

    @FXML
    void onButtonSendToBlankAction(ActionEvent event) {
        // TODO: 14.03.2021 replace this because task is in field
        boolean isFieldsEmpty = false;
        for (Node node : centerBox.getChildren()) {
            if (node.getClass().equals(VBox.class)) {
                boolean isBoxEmpty = isTeamBoxEmpty((VBox) node);
                isFieldsEmpty |= isBoxEmpty;
            }
        }


        if (!isFieldsEmpty) {
            controller.sendTaskToBlank();
        }
    }

    @FXML
    void onScheduleFileChange(MouseEvent event) {
        if (event.getClickCount() == 2) {
            propertyProvider.setFileForProperty(PropertyProvider.PropertyName.SCHEDULE_FILE_NAME);
            propertyProvider.saveProperties();
            controller.scheduleFileChanged();
            lblFileScheduleName.setText(controller.getScheduleFileName());
        }
    }

    private boolean isTeamBoxEmpty(VBox vBox) {
        int indexOfTeamName = 0;
        int indexOfMembersContainer = 1;
        int indexOfTaskTextArea = 2;
        Label teamName = (Label) vBox.getChildren().get(indexOfTeamName);
        FlowPane flowPane = (FlowPane) vBox.getChildren().get(indexOfMembersContainer);
        TextArea textArea = (TextArea) vBox.getChildren().get(indexOfTaskTextArea);

        if (flowPane.getChildren().size() == 0) {
            String message = "Нет ни одного работника в " + teamName.getText();
            JOptionPane.showMessageDialog(null,
                    message, "InfoBox", JOptionPane.INFORMATION_MESSAGE);
            return true;
        }
        if (textArea.getText().trim().isEmpty()) {
            String message = String.format("Задание для %s пустое", teamName.getText());
            JOptionPane.showMessageDialog(null,
                    message, "InfoBox", JOptionPane.INFORMATION_MESSAGE);
            return true;
        }
        return false;
    }

    @FXML
    public void onMenuSettingAction(ActionEvent event) {
        settingController.action();
    }

    public void addAnotherWorker(ActionEvent actionEvent) {

        JComboBox jcb = new JComboBox(controller.getAllWorkersNames().toArray());
        jcb.setEditable(true);
        int dialogResult = JOptionPane.showConfirmDialog(null,
                jcb,
                "Ф.И.О. и должность члена бригады",
                JOptionPane.OK_CANCEL_OPTION);
        if (dialogResult == 0) {
            String line = (String) jcb.getSelectedItem();

            if (line != null && !line.isEmpty()) {
                String fio = Util.getFIO(line);
                if (fio.equals(Util.WRONG_FIO)) {

                    int confirm = JOptionPane.showConfirmDialog(null, Util.WRONG_FIO, "Wrong input", JOptionPane.OK_CANCEL_OPTION);
                    switch (confirm) {
                        case 0: {
                            addAnotherWorker(null);
                        }
                        case 2: {
                            return;
                        }
                    }
                    return;
                }
                String profession = (line.length() > fio.length()) ? line.substring(fio.length() + 1) : "";
                controller.addAnotherWorker(fio, profession);
                Worker worker = new Worker(fio, profession);
                addWorkerLabelToLeftBox(worker);
            }
        }
    }

    public void addMemberToTeam(VBox teamLocalTaskBox, String fio) {

        String id = teamLocalTaskBox.getId();
        id = id.substring(id.indexOf('#') + 1);
        int teamNumber = (Integer.parseInt(id));
        Worker worker = workers.stream().filter(wrkr -> wrkr.getFio().equals(fio)).findFirst().get();
        task.getTeams().get(teamNumber).getMembers().add(worker);
    }

    public void removeMemberFromTeam(Pane teamLocalTaskBox, String fio) {

        if (teamLocalTaskBox.getId().startsWith("teamBox#")) {
            String id = teamLocalTaskBox.getId();
            id = id.substring(id.indexOf('#') + 1);
            int teamNumber = (Integer.parseInt(id));
            Worker worker = workers.stream().filter(wrkr -> wrkr.getFio().equals(fio)).findFirst().get();
            task.getTeams().get(teamNumber).getMembers().remove(worker);
        }
    }

    public void setController(TaskController controller) {
        this.controller = controller;
    }
}
