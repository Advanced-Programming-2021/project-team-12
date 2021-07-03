package view;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import models.Deck;

public class DeckData extends Application {
    public static Stage stage;
    private static Deck deck;

    public static void setDeck(Deck _deck) {
        deck = _deck;
    }

    @Override
    public void start(Stage stage) throws Exception {
        DeckData.stage = stage;
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/DeckData.fxml"));
        stage.setScene(new Scene(root));
        stage.show();
    }
}
