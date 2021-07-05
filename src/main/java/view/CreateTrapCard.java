package view;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class CreateTrapCard extends Application {
    public static Stage stage;

    @Override
    public void start(Stage stage) throws Exception {
        CreateTrapCard.stage = stage;
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/CreateTrapCard.fxml"));
        stage.setScene(new Scene(root));
        stage.show();
    }
}
