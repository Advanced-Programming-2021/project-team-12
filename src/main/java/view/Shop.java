package view;

import java.awt.*;
import java.security.spec.ECField;
import java.util.ArrayList;
import java.util.regex.Matcher;

import Exceptions.MyException;
import Utility.CommandMatcher;
import controllers.ShopControl;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.paint.ImagePattern;
import javafx.stage.Stage;
import models.Card;
import models.User;

public class Shop extends Application {

    public javafx.scene.control.Label label;
    private Button[] buttons = new Button[1000];
    private User user;
    private ArrayList<Card> cards;
    private ArrayList<Card> allCards;
    private int counter = 0;
    public static Stage stage;

    @Override
    public void start(Stage stage) throws Exception {
        initialize();
        MainMenu.stage = stage;
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/Shop.fxml"));
        stage.setScene(new Scene(root));
        stage.show();
    }

    private void initialize() {
        user = MainMenu.user;
        allCards = Card.getAllCards();
        cards = user.getAllCards();
        for (Card card : cards) {
            addCardAsImage(counter, card);
            counter++;
        }
        int counter1 = 0;
        for (Card allCard : allCards) {
            addCardAsButton(counter1, allCard);
            counter1++;
        }
    }

    private void addCardAsImage(int counter1, Card card) {
        int x = counter1 % 9;
        int y = counter1 / 9;
        String kind = "SpellTrap";
        if (card.getKind().equals("Monster")) kind = "Monsters";
        Image image = new Image(getClass().getResource("/PNG/Card1/" + kind + "/" + card.getCardName() + ".jpg").toExternalForm());
        ImagePattern imagePattern = new ImagePattern(image);
        ImageView imageView = new ImageView();
        imageView.setImage(image);
        imageView.setX(y * 130);
        imageView.setX(x * 90);
        imageView.setFitHeight(90);
        imageView.setFitWidth(130);
        label.setText("money: " + user.getMoney());
    }

    private void addCardAsButton(int counter1, Card card) {
        int x = counter1 % 9;
        int y = counter1 / 9;
        String kind = "SpellTrap";
        if (card.getKind().equals("Monster")) kind = "Monsters";
        Image image = new Image(getClass().getResource("/PNG/Card1/" + kind + "/" + card.getCardName() + ".jpg").toExternalForm());
        ImagePattern imagePattern = new ImagePattern(image);
        VBox vBox = new VBox();
        Button button = new Button();
        button.setText("buy");
        button.setLayoutX(x * 90 + 20 + 900);
        button.setLayoutY(y * 150 + 130);
        button.setPrefWidth(50);
        button.setPrefHeight(20);
        button.setStyle("-fx-background-color: cyan");
        button.setOnAction(e -> {
            buy(card);
        });
        buttons[counter1] = button;
        ImageView imageView = new ImageView();
        imageView.setImage(image);
        imageView.setX(y * 150);
        imageView.setX(x * 90 + 900);
        imageView.setFitHeight(90);
        imageView.setFitWidth(130);
        vBox.getChildren().add(button);
        if (user.getMoney() >= card.getPrice()) {
            cancelButton(button);
        }
        vBox.getChildren().add(imageView);
    }

    private void cancelButton(Button button) {
        button.setDisable(true);
        button.setCancelButton(true);
        button.setStyle("-fx-background-color: gray");
    }

    private void buy(Card card) {
        cards.add(card);
        addCardAsImage(counter, card);
        user.setMoney(user.getMoney() - card.getPrice());
        int counter1 = 0;
        for (Card allCard : allCards) {
            if (allCard.getPrice() > user.getMoney()) {
                cancelButton(buttons[counter1]);
            }
            counter1++;
        }
        label.setText("money: " + user.getMoney());
    }
}
