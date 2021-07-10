package ru.tyunikovag.schedule.view;

import com.sun.javafx.collections.ObservableListWrapper;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.DragEvent;
import javafx.scene.input.InputEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import ru.tyunikovag.schedule.controller.TaskController;
import ru.tyunikovag.schedule.providers.DnDProvider;
import ru.tyunikovag.schedule.providers.PropertyProvider;
import ru.tyunikovag.schedule.controller.SettingController;
import ru.tyunikovag.schedule.model.Shift;
import ru.tyunikovag.schedule.model.Worker;
import ru.tyunikovag.schedule.util.Util;

import javax.swing.*;
import java.net.URL;
import java.time.LocalDate;
import java.util.*;
import java.util.List;

public class TaskView implements Initializable {

    private TaskController controller;
    private SettingController settingController;
    private PropertyProvider propertyProvider = PropertyProvider.getInstance();

    @FXML
    private Label leftBoxDate;
    @FXML
    private VBox teamListBox;
    @FXML
    private VBox leftBox;
    @FXML
    public VBox mainLeftBox;
    @FXML
    private VBox centerBox;
    @FXML
    private Button btnAddTeam;
    @FXML
    private Button btnDeleteTeam;
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
    private final int indexOfTaskTextArea = 2;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        cmbShift.getItems().addAll("Ночь", "Утро", "Вечер");

        datePicker.setValue(LocalDate.now());
        leftBox.setOnDragOver(DnDProvider::onLeftBoxDragOver);
        leftBox.setOnDragDropped(DnDProvider::onLeftBoxDragDropped);
        DnDProvider.setView(this);
        setImage();
    }

    @FXML
    void OnMainButtonAction(ActionEvent event) {

        leftBoxDate.setText(datePicker.getValue().toString());

        if (cmbShift.getValue() == null || cmbShift.getValue().equals("Укажите смену")) {
            JOptionPane.showMessageDialog(null,
                    "Не указана смена", "InfoBox", JOptionPane.INFORMATION_MESSAGE);

        } else {
            clearTeamListBox();
            leftBox.getChildren().clear();
            controller.resetTask();
            fillLeftBoxByWorkers(controller.getWorkersOnShift(datePicker.getValue().getDayOfMonth(), getSelectedShift()));
            controller.setShift(getSelectedShift());
            controller.setTaskDate(datePicker.getValue());
            addTeam(new ActionEvent());
        }
    }

    public void setLblFileName(String fileName) {
        lblFileScheduleName.setText("Файл графика: " + fileName);
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

    private void fillLeftBoxByWorkers(List<Worker> workersOnShift) {

        for (Worker worker : workersOnShift
        ) {
            addWorkerLabelToLeftBox(worker.getFio());
        }
    }

    public void addWorkerLabelToLeftBox(String fio) {
        for (Node child : leftBox.getChildren()) {
            Label label = (Label) child;
            if (label.getText().equals(fio)) {
                return;
            }
        }
        leftBox.getChildren().add(createFioLabelForLeftBox(fio));

    }

    private Label createFioLabelForLeftBox(String fio) {
        Label label = new Label(fio);
        label.setFont(Font.font(12));
        label.setOnDragDetected(DnDProvider::onLabelDragDetected);
        label.setOnMouseClicked(event -> onLeftBoxOnWorkerLabelClicked(event, label.getText()));
        return label;
    }

    private Label createFioLabelForTeamMembers(String fio) {
        Label label = new Label(fio);
        label.setFont(Font.font(16));
        label.setPadding(new Insets(0, 0, 10, 10));
        label.setOnDragDetected(DnDProvider::onLabelDragDetected);
        label.setOnMouseClicked(event -> onTeamMemberClicked(event, label.getText()));
        return label;
    }


    private void onLeftBoxOnWorkerLabelClicked(MouseEvent event, String fio) {
        if (event.getClickCount() == 2) {
            int teamsCount = teamListBox.getChildren().size();
            controller.addMemberToTeam(teamsCount, fio);
        }
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

    private void deleteWorkersFromLeftBox() {
//        leftBox.getChildren().clear();
        List<Node> list = leftBox.getChildren();
        while (list.size() > 1) {
            list.remove(1);
        }
    }

    private void clearTeamListBox() {
        teamListBox.getChildren().clear();
    }

    @FXML
    public void addTeam(ActionEvent event) {
        int teamNumber = controller.addTeam();
        VBox teamBox = new VBox();
        teamBox.setId("teamBox#" + teamNumber);
        teamBox.setSpacing(10);

        BorderPane topPane = new BorderPane();
        Label lblTeamNumber = new Label("Звено №" + teamNumber);
        lblTeamNumber.setFont(Font.font(24));
        lblTeamNumber.setPadding(new Insets(0, 0, 5, 15));
        topPane.setLeft(lblTeamNumber);
        topPane.setRight(getCmbTaskPatterns());

        FlowPane teamMembersFlowPane = new FlowPane();
        teamMembersFlowPane.setId("teamMembers#" + teamNumber);

        TextArea teamTask = new TextArea();
        teamTask.setId("teamTask#" + teamNumber);
        teamTask.setFont(new Font(16));
        teamTask.setWrapText(true);
        teamTask.setOnDragOver(DnDProvider::onTeamTaskAreaDragOver);
        teamTask.setOnDragDropped(DnDProvider::onTeamTaskAreaDragDropped);
        teamTask.setOnMouseClicked(this::onTaskAreaClicked);

        teamBox.getChildren().addAll(topPane, teamMembersFlowPane, teamTask);

        teamListBox.getChildren().add(teamBox);
    }

    private ComboBox<String> getCmbTaskPatterns() {
        ComboBox<String> patterns = new ComboBox<String>();
        patterns.setItems(new ObservableListWrapper<String>(Arrays.asList(
                "Осмотр ГВУ-3 согласно инструкции. Обслуживание насосных установок согласно инструкции по эксплуатации.\n",
                "Осмотр насосной станции + 0 м. Контроль работы насосов.\n",
                "Осмотр ГВКУ-1 согласно инструкции. Осмотр ЦПП-4.\n",
                "Зачистка ЭТШ + 100 м. юг. Зачистка водосборников водоотлива + 100 м.",
                "Зачистка ЭТШ + 0 м. юг. Зачистка водосборников водоотлива + 0 м.",
                "Работы по заявкам. Устранение выявленных неисправностей в работе оборудования.\n"
        )));
        patterns.setMaxWidth(50);
        patterns.setOnAction(this::selectedTaskPattern);
        return patterns;
    }

    private void selectedTaskPattern(Event event) {
        ComboBox<String> patterns = (ComboBox<String>) event.getTarget();
        VBox taskBox = (VBox) ((ComboBox) event.getTarget()).getParent().getParent();
        TextArea taskArea = (TextArea) taskBox.getChildren().get(2);
        taskArea.setText(taskArea.getText() + patterns.getValue());
    }

    private void onTaskAreaClicked(MouseEvent event) {
        if (event.getClickCount() == 2) {
//            final Stage dialog = new Stage();
//            dialog.initModality(Modality.APPLICATION_MODAL);
//            dialog.initOwner(null);
//            VBox dialogVbox = new VBox(20);
//            dialogVbox.getChildren().add(new Text("This is a Dialog"));
//            Scene dialogScene = new Scene(dialogVbox, 300, 200);
//            dialog.setScene(dialogScene);
//            dialog.show();
        }
    }

    @FXML
    void onBtnDeleteTeam(ActionEvent event) {
        List<Node> teamBoxList = teamListBox.getChildren();
        if (teamBoxList.size() > 1) {
            VBox teamBox = (VBox) teamBoxList.get(teamBoxList.size() - 1);
            List<Node> workers = ((FlowPane) teamBox.getChildren().get(1)).getChildren();
            for (Node worker : workers) {
                addWorkerLabelToLeftBox(((Label) worker).getText());
            }
            teamBoxList.remove(teamBoxList.size() - 1);
            controller.deleteLastTeam();
        }
    }

    @FXML
    void onButtonSendToBlankAction(ActionEvent event) {
        boolean isFieldsEmpty = false;
        for (Node teamBox : teamListBox.getChildren()) {
            isFieldsEmpty |= isTeamBoxEmpty((VBox) teamBox);
            TextArea teamTaskArea = (TextArea) ((VBox) teamBox).getChildren().get(indexOfTaskTextArea);

            controller.setTeamTask(getTeamNumberFromId(teamBox.getId()), teamTaskArea.getText());
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
        }
    }

    private boolean isTeamBoxEmpty(VBox teamBox) {
        int indexOfMembersContainer = 1;
        int teamNumber = getTeamNumberFromId(teamBox.getId());
        FlowPane workersPane = (FlowPane) teamBox.getChildren().get(indexOfMembersContainer);
        TextArea teamTaskArea = (TextArea) teamBox.getChildren().get(indexOfTaskTextArea);

        if (workersPane.getChildren().size() == 0) {
            String message = "Нет ни одного работника в звене №" + teamNumber;
            JOptionPane.showMessageDialog(null,
                    message, "InfoBox", JOptionPane.INFORMATION_MESSAGE);
            return true;
        }
        if (teamTaskArea.getText().trim().isEmpty()) {
            String message = String.format("Задание для звена№%s пустое", teamNumber);
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
            }
        }
    }

    public void draggedMemberToLeft(DragEvent event) {
        String fio = event.getDragboard().getString();
        addWorkerLabelToLeftBox(fio);

        prepareToRemoveMemberFromTeam(event);
    }

    public void draggedMemberToTeam(DragEvent event) {
        String fio = event.getDragboard().getString();
        TextArea teamTaskArea = (TextArea) event.getSource();
        int teamsCount = teamListBox.getChildren().size();
        controller.addMemberToTeam(teamsCount, fio);
    }

    private void removeMemberFromLeftBox(Object label) {
        leftBox.getChildren().remove(label);
    }

    private void addMemberToTeam(String fio, VBox teamLocalTaskBox) {
        if (teamLocalTaskBox == null) {
            int teamsCount = teamListBox.getChildren().size() - 1;
            teamLocalTaskBox = (VBox) (teamListBox.getChildren().get(teamsCount));
        }
        FlowPane teamMembers = (FlowPane) teamLocalTaskBox.getChildren().get(1);
        int teamNumber = getTeamNumberFromId(teamLocalTaskBox.getId());
        if (controller.addMemberToTeam(teamNumber, fio)) {
            Label teamMember = new Label(fio);
            teamMember.setFont(Font.font(16));
            teamMember.setPadding(new Insets(0, 0, 10, 10));
            teamMember.setOnDragDetected(DnDProvider::onLabelDragDetected);
            teamMember.setOnMouseClicked(event -> onTeamMemberClicked(event, fio));

            teamMembers.getChildren().add(teamMember);
        }


    }

    private void onTeamMemberClicked(MouseEvent event, String fio) {
        if (event.getClickCount() == 2) {
            addWorkerLabelToLeftBox(fio);
            prepareToRemoveMemberFromTeam(event);
        }
    }

    public void setWorkerLeftColor(String fio, Paint color) {
        leftBox.getChildren().stream()
                .filter(node -> node.getClass().equals(Label.class))
                .map(node -> (Label) node)
                .filter(label -> label.getText().equals(fio))
                .findFirst().get()
                .setBackground(new Background(new BackgroundFill(color, null, null)));
    }

    public void addWorkerToTeam(String fio, int teamNumber) {
        VBox teamBox = (VBox) teamListBox.getChildren().get(teamNumber - 1);
        FlowPane members = (FlowPane) teamBox.getChildren().get(1);
        members.getChildren().add(createFioLabelForTeamMembers(fio));
    }

    public void removeMemberFromTeam(String fio, int teamNumber) {
        VBox teamBox = (VBox) teamListBox.getChildren().get(teamNumber - 1);
        FlowPane members = (FlowPane) teamBox.getChildren().get(1);
        members.getChildren().remove(
                members.getChildren().stream()
                        .filter(node -> node.getClass().equals(Label.class))
                        .map(node -> (Label) node)
                        .filter(label -> label.getText().equals(fio))
                        .findFirst().get());
    }

    public void prepareToRemoveMemberFromTeam(InputEvent event) {
        Label dragSource = null;
        int teamNumber = 0;
        String fio = null;
        if (event.getClass().equals(DragEvent.class)) {
            // перетянули из
            dragSource = (Label) ((DragEvent) event).getGestureSource();
            fio = ((DragEvent) event).getDragboard().getString();
        } else if (event.getClass().equals(MouseEvent.class)) {
            dragSource = (Label) event.getSource();
            fio = dragSource.getText();
        }
        teamNumber = getTeamNumberFromId(dragSource.getParent().getParent().getId());
        assert dragSource != null;
        controller.removeMemberFromTeam(teamNumber, fio);
    }

    private int getTeamNumberFromId(String id) {
        id = id.substring(id.indexOf('#') + 1);
        return Integer.parseInt(id);
    }

    public void setController(TaskController controller) {
        this.controller = controller;
    }

    public void setTaskAuthors(List<String> authors) {
        cmbTaskAuthor.getItems().addAll(authors);
    }

    public void onAuthorSelect(ActionEvent actionEvent) {
        controller.changeAuthor((String) ((ComboBox) actionEvent.getSource()).getValue());
    }

}
