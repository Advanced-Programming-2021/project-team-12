package view;

import controllers.Game;
import controllers.PhaseControl;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import models.Card;
import models.Player;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class GameView extends Application {
    private static Stage stage;
    public Pane pane;
    public Button messageBox;
    public ProgressBar turnProgress;
    public ProgressBar rivalProgress;
    public Label turnName;
    public Label rivalName;
    public Label turnLP;
    public Label rivalLP;
    public ImageView turnAvatar;
    public ImageView rivalAvatar;
    public ImageView[] turnHand = new ImageView[7];
    public ImageView[] turnMonsters = new ImageView[6];
    public ImageView[] turnSpells = new ImageView[6];
    public ImageView[] rivalHand = new ImageView[7];
    public ImageView[] rivalMonsters = new ImageView[6];
    public ImageView[] rivalSpells = new ImageView[6];

    @Override
    public void start(Stage primaryStage) throws Exception {
        stage = primaryStage;
        pane = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/fxml/GameView.fxml")));
        createBackground("normal");
//        setHand(Game.whoseRivalPlayer(), true);
//        setHand(Game.whoseTurnPlayer(), false);
        createBackgroundCards();
        createPlayers();
        Scene scene = new Scene(pane);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    public void initialize() throws Exception {
        doDrawPhase();
        setAvatar();
        setNickName();
        setProgresses();
        createHandImageView(turnHand, false);
        createHandImageView(rivalHand, true);
        setHand();
    }

    private void createHandImageView(ImageView[] imageView, boolean isRival) {
        for (int i = 1; i <= 6; i++) {
            imageView[i] = new ImageView();
            imageView[i].setFitWidth(75);
            imageView[i].setFitHeight(100);
            imageView[i].setPreserveRatio(false);
            imageView[i].setLayoutX(260 + 80 * (i - 1));
            imageView[i].setLayoutY(535);
            if (isRival)
                imageView[i].setLayoutY(-50);
            int finalI = i;
            imageView[i].setOnMouseEntered(e -> {
                ImageView imageViewInfo;
                if (!isRival)
                    imageViewInfo = createImage(Game.whoseTurnPlayer().getHandCard().get(finalI).getCardName(), 7, 152, 205, 296);
                else
                    imageViewInfo = createImage("Unknown", 7, 152, 205, 296);
                pane.getChildren().add(imageViewInfo);
            });
            imageView[i].setOnMouseExited(e -> {
                removeCard(7, 152);
            });
            pane.getChildren().add(imageView[i]);
        }
    }

    private void setNickName() {
        turnName.setText("Name: " + Game.whoseTurnPlayer().getNickName());
        rivalName.setText("Name: " + Game.whoseRivalPlayer().getNickName());
    }

    private void setProgresses() {
        turnProgress.setProgress(Game.whoseTurnPlayer().getLP() / 8000);
        rivalProgress.setProgress(Game.whoseRivalPlayer().getLP() / 8000);
    }

    private void setLP() {
        turnLP.setText("LP: " + Game.whoseTurnPlayer().getLP());
        rivalLP.setText("LP: " + Game.whoseRivalPlayer().getLP());
    }

    private void setAvatar() {
        turnAvatar.setImage(createAvatarImage(Game.whoseTurnPlayer()));
        rivalAvatar.setImage(createAvatarImage(Game.whoseRivalPlayer()));
    }

    private Image createAvatarImage(Player player) {
        File file = new File(player.getUser().getAvatarAddress());
        String string = file.toURI().toString();
        return new Image(string);
    }

    private void doDrawPhase() throws Exception {
        Game.setDidWePassBattle(false);
        String drawCardMessage = PhaseControl.getInstance().drawOneCard();
        addMessageToLabel(drawCardMessage);
        if(drawCardMessage.equals("first player cannot draw a card, second player is the winner")){
            Game.playTurn("EndGame");
        } else if(drawCardMessage.equals("second player cannot draw a card, first player is the winner")){
            Game.playTurn("EndGame");
        }
    }

    private void addMessageToLabel(String message) {
        messageBox.setText(message);
    }

//    private void setHand(Player player, Boolean isRival) {
//        for (int i = 1; i <= 6; i++) {
//            HashMap<Integer, Card> handCards = player.getHandCard();
//            if (handCards.containsKey(i)) {
//                ImageView imageView;
//                if (!isRival) {
//                    imageView = createImage(handCards.get(i).getCardName(), 260 + 80 * (i - 1), 535, 75, 100);
//                } else {
//                    imageView = createImage("Unknown", 260 + 80 * (i - 1), -50, 75, 100);
//                }
//                int finalI = i;
//                imageView.setOnMouseEntered(e -> {
//                    ImageView imageViewInfo;
//                    if (!isRival) {
//                        imageViewInfo = createImage(handCards.get(finalI).getCardName(), 7, 152, 205, 296);
//                    } else {
//                        imageViewInfo = createImage("Unknown", 7, 152, 205, 296);
//                    }
//                    pane.getChildren().add(imageViewInfo);
//                });
//                imageView.setOnMouseExited(e -> {
//                    removeCard(7, 152);
//                });
//                if (isRival)
//                    rivalHand[i] = imageView;
//                else turnHand[i] = imageView;
//                pane.getChildren().add(imageView);
//            }
//        }
//    }

    public void setHand() {
        for (int i = 1; i <= 6; i++)
            if (Game.whoseTurnPlayer().getHandCard().containsKey(i))
                turnHand[i].setImage(createImage(Game.whoseTurnPlayer().getCardHand(i).getCardName()));
        for (int i = 1; i <=6; i++)
            if (Game.whoseRivalPlayer().getHandCard().containsKey(i))
                rivalHand[i].setImage(new Image(getClass().getResource("/PNG/Cards1/Unknown.jpg").toExternalForm()));
    }

    private Image createImage(String cardName) {
        return new Image(getClass().getResource("/PNG/Cards1/" + cardName + ".jpg").toExternalForm());
    }

    private void removeCard(int layoutX, int layoutY) {
        for (int i = 0; i < pane.getChildren().size(); i++) {
            if(pane.getChildren().get(i).getLayoutX() == layoutX && pane.getChildren().get(i).getLayoutY() == layoutY){
                pane.getChildren().remove(i);
            }
        }
    }

    private ImageView createImage(String cardName, int X, int Y, int layoutX, int layoutY) {
        Image image = new Image(getClass().getResource("/PNG/Cards1/" + cardName + ".jpg").toExternalForm());
        ImageView imageView = new ImageView();
        imageView.setImage(image);
        imageView.setImage(image);
        imageView.setFitWidth(layoutX);
        imageView.setFitHeight(layoutY);
        imageView.setPreserveRatio(false);
        imageView.setLayoutX(X);
        imageView.setLayoutY(Y);
        return imageView;
    }

    private void createBackgroundCards() {
//        Image image = new Image(getClass().getResource("/PNG/Cards/Monsters/Unknown.jpg").toExternalForm());
//        ImageView imageView = new ImageView();
//        imageView.setImage(image);
//        imageView.setImage(image);
//        imageView.setFitWidth(600.0);
//        imageView.setFitHeight(600.0);
//        imageView.setPreserveRatio(false);
//        imageView.setX(200);
//        imageView.setY(0);
//        pane.getChildren().add(imageView);
    }

    private void createPlayers() {
//        File pictureFile =  new File(.getAvatarAddress());
//        String string = pictureFile.toURI().toString();
//        Image image = new Image(getClass().getResource(string).toExternalForm());
//        ImageView imageView = new ImageView();
//        imageView.setImage(image);
//        imageView.setImage(image);
//        imageView.setFitWidth(600.0);
//        imageView.setFitHeight(600.0);
//        imageView.setPreserveRatio(false);
//        imageView.setX(200);
//        imageView.setY(0);
    }

    private void createBackground(String type) {
//        Image image = new Image(getClass().getResource("/PNG/Field/FieldPNG/fie_" + type + ".png").toExternalForm());
//        ImageView imageView = new ImageView();
//        imageView.setImage(image);
//        imageView.setImage(image);
//        imageView.setFitWidth(600.0);
//        imageView.setFitHeight(600.0);
//        imageView.setPreserveRatio(false);
//        imageView.setX(200);
//        imageView.setY(0);
//        pane.getChildren().add(imageView);
    }

    public void checkSurrender(MouseEvent mouseEvent) {

    }

    public void goToDrawPhase(ActionEvent actionEvent) {
    }

    public void goToStandByPhase(ActionEvent actionEvent) {
    }

    public void goToMainPhaseOne(ActionEvent actionEvent) {
    }

    public void goToBattlePhase(ActionEvent actionEvent) {
    }

    public void goToMainPhaseTwo(ActionEvent actionEvent) {
    }

    public void goToEndPhase(ActionEvent actionEvent) {
    }
}
