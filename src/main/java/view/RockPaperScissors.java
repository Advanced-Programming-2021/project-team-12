package view;

import Exceptions.MyException;
import controllers.DuelControl;
import controllers.Game;
import javafx.application.Application;
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
import models.PlayerTurn;

import java.util.Objects;
import java.util.Random;

public class RockPaperScissors extends Application {
    private static Stage stage;

    @Override
    public void start(Stage primaryStage) throws Exception {
        stage = primaryStage;
        Pane pane =  FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/fxml/RockPaperScissors.fxml")));
        createRockPaperScissorsObjects(pane);
        Scene scene = new Scene(pane);
        stage.setScene(scene);
        stage.show();
    }

    private void createRockPaperScissorsObjects(Pane pane) {
        createOpponentRockObject(pane);
        createOpponentPaperObject(pane);
        createOpponentScissorObject(pane);
        createRockObject(pane);
        createPaperObject(pane);
        createScissorObject(pane);
    }

    private void makeResult(String type) {
        Random random = new Random();
        int randomNumber = random.nextInt(3);
        if(type.equals("Scissors")){
            if(randomNumber == 0){
                createDraw("Scissors");
            } else if(randomNumber == 1){
                createOpponentWinner("Scissors", "Rock");
            }  else if(randomNumber == 2){
                createSelfWinner("Scissors", "Paper");
            }
        } else if(type.equals("Rock")){
            if(randomNumber == 0){
                createSelfWinner("Rock", "Scissors");
            } else if(randomNumber == 1){
                createDraw("Rock");
            }  else if(randomNumber == 2){
                createOpponentWinner("Rock", "Paper");
            }
        } else {
            if(randomNumber == 0){
                createOpponentWinner("Paper", "Scissors");
            } else if(randomNumber == 1){
                createSelfWinner("Paper", "Rock");
            }  else if(randomNumber == 2){
                createDraw("Paper");
            }
        }
    }

    private void createObjects(String selfType, String opponentType, Pane pane, String message) {
        Image image = new Image(getClass().getResource("/PNG/RockPaperScissor/rockPaperScissorPNG/"+selfType+"Normal.png").toExternalForm());
        ImageView imageView = new ImageView();
        imageView.setImage(image);
        imageView.setFitWidth(150);
        imageView.setFitHeight(150);
        imageView.setX(225);
        imageView.setY(220);
        Image image2 = new Image(getClass().getResource("/PNG/RockPaperScissor/rockPaperScissorPNG/"+opponentType+"NormalOpponent.png").toExternalForm());
        ImageView imageView2 = new ImageView();
        imageView2.setImage(image2);
        imageView2.setFitWidth(150);
        imageView2.setFitHeight(150);
        imageView2.setX(225);
        imageView2.setY(30);
        Text messageText = new Text();
        messageText.setFont(Font.font("StoneSerif-Semibold", FontWeight.BOLD, 15));
        messageText.setFill(Color.valueOf("white"));
        messageText.setText(message);
        if(message.equals("Draw")){
            messageText.setX(280);
            messageText.setY(205);
        } else if(message.equals("Opponent starts first")) {
            messageText.setX(225);
            messageText.setY(205);
        } else {
            messageText.setX(250);
            messageText.setY(205);
        }
        pane.getChildren().add(imageView);
        pane.getChildren().add(imageView2);
        pane.getChildren().add(messageText);
    }

    private void createBackgroundImage(Pane pane) {
        Image image = new Image(getClass().getResource("/PNG/NEW/background3Blur.png").toExternalForm());
        ImageView imageView = new ImageView();
        imageView.setImage(image);
        imageView.setFitWidth(600.0);
        imageView.setFitHeight(400.0);
        imageView.setPreserveRatio(false);
        imageView.setX(0);
        imageView.setY(0);
        pane.getChildren().add(imageView);
    }

    private void createSelfWinner(String selfType, String opponentType) {
        Pane selfWinnerPane = new Pane();
        createBackgroundImage(selfWinnerPane);
        createObjects(selfType,opponentType, selfWinnerPane, "You start first");
        Scene scene = new Scene(selfWinnerPane);
        selfWinnerPane.setOnMouseClicked(e ->{
            try {
                Game.startGame("First");
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        });
        stage.setScene(scene);
        stage.show();
    }

    private void createOpponentWinner(String selfType, String opponentType) {
        Pane opponentWinnerPane = new Pane();
        createBackgroundImage(opponentWinnerPane);
        createObjects(selfType,opponentType, opponentWinnerPane, "Opponent starts first");
        Scene scene = new Scene(opponentWinnerPane);
        opponentWinnerPane.setOnMouseClicked(e ->{
            try {
                Game.startGame("Second");
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        });
        stage.setScene(scene);
        stage.show();
    }

    private void createDraw(String selfType) {
        Pane drawPane = new Pane();
        createBackgroundImage(drawPane);
        createObjects(selfType,selfType, drawPane, "Draw");
        Scene scene = new Scene(drawPane);
        drawPane.setOnMouseClicked(e ->{
            try {
                new RockPaperScissors().start(stage);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        });
        stage.setScene(scene);
        stage.show();
    }

    private void createScissorObject(Pane pane) {
        Image image = new Image(getClass().getResource("/PNG/RockPaperScissor/rockPaperScissorPNG/ScissorsNormal.png").toExternalForm());
        ImageView imageView = new ImageView();
        imageView.setImage(image);
        imageView.setFitWidth(150);
        imageView.setFitHeight(150);
        imageView.setX(412.5);
        imageView.setY(217);
        imageView.setOnMouseClicked(e ->{
            makeResult("Scissors");
        });
        imageView.setOnMouseEntered(e ->{
            imageView.setImage(new Image(getClass().getResource("/PNG/RockPaperScissor/rockPaperScissorPNG/ScissorsLight.png").toExternalForm()));

        });
        imageView.setOnMouseExited(e ->{
            imageView.setImage(new Image(getClass().getResource("/PNG/RockPaperScissor/rockPaperScissorPNG/ScissorsNormal.png").toExternalForm()));
        });
        pane.getChildren().add(imageView);
    }

    private void createPaperObject(Pane pane) {
        Image image = new Image(getClass().getResource("/PNG/RockPaperScissor/rockPaperScissorPNG/PaperNormal.png").toExternalForm());
        ImageView imageView = new ImageView();
        imageView.setImage(image);
        imageView.setImage(image);
        imageView.setFitWidth(150);
        imageView.setFitHeight(150);
        imageView.setX(225);
        imageView.setY(217);
        imageView.setOnMouseClicked(e ->{
            makeResult("Paper");
        });
        imageView.setOnMouseEntered(e ->{
            imageView.setImage(new Image(getClass().getResource("/PNG/RockPaperScissor/rockPaperScissorPNG/PaperLight.png").toExternalForm()));

        });
        imageView.setOnMouseExited(e ->{
            imageView.setImage(new Image(getClass().getResource("/PNG/RockPaperScissor/rockPaperScissorPNG/PaperNormal.png").toExternalForm()));
        });
        pane.getChildren().add(imageView);
    }

    private void createRockObject(Pane pane) {
        Image image = new Image(getClass().getResource("/PNG/RockPaperScissor/rockPaperScissorPNG/RockNormal.png").toExternalForm());
        ImageView imageView = new ImageView();
        imageView.setImage(image);
        imageView.setImage(image);
        imageView.setFitWidth(150);
        imageView.setFitHeight(150);
        imageView.setX(37.5);
        imageView.setY(217);
        imageView.setOnMouseClicked(e ->{
            makeResult("Rock");
        });
        imageView.setOnMouseEntered(e ->{
            imageView.setImage(new Image(getClass().getResource("/PNG/RockPaperScissor/rockPaperScissorPNG/RockLight.png").toExternalForm()));

        });
        imageView.setOnMouseExited(e ->{
            imageView.setImage(new Image(getClass().getResource("/PNG/RockPaperScissor/rockPaperScissorPNG/RockNormal.png").toExternalForm()));
        });
        pane.getChildren().add(imageView);
    }

    private void createOpponentPaperObject(Pane pane) {
        Image image = new Image(getClass().getResource("/PNG/RockPaperScissor/rockPaperScissorPNG/PaperNormalOpponent.png").toExternalForm());
        ImageView imageView = new ImageView();
        imageView.setImage(image);
        imageView.setImage(image);
        imageView.setFitWidth(150);
        imageView.setFitHeight(150);
        imageView.setX(412.5);
        imageView.setY(33);
        pane.getChildren().add(imageView);
    }

    private void createOpponentRockObject(Pane pane) {
        Image image = new Image(getClass().getResource("/PNG/RockPaperScissor/rockPaperScissorPNG/RockNormalOpponent.png").toExternalForm());
        ImageView imageView = new ImageView();
        imageView.setImage(image);
        imageView.setImage(image);
        imageView.setFitWidth(150);
        imageView.setFitHeight(150);
        imageView.setX(225);
        imageView.setY(33);
        pane.getChildren().add(imageView);
    }

    private void createOpponentScissorObject(Pane pane) {
        Image image = new Image(getClass().getResource("/PNG/RockPaperScissor/rockPaperScissorPNG/ScissorsNormalOpponent.png").toExternalForm());
        ImageView imageView = new ImageView();
        imageView.setImage(image);
        imageView.setImage(image);
        imageView.setFitWidth(150);
        imageView.setFitHeight(150);
        imageView.setX(37.5);
        imageView.setY(33);
        pane.getChildren().add(imageView);
    }
}
