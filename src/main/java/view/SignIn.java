package view;

import controllers.LoadFile;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class SignIn extends Application {
    public BorderPane borderPane;
    private static Stage stage;

    @Override
    public void start(Stage stage) throws Exception {
        SignIn.stage = stage;
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/SignIn.fxml"));
        stage.setScene(new Scene(root));
        stage.show();
    }
}
