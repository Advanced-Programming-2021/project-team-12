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
        System.out.println("1111");
    }

    private void createScoreboardObjects(Pane pane) {
        ArrayList<User> sortedUsers = User.getUsers();
        for (int i = 0; i < sortedUsers.size(); i++) {
            Text rankText = new Text();
            Text nickNameText = new Text();
            Text scoreText = new Text();
            rankText.setFont(Font.font("Tempus Sans ITC", FontWeight.BOLD, 15));
            rankText.setLayoutX(175);
            rankText.setLayoutY(i * 17 + 14);
            nickNameText.setFont(Font.font("Tempus Sans ITC", FontWeight.BOLD, 15));
            nickNameText.setLayoutX(275);
            nickNameText.setLayoutY(i * 17 + 14);
            scoreText.setFont(Font.font("Tempus Sans ITC", FontWeight.BOLD, 15));
            scoreText.setLayoutX(375);
            scoreText.setLayoutY(i * 17 + 14);
            if(sortedUsers.get(i) == MainMenu.user){
                scoreText.setFill(Color.valueOf("green"));
                nickNameText.setFill(Color.valueOf("green"));
                rankText.setFill(Color.valueOf("green"));
            } else {
                scoreText.setFill(Color.valueOf("white"));
                nickNameText.setFill(Color.valueOf("white"));
                rankText.setFill(Color.valueOf("white"));
            }
            rankText.setText(Integer.toString(i + 1));
            nickNameText.setText(sortedUsers.get(i).getNickName());
            scoreText.setText(Integer.toString(sortedUsers.get(i).getScore()));
            pane.getChildren().add(rankText);
            pane.getChildren().add(nickNameText);
            pane.getChildren().add(scoreText);
        }
        for (int i = sortedUsers.size(); i < 20; i++) {
            Text rankText = new Text();
            Text nickNameText = new Text();
            Text scoreText = new Text();
            rankText.setFont(Font.font("Tempus Sans ITC", FontWeight.BOLD, 15));
            rankText.setLayoutX(175);
            rankText.setLayoutY(i * 17 + 14);
            nickNameText.setFont(Font.font("Tempus Sans ITC", FontWeight.BOLD, 15));
            nickNameText.setLayoutX(275);
            nickNameText.setLayoutY(i * 17 + 14);
            scoreText.setFont(Font.font("Tempus Sans ITC", FontWeight.BOLD, 15));
            scoreText.setLayoutX(375);
            scoreText.setLayoutY(i * 17 + 14);
            scoreText.setFill(Color.valueOf("white"));
            nickNameText.setFill(Color.valueOf("white"));
            rankText.setFill(Color.valueOf("white"));
            rankText.setText(Integer.toString(i + 1));
            nickNameText.setText("-----");
            scoreText.setText("-----");
            pane.getChildren().add(rankText);
            pane.getChildren().add(nickNameText);
            pane.getChildren().add(scoreText);
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

    public void goBack(ActionEvent actionEvent) {
        try {
            new MainMenu().start(stage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
