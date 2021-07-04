package view;

import java.util.ArrayList;

import controllers.SaveFile;
import controllers.ShopControl;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.VerticalDirection;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import models.Card;
import models.User;

public class Shop extends Application {

    public javafx.scene.control.Label label;
    public Pane pane;
    private Button[] buttons = new Button[1000];
    private User user;
    private ArrayList<Card> cards;
    private ArrayList<Card> allCards;
    private int counter = 0;
    public static Stage stage;

    @Override
    public void start(Stage stage) throws Exception {
     //   initialize();
        Shop.stage = stage;
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/Shop.fxml"));
        stage.setScene(new Scene(root));
        stage.show();
    }

    @FXML
    private void initialize() {
        user = MainMenu.user;
        allCards = Card.getAllCards();
        cards = user.getDeckAndAllCards();
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
        int x = counter1 % 10;
        int y = counter1 / 10;
        String kind = "SpellTrap";
        if (card.getKind().equals("Monster")) kind = "Monsters";
        Image image = new Image(getClass().getResource("/PNG/Cards1/" + kind + "/" + card.getCardName() + ".jpg").toExternalForm());
        ImageView imageView = new ImageView();
        imageView.setImage(image);
        imageView.setLayoutX(x * scale(90));
        imageView.setLayoutY(y * scale(130));
        imageView.setFitHeight(scale(130));
        imageView.setFitWidth(scale(90));
        pane.getChildren().add(imageView);
        label.setText("money: " + String.valueOf(user.getMoney()));
    }

    private void addCardAsButton(int counter1, Card card) {
        int x = counter1 % 11;
        int y = counter1 / 11;
        String kind = "SpellTrap";
        if (card.getKind().equals("Monster")) kind = "Monsters";
        Image image = new Image(getClass().getResource("/PNG/Cards1/" + kind + "/" + card.getCardName() + ".jpg").toExternalForm());
        Button button = new Button();
        button.setText("buy");
//        button.(VerticalDirection.UP);
        button.setLayoutX(x * scale(90) + scale(20) + 790);
        button.setLayoutY(y * scale(180) + scale(130));
        button.setPrefWidth(scale(80));
        button.setPrefHeight(scale(5));
        button.setStyle("-fx-background-color: cyan");
        button.setOnAction(e -> {
            buy(card);
        });
        buttons[counter1] = button;
        ImageView imageView = new ImageView();
        imageView.setImage(image);
        imageView.setLayoutX(x * scale(90) + 800);
        imageView.setLayoutY(y * scale(180));
        imageView.setFitHeight(scale(130));
        imageView.setFitWidth(scale(90));
        if (user.getMoney() < card.getPrice()) {
            cancelButton(button);
        }
        pane.getChildren().addAll(button, imageView);
    }

    private void cancelButton(Button button) {
        button.setDisable(true);
        button.setCancelButton(true);
        button.setStyle("-fx-background-color: gray");
    }

    private void buy(Card card) {
        new ShopControl().buyCard(card, user);
        addCardAsImage(counter, card);
        label.setText("money: " + user.getMoney());
        counter++;
        int counter1 = 0;
        for (Card allCard : allCards) {
            if (allCard.getPrice() > user.getMoney()) {
                cancelButton(buttons[counter1]);
            }
            counter1++;
        }
    }

    public double scale(double v){
        return 3*v/5;
    }

    public void back(MouseEvent mouseEvent) throws Exception {
        new MainMenu().start(stage);
    }
}
