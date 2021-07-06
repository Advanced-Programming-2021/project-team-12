package view;

import Exceptions.MyException;
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
import models.*;

import java.io.File;
import java.util.HashMap;
import java.util.Objects;

public class GameView extends Application {
    private static Stage stage;
    private static GameView instance;
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
    public ImageView imageViewInfo;
    public Address selectedCardAddress;
    public ImageView[] turnHand = new ImageView[7];
    public ImageView[] turnMonsters = new ImageView[6];
    public ImageView[] turnSpells = new ImageView[6];
    public ImageView[] rivalHand = new ImageView[7];
    public ImageView[] rivalMonsters = new ImageView[6];
    public ImageView[] rivalSpells = new ImageView[6];
    public Button submitButton;
    public TextField messageFromPlayer;
    public Button drawPhase;
    public Button standbyPhase;
    public Button mainPhase1;
    public Button battlePhase;
    public Button mainPhase2;
    public Button endPhase;
    public Boolean sendData = false;
    public String firstInput;
    public String secondInput;
    public String thirdInput;
    public String tributeCard;

    public static GameView getInstance() {
        if (instance == null) {
            instance = new GameView();
        }
        return instance;
    }

    public void summonForTribute(int numberOfTributes, Address address) throws MyException {
        newMessageToLabel(Game.getCurrentPhase());
        if (!Game.isAITurn())
            addMessageToLabel("Select" + numberOfTributes + "monsters for tribute\nType numbers from monster zone from 1 to 5 and submit separately\nType 'cancel' to cancel the tribute");
        if (numberOfTributes == 1) PhaseControl.getInstance().summonAMediumLevelMonster(address);
        else if (numberOfTributes == 2) PhaseControl.getInstance().summonAHighLevelMonster(address);
        else if (numberOfTributes == 3) PhaseControl.getInstance().summonASuperHighLevelMonster(address);
    }

    public String scanForTribute(int i) {
        setStateOfSubmit(false);
        setButtonsActivate(true);
        newMessageToLabel(Game.getCurrentPhase());
        if (Game.isAITurn()){
            setStateOfSubmit(true);
            setButtonsActivate(false);
            return scanForAITribute(i);
        }
        else {
            addMessageToLabel("Select a monsters for tribute\nType a number from monster zone from 1 to 5\nType 'cancel' to cancel the tribute");
            tributeCard = null;
            submitButton.setOnMouseClicked(e ->{
                setTributeCard();
            });
            while (!(tributeCard.matches("[12345]{1}") && !(tributeCard.matches("cancel")))) {
                newMessageToLabel(Game.getCurrentPhase());
                addMessageToLabel("Incorrect input");
                addMessageToLabel("Select a monsters for tribute\nType a number from monster zone from 1 to 5\nType 'cancel' to cancel the tribute");
                submitButton.setOnMouseClicked(e ->{
                    setTributeCard();
                });
            }
            setStateOfSubmit(true);
            setButtonsActivate(false);
            return tributeCard;
        }
    }

    private void setTributeCard() {
        tributeCard = messageFromPlayer.getText();
    }

    private String scanForAITribute(int number) {
        Player player = Game.whoseTurnPlayer();
        int minAttack = 1000000;
        int place1 = 0;
        int place2 = 0;
        int place3 = 0;
        for (int i = 1; i < 6; i++) {
            if (player.getMonsterZoneCard().containsKey(i) && player.getCardMonster(i).getAttack() < minAttack) {
                minAttack = player.getCardMonster(i).getAttack();
                place1 = i;
            }
        }
        minAttack = 1000000;
        for (int i = 1; i < 6; i++) {
            if (player.getMonsterZoneCard().containsKey(i) && player.getCardMonster(i).getAttack() < minAttack && i != place1) {
                minAttack = player.getCardMonster(i).getAttack();
                place2 = i;
            }
        }
        minAttack = 1000000;
        for (int i = 1; i < 6; i++) {
            if (player.getMonsterZoneCard().containsKey(i) && player.getCardMonster(i).getAttack() < minAttack && i != place1 && i != place2) {
                minAttack = player.getCardMonster(i).getAttack();
                place3 = i;
            }
        }
        if (number == 1)
            return String.valueOf(place1);
        if (number == 2)
            return String.valueOf(place2);
        else
            return String.valueOf(place3);
    }

    public String runEffect(String cardName) {
        setButtonsActivate(true);
        setStateOfSubmit(false);
        GameView.getInstance().newMessageToLabel(Game.getCurrentPhase());
        if (Game.isAITurn()){
            setButtonsActivate(false);
            setStateOfSubmit(true);
            return Effect.AIEffect(cardName);
        }
        if (cardName.equals("Suijin"))
            addMessageToLabel("Do you want to use Suijin's effect?\nType 'yes' or 'no'");
        if (cardName.equals("Man-Eater Bug"))
            addMessageToLabel("Which rival's monster card you want to be destroyed because of the effect of Man Eater Bug?\nType monster zone number from 1 to 5");
        if (cardName.equals("Scanner"))
            addMessageToLabel("Which card do you want to be scanned from rival's graveyard for Scanner?\nType graveyard number");
        if (cardName.equals("Beast King Barbaros"))
            addMessageToLabel("How many monsters do you want to tribute for Beast King Barbaros?\nType 2 or 3");
        if (cardName.equals("Herald of Creation1"))
            addMessageToLabel("How many Herald Of Creation cards do you want to use?");
        if (cardName.equals("Herald of Creation2"))
            addMessageToLabel("Choose a card for tribute\nType monster zone number from 1 to 5");
        if (cardName.equals("Herald of Creation3"))
            addMessageToLabel("Choose a monster card with level 7 or more from your graveyard\ntype number");
        if (cardName.equals("Terratiger, the Empowered Warrior"))
            addMessageToLabel("Choose a monster card with level 4 or less from your hand!\nType a hand zone number from 1 to 6 or 0 if you want to cancel");
        if (cardName.equals("Terraforming"))
            addMessageToLabel("Type the name of the field spell card from your deck to come to your hand");
        if (cardName.equals("Twin Twisters")) {
            addMessageToLabel("Choose a card from your hand to remove and choose two of rival's trap or trap to be destroyed!\ntype 3 numbers and submit them separately");
        }
        if (cardName.equals("Mystical space typhoon"))
            addMessageToLabel("Choose one of rival's spell or trap to be destroyed\ntype a spell zone number from 1 to 5");
        if (!cardName.equals("Twin Twisters")) {
            int i = 1;
            while (i <= 3) {
                int finalI = i;
                submitButton.setOnMouseClicked(e -> {
                    if (finalI == 1) {
                        setFirstInput();
                    } else if (finalI == 2) {
                        setSecondInput();
                    } else if (finalI == 3) {
                        setThirdInput();
                        sendDataTrue();
                        setButtonsActivate(false);
                        setStateOfSubmit(true);
                    }
                });
                i++;
            }
            return firstInput + "," + secondInput + "," + thirdInput;
        } else {
            submitButton.setOnMouseClicked(e -> {
                sendDataTrue();
                setButtonsActivate(false);
                setStateOfSubmit(true);
            });
        }
        if (sendData = true) {
            sendData = false;
            return messageFromPlayer.getText();
        }
        return null;
    }

    private void setFirstInput() {
        firstInput = messageFromPlayer.getText();
    }

    private void setSecondInput() {
        secondInput = messageFromPlayer.getText();
    }

    private void setThirdInput() {
        thirdInput = messageFromPlayer.getText();
    }

    private void sendDataTrue() {
        sendData = true;
    }

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
        Game.setPhase("Draw Phase");
        setAvatar();
        setNickName();
        setLP();
        createImageView(turnHand, 260, 535, 6);
        createImageView(rivalHand, 260, -50, 6);
        createImageView(turnMonsters, 320, 300, 5);
        createImageView(rivalMonsters, 320, 190, 5);
        createImageView(turnSpells, 320, 420, 5);
        createImageView(rivalSpells, 320, 70, 5);
        setHand();
        initializeMouseClick();
        doDrawPhase(Game.whoseTurnPlayer());
    }

    private void setStateOfSubmit(boolean isDisable) {
        submitButton.setDisable(isDisable);
        messageFromPlayer.setDisable(isDisable);
    }

    public void reset() {
        setHand();
        setMonster();
        setSpells();
        setLP();
        setAvatar();
        setNickName();
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
                if (Y < 200 || Y > 450)
                    imageViewInfo.setImage(imageView[finalI].getImage());
                else if (Y < 350)
                    imageViewInfo.setImage(createImage(Game.whoseTurnPlayer().getCardMonster(finalI).getCardName()));
                else imageViewInfo.setImage(createImage(Game.whoseTurnPlayer().getCardSpell(finalI).getCardName()));
                imageViewInfo.setVisible(true);
            });
            imageView[i].setOnMouseExited(e -> {
                imageViewInfo.setVisible(false);
            });
            pane.getChildren().add(imageView[i]);
        }
    }

    private void initializeMouseClick() {
        for (int i = 1; i <= 6; i++) {
            setHandOnMouseClick(turnHand[i], i);
        }
        for (int i = 1; i <= 5; i++)
            setMonsterOnMouseClicked(turnMonsters[i], i);
    }

    private void setMonsterOnMouseClicked(ImageView monsterZoneCard, int i) {
        if (!monsterZoneCard.isVisible()) {
            monsterZoneCard.setOnMouseClicked(e -> {
                if (selectedCardAddress == null) {
                    setSelectedCard("monster", i, true);
                } else if (selectedCardAddress.getKind().equals("Hand")) {
                    if (e.getButton() == MouseButton.SECONDARY) {

                    }
                    if (monsterZoneCard.isVisible()) {
                        selectedCardAddress = new Address(i, "Monster", true);
                    } else {
//                    Address address =
//                            PhaseControl.getInstance().summonControl();
                    }
                }
            });
        } else {

        }
    }

    private void setSelectedCard(String zone, int i, Boolean isMine) {
        selectedCardAddress = new Address(i, zone, isMine);
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
        setStateOfSubmit(true);
        setButtonsActivate(false);
        Game.setPhase("Draw Phase");
        newMessageToLabel(Game.getCurrentPhase());
        reset();
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
        Game.setPhase("Standby Phase");
        newMessageToLabel(Game.getCurrentPhase());
        reset();
        if (SetSpell.doIHaveMessengerOfPeace()) {
            addMessageToLabel("Do you want to destroy Messenger Of Peace(If not you'll lose 100 LP)?\nEnter 'yes' or 'no'");
            setStateOfSubmit(false);
            setButtonsActivate(true);
            submitButton.setOnMouseClicked(e -> {
                newMessageToLabel("Standby Phase");
                if (!messageFromPlayer.getText().equals("yes") && !messageFromPlayer.getText().equals("no")) {
                    addMessageToLabel("Incorrect input\nDo you want to destroy Messenger Of Peace(If not you'll lose 100 LP)?\nEnter 'yes' or 'no'");
                    setStateOfSubmit(false);
                    setButtonsActivate(true);
                } else if (messageFromPlayer.getText().equals("yes")) {
                    addMessageToLabel("Messenger Of Peace was destroyed");
                    setStateOfSubmit(true);
                    setButtonsActivate(false);
                } else if (messageFromPlayer.getText().equals("no")) {
                    addMessageToLabel("You lost 100 LP because of Messenger Of Peace");
                    setStateOfSubmit(true);
                    setButtonsActivate(false);
                }
                PhaseControl.getInstance().payMessengerOfPeaceSpellCardHarm(messageFromPlayer.getText());
                messageFromPlayer.setText("");
                reset();
            });
        }
        reset();
        PhaseControl.getInstance().resetMoves();
        PhaseControl.getInstance().checkIfGameEnded();
        reset();
    }

    private void setButtonsActivate(boolean activate) {
        for (int i = 1; i <= 5; i++) {
            turnMonsters[i].setDisable(activate);
            rivalMonsters[i].setDisable(activate);
            turnHand[i].setDisable(activate);
            rivalHand[i].setDisable(activate);
            turnSpells[i].setDisable(activate);
            rivalSpells[i].setDisable(activate);
        }
        turnHand[6].setDisable(activate);
        rivalHand[6].setDisable(activate);
        battlePhase.setDisable(activate);
        mainPhase1.setDisable(activate);
        mainPhase2.setDisable(activate);
        drawPhase.setDisable(activate);
        endPhase.setDisable(activate);
        standbyPhase.setDisable(activate);
    }

    private void doMainPhase1() {
        setStateOfSubmit(true);
        setButtonsActivate(false);
        Game.setPhase("Main Phase 1");
        newMessageToLabel(Game.getCurrentPhase());
        Game.getMainPhase2().setHowManyHeraldOfCreationDidWeUseEffect(0);
        PhaseControl.getInstance().doEffectMainPhase();
        reset();
    }

    private void doBattlePhase() {
        setStateOfSubmit(true);
        setButtonsActivate(false);
        Game.setPhase("Battle Phase");
        newMessageToLabel(Game.getCurrentPhase());
        reset();
    }

    private void doMainPhase2() {
        setStateOfSubmit(true);
        setButtonsActivate(false);
        Game.setPhase("Main Phase 2");
        newMessageToLabel(Game.getCurrentPhase());
        reset();
    }

    private void doEndPhase() throws Exception {
        Game.setPhase("End Phase");
        newMessageToLabel(Game.getCurrentPhase());
        reset();
        PhaseControl.getInstance().doEffectEndPhase();
        if (Game.whoseTurnPlayer().howManyCardIsInTheHandCard() == 6) {
            reset();
            addMessageToLabel("Select a card to be deleted from your hand\nEnter a number from 1 to 6");
            setStateOfSubmit(false);
            setButtonsActivate(true);
            submitButton.setOnMouseClicked(e -> {
                if (!messageFromPlayer.getText().matches("[123456]")) {
                    newMessageToLabel("Incorrect input\nSelect a card to be deleted from your hand\nEnter a number from 1 to 6");
                    messageFromPlayer.setText("");
                    setStateOfSubmit(false);
                    setButtonsActivate(true);
                    reset();
                } else {
                    Address address = new Address(Integer.parseInt(messageFromPlayer.getText()), "hand", true);
                    Game.whoseTurnPlayer().removeCard(address);
                    newMessageToLabel("Card has been successfully removed from hand");
                    messageFromPlayer.setText("");
                    reset();
                    PhaseControl.getInstance().checkIfGameEnded();
                    PhaseControl.getInstance().switchPlayerTurn();
                    setStateOfSubmit(true);
                    setButtonsActivate(false);
                    reset();
                    try {
                        doDrawPhase(Game.whoseTurnPlayer());
                    } catch (Exception exception) {
                        exception.printStackTrace();
                    }
                }
                reset();
            });
        } else {
            reset();
            PhaseControl.getInstance().checkIfGameEnded();
            PhaseControl.getInstance().switchPlayerTurn();
            doDrawPhase(Game.whoseTurnPlayer());
        }
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

    public void newMessageToLabel(String newMessage) {
        messageBox.setText(newMessage);
    }

    public void addMessageToLabel(String message) {
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
        for (int i = 1; i <= 6; i++) {
            handsImage(Game.whoseTurnPlayer(), turnHand, i);
        }
        for (int i = 1; i <= 6; i++)
            handsImage(Game.whoseRivalPlayer(), rivalHand, i);
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

    private void handsImage(Player player, ImageView[] hand, int i) {
        if (player.getHandCard().containsKey(i)) {
            if (player.getName().equals(Game.whoseTurnPlayer().getName()))
                hand[i].setImage(createImage(player.getCardHand(i).getCardName()));
            else
                hand[i].setImage(new Image(getClass().getResource("/PNG/Cards1/Unknown.jpg").toExternalForm()));
            hand[i].setVisible(true);
            hand[i].setDisable(false);
        } else {
            hand[i].setVisible(false);
            hand[i].setDisable(true);
        }
    }

    private void monstersImage(Player player, ImageView[] monsters, int i) {
        if (player.getMonsterZoneCard().containsKey(i)) {
            monsters[i].setImage(createImage(player.getCardMonster(i).getCardName()));
            if (player.getMonsterPosition(i).equals(PositionOfCardInBoard.DH))
                monsters[i].setImage(new Image(getClass().getResource("/PNG/Cards1/Unknown.jpg").toExternalForm()));
            monsters[i].setRotate(90);
            if (player.getMonsterPosition(i).equals(PositionOfCardInBoard.OO))
                monsters[i].setRotate(0);
            monsters[i].setVisible(true);
            monsters[i].setDisable(false);
        } else {
            monsters[i].setVisible(false);
            monsters[i].setDisable(true);
        }
    }

    private void spellsImage(Player player, ImageView[] spells, int i) {
        if (player.getSpellZoneCard().containsKey(i)) {
            spells[i].setImage(createImage(player.getCardSpell(i).getCardName()));
            if (!player.isSpellFaceUp(i))
                spells[i].setImage(new Image(getClass().getResource("/PNG/Cards1/Unknown.jpg").toExternalForm()));
            spells[i].setVisible(true);
            spells[i].setDisable(false);
        } else {
            spells[i].setVisible(false);
            spells[i].setDisable(true);
        }
    }

    private void setHandOnMouseClick(ImageView handCard, int i) {
        handCard.setOnMouseClicked(e -> {
            setSelectedCard("hand", i, true);
            if (e.getButton() == MouseButton.SECONDARY) {
                try {
                    if (Board.getCardByAddress(selectedCardAddress).getKind().equals("Spell"))
                        PhaseControl.getInstance().spellSet(selectedCardAddress);
                    else if (Board.getCardByAddress(selectedCardAddress).getKind().equals("Monster"))
                        PhaseControl.getInstance().monsterSet(selectedCardAddress);
                    else if (Board.getCardByAddress(selectedCardAddress).getKind().equals("Trap"))
                        PhaseControl.getInstance().trapSet(selectedCardAddress);
                    reset();
                } catch (MyException myException) {
                    newMessageToLabel(Game.getCurrentPhase());
                    addMessageToLabel(myException.getMessage());
                }
            } else {
                try {
                    if (Board.getCardByAddress(selectedCardAddress).getKind().equals("Spell"))
                        PhaseControl.getInstance().activeSpell(selectedCardAddress);
                    else if (Board.getCardByAddress(selectedCardAddress).getKind().equals("Monster"))
                        PhaseControl.getInstance().summonControl(selectedCardAddress);
                    else if (Board.getCardByAddress(selectedCardAddress).getKind().equals("Trap"))
                        PhaseControl.getInstance().trapSet(selectedCardAddress);
                    reset();
                } catch (MyException myException) {
                    newMessageToLabel(Game.getCurrentPhase());
                    addMessageToLabel(myException.getMessage());
                }
            }
            reset();
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
        if (Game.getCurrentPhase().equals("Draw Phase")) {
            doStandByPhase();
        }
    }

    public void goToMainPhaseOne(ActionEvent actionEvent) {
        if (Game.getCurrentPhase().equals("Draw Phase") || Game.getCurrentPhase().equals("Standby Phase")) {
            doMainPhase1();
        }
    }

    public void goToBattlePhase(ActionEvent actionEvent) {
        if (Game.getCurrentPhase().equals("Draw Phase") || Game.getCurrentPhase().equals("Standby Phase") || Game.getCurrentPhase().equals("Main Phase 1")) {
            doBattlePhase();
        }
    }

    public void goToMainPhaseTwo(ActionEvent actionEvent) {
        if (Game.getCurrentPhase().equals("Draw Phase") || Game.getCurrentPhase().equals("Standby Phase") || Game.getCurrentPhase().equals("Main Phase 1")
                || Game.getCurrentPhase().equals("Battle Phase")) {
            doMainPhase2();
        }
    }

    public void goToEndPhase(ActionEvent actionEvent) throws Exception {
        if (Game.getCurrentPhase().equals("Draw Phase") || Game.getCurrentPhase().equals("Standby Phase") || Game.getCurrentPhase().equals("Main Phase 1")
                || Game.getCurrentPhase().equals("Battle Phase") || Game.getCurrentPhase().equals("Main Phase 2")) {
            doEndPhase();
        }
    }
}
