package view;

import controllers.Game;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
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
import java.util.HashMap;
import java.util.Objects;

public class GameView extends Application {
    private static Stage stage;
    public Pane pane;

    @Override
    public void start(Stage primaryStage) throws Exception {
        stage = primaryStage;
        pane = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/fxml/GameView.fxml")));
        createBackground("normal");
        setHand(Game.whoseRivalPlayer(), true);
        setHand(Game.whoseTurnPlayer(), false);
        createBackgroundCards();
        createPlayers();
        Scene scene = new Scene(pane);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    public void Initialize() {
        setAvatar();
        setLP();
        setLineLP();
    }

    private void setLP() {

    }

    private void setAvatar() {
        createAvatarImage(Game.whoseTurnPlayer(), false);
        createAvatarImage(Game.whoseRivalPlayer(), true);
    }

    private Label createLPLabel(Player player, boolean isRival) {
        Label label = new Label();
        label.setText("LP: " + player.getLP());
        label.setFont(Font.font("Yu-Gi-Oh! StoneSerif LT", FontWeight.BOLD, 16));
        label.setLayoutX();
        label.setLayoutY();
    }

    private ImageView createAvatarImage(Player player, boolean isRival) {
        File file =  new File(player.getUser().getAvatarAddress());
        String string = file.toURI().toString();
        Image image = new Image(string);
        ImageView imageView = new ImageView(image);
        imageView.setLayoutX(880);
        imageView.setLayoutY(385);
        imageView.setFitWidth(120);
        imageView.setFitHeight(130);
        if (isRival)
            imageView.setLayoutY(100);
        return imageView;
    }

    private void setHand(Player player, Boolean isRival) {
        for (int i = 1; i < 6; i++) {
            HashMap<Integer, Card> handCards = player.getHandCard();
            if (handCards.containsKey(i)) {
                ImageView imageView;
                if (!isRival) {
                    imageView = createImage(handCards.get(i).getCardName(), 260 + 80 * (i - 1), 535, 75, 100);
                } else {
                    imageView = createImage("Unknown", 260 + 80 * (i - 1), -50, 75, 100);
                }
                int finalI = i;
                imageView.setOnMouseEntered(e -> {
                    ImageView imageViewInfo;
                    if (!isRival) {
                        imageViewInfo = createImage(handCards.get(finalI).getCardName(), 7, 152, 205, 296);
                    } else {
                        imageViewInfo = createImage("Unknown", 7, 152, 205, 296);
                    }
                    pane.getChildren().add(imageViewInfo);
                });
                imageView.setOnMouseExited(e -> {
                    removeCard(7, 152);
                });
                pane.getChildren().add(imageView);
            }
        }
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
