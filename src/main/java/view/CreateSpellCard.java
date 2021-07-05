package view;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class CreateSpellCard extends Application {
    public static Stage stage;

    @Override
    public void start(Stage stage) throws Exception {
        CreateSpellCard.stage = stage;
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/CreateSpellCard.fxml"));
        stage.setScene(new Scene(root));
        stage.show();
    }
}
