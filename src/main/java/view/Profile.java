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
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.imageio.stream.FileCacheImageOutputStream;

public class Profile extends Application {
    public static Stage stage;
    public TextField nickName;
    public ImageView avatar;
    public TextField oldPassword;
    public TextField newPassword;
    public Label text;
    public Button userNickName;
    public Button userName;

    @Override
    public void start(Stage stage) throws Exception {
        Profile.stage = stage;
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/fxml/ChangeProfile.fxml")));
        stage.setScene(new Scene(root));
        stage.show();
    }

    @FXML
    private void initialize() {
        File file =  new File(MainMenu.user.getAvatarAddress());
        String string = file.toURI().toString();
        Image image = new Image(string);
        avatar.setImage(image);
        userName.setText("USER NAME: " + MainMenu.user.getName());
        userNickName.setText("NICK NAME: " + MainMenu.user.getNickName());

    }

    public void leftAvatar(MouseEvent mouseEvent) {
        MainMenu.user.decreaseAvatar();
        File file =  new File(MainMenu.user.getAvatarAddress());
        String string = file.toURI().toString();
        Image image = new Image(string);
        avatar.setImage(image);
    }

    public void rightAvatar(MouseEvent mouseEvent) {
        MainMenu.user.increaseAvatar();
        File file =  new File(MainMenu.user.getAvatarAddress());
        String string = file.toURI().toString();
        Image image = new Image(string);
        avatar.setImage(image);
    }

    public void back(MouseEvent mouseEvent) throws Exception {
        SaveFile.saveUser(MainMenu.user);
        new MainMenu().start(stage);
    }

    public void changeNickname(MouseEvent mouseEvent) {
        text.setText("");
        try {
            new ProfileControl().changeNickName(this.nickName.getText());
            userNickName.setText("NICK NAME: " + MainMenu.user.getNickName());
            text.setText("Changed successfully");
            text.setStyle("-fx-text-fill: #16045c");
        } catch (MyException var3) {
            text.setText(var3.getMessage());
            text.setStyle("-fx-text-fill: #590000");
        } catch (Exception e) {
            e.printStackTrace();
        }
        nickName.clear();
    }

    public void changePassword(MouseEvent mouseEvent) {
        text.setText("");
        try {
            new ProfileControl().changePass(this.oldPassword.getText(), this.newPassword.getText());
            text.setText("Changed successfully");
            text.setStyle("-fx-text-fill: #16045c");
        } catch (MyException var3) {
            text.setText(var3.getMessage());
            text.setStyle("-fx-text-fill: #590000");
        } catch (Exception e) {
            e.printStackTrace();
        }
        oldPassword.clear();
        newPassword.clear();
    }

    public void fileChooser(ActionEvent actionEvent) {
        text.setText("");
        FileChooser fc = new FileChooser();
        fc.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("PNG", "*.png"));
        File file = fc.showOpenDialog(null);
        if (file != null) {
            try {
                new ProfileControl().changeAvatar(file);
                File pictureFile =  new File(MainMenu.user.getAvatarAddress());
                String string = pictureFile.toURI().toString();
                Image image = new Image(string);
                avatar.setImage(image);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
