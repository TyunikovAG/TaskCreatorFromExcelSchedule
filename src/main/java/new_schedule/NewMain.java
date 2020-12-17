package new_schedule;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class NewMain extends Application {

    public static void main(String[] args) {
        Application.launch();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(NewMain.class.getResource("test.fxml"));
        //Parent mainLayout = loader.load();

        Parent fxmlRoot = loader.load(getClass().getClassLoader().getResource("test.fxml"));

        Scene scene = new Scene(fxmlRoot);
        primaryStage.setScene(scene);
        primaryStage.setTitle("TyunikovAG inc.");
        primaryStage.show();
    }
}
