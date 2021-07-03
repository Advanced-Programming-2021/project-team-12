package view;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import models.User;

import javax.sound.sampled.Clip;
import java.io.File;
import java.util.ArrayList;
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
        ScrollPane scrollPane = (ScrollPane) pane.getChildren().get(0);
        Pane innerPane = (Pane) scrollPane.getContent();
        createScoreboardObjects(innerPane);
        stage.setScene(scene);
        stage.show();
    }

    private void createScoreboardObjects(Pane innerPane) {
        ArrayList<User> sortedUsers = User.getUsers();
        createTextForTop(sortedUsers, innerPane);
        createTextForEmpty(sortedUsers, innerPane);
        createImageForTop(sortedUsers, innerPane);
    }

    private void createImageForTop(ArrayList<User> sortedUsers, Pane innerPane) {
        for (int i = 0; i < sortedUsers.size(); i++) {
            File pictureFile =  new File(MainMenu.user.getAvatarAddress());
            String string = pictureFile.toURI().toString();
            Image image = new Image(string);
            ImageView imageView = new ImageView();
            imageView.setImage(image);
            imageView.setImage(image);
            imageView.setFitWidth(40);
            imageView.setFitHeight(40);
            imageView.setX(200);
            imageView.setY(i * 45 + 40);
            innerPane.getChildren().add(imageView);
            if(sortedUsers.get(i) == MainMenu.user){
                Image imageStar = new Image(getClass().getResource("/PNG/NEW/goldStar.png").toExternalForm());
                ImageView imageViewStar = new ImageView();
                imageViewStar.setImage(imageStar);
                imageViewStar.setFitWidth(15);
                imageViewStar.setFitHeight(15);
                imageViewStar.setX(155);
                imageViewStar.setY(i * 45 + 47);
                innerPane.getChildren().add(imageViewStar);
            }
        }
    }

    private void createTextForEmpty(ArrayList<User> sortedUsers, Pane innerPane) {
        for (int i = sortedUsers.size(); i < 20; i++) {
            Text rankText = new Text();
            Text nickNameText = new Text();
            Text scoreText = new Text();
            rankText.setFont(Font.font("Yu-Gi-Oh! StoneSerif LT", FontWeight.BOLD, 25));
            rankText.setLayoutX(175);
            rankText.setLayoutY(i * 45 + 60);
            nickNameText.setFont(Font.font("Yu-Gi-Oh! StoneSerif LT", FontWeight.BOLD, 25));
            nickNameText.setLayoutX(270);
            nickNameText.setLayoutY(i * 45 + 60);
            scoreText.setFont(Font.font("Yu-Gi-Oh! StoneSerif LT", FontWeight.BOLD, 25));
            scoreText.setLayoutX(375);
            scoreText.setLayoutY(i * 45 + 60);
            scoreText.setFill(Color.valueOf("white"));
            nickNameText.setFill(Color.valueOf("white"));
            rankText.setFill(Color.valueOf("white"));
            rankText.setText(Integer.toString(i + 1));
            nickNameText.setText("-----");
            scoreText.setText("-----");
            innerPane.getChildren().add(rankText);
            innerPane.getChildren().add(nickNameText);
            innerPane.getChildren().add(scoreText);
        }
    }

    private void createTextForTop(ArrayList<User> sortedUsers, Pane innerPane) {
        for (int i = 0; i < sortedUsers.size(); i++) {
            Text rankText = new Text();
            Text nickNameText = new Text();
            Text scoreText = new Text();
            rankText.setFont(Font.font("Yu-Gi-Oh! StoneSerif LT", FontWeight.BOLD, 25));
            rankText.setLayoutX(175);
            rankText.setLayoutY(i * 45 + 60);
            nickNameText.setFont(Font.font("Yu-Gi-Oh! StoneSerif LT", FontWeight.BOLD, 25));
            nickNameText.setLayoutX(270);
            nickNameText.setLayoutY(i * 45 + 60);
            scoreText.setFont(Font.font("Yu-Gi-Oh! StoneSerif LT", FontWeight.BOLD, 25));
            scoreText.setLayoutX(375);
            scoreText.setLayoutY(i * 45 + 60);
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
            innerPane.getChildren().add(rankText);
            innerPane.getChildren().add(nickNameText);
            innerPane.getChildren().add(scoreText);
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
