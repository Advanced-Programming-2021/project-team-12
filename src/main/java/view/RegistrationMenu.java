package view;
import Exceptions.MyException;
import Utility.CommandMatcher;
import controllers.LoadFile;
import controllers.LogInController;
import controllers.SignInController;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import models.User;

import javax.sound.sampled.Clip;
import java.io.IOException;
import java.util.Objects;
import java.util.regex.Matcher;

public class RegistrationMenu extends Application {
    public static Boolean isMute = false;
    private static Stage stage;
    private static Clip clip;
    private static boolean startLoad;

    @Override
    public void start(Stage primaryStage) throws Exception {
        if (!startLoad) {
            new LoadFile();
            startLoad = true;
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