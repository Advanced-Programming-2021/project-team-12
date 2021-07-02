package view;
import java.util.regex.Matcher;

import Exceptions.MyException;
import Utility.CommandMatcher;
import controllers.ProfileControl;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class Profile extends Application {
    public static Stage stage;
    public static PasswordField pass1;
    public static PasswordField pass2;
    public TextField nickName;
    public TextField oldPassword;
    public TextField newPassword;
    public Label text;


    @Override
    public void start(Stage stage) throws Exception {
        Profile.stage = stage;
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/SignIn.fxml"));
        stage.setScene(new Scene(root));
        stage.show();
    }

    public void back(MouseEvent mouseEvent) throws Exception {
        new MainMenu().start(stage);
    }

    public void changeNickname(MouseEvent mouseEvent) {
        text.setText("");
        try {
            new ProfileControl().changeNickName(nickName.getText());
            nickName.clear();
        }catch (Exception e){
          text.setText(e.getMessage());
        }
    }

    public void changePassword(MouseEvent mouseEvent) {
        text.setText("");
        try {
            new ProfileControl().changePass(oldPassword.getText(), newPassword.getText());
            oldPassword.clear();
            newPassword.clear();
        }catch (MyException e){
            text.setText(e.getMessage());
        }
    }
}
