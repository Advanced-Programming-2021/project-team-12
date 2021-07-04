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
import javafx.stage.Stage;
import models.Card;
import models.Deck;
import models.User;

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
        setData();
        setNameButton();
        setButton();
        setNumbers();
    }

    private void setNumbers() {
        ArrayList<Deck> decks = Deck.getDecksOfUser(user);
        for (int i = 0; i < decks.size(); i++) {
            labels[i] = new Button();
            deckNames[i].setLayoutY(225);
            deckNames[i].setLayoutX(130 * i + 25);
            deckNames[i].setText(decks.get(i).getName());
            deckNames[i].setDisable(true);
            deckNames[i].setMinWidth(120);
            deckNames[i].setCancelButton(true);
            deckNames[i].setStyle("-fx-text-fill: #6d1b00");
            deckNames[i].setFont(Font.font("Yu-Gi-Oh! StoneSerif LT", FontWeight.BOLD, 15));
            deckNames[i].setOpacity(0.70);
            pane.getChildren().add(decksButton[i]);
        }
    }

    private void setButton() {
        ArrayList<Deck> decks = Deck.getDecksOfUser(user);
        for (int i = 0; i < decks.size(); i++) {
            int numberOfCard = decks.get(i).getNumberOfCard("m") + decks.get(i).getNumberOfCard("s");
            File pictureFile = new File("src//main//resources//PNG//NEW//deck.png");
            if (decks.get(i).checkIsActive())
                pictureFile = new File("src//main//resources//PNG//NEW//ActiveDeck.png");
            String string = pictureFile.toURI().toString();
            Image image = new Image(string);
            ImageView imageView = new ImageView(image);
            decksButton[i] = new Button();
            imageView.setLayoutY(70);
            imageView.setLayoutX(130 * i + 40);
            imageView.setFitWidth(80);
            imageView.setFitHeight(150);
            decksButton[i].setGraphic(imageView);
            int finalI = i;
            decksButton[i].setOnMouseClicked(e ->{
                selectedDeck = finalI;
                setStyles();
            });
            pane.getChildren().add(imageView);
        }
    }

    private void setStyles() {

    }

    private void setNameButton() {
        ArrayList<Deck> decks = Deck.getDecksOfUser(user);
        for (int i = 0; i < decks.size(); i++) {
            deckNames[i] = new Button();
            deckNames[i].setLayoutY(225);
            deckNames[i].setLayoutX(130 * i + 25);
            deckNames[i].setText(decks.get(i).getName());
            deckNames[i].setDisable(true);
            deckNames[i].setMinWidth(120);
            deckNames[i].setCancelButton(true);
            deckNames[i].setStyle("-fx-text-fill: #6d1b00");
            deckNames[i].setFont(Font.font("Yu-Gi-Oh! StoneSerif LT", FontWeight.BOLD, 15));
            deckNames[i].setOpacity(0.70);
            pane.getChildren().add(decksButton[i]);
        }
    }

    private void setData() {
        data.setText("  User Name: " + user.getName() + "\t Number Of Deck: " + Deck.getDecksOfUser(user).size()
                + "\t   Active Deck: " + Deck.getActiveDeckOfUser(user.getName()).getName());
    }

    public void setActive(MouseEvent mouseEvent) {
        msg.setVisible(false);
        if (selectedDeck == -1)
            setMsg(false, "Didn't Choose Any Deck");
        else try {
            new DeckControllers().setActive(Deck.getDecksOfUser(user).get(selectedDeck).getName());
            setButton();
            setNameButton();
            selectedDeck = -1;
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
        try {
            DeckData.setDeck(Deck.getDecksOfUser(user).get(selectedDeck));
            new DeckData().start(stage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addDeck(MouseEvent mouseEvent) {
        msg.setVisible(false);
        try {
            new DeckControllers().createDeck(addDeckName.getText(), user);
            setButton();
            setNameButton();
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
            new DeckControllers().deleteDeck(Deck.getDecksOfUser(user).get(selectedDeck).getName());
            setButton();
            setNameButton();
            selectedDeck = -1;
            setMsg(true, "Deck Deleted Successfully");
        } catch (Exception e) {
            e.printStackTrace();
            setMsg(false, e.getMessage());
        }
    }
}
