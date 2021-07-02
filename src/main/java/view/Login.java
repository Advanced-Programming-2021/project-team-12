package view;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import javax.sound.sampled.Clip;
import java.util.Objects;

public class Login extends Application {
    public static Stage stage;
    public TextField userName;
    public PasswordField password;

    @Override
    public void start(Stage stage) throws Exception {
        Login.stage = stage;
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/LoginMenu.fxml"));
        stage.setScene(new Scene(root));
        stage.show();
    }
}
