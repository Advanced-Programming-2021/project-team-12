package view;

import java.io.File;
import java.util.ArrayList;
import java.util.regex.Matcher;

import Exceptions.*;
import Utility.CommandMatcher;
import controllers.DeckControllers;
import controllers.DuelControl;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import models.Card;
import models.Deck;
import models.User;

import javax.swing.*;

public class DeckMenu extends Application {
    public static Stage stage;
    public Button data;
    private Button[] decksButton = new Button[6];
    private Button[] deckNames = new Button[6];
    private Label[] labels = new Label[6];
    private User user;
    private int selectedDeck;
    public TextField addDeckName;
    public Button msg;
    public Pane pane;

    @Override
    public void start(Stage stage) throws Exception {
        DeckMenu.stage = stage;
        Pane pane = FXMLLoader.load(getClass().getResource("/fxml/DeckMenu.fxml"));
        stage.setScene(new Scene(pane));
        stage.show();
    }

    @FXML
    private void initialize() {
        selectedDeck = -1;
        user = MainMenu.user;
        setClassStyle();
        setData();
        setButton();
        setNameButton();
        setNumbers();
        setStyles();
    }

    private void setClassStyle() {
        File file = new File ("src//main//resources//CSS//Buttons.css");
        pane.getStylesheets().add(file.toURI().toString());
    }

    private void setNumbers() {
        ArrayList<Deck> decks = Deck.getDecksOfUser(user);
        for (int i = 0; i < decks.size(); i++) {
            int number = decks.get(i).getNumberOfCard("m") + decks.get(i).getNumberOfCard("s");
            labels[i] = new Label();
            labels[i].setLayoutY(102);
            labels[i].setLayoutX(130 * i + 42);
            if (number < 10)
                labels[i].setLayoutX(130 * i + 51);
            labels[i].setText(String.valueOf(number));
            labels[i].setMinWidth(120);
            labels[i].setStyle("-fx-text-fill: #f8b090");
            labels[i].setFont(Font.font("Yu-Gi-Oh! StoneSerif LT", FontWeight.BOLD, 40));
            labels[i].setTextAlignment(TextAlignment.CENTER);
            pane.getChildren().add(labels[i]);
        }
    }

    private void setButton() {
        ArrayList<Deck> decks = Deck.getDecksOfUser(user);
        for (int i = 0; i < decks.size(); i++) {
            Image image = new Image(getClass().getResource("/PNG/NEW/deck.png").toExternalForm());
            if (decks.get(i).checkIsActive())
                image = new Image(getClass().getResource("/PNG/NEW/ActiveDeck.png").toExternalForm());
            ImageView imageView = new ImageView(image);
            decksButton[i] = new Button();
            decksButton[i].setLayoutY(50);
            decksButton[i].setLayoutX(130 * i + 15);
            imageView.setLayoutY(50);
            imageView.setLayoutX(130 * i + 15);
            imageView.setFitWidth(80);
            imageView.setFitHeight(150);
            decksButton[i].setGraphic(imageView);
            int finalI = i;
            decksButton[i].setOnMouseClicked(e ->{
                selectedDeck = finalI;
                setStyles();
            });
            decksButton[i].setOnMouseEntered(e ->{
                data.setFont(Font.font("Yu-Gi-Oh! StoneSerif LT", FontWeight.NORMAL, 8));
                data.setTextAlignment(TextAlignment.LEFT);
                Deck enteredDeck = Deck.getDecksOfUser(user).get(finalI);
                StringBuilder deckCards = new StringBuilder("Main: ");
                for (int j = 0; j < enteredDeck.getMainCards().size(); j++)
                    deckCards.append(enteredDeck.getMainCards().get(j).getCardName() + ", ");
                deckCards.append("\n---------------\nSide: ");
                for (int j = 0; j < enteredDeck.getSideCards().size(); j++)
                    deckCards.append(enteredDeck.getSideCards().get(j).getCardName() + ", ");
                data.setText(deckCards.toString());
            });
            decksButton[i].setOnMouseExited(e ->{
                data.setFont(Font.font("Yu-Gi-Oh! StoneSerif LT", FontWeight.BOLD, 19));
                data.setTextAlignment(TextAlignment.CENTER);
                setData();
            });
            pane.getChildren().add(decksButton[i]);
        }
    }

    private void setStyles() {
        ArrayList<Deck> decks = Deck.getDecksOfUser(user);
        for (int i = 0; i < decks.size(); i++) {
            if (i == selectedDeck)
                decksButton[i].setId("selected");
            else decksButton[i].setId("nonSelected");
        }
    }

    private void setNameButton() {
        ArrayList<Deck> decks = Deck.getDecksOfUser(user);
        for (int i = 0; i < decks.size(); i++) {
            deckNames[i] = new Button();
            deckNames[i].setLayoutY(220);
            deckNames[i].setLayoutX(130 * i + 5);
            deckNames[i].setText(decks.get(i).getName());
            deckNames[i].setDisable(true);
            deckNames[i].setMinWidth(120);
            deckNames[i].setCancelButton(true);
            deckNames[i].setStyle("-fx-text-fill: #6d1b00");
            deckNames[i].setFont(Font.font("Yu-Gi-Oh! StoneSerif LT", FontWeight.BOLD, 15));
            deckNames[i].setOpacity(0.70);
            pane.getChildren().add(deckNames[i]);
        }
    }

    private void setData() {
        if (Deck.getActiveDeckOfUser(user.getName()) != null)
            data.setText("  User Name: " + user.getName() + "\t Number Of Deck: " + Deck.getDecksOfUser(user).size()
                + "\t   Active Deck: " + Deck.getActiveDeckOfUser(user.getName()).getName());
        else data.setText("  User Name: " + user.getName() + "   Number Of Deck: " + Deck.getDecksOfUser(user).size()
                + "  Active Deck: * Dont Have Active Deck *" );
    }

    public void setActive(MouseEvent mouseEvent) {
        msg.setVisible(false);
        if (selectedDeck == -1)
            setMsg(false, "Didn't Choose Any Deck");
        else try {
            new DeckControllers().setActive(Deck.getDecksOfUser(user).get(selectedDeck).getName());
            removeAll();
            setAll();
            setMsg(true, "Deck Set Active Successfully");
        } catch (Exception e) {
            e.printStackTrace();
            setMsg(false, e.getMessage());
        }
    }

    public void openDeck(MouseEvent mouseEvent) {
        msg.setVisible(false);
        if (selectedDeck == -1)
            setMsg(false, "Didn't Choose Any Deck");
        else {
            try {
                DeckData.setDeck(Deck.getDecksOfUser(user).get(selectedDeck));
                new DeckData().start(stage);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void addDeck(MouseEvent mouseEvent) {
        msg.setVisible(false);
        try {
            new DeckControllers().createDeck(addDeckName.getText(), user);
            removeAll();
            setAll();
            addDeckName.clear();
            setMsg(true, "Add Successfully");
        } catch (MyException e) {
            setMsg(false, e.getMessage());
        }
    }

    public void setMsg(boolean isSuccessful, String message) {
        msg.setText(message);
        msg.setVisible(true);
        if (isSuccessful)
            msg.setStyle("-fx-text-fill: #16045c");
        else
            msg.setStyle("-fx-text-fill: #590000");
    }

    public void back(MouseEvent mouseEvent) {
        try {
            new MainMenu().start(stage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void delete(MouseEvent mouseEvent) {
        msg.setVisible(false);
        if (selectedDeck == -1)
            setMsg(false, "Didn't Choose Any Deck");
         else try {
            removeAll();
            new DeckControllers().deleteDeck(Deck.getDecksOfUser(user).get(selectedDeck).getName());
            selectedDeck = -1;
            setAll();
            setMsg(true, "Deck Deleted Successfully");
        } catch (Exception e) {
            e.printStackTrace();
            setMsg(false, e.getMessage());
        }
    }

    private void setAll() {
        setButton();
        setNameButton();
        setNumbers();
        setStyles();
        setData();
    }

    private void removeAll() {
        ArrayList<Deck> decks = Deck.getDecksOfUser(user);
        for (int i = 0; i < decks.size(); i++) {
            pane.getChildren().remove(deckNames[i]);
            pane.getChildren().remove(decksButton[i]);
            pane.getChildren().remove(labels[i]);
        }
    }
}
