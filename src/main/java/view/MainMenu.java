package view;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import models.User;

public class MainMenu {
    public static User user;
    public static Stage stage;
    public TextField userName;
    public TextField nickName;
    public PasswordField password;

    @Override
    public void start(Stage stage) throws Exception {
        MainMenu.stage = stage;
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/MainMenu.fxml"));
        stage.setScene(new Scene(root));
        stage.show();
    }

    public void setUser(User _user) {
        user = _user;
    }

}
