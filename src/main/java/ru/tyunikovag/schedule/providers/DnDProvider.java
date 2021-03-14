package ru.tyunikovag.schedule.providers;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.input.*;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import ru.tyunikovag.schedule.view.TaskView;

public class DnDProvider {

    private static Pane dragParent;
    private static Object dragSource;
    private static Object dragTarget;
    private static TaskView taskView;

    public static void setTaskView(TaskView taskView) {
        DnDProvider.taskView = taskView;
    }

    public static void labelDragDetected(MouseEvent event) {
        /* drag was detected, start drag-and-drop gesture*/
//        System.out.println("onDragDetected");
        Label label = (Label) event.getSource();
        dragParent = (Pane) label.getParent();
        /* allow any transfer mode */
        Dragboard dragboard = label.startDragAndDrop(TransferMode.ANY);

        /* put a string on dragboard */
        ClipboardContent content = new ClipboardContent();
        content.putString(label.getText());
        dragboard.setContent(content);

        event.consume();
//        System.out.println(content.getString());
        dragSource = event.getSource();
    }

    public static void onLeftBoxDragOver(DragEvent event) {
        /* data is dragged over the target */
//        System.out.println("onLeftOver");

        /* accept it only if it is  not dragged from the same node
         * and if it has a string data */
        VBox target = (VBox) event.getSource();
        if (event.getGestureSource() != target && event.getDragboard().hasString()) {
            /* allow for both copying and moving, whatever user chooses */
            event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
        }

        event.consume();
    }

    public static void onTeamTaskAreaDragOver(DragEvent event) {
        /* data is dragged over the target */
//        System.out.println("onDragOver");

        /* accept it only if it is  not dragged from the same node
         * and if it has a string data */
        TextArea target = (TextArea) event.getSource();
        if (event.getGestureSource() != target && event.getDragboard().hasString()) {
            /* allow for both copying and moving, whatever user chooses */
            event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
        }

        event.consume();
    }

    public static void onLeftBoxDragDropped(DragEvent event) {

//        System.out.println("onLeftDropped");
        /* if there is a string data on dragboard, read it and use it */
        Dragboard dragboard = event.getDragboard();
        boolean success = false;
        if (dragboard.hasString()) {
            Label label = new Label(dragboard.getString());
            label.setOnDragDetected(DnDProvider::labelDragDetected);
            VBox leftBox = (VBox) event.getGestureTarget();
            leftBox.getChildren().add(label);
            dragParent.getChildren().remove(dragSource);

            taskView.removeMemberFromTeam((Pane) dragParent.getParent(), dragboard.getString());

            dragSource = null;
            dragParent = null;
            success = true;
        }
        /* let the source know whether the string was successfully
         * transferred and used */
        event.setDropCompleted(success);

        event.consume();
    }

    public static void onTeamTaskAreaDragDropped(DragEvent event) {

//        System.out.println("onDragDropped");
        /* if there is a string data on dragboard, read it and use it */
        Dragboard dragboard = event.getDragboard();
        boolean success = false;
        if (dragboard.hasString()) {
            Label teamMember = new Label(dragboard.getString());
            teamMember.setFont(Font.font(16));
            teamMember.setPadding(new Insets(0, 0, 10, 10));
            teamMember.setOnDragDetected(DnDProvider::labelDragDetected);
//            teamMember.setOnMouseClicked(taskController::removeMemberFromTeam);

            TextArea teamTaskArea = (TextArea) event.getSource();
            VBox teamLocalTaskBox = (VBox) teamTaskArea.getParent();
            FlowPane teamMembers = (FlowPane) teamLocalTaskBox.getChildren().get(1);
            teamMembers.getChildren().add(teamMember);

            taskView.addMemberToTeam(teamLocalTaskBox, dragboard.getString());
            taskView.removeMemberFromTeam(dragParent, dragboard.getString());

            dragParent.getChildren().remove(dragSource);
            dragSource = null;
            dragParent = null;
            success = true;
        }
        /* let the source know whether the string was successfully
         * transferred and used */
        event.setDropCompleted(success);

        event.consume();
    }
}
