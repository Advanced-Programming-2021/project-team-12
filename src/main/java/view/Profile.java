package view;
import java.util.Objects;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class Profile extends Application {
    public static Stage stage;
    public static TextField nickName;
    public ImageView avatar;

    @Override
    public void start(Stage stage) throws Exception {
        Profile.stage = stage;
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/SignIn.fxml"));
        stage.setScene(new Scene(root));
        avatar.setImage(new Image(Objects.requireNonNull(getClass().getResource("/Characters/Chara001.dds" + MainMenu.user.getAvatar() + ".png")).toExternalForm()));
        stage.show();
    }

    public void leftAvatar(MouseEvent mouseEvent) {
        MainMenu.user.decreaseAvatar();
        avatar.setImage(new Image(Objects.requireNonNull(getClass().getResource("/Characters/Chara001.dds" + MainMenu.user.getAvatar() + ".png")).toExternalForm()));
    }

    public void rightAvatar(MouseEvent mouseEvent) {
        MainMenu.user.increaseAvatar();
        avatar.setImage(new Image(Objects.requireNonNull(getClass().getResource("/Characters/Chara001.dds" + MainMenu.user.getAvatar() + ".png")).toExternalForm()));
    }
}
