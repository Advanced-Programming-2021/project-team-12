package view;

import controllers.Game;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import models.User;

import java.util.Objects;

public class EndGameMenu extends Application {
    public static User user;
    public static Stage stage;
    public Label label;
    public static String text;


    @Override
    public void start(Stage stage) throws Exception {
        EndGameMenu.stage = stage;
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/fxml/EndGame.fxml")));
        stage.setScene(new Scene(root));
        stage.show();
    }

    @FXML
    public void initialize() {
        if (Game.getWinnerName().equals(MainMenu.user.getName()))
            label.setText("you win" + text);
        else
            label.setText("you loose" + text);
    }

    public static void setLabelText(String score) {
        text = score;
    }

    public void back() {
        try {
            new MainMenu().start(stage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
