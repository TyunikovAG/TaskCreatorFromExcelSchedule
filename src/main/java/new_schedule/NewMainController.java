package new_schedule;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import javax.swing.*;
import java.io.File;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class NewMainController implements Initializable {

    Map<Worker, Map<Integer, Shift>> scheduleOfAllWorkers;
    ExelProvider exelProvider;

    PropertyProvider propertyProvider = new PropertyProvider();
    private static Properties properties;
    private static String taskBlankFileName;
    private static String scheduleFileName;
    private static int teamCount = 0;

    private Pane dragParent;
    private Object dragSource;
    private Object dragTarget;

    @FXML
    private VBox leftBox;

    @FXML
    private VBox ctnterBox;

    @FXML
    private Button btnAddSection;

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
    private Label lblFileOfBlankName;

    @FXML
    private Label lblFileScheduleName;

    @FXML
    private Label lblChangeScheduleFile;

    @FXML
    private Label lblChangeBlankFile;

    private Object sendedObject;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initProperty();
        exelProvider = new ExelProvider();

        cmbShift.getItems().addAll("Ночь", "Утро", "Вечер");
        cmbTaskAuthor.getItems().addAll("Семёнов И.П.", "Шаповалов Д.С.", "Крамер А.В.");

        datePicker.setValue(NOW_LOCAL_DATE());
        onButtonAddScetionAction(new ActionEvent());
        lblFileScheduleName.setText("Файл графика: " + scheduleFileName);
        lblFileOfBlankName.setText("Файл бланка наряда: " + taskBlankFileName);
        leftBox.setOnDragOver(DnDProvider::onLeftBoxDragOver);
        leftBox.setOnDragDropped(DnDProvider::onLeftBoxDragDropped);
        setMembersImage();
    }

    public static final LocalDate NOW_LOCAL_DATE (){
        String date = new SimpleDateFormat("dd-MM-yyyy").format(Calendar.getInstance().getTime());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        LocalDate localDate = LocalDate.parse(date, formatter);
        return localDate;
    }

    private void setMembersImage(){
        Image image = new Image("members.jpg");
        BackgroundSize backgroundSize = new BackgroundSize(300, 300, true, true, true, false);
        BackgroundPosition bottom = new BackgroundPosition(Side.LEFT, 0, false,
                                                            Side.BOTTOM, 0, false);
        BackgroundImage backgroundImage = new BackgroundImage(image, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, bottom, backgroundSize);
        BackgroundFill fill = new BackgroundFill(Color.LIGHTGRAY, null, null);

        Background background0 = new Background(new BackgroundFill[] {fill}, new BackgroundImage[] {backgroundImage});
        leftBox.setBackground(background0);
    }

    private void fillLeftBoxByWorkers() {

        deleteAllFromLeft();
        Shift shift = getShiftByString(cmbShift.getValue());
        LocalDate localDate = datePicker.getValue();
        File scheduleFile = new File(scheduleFileName);
        scheduleOfAllWorkers = exelProvider.getScheduleOfAllWorkers(scheduleFile, localDate);
        ArrayList<Worker> workers = getEmployesOnShift(localDate.getDayOfMonth(), shift);

        for (Worker worker : workers
        ) {
            Label label = new Label(worker.fio);
            label.setOnDragDetected(DnDProvider::labelDragDetected);
            leftBox.getChildren().add(label);
        }
    }

    private void checkForMe(Map<Worker, Map<Integer, Shift>> scheduleOfAllWorkers) {
        boolean toExit = true;
        for (Worker worker : scheduleOfAllWorkers.keySet()) {
            if (worker.fio.contains("Тюников")){
                toExit = false;
            }
        }
        if (toExit){
            JOptionPane.showMessageDialog(null,
                    "Неверное структурное подразделение", "Error", JOptionPane.ERROR_MESSAGE);
            Platform.exit();

        }
    }

    public ArrayList<Worker> getEmployesOnShift(int date, Shift shift) {
        ArrayList<Worker> workersOnShift = new ArrayList<>(10);
        for (Map.Entry<Worker, Map<Integer, Shift>> personMap : scheduleOfAllWorkers.entrySet()) {
            Map<Integer, Shift> schedule = personMap.getValue();
            Shift personShift = schedule.get(date);
            if (personShift != null) {
                if (personShift.equals(shift)) {
                    workersOnShift.add(personMap.getKey());
                }
            }
        }
        return workersOnShift;
    }

    Shift getShiftByString(String s){
        switch (s){
            case "Утро":{
                return Shift.MORNING;
            }
            case "Вечер":{
                return Shift.EVENING;
            }
            case "Ночь":{
                return Shift.NIGHT;
            }
        }
        return  null;
    }

    public void deleteAllFromLeft() {
        List<Node> list = leftBox.getChildren();
        while (list.size() > 1){
            list.remove(1);
        }
    }

    @FXML
    void OnMainButtonAction(ActionEvent event) {
        Label label = (Label) leftBox.getChildren().get(0);
        label.setText(datePicker.getValue().toString());
        if (cmbShift.getValue() == null || cmbShift.getValue().equals("Укажите смену")){
            JOptionPane.showMessageDialog(null,
                    "Не указана смена", "InfoBox", JOptionPane.INFORMATION_MESSAGE);

        } else {
            claenCentralBox();
            onButtonAddScetionAction(new ActionEvent());
            fillLeftBoxByWorkers();
        }
    }

    private void claenCentralBox() {
        List<Node> centerList = ctnterBox.getChildren();
        int count = centerList.size() - 2;
        for (int i = 0; i < count; i++) {
            centerList.remove(centerList.size() - 3);
            teamCount--;
        }
    }

    @FXML
    void onButtonAddScetionAction(ActionEvent event) {
        VBox teamBox = new VBox();
        teamBox.setSpacing(10);
        Label lblTeamNumber = new Label("Звено №" + (teamCount + 1));
        lblTeamNumber.setFont(Font.font(24));
        lblTeamNumber.setPadding(new Insets(0, 0, 5, 15));
        FlowPane flowPane = new FlowPane();
        TextArea teamTask = new TextArea();
        teamTask.setFont(new Font(16));

        teamTask.setOnDragOver(DnDProvider::onTextAreaDragOver);
        teamTask.setOnDragDropped(DnDProvider::onTextAreaDragDropped);

        teamBox.getChildren().addAll(lblTeamNumber, flowPane, teamTask);

        ctnterBox.getChildren().add(teamCount, teamBox);
        teamCount++;
    }

    @FXML
    void onDeleteSectionAction (ActionEvent event){
        List<Node> ceneterList = ctnterBox.getChildren();
        if (ceneterList.size() > 3){
            VBox teamBox = (VBox) ceneterList.get(ceneterList.size() - 3);
            FlowPane flowPane = (FlowPane) teamBox.getChildren().get(1);
            List<Node> labels = flowPane.getChildren();
            for (Node node : labels){
                Label label = (Label) node;
                leftBox.getChildren().add(new Label(label.getText()));
            }
            ceneterList.remove(ceneterList.size() - 3);
            teamCount--;
        }
    }

    @FXML
    void onButtonSendToBlankAction(ActionEvent event) {
        boolean isFieldsEmpty = false;
        for (Node node : ctnterBox.getChildren()){
            if (node.getClass().equals(VBox.class)){
                boolean isBoxEmpty = isTeamBoxEmpty((VBox) node);
                isFieldsEmpty |= isBoxEmpty;
            }
        }

        if (!isFieldsEmpty){
            TaskBlank taskBlank = createTaskBlank();
            new ExelCreator().createTaskBlank(taskBlank, taskBlankFileName);
        }
    }

    @FXML
    void onScheduleFileChange(MouseEvent event) {
        if (event.getClickCount() == 2) {
            propertyProvider.setFileForProperty(PropertyProvider.PropertyName.SCHEDULE_FILE_NAME);
            scheduleFileName = propertyProvider.get(PropertyProvider.PropertyName.SCHEDULE_FILE_NAME);
            lblFileScheduleName.setText(scheduleFileName);
            propertyProvider.saveProperties();
        }
    }

    @FXML
    void onBlankFileChange(MouseEvent event) {
        if (event.getClickCount() == 2) {
            propertyProvider.setFileForProperty(PropertyProvider.PropertyName.TASK_BLANK_FILE_NAME);
            taskBlankFileName = propertyProvider.get(PropertyProvider.PropertyName.TASK_BLANK_FILE_NAME);
            lblFileOfBlankName.setText(taskBlankFileName);
            propertyProvider.saveProperties();
        }
    }

    private TaskBlank createTaskBlank() {
        TaskBlank taskBlank = new TaskBlank(datePicker.getValue(), getShiftByString(cmbShift.getValue()));
        List<Team> teams = getTeams();
        taskBlank.setTeams(teams);
        return taskBlank;
    }

    private List<Team> getTeams() {
        int teamCount = ctnterBox.getChildren().size();
        List<Team> teams = new ArrayList<>(teamCount);
        for (int i = 0; i < teamCount; i++) {
            Object object = ctnterBox.getChildren().get(i);
            if (!object.getClass().equals(VBox.class)){
                continue;
            }
            VBox teamBox = (VBox) object;
            Label teamNuber = (Label)teamBox.getChildren().get(0);
            FlowPane flowPane = (FlowPane)teamBox.getChildren().get(1);
            TextArea teamtask = (TextArea) teamBox.getChildren().get(2);

            Team team = new Team(teamNuber.getText(), teamtask.getText());
            int memberCount = flowPane.getChildren().size();
            ArrayList<Worker> members = new ArrayList<>(memberCount);
            Set<Worker> allWorkers = scheduleOfAllWorkers.keySet();
            for (int j = 0; j < memberCount; j++) {
                Label label = (Label) flowPane.getChildren().get(j);
                for (Worker worker : allWorkers){
                    if (worker.getFio().equals(label.getText())){
                        members.add(worker);
                    }
                }
            }
            team.setMembers(members);
            teams.add(team);
        }
        return teams;
    }

    private boolean isTeamBoxEmpty(VBox vBox){
        int indexOfTeamName = 0;
        int indexOfMembersContainer = 1;
        int indexOfTaskTextArea = 2;
        Label teamName = (Label) vBox.getChildren().get(indexOfTeamName);
        FlowPane flowPane = (FlowPane) vBox.getChildren().get(indexOfMembersContainer);
        TextArea textArea = (TextArea) vBox.getChildren().get(indexOfTaskTextArea);

        if (flowPane.getChildren().size() == 0){
            String message = "Нет ни одного работника в " + teamName.getText();
            JOptionPane.showMessageDialog(null,
                    message, "InfoBox", JOptionPane.INFORMATION_MESSAGE);
            return true;
        }
        if (textArea.getText().trim().isEmpty()){
            String message = String.format("Задание для %s пустое", teamName.getText());
            JOptionPane.showMessageDialog(null,
                    message, "InfoBox", JOptionPane.INFORMATION_MESSAGE);
            return true;
        }
        return false;
    }

    public void setObject(Object recivedObject) {
        this.sendedObject = recivedObject;
    }

    private void initProperty() {

        PropertyProvider propertyProvider = new PropertyProvider();
        propertyProvider.getProperties();
        scheduleFileName = propertyProvider.get(PropertyProvider.PropertyName.SCHEDULE_FILE_NAME);
        taskBlankFileName = propertyProvider.get(PropertyProvider.PropertyName.TASK_BLANK_FILE_NAME);

        propertyProvider.saveProperties();
    }
}
