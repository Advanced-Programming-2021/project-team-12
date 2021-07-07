package view;
import controllers.LoadFile;
import controllers.SaveFile;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import Utility.Sounds;

import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;
import java.util.Objects;

public class RegistrationMenu extends Application {
    private static Stage stage;
    public static Clip clip;
    private static boolean startLoad;

    @Override
    public void start(Stage primaryStage) throws Exception {
        if (!startLoad) {
            LoadFile.loadData();
            startLoad = true;
            try {
                clip = Sounds.play("src//main//resources//Sound//Menu.wav", 0);
                clip.start();
                clip.stop();
            } catch (LineUnavailableException | UnsupportedAudioFileException | IOException e) {
                e.printStackTrace();
            }
        }
        stage = primaryStage;
        Pane pane =  FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/fxml/RegistrationMenu.fxml")));
        Scene scene = new Scene(pane);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    public void login(ActionEvent actionEvent) throws Exception {
        new LoginMenu().start(stage);
    }

    public void register(ActionEvent actionEvent) throws Exception {
        new SignIn().start(stage);
    }

    public void exit(ActionEvent actionEvent) {
        System.exit(0);
    }
}