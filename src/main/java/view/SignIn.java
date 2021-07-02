package view;

import Exceptions.MyException;
import controllers.LoadFile;
import controllers.SignInController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import org.w3c.dom.Text;

public class SignIn extends Application {
    public static Stage stage;
    public TextField userName;
    public TextField nickName;
    public PasswordField password;

    @Override
    public void start(Stage stage) throws Exception {
        SignIn.stage = stage;
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/SignIn.fxml"));
        stage.setScene(new Scene(root));
        stage.show();
    }

    public void signInUser(MouseEvent mouseEvent) {
        String name = userName.getText();
        String nick = userName.getText();
        String pass = password.getText();
        userName.clear();
        nickName.clear();
        password.clear();
        try {
            new SignInController().checkData(name, nick, pass);
            new RegistrationMenu().start(stage);
        } catch(MyException e) {
            String message = e.getMessage();
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Error");
            alert.setHeaderText(message);
            alert.setContentText("Please Try Again");
            alert.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
