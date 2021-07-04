package view;

import Exceptions.MyException;
import controllers.DeckControllers;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import models.Card;
import models.Deck;

import java.io.File;
import java.util.ArrayList;

public class DeckData extends Application {
    public static Stage stage;
    private static Deck deck;
    public Pane pane;
    public Integer selectedHand;
    public Integer selectedMain;
    public Integer selectedSide ;
    public Label msg;
    public Label deckName;
    private ArrayList<Card> handCards;
    private ArrayList<Card> mainCards;
    private ArrayList<Card> sideCards;
    private Button[][] handButtons = new Button[10][8];
    private Button[][] mainButtons = new Button[40][25];
    private Button[][] sideButtons = new Button[10][25];

    @Override
    public void start(Stage stage) throws Exception {
        DeckData.stage = stage;
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/DeckData.fxml"));
        stage.setScene(new Scene(root));
        stage.show();
    }

    @FXML
    private void initialize() {
        selectedHand = -1;
        selectedSide = -1;
        selectedMain = -1;
        File file = new File ("src//main//resources//CSS//Buttons.css");
        pane.getStylesheets().add(file.toURI().toString());
        setDeckName();
        handCards = MainMenu.user.getAllCards();
        mainCards = deck.getMainCards();
        sideCards = deck.getSideCards();
        setHand();
        setMain();
        setSide();
        setAllStyle();
    }

    private void setAllStyle() {
        setStyle(sideCards, selectedSide, sideButtons, 12);
        setStyle(mainCards, selectedMain, mainButtons, 12);
        setStyle(handCards, selectedHand, handButtons, 6);
    }

    private void setSide() {
        for (int i = 0; i < sideCards.size(); i++)
            setButton(i, 12, selectedSide , sideCards, sideButtons, "s", 500, 350);
    }

    private void setMain() {
        for (int i = 0; i < mainCards.size(); i++)
            setButton(i, 12, selectedMain , mainCards, mainButtons, "m", 27, 350);
    }

    private void setHand() {
        for (int i = 0; i < handCards.size(); i++)
            setButton(i, 6, selectedHand , handCards, handButtons, "h", 27, 20);
    }

    private void setButton(int number, int max, int selected, ArrayList<Card> cards, Button[][] buttons, String flag, int x, int y) {
        int row = number / max;
        int column = number % max;
        Card card = cards.get(number);
        File pictureFile = new File("src//main//resources//PNG//Cards1//" + card.getCardName() + ".jpg");
        String string = pictureFile.toURI().toString();
        Image image = new Image(string);
        ImageView imageView = new ImageView(image);
        buttons[row][column] = new Button();
        buttons[row][column].setLayoutY(x + row * 64);
        buttons[row][column].setLayoutX(y + column * 45);
        imageView.setFitWidth(70);
        imageView.setFitHeight(90);
        buttons[row][column].setGraphic(imageView);
        buttons[row][column].setOnMouseClicked(e ->{
            switch (flag) {
                case "m":
                    selectedMain = number;
                    break;
                case "s":
                    selectedSide = number;
                    break;
                case "h":
                    selectedHand = number;
                    break;
            }
            setStyle(cards, number, buttons, max);
        });
        pane.getChildren().add(buttons[row][column]);
    }

    private void setStyle(ArrayList<Card> cards, int selected, Button[][] handButtons, int maxColumn) {
        for (int i = 0; i < cards.size(); i++) {
            int row = i / maxColumn;
            int column = i % maxColumn;
            if (selected == i)
                handButtons[row][column].setId("selected");
            else handButtons[row][column].setId("nonSelected");
        }
    }

    private void setDeckName() {
        deckName.setText("Deck Name: " + deck.getName());
    }

    public static void setDeck(Deck _deck) {
        deck = _deck;
    }

    public void addSideToMain(MouseEvent mouseEvent) {
        if (selectedSide == -1)
            msg.setText("Didn't Choose Any Card");
        else {
            try {
                removeAll();
                Card card = sideCards.get(selectedSide);
                new DeckControllers().removeCard(card.getCardName(), deck.getName(), true);
                new DeckControllers().addCard(card.getCardName(), deck.getName(), false);
            } catch (MyException e) {
                msg.setText(e.getMessage());
            }
            resetAll();
        }
    }

    public void addMainToSide(MouseEvent mouseEvent) {
        if (selectedMain == -1)
            msg.setText("Didn't Choose Any Card");
        else {
            try {
                removeAll();
                Card card = mainCards.get(selectedMain);
                new DeckControllers().removeCard(card.getCardName(), deck.getName(), false);
                new DeckControllers().addCard(card.getCardName(), deck.getName(), true);
            } catch (MyException e) {
                msg.setText(e.getMessage());
            }
            resetAll();
        }
    }

    public void removeFromSide(MouseEvent mouseEvent) {
        removeCard(selectedSide, sideCards, true);
    }

    public void removeFromMain(MouseEvent mouseEvent) {
        removeCard(selectedMain, mainCards, false);
    }

    public void removeCard(Integer selected, ArrayList<Card> cards, boolean isSide) {
        if (selected == -1)
            msg.setText("Didn't Choose Any Card");
        else {
            try {
                removeAll();
                new DeckControllers().removeCard(cards.get(selected).getCardName(), deck.getName(), isSide);
            } catch (MyException e) {
                msg.setText(e.getMessage());
            }
            resetAll();
        }
    }

    public void back(MouseEvent mouseEvent) {
        try {
            new DeckMenu().start(stage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addCardToSide(MouseEvent mouseEvent) {
        moveCardFromHand(selectedHand, handCards, true);
    }

    private void moveCardFromHand(Integer selected, ArrayList<Card> cards, boolean isSide) {
        if (selected == -1)
            msg.setText("Didn't Choose Any Card");
        else {
            try {
                removeAll();
                new DeckControllers().addCard(cards.get(selected).getCardName(), deck.getName(), isSide);
            } catch (MyException e) {
                msg.setText(e.getMessage());
            }
            resetAll();
        }
    }

    public void addCardToMain(MouseEvent mouseEvent) {
        moveCardFromHand(selectedHand, handCards, false);
    }

    private void resetAll() {
        selectedHand = -1;
        selectedMain = -1;
        selectedSide = -1;
        setHand();
        setMain();
        setSide();
        setAllStyle();
    }

    private void removeAll() {
        msg.setText("");
        for (int i = 0; i < handCards.size(); i++)
            pane.getChildren().remove(handButtons[i / 6][i % 6]);
        for (int i = 0; i < sideCards.size(); i++)
            pane.getChildren().remove(sideButtons[i / 12][i % 12]);
        for (int i = 0; i < mainCards.size(); i++)
            pane.getChildren().remove(mainButtons[i / 12][i % 12]);
    }
}
