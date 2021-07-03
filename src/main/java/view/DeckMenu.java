package view;

import java.util.ArrayList;
import java.util.regex.Matcher;

import Exceptions.*;
import Utility.CommandMatcher;
import controllers.DeckControllers;
import javafx.application.Application;
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
    public Button msg;
    public TextField cardName;
    public Button importData;

    @Override
    public void start(Stage stage) throws Exception {
        DeckMenu.stage = stage;
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/DeckMenu.fxml"));
        stage.setScene(new Scene(root));
        stage.show();
    }

    public void msg(MouseEvent mouseEvent) {

    }
}
