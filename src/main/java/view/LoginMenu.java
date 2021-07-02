package view;

import Exceptions.MyException;
import controllers.LogInController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class LoginMenu extends Application {
    public static Stage stage;
    public TextField userName;
    public PasswordField password;

    @Override
    public void start(Stage stage) throws Exception {
        LoginMenu.stage = stage;
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/LoginMenu.fxml"));
        stage.setScene(new Scene(root));
        stage.show();
    }

    public void userLogin(MouseEvent mouseEvent) {
        String name = userName.getText();
        String pass = password.getText();
        userName.clear();
        password.clear();
        try {
            new LogInController().checkData(name, pass);
        } catch (MyException e) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Error");
            alert.setHeaderText("Wrong User Name Or Password");
            alert.setContentText("Please Try Again");
            alert.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
