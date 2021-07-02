package view;
import java.util.regex.Matcher;

import Exceptions.MyException;
import Utility.CommandMatcher;
import controllers.ProfileControl;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class Profile extends Application {
    public static Stage stage;
    public static TextField nickName;
    public static PasswordField pass1;
    public static PasswordField pass2;

    @Override
    public void start(Stage stage) throws Exception {
        Profile.stage = stage;
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/SignIn.fxml"));
        stage.setScene(new Scene(root));
        stage.show();
    }
}
