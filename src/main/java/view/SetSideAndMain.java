package view;

import Exceptions.MyException;
import Utility.Sounds;
import controllers.Game;
import controllers.SetMainAndSideController;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import models.*;

import java.io.File;
import java.util.ArrayList;
import java.util.Objects;

public class SetSideAndMain extends Application {
    public static Stage stage;
    private static User user;
    private static Player player;
    public Pane pane;
    public Integer selectedMain;
    public Integer selectedSide;
    public Label msg;
    public Label deckName;
    public Label mainNumber;
    public Label sideNumber;
    public Label userName;
    private static String scoreAndWinnerData;
    public Label scoreAndWinner;
    private ArrayList<Card> mainCards;
    private ArrayList<Card> sideCards;
    private final Button[][] mainButtons = new Button[40][25];
    private final Button[][] sideButtons = new Button[10][25];
    private Address dragAddress;

    @Override
    public void start(Stage stage) throws Exception {
        SetSideAndMain.stage = stage;
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/fxml/SetSideAndMain.fxml")));
        stage.setScene(new Scene(root));
        stage.show();
    }

    @FXML
    private void initialize() {
        selectedSide = -1;
        selectedMain = -1;
        File file = new File ("src//main//resources//CSS//Buttons.css");
        pane.getStylesheets().add(file.toURI().toString());
        setDeckName();
        mainCards = player.getMainCards();
        sideCards = player.getSideCards();
        setMain();
        setSide();
        setAllStyle();
        scoreAndWinner.setText(scoreAndWinnerData);
        System.out.println(player.getMainCards().size());
    }

    private void setAllStyle() {
        setStyle(sideCards, selectedSide, sideButtons);
        setStyle(mainCards, selectedMain, mainButtons);
    }

    private void setSide() {
        for (int i = 0; i < sideCards.size(); i++)
            setButton(i, sideCards, sideButtons, "s", 500);
    }

    private void setMain() {
        for (int i = 0; i < mainCards.size(); i++)
            setButton(i, mainCards, mainButtons, "m", 27);
    }

    private void setButton(int number, ArrayList<Card> cards, Button[][] buttons, String flag, int x) {
        Image image;
        int row = number / 12;
        int column = number % 12;
        Card card = cards.get(number);
        if (card.isOriginal())
            image = new Image(Objects.requireNonNull(getClass().getResource("/PNG/Cards1/" + card.getCardName() + ".jpg")).toExternalForm());
        else image = new Image(Objects.requireNonNull(getClass().getResource("/PNG/Cards1/" + card.getKind() + ".png")).toExternalForm());
        ImageView imageView = new ImageView(image);
        buttons[row][column] = new Button();
        buttons[row][column].setLayoutY(x + row * 64);
        buttons[row][column].setLayoutX(350 + column * 45);
        imageView.setFitWidth(70);
        imageView.setFitHeight(90);
        buttons[row][column].setGraphic(imageView);
        setEvents(number, cards, buttons, flag, buttons[row][column], imageView);
        pane.getChildren().add(buttons[row][column]);
    }

    private void setEvents(int number, ArrayList<Card> cards, Button[][] buttons, String flag, Button button, ImageView imageView) {
        button.setOnMouseClicked(e ->{
            switch (flag) {
                case "m":
                    selectedMain = number;
                    break;
                case "s":
                    selectedSide = number;
                    break;
            }
            setStyle(cards, number, buttons);
        });
        button.setOnDragDetected(event -> {
            switch (flag) {
                case "m":
                    dragAddress = new Address(number, "main", true);
                    break;
                case "s":
                    dragAddress = new Address(number, "side", true);
                    break;
            }
            Dragboard db = button.startDragAndDrop(TransferMode.ANY);
            ClipboardContent content = new ClipboardContent();
            content.putImage(imageView.getImage());
            db.setContent(content);
            event.consume();
        });
    }

    private void setStyle(ArrayList<Card> cards, int selected, Button[][] buttons) {
        for (int i = 0; i < cards.size(); i++) {
            int row = i / 12;
            int column = i % 12;
            if (selected == i)
                buttons[row][column].setId("selected");
            else buttons[row][column].setId("nonSelected");
        }
    }

    private void setDeckName() {
        sideNumber.setText(String.valueOf(player.getSideCards().size()));
        mainNumber.setText(String.valueOf(player.getMainCards().size()));
        userName.setText("User Name: " + user.getName());
    }

    public static void setScoreAndWinner(String msg) {
        scoreAndWinnerData = msg;
    }

    public static void setUser(User _user, Player _player) {
        user = _user;
        player = _player;
    }

    public void addSideToMain() {
        if (selectedSide == -1)
            msg.setText("Didn't Choose Any Card");
        else {
            try {
                removeAll();
                new SetMainAndSideController().setSideToMain(selectedSide, player);
                soundEffect(0);
            } catch (MyException e) {
                msg.setText(e.getMessage());
            }
            resetAll();
        }
    }

    public void addMainToSide() {
        if (selectedMain == -1)
            msg.setText("Didn't Choose Any Card");
        else {
            try {
                removeAll();
                new SetMainAndSideController().setMainToSide(selectedMain, player);
                soundEffect(0);
            } catch (MyException e) {
                msg.setText(e.getMessage());
            }
            resetAll();
        }
    }

    public void back() {
        try {
            soundEffect(1);
            if (player.getName().equals(Game.firstPlayer.getName()) && !Game.getIsAI()) {
                removeAll();
                setUser(Game.secondPlayer.getUser(), Game.secondPlayer);
                resetAll();
            }
            else if (player.getName().equals(Game.firstPlayer.getName())) {
                new SetMainAndSideController().setAICard();
                new RockPaperScissors().start(stage);
            }
            else {
                Game.firstPlayer.setHandCard();
                if (!Game.getIsAI())
                    Game.secondPlayer.setHandCard();
                new RockPaperScissors().start(stage);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void soundEffect(int number) {
        try {
            if (number == 0)
                Sounds.play("src//main//resources//Sound//CARD_MOVE_1.wav", 1).start();
            if (number == 1)
                Sounds.play("src//main//resources//Sound//TURNEX.wav", 1).start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void resetAll() {
        setDeckName();
        mainCards = player.getMainCards();
        sideCards = player.getSideCards();
        selectedMain = -1;
        selectedSide = -1;
        setMain();
        setSide();
        setAllStyle();
        setDeckName();
    }

    private void removeAll() {
        msg.setText("");
        for (int i = 0; i < sideCards.size(); i++)
            pane.getChildren().remove(sideButtons[i / 12][i % 12]);
        for (int i = 0; i < mainCards.size(); i++)
            pane.getChildren().remove(mainButtons[i / 12][i % 12]);
    }

    public void mainHandlerDrop() {
        if (dragAddress.getKind().equals("side")) {
            try {
                removeAll();
                new SetMainAndSideController().setSideToMain(dragAddress.getNumber(), player);
                soundEffect(0);
            } catch (MyException e) {
                msg.setText(e.getMessage());
            }
            resetAll();
        }
    }

    public void dragOverHandler(DragEvent dragEvent) {
        System.out.println(player.getMainCards().size());
        if (dragEvent.getDragboard().hasImage())
            dragEvent.acceptTransferModes(TransferMode.ANY);
    }

    public void sideHandlerDrop() {
        if (dragAddress.getKind().equals("main")) {
            try {
                removeAll();
                new SetMainAndSideController().setMainToSide(dragAddress.getNumber(), player);
                soundEffect(0);
            } catch (MyException e) {
                msg.setText(e.getMessage());
            }
            resetAll();
        }
    }
}
