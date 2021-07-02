package view;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import models.User;

import javax.sound.sampled.Clip;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Objects;

public class Scoreboard extends Application {
    public static Boolean isMute = false;
    private static Stage stage;
    private static Clip clip;

    @Override
    public void start(Stage primaryStage) throws Exception {
        stage = primaryStage;
        Pane pane =  FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/fxml/Scoreboard.fxml")));
        Scene scene = new Scene(pane);
        createScoreboardObjects(pane);
        stage.setScene(scene);
        stage.show();
    }

    private void createScoreboardObjects(Pane pane) {
        ArrayList<User> sortedUsers = User.getUsers();
        for (int i = 0; i < sortedUsers.size(); i++) {
            Text rankText = new Text();
            Text nickNameText = new Text();
            Text scoreText = new Text();
            rankText.setFont(Font.font("Tempus Sans ITC", FontWeight.BOLD, 15));
            rankText.setFill(Color.valueOf("black"));
            rankText.setLayoutX(100);
            rankText.setLayoutY(i * 17 + 14);
            nickNameText.setFont(Font.font("Tempus Sans ITC", FontWeight.BOLD, 15));
            nickNameText.setFill(Color.valueOf("black"));
            nickNameText.setLayoutX(150);
            nickNameText.setLayoutY(i * 17 + 14);
            scoreText.setFont(Font.font("Tempus Sans ITC", FontWeight.BOLD, 15));
            scoreText.setFill(Color.valueOf("black"));
            scoreText.setLayoutX(300);
            scoreText.setLayoutY(i * 17 + 14);
            rankText.setText(Integer.toString(i));
            nickNameText.setText(sortedUsers.get(i).getNickName());
            scoreText.setText(Integer.toString(sortedUsers.get(i).getScore()));
            pane.getChildren().add(rankText);
        }
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
