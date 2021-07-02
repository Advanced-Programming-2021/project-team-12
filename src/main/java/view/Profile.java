package view;
import java.io.File;
import java.io.FileInputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

import Exceptions.MyException;
import controllers.ProfileControl;
import controllers.SaveFile;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class Profile extends Application {
    public static Stage stage;
    public TextField nickName;
    public ImageView avatar;
    public TextField oldPassword;
    public TextField newPassword;
    public Label text;

    @Override
    public void start(Stage stage) throws Exception {
        Profile.stage = stage;
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/ChangeProfile.fxml"));
        stage.setScene(new Scene(root));
        stage.show();
    }

    @FXML
    private void initialize() {
        avatar.setImage(new Image(Objects.requireNonNull(getClass().getResource("/PNG/Characters/Chara001.dds" + MainMenu.user.getAvatar() + ".png")).toExternalForm()));
    }

    public void leftAvatar(MouseEvent mouseEvent) {
        MainMenu.user.decreaseAvatar();
        avatar.setImage(new Image(Objects.requireNonNull(getClass().getResource("/PNG/Characters/Chara001.dds" + MainMenu.user.getAvatar() + ".png")).toExternalForm()));
    }

    public void rightAvatar(MouseEvent mouseEvent) {
        MainMenu.user.increaseAvatar();
        avatar.setImage(new Image(Objects.requireNonNull(getClass().getResource("/PNG/Characters/Chara001.dds" + MainMenu.user.getAvatar() + ".png")).toExternalForm()));
    }

    public void back(MouseEvent mouseEvent) throws Exception {
        SaveFile.saveUser(MainMenu.user);
        new MainMenu().start(stage);
    }

    public void changeNickname(MouseEvent mouseEvent) {
        this.text.setText("");
        try {
            new ProfileControl().changeNickName(this.nickName.getText());
        } catch (MyException var3) {
            this.text.setText(var3.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
        nickName.clear();
    }

    public void changePassword(MouseEvent mouseEvent) {
        this.text.setText("");
        try {
            new ProfileControl().changePass(this.oldPassword.getText(), this.newPassword.getText());
        } catch (MyException var3) {
            this.text.setText(var3.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
        oldPassword.clear();
        newPassword.clear();
    }
}
