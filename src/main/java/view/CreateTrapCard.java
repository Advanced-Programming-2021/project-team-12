package view;

import controllers.CreateCardController;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import models.card.spell.SpellCard;
import models.card.trap.TrapCard;

import java.util.ArrayList;

public class CreateTrapCard extends Application {
    public static Stage stage;
    public Pane pane;
    public Label priceLabel;
    public TextField realName;
    private ArrayList<TrapCard> trapCards = new ArrayList<>();

    @FXML
    public void initialize() {
        ArrayList<TrapCard> trapCards = TrapCard.getOriginalTrapCards();
        int count = 0;
        for (TrapCard trapCard : trapCards) {
            int x = count % 2;
            int y = (count + x) / 2 - 1;
            Button button = new Button();
            button.setLayoutX(x * 655 + 260);
            button.setLayoutY(y * 25 + 100);
            button.setPrefWidth(650);
            button.setPrefHeight(25);
            button.setText(trapCard.getDescription());
            button.setStyle("-fx-background-color: gray");
            button.setOnAction(e -> {
                setThisButton(button, trapCard);
            });
            pane.getChildren().add(button);
            count++;
        }
    }

    private void setThisButton(Button button, TrapCard trapCard) {
        if (trapCards.contains(trapCard)) {
            button.setStyle("-fx-background-color: gray");
            trapCards.remove(trapCard);
        } else {
            button.setStyle("-fx-background-color: cyan");
            trapCards.add(trapCard);
        }
    }

    @Override
    public void start(Stage stage) throws Exception {
        CreateTrapCard.stage = stage;
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/CreateTrapCard.fxml"));
        stage.setScene(new Scene(root));
        stage.show();
    }

    public void countPrice(MouseEvent mouseEvent) {
        priceLabel.setText(String.valueOf(countThePrice()));
    }

    private int countThePrice() {
        return trapCards.size() * 2500;
    }

    public void back(MouseEvent mouseEvent) throws Exception {
        new MainMenu().start(stage);
    }

    public void submit(MouseEvent mouseEvent) throws Exception {
        CreateCardController.createTrapCard(realName.getText(), trapCards, countThePrice());
        new MainMenu().start(stage);
    }
}
