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
import models.card.spell.SpellMode;

import java.util.ArrayList;

public class CreateSpellCard extends Application {
    public static Stage stage;
    public Pane pane;
    public TextField nameTextField;
    public Label priceLabel;
    public Button normal;
    public Button ritual;
    public Button equip;
    public Button continiuous;
    public Button quickPlay;
    private ArrayList<SpellCard> spellCards = new ArrayList<>();
    private String realName;
    private SpellMode spellMode;

    @FXML
    public void initialize() {
        ArrayList<SpellCard> spellCards = SpellCard.getOriginalSpellCards();
        int count = 0;
        for (SpellCard spellCard : spellCards) {
            int x = count % 2;
            int y = (count + x) / 2 - 1;
            Button button = new Button();
            button.setLayoutX(x * 655 + 260);
            button.setLayoutY(y * 25 + 100);
            button.setPrefWidth(650);
            button.setPrefHeight(25);
            button.setText(spellCard.getDescription());
            button.setStyle("-fx-background-color: gray");
            button.setOnAction(e -> {
                setThisButton(button, spellCard);
            });
            pane.getChildren().add(button);
            count++;
        }
    }

    private void setThisButton(Button button, SpellCard spellCard) {
        if (spellCards.contains(spellCard)) {
            button.setStyle("-fx-background-color: gray");
            spellCards.remove(spellCard);
        } else {
            button.setStyle("-fx-background-color: cyan");
            spellCards.add(spellCard);
        }
    }

    @Override
    public void start(Stage stage) throws Exception {
        CreateSpellCard.stage = stage;
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/CreateSpellCard.fxml"));
        stage.setScene(new Scene(root));
        stage.show();
    }

    private int countThePrice() {
        return spellCards.size() * 2500;
    }

    public void countPrice(MouseEvent mouseEvent) {
        priceLabel.setText(String.valueOf(countThePrice()));
    }

    public void beNormal(MouseEvent mouseEvent) {
        makeAllButtonsGray();
        setSpellMode(SpellMode.NORMAL, normal);
    }

    public void beRitual(MouseEvent mouseEvent) {
        makeAllButtonsGray();
        setSpellMode(SpellMode.RITUAL, ritual);
    }

    public void beEquip(MouseEvent mouseEvent) {
        makeAllButtonsGray();
        setSpellMode(SpellMode.EQUIP, equip);
    }

    public void beContiniuous(MouseEvent mouseEvent) {
        makeAllButtonsGray();
        setSpellMode(SpellMode.CONTINUOUS, continiuous);
    }

    public void beQuickPlay(MouseEvent mouseEvent) {
        makeAllButtonsGray();
        setSpellMode(SpellMode.QUICKPLAY, quickPlay);
    }

    private void makeAllButtonsGray() {
        normal.setStyle("-fx-background-color: gray");
        equip.setStyle("-fx-background-color: gray");
        continiuous.setStyle("-fx-background-color: gray");
        quickPlay.setStyle("-fx-background-color: gray");
        ritual.setStyle("-fx-background-color: gray");
    }

    private void setSpellMode(SpellMode spellMode, Button button) {
        button.setStyle("-fx-background-color: cyan");
        this.spellMode = spellMode;
    }

    public void submit(MouseEvent mouseEvent) throws Exception {
        realName = nameTextField.getText();
        CreateCardController.createSpellCard(nameTextField.getText(), spellMode, countThePrice(), spellCards);
        new MainMenu().start(stage);
    }

    public void back(MouseEvent mouseEvent) throws Exception{
        new MainMenu().start(stage);
    }
}
