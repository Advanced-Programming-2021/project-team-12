package view;

import java.util.ArrayList;
import java.util.regex.Matcher;

import Exceptions.*;
import Utility.CommandMatcher;
import controllers.DeckControllers;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import models.Card;
import models.Deck;
import models.User;

public class DeckMenu extends Application {
    public static Stage stage;
    public Button data;
    private Button[] decks = new Button[6];
    private Button[] deckNames = new Button[6];
    private User user;
    private int selectedDeck;
    public TextField addDeckName;
    public Button msg;

    @Override
    public void start(Stage stage) throws Exception {
        DeckMenu.stage = stage;
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/DeckMenu.fxml"));
        stage.setScene(new Scene(root));
        stage.show();
    }

    @FXML
    private void initialize() {
        selectedDeck = -1;
        user = MainMenu.user;
        setData();
        setButton();
    }

    private void setButton() {
    }

    private void setNameButton() {
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
