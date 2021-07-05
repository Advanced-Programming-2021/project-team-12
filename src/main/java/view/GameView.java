package view;

import controllers.Game;
import controllers.PhaseControl;
import controllers.move.SetSpell;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import models.Address;
import models.Card;
import models.Player;
import models.PositionOfCardInBoard;

import java.io.File;
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
    public ImageView selectedCard;
    public String selectedCardZone;
    public ImageView turnAvatar;
    public ImageView rivalAvatar;
    public ImageView imageViewInfo;
    public ImageView[] turnHand = new ImageView[7];
    public ImageView[] turnMonsters = new ImageView[6];
    public ImageView[] turnSpells = new ImageView[6];
    public ImageView[] rivalHand = new ImageView[7];
    public ImageView[] rivalMonsters = new ImageView[6];
    public ImageView[] rivalSpells = new ImageView[6];
    public Button submitButton;
    public TextField messageFromPlayer;

    @Override
    public void start(Stage primaryStage) throws Exception {
        stage = primaryStage;
        pane = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/fxml/GameView.fxml")));
        createBackground("normal");
        createBackgroundCards();
        createPlayers();
        Scene scene = new Scene(pane);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    public void initialize() throws Exception {
        setAvatar();
        setNickName();
        setLP();
        createImageView(turnHand, 260, 535, 6);
        createImageView(rivalHand, 260, -50, 6);
        createImageView(turnMonsters, 300, 300, 5);
        createImageView(rivalMonsters, 300, 190, 5);
        createImageView(turnSpells, 300, 420, 5);
        createImageView(rivalSpells, 300, 70, 5);
        setHand();
        setMonsterOnMouseClicked(turnMonsters);
        doDrawPhase(Game.whoseTurnPlayer());
    }

    private void createImageView(ImageView[] imageView, int X, int Y, int size) {
        for (int i = 1; i <= size; i++) {
            imageView[i] = new ImageView();
            imageView[i].setFitWidth(75);
            imageView[i].setFitHeight(100);
            imageView[i].setPreserveRatio(false);
            imageView[i].setLayoutX(X + 80 * (i - 1));
            imageView[i].setLayoutY(Y);
            int finalI = i;
            imageView[i].setOnMouseEntered(e -> {
                imageViewInfo.setImage(imageView[finalI].getImage());
                imageViewInfo.setVisible(true);
//                if (!isRival)
//                    imageViewInfo = createImage(Game.whoseTurnPlayer().getHandCard().get(finalI).getCardName(), 7, 152, 205, 296);
//                else
//                    imageViewInfo = createImage("Unknown", 7, 152, 205, 296);
//                pane.getChildren().add(imageViewInfo);
            });
            imageView[i].setOnMouseExited(e -> {
                imageViewInfo.setVisible(false);
//                removeCard(7, 152);
            });
            pane.getChildren().add(imageView[i]);
        }
    }

    private void setMonsterOnMouseClicked(ImageView monsterZoneCard) {
        monsterZoneCard.setOnMouseClicked(e -> {
            if (selectedCard == null) {
                setSelectedCard("Monster", monsterZoneCard);
                selectedCardZone = "Monster";
                selectedCard = monsterZoneCard;
            } else if (selectedCardZone.equals("Hand")) {
                if (e.getButton() == MouseButton.SECONDARY) {
                    System.out.println("hhhhh");
                }
                if (monsterZoneCard.isVisible()) {
                    selectedCardZone = "Monster";
                    selectedCard = monsterZoneCard;
                } else {
//                    Address address =
//                            PhaseControl.getInstance().summonControl();
                }
            }
        });
    }

    private void setSelectedCard(String zone, ImageView monsterZoneCard) {
        selectedCard = monsterZoneCard;
        selectedCardZone = zone;
    }

    private void setNickName() {
        turnName.setText("Name: " + Game.whoseTurnPlayer().getNickName());
        rivalName.setText("Name: " + Game.whoseRivalPlayer().getNickName());
    }

    private void setLP() {
        turnLP.setText("LP: " + Game.whoseTurnPlayer().getLP());
        rivalLP.setText("LP: " + Game.whoseRivalPlayer().getLP());
        turnProgress.setProgress((double) Game.whoseTurnPlayer().getLP() / 8000);
        rivalProgress.setProgress((double) Game.whoseRivalPlayer().getLP() / 8000);
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

    private void doDrawPhase(Player player) throws Exception {
        newMessageToLabel("Draw Phase");
        addMessageToLabel(Game.whoseTurnPlayer().getNickName() + "'s turn");
        Game.setDidWePassBattle(false);
        String drawCardMessage = PhaseControl.getInstance().drawOneCard();
        addMessageToLabel(drawCardMessage);
        if (drawCardMessage.startsWith("GAME"))
            Game.playTurn("EndGame");
        else if (!drawCardMessage.equals("You can't draw a card because of rival's Time Seal"))
            drawCardFromDeckToHand(player, false);
    }

    private void doStandByPhase() {
        newMessageToLabel("Standby Phase");
        if (SetSpell.doIHaveMessengerOfPeace()) {
            addMessageToLabel("Do you want to destroy Messenger Of Peace(If not you'll lose 100 LP)?\nEnter 'yes' or 'no'");
            submitButton.setOnMouseClicked(e -> {
                newMessageToLabel("Standby Phase");
                if (!messageFromPlayer.getText().equals("yes") && !messageFromPlayer.getText().equals("no")) {
                    addMessageToLabel("Incorrect input\nDo you want to destroy Messenger Of Peace(If not you'll lose 100 LP)?\nEnter 'yes' or 'no'");
                } else if (messageFromPlayer.getText().equals("yes")) {
                    addMessageToLabel("Messenger Of Peace was destroyed");
                } else if (messageFromPlayer.getText().equals("no")) {
                    addMessageToLabel("You lost 100 LP because of Messenger Of Peace");
                }
                PhaseControl.getInstance().payMessengerOfPeaceSpellCardHarm(messageFromPlayer.getText());
                messageFromPlayer.setText("");
            });
        }
        PhaseControl.getInstance().resetMoves();
        PhaseControl.getInstance().checkIfGameEnded();
    }

    private void doMainPhase1() {

    }

    private void doBattlePhase() {
    }

    private void doMainPhase2() {
    }

    private void doEndPhase() throws Exception {
        newMessageToLabel("End Phase");
        PhaseControl.getInstance().doEffectEndPhase();
        if (Game.whoseTurnPlayer().howManyCardIsInTheHandCard() == 6) {
            addMessageToLabel("Select a card to be deleted from your hand\nEnter a number from 1 to 6");
            submitButton.setOnMouseClicked(e -> {
                newMessageToLabel("Standby Phase");
                if (!messageFromPlayer.getText().matches("[123456]")) {
                    newMessageToLabel("Incorrect input\nSelect a card to be deleted from your hand\nEnter a number from 1 to 6");
                } else {
                    Address address = new Address(Integer.parseInt(messageFromPlayer.getText()), "hand", true);
                    Game.whoseTurnPlayer().removeCard(address);
                    setHand();
                    newMessageToLabel("Card has been successfully removed from hand");
                }
                messageFromPlayer.setText("");
            });
        }
        PhaseControl.getInstance().checkIfGameEnded();
        //PhaseControl.getInstance().switchPlayerTurn();
        //doDrawPhase(Game.whoseTurnPlayer(), false);
    }

    private void drawCardFromDeckToHand(Player player, Boolean isRival) {
        HashMap<Integer, Card> handCards = player.getHandCard();
        for (int i = 1; i <= 6; i++) {
            if (handCards.containsKey(i) && (!handCards.containsKey(i + 1))) {
                ImageView ImageView;
                if (!isRival) {
                    ImageView = createImage(handCards.get(i).getCardName(), 260 + 80 * (i - 1), 535, 75, 100);
                } else {
                    ImageView = createImage("Unknown", 260 + 80 * (i - 1), -50, 75, 100);
                }
                int finalI = i;
                ImageView.setOnMouseEntered(e -> {
                    ImageView imageViewInfo;
                    if (!isRival) {
                        imageViewInfo = createImage(handCards.get(finalI).getCardName(), 7, 152, 205, 296);
                    } else {
                        imageViewInfo = createImage("Unknown", 7, 152, 205, 296);
                    }
                    pane.getChildren().add(imageViewInfo);
                });
                ImageView.setOnMouseExited(e -> {
                    removeCard(7, 152);
                });
                pane.getChildren().add(ImageView);
            }
        }
    }

    private void newMessageToLabel(String newMessage) {
        messageBox.setText(newMessage);
    }

    private void addMessageToLabel(String message) {
        String oldMessage = messageBox.getText();
        String newMessage = oldMessage + "\n" + message;
        messageBox.setText(newMessage);
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
            handsImage(Game.whoseTurnPlayer(), turnHand, i);
        for (int i = 1; i <=6; i++)
            handsImage(Game.whoseRivalPlayer(), rivalHand , i);
    }

    public void setSpells() {
        Player turnPlayer = Game.whoseTurnPlayer();
        Player rivalPlayer = Game.whoseRivalPlayer();
        for (int i = 1; i <= 5; i++)
            spellsImage(turnPlayer, turnSpells, i);
        for (int i = 1; i <= 5; i++)
            spellsImage(rivalPlayer, rivalSpells, i);
    }

    public void setMonster() {
        Player turnPlayer = Game.whoseTurnPlayer();
        Player rivalPlayer = Game.whoseRivalPlayer();
        for (int i = 1; i <= 5; i++)
            monstersImage(turnPlayer, turnMonsters, i);
        for (int i = 1; i <= 5; i++)
            monstersImage(rivalPlayer, rivalMonsters, i);
    }

    private void handsImage(Player player, ImageView[] hand , int i) {
        if (player.getHandCard().containsKey(i)) {
            if (player.getName().equals(Game.whoseTurnPlayer().getName()))
                hand[i].setImage(createImage(player.getCardHand(i).getCardName()));
            else
                hand[i].setImage(new Image(getClass().getResource("/PNG/Cards1/Unknown.jpg").toExternalForm()));
            
            hand[i].setVisible(true);
            hand[i].setDisable(false);
        }
        else {
            hand[i].setVisible(false);
            hand[i].setDisable(true);
        }
    }

    private void monstersImage(Player player, ImageView[] monsters, int i) {
        if (player.getMonsterZoneCard().containsKey(i)) {
            monsters[i].setImage(createImage(player.getCardMonster(i).getCardName()));
            if (player.getMonsterPosition(i).equals(PositionOfCardInBoard.DH) && player.getName().equals(Game.whoseRivalPlayer().getName()))
                monsters[i].setImage(new Image(getClass().getResource("/PNG/Cards1/Unknown.jpg").toExternalForm()));
            monsters[i].setRotate(90);
            if (player.getMonsterPosition(i).equals(PositionOfCardInBoard.OO))
                monsters[i].setRotate(0);
            monsters[i].setVisible(true);
            monsters[i].setDisable(false);
        }
        else {
            monsters[i].setVisible(false);
            monsters[i].setDisable(true);
        }
    }

    private void spellsImage(Player player, ImageView[] spells, int i) {
        if (player.getSpellZoneCard().containsKey(i)) {
            spells[i].setImage(createImage(player.getCardSpell(i).getCardName()));
            if (!player.isSpellFaceUp(i) && player.getName().equals(Game.whoseRivalPlayer().getName()))
                spells[i].setImage(new Image(getClass().getResource("/PNG/Cards1/Unknown.jpg").toExternalForm()));
            spells[i].setVisible(true);
            spells[i].setDisable(false);
        }
        else {
            spells[i].setVisible(false);
            spells[i].setDisable(true);
        }
    }

    private void setHandOnMouseClick(ImageView handCard) {
        handCard.setOnMouseClicked(e -> {
            setSelectedCard("hand", handCard);
        });
    }

    private Image createImage(String cardName) {
        return new Image(getClass().getResource("/PNG/Cards1/" + cardName + ".jpg").toExternalForm());
    }

    private void removeCard(int layoutX, int layoutY) {
        for (int i = 0; i < pane.getChildren().size(); i++) {
            if (pane.getChildren().get(i).getLayoutX() == layoutX && pane.getChildren().get(i).getLayoutY() == layoutY) {
                pane.getChildren().remove(i);
            }
        }
    }

    private ImageView createImage(String cardName, int X, int Y, int layoutX, int layoutY) {
        Image image = new Image(getClass().getResource("/PNG/Cards1/" + cardName + ".jpg").toExternalForm());
        ImageView imageView = new ImageView();
        imageView.setImage(image);
        imageView.setFitHeight(layoutY);
        imageView.setFitWidth(layoutX);
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
        if (!Game.getCurrentPhase().equals("DrawPhase") && (!Game.getCurrentPhase().equals("StandByPhase"))) {
            doStandByPhase();
        }
    }

    public void goToMainPhaseOne(ActionEvent actionEvent) {
        if (!Game.getCurrentPhase().equals("DrawPhase") && (!Game.getCurrentPhase().equals("StandByPhase") && (!Game.getCurrentPhase().equals("MainPhase1")))) {
            doMainPhase1();
        }
    }

    public void goToBattlePhase(ActionEvent actionEvent) {
        if (!Game.getCurrentPhase().equals("DrawPhase") && (!Game.getCurrentPhase().equals("StandByPhase") && (!Game.getCurrentPhase().equals("MainPhase1")
                && !Game.getCurrentPhase().equals("BattlePhase")))) {
            doBattlePhase();
        }
    }

    public void goToMainPhaseTwo(ActionEvent actionEvent) {
        if (!Game.getCurrentPhase().equals("DrawPhase") && (!Game.getCurrentPhase().equals("StandByPhase") && (!Game.getCurrentPhase().equals("MainPhase1")
                && !Game.getCurrentPhase().equals("BattlePhase") && !Game.getCurrentPhase().equals("MainPhase2")))) {
            doMainPhase2();
        }
    }

    public void goToEndPhase(ActionEvent actionEvent) throws Exception {
        if (!Game.getCurrentPhase().equals("DrawPhase") && (!Game.getCurrentPhase().equals("StandByPhase") && (!Game.getCurrentPhase().equals("MainPhase1")
                && !Game.getCurrentPhase().equals("BattlePhase") && !Game.getCurrentPhase().equals("MainPhase2")))) {
            doEndPhase();
        }
    }
}
