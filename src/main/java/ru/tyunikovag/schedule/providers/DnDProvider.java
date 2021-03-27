package ru.tyunikovag.schedule.providers;

import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.input.*;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import ru.tyunikovag.schedule.view.TaskView;

public class DnDProvider {

    private static TaskView view;

    public static void setView(TaskView view) {
        DnDProvider.view = view;
    }

    public static void onLabelDragDetected(MouseEvent event) {
        /* drag was detected, start drag-and-drop gesture*/
//        System.out.println("onDragDetected");
        Label label = (Label) event.getSource();
        /* allow any transfer mode */
        Dragboard dragboard = label.startDragAndDrop(TransferMode.ANY);

        /* put a string on dragboard */
        ClipboardContent content = new ClipboardContent();
        content.putString(label.getText());
        dragboard.setContent(content);

        event.consume();
    }

    public static void onLeftBoxDragOver(DragEvent event) {
        /* data is dragged over the target */

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
        boolean success = false;
        if (((Label) event.getGestureSource()).getParent() != event.getSource() && event.getDragboard().hasString()) {
            view.draggedMemberToLeft(event);
            success = true;
        }
        /* let the source know whether the string was successfully
         * transferred and used */
        event.setDropCompleted(success);

        event.consume();
    }

    public static void onTeamTaskAreaDragDropped(DragEvent event) {

        /* if there is a string data on dragboard, read it and use it */
        Dragboard dragboard = event.getDragboard();
        boolean success = false;
        if (dragboard.hasString()) {
            view.draggedMemberToTeam(event);
            success = true;
        }
        /* let the source know whether the string was successfully transferred and used */
        event.setDropCompleted(success);
        event.consume();
    }
}
