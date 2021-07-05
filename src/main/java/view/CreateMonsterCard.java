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
import models.card.monster.MonsterCard;
import models.card.monster.MonsterMode;

import java.util.ArrayList;

public class CreateMonsterCard extends Application {
    private static ArrayList<MonsterCard> monsterCards;
    public Pane pane;
    private String name;
    private int level = 1;
    private int attack = 0;
    private int defence = 0;
    private MonsterMode monsterMode;
    private ArrayList<MonsterCard> monsters = new ArrayList<>();
    private int price;
    private String description;
    private boolean isRitual;

    public static Stage stage;

    public Button Warrior;
    public Button BeastWarrior;
    public Button Fiend;
    public Button Aqua;
    public Button Beast;
    public Button Pyro;
    public Button Spellcaster;
    public Button Thunder;
    public Button Dragon;
    public Button Machine;
    public Button Rock;
    public Button Insect;
    public Button Cybrese;
    public Button Fairy;
    public Button SerpentSea;
    public Label priceLabel;
    public TextField nameTextField;
    public TextField attackTextField;
    public TextField defenceTextField;
    public TextField levelTextField;


    @FXML
    public void initialize() {
        monsterCards = MonsterCard.getMonsterCards();
        int count = 0;
        for (MonsterCard monsterCard : monsterCards) {
            int x = count % 2;
            int y = (count + x) / 2 - 1;
            Button button = new Button();
            button.setLayoutX(x * 655 + 260);
            button.setLayoutY(y * 25 + 100);
            button.setPrefWidth(650);
            button.setPrefHeight(25);
            button.setText(monsterCard.getDescription());
            button.setStyle("-fx-background-color: gray");
            button.setOnAction(e -> {
                setThisButton(button, monsterCard);
            });
            pane.getChildren().add(button);
            count++;
        }
    }

    private void setThisButton(Button button, MonsterCard monsterCard) {
        if (monsters.contains(monsterCard)) {
            button.setStyle("-fx-background-color: gray");
            monsters.remove(monsterCard);
        } else {
            button.setStyle("-fx-background-color: cyan");
            monsters.add(monsterCard);
        }
    }

    @Override
    public void start(Stage stage) throws Exception {
        CreateMonsterCard.stage = stage;
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/CreateMonsterCard.fxml"));
        stage.setScene(new Scene(root));
        stage.show();
    }

    public void submit(MouseEvent mouseEvent) throws Exception {
        CreateCardController.createMonsterCard(levelTextField.getText(), attackTextField.getText(), defenceTextField.getText(), monsterMode
        ,name, countThePrice(), monsters);
        new MainMenu().start(stage);
    }

//    private void createCard() {
//        try {
//            ArrayList<String> names = new ArrayList<>();
//            price = countThePrice();
//            level = Integer.parseInt(levelTextField.getText());
//            attack = Integer.parseInt(attackTextField.getText());
//            defence = Integer.parseInt(defenceTextField.getText());
//            name = nameTextField.getText();
//            boolean isRitual = false;
//            for (MonsterCard monster : monsters) {
//                description += monster.getDescription();
//                names.add(monster.getName());
//                if (monster.isRitual()) isRitual = true;
//            }
//            if (monsterMode != null && name != null) {
//                new MonsterCard(level, attack, defence, monsterMode, isRitual, name, price, description, names);
//            }
//            MainMenu.user.decreaseMoney(price / 10);
//        } catch (Exception e) {
//
//        }
//    }

    private int countThePrice() {
        try {
            level = Integer.parseInt(levelTextField.getText());
            attack = Integer.parseInt(attackTextField.getText());
            defence = Integer.parseInt(defenceTextField.getText());
            int price = attack + defence;
            price = price * (20 + level) / 20;
            price = price * (10 + monsters.size()) / 10;
            price = price + 200 * monsters.size();
            return price;
        } catch (Exception e) {
            return 0;
        }
    }

    public void beSerpentSea(MouseEvent mouseEvent) {
        monsterMode = MonsterMode.SEASERPENT;
        makeAllButtonsGray();
        turnThisButtonToCyan(SerpentSea);
    }

    public void beFairy(MouseEvent mouseEvent) {
        monsterMode = MonsterMode.FAIRY;
        makeAllButtonsGray();
        turnThisButtonToCyan(Fairy);
    }

    public void beCybrese(MouseEvent mouseEvent) {
        monsterMode = MonsterMode.CYBERSE;
        makeAllButtonsGray();
        turnThisButtonToCyan(Cybrese);
    }

    public void beInsect(MouseEvent mouseEvent) {
        monsterMode = MonsterMode.INSECT;
        makeAllButtonsGray();
        turnThisButtonToCyan(Insect);
    }

    public void beRock(MouseEvent mouseEvent) {
        monsterMode = MonsterMode.ROCK;
        makeAllButtonsGray();
        turnThisButtonToCyan(Rock);
    }

    public void beMachine(MouseEvent mouseEvent) {
        monsterMode = MonsterMode.MACHINE;
        makeAllButtonsGray();
        turnThisButtonToCyan(Machine);
    }

    public void beDragon(MouseEvent mouseEvent) {
        monsterMode = MonsterMode.DRAGON;
        makeAllButtonsGray();
        turnThisButtonToCyan(Dragon);
    }


    public void beThunder(MouseEvent mouseEvent) {
        monsterMode = MonsterMode.THUNDER;
        makeAllButtonsGray();
        turnThisButtonToCyan(Thunder);
    }

    public void beSpellcaster(MouseEvent mouseEvent) {
        monsterMode = MonsterMode.SPELLCASTER;
        makeAllButtonsGray();
        turnThisButtonToCyan(Spellcaster);
    }

    public void bePyro(MouseEvent mouseEvent) {
        monsterMode = MonsterMode.PYRO;
        makeAllButtonsGray();
        turnThisButtonToCyan(Pyro);
    }

    public void beBeast(MouseEvent mouseEvent) {
        monsterMode = MonsterMode.BEAST;
        makeAllButtonsGray();
        turnThisButtonToCyan(Beast);
    }

    public void beAqua(MouseEvent mouseEvent) {
        monsterMode = MonsterMode.AQUA;
        makeAllButtonsGray();
        turnThisButtonToCyan(Aqua);
    }

    public void beFiend(MouseEvent mouseEvent) {
        monsterMode = MonsterMode.FIEND;
        makeAllButtonsGray();
        turnThisButtonToCyan(Fiend);
    }

    public void beBeastWarrior(MouseEvent mouseEvent) {
        monsterMode = MonsterMode.BEAST_WARRIOR;
        makeAllButtonsGray();
        turnThisButtonToCyan(BeastWarrior);
    }

    public void beWarrior(MouseEvent mouseEvent) {
        monsterMode = MonsterMode.WARRIOR;
        makeAllButtonsGray();
        turnThisButtonToCyan(Warrior);
    }

    private void turnThisButtonToCyan(Button button) {
        button.setStyle("-fx-background-color: cyan");
    }

    private void makeAllButtonsGray() {
        Warrior.setStyle("-fx-background-color: gray");
        Aqua.setStyle("-fx-background-color: gray");
        Beast.setStyle("-fx-background-color: gray");
        BeastWarrior.setStyle("-fx-background-color: gray");
        Pyro.setStyle("-fx-background-color: gray");
        Spellcaster.setStyle("-fx-background-color: gray");
        Thunder.setStyle("-fx-background-color: gray");
        Dragon.setStyle("-fx-background-color: gray");
        Machine.setStyle("-fx-background-color: gray");
        Insect.setStyle("-fx-background-color: gray");
        Fairy.setStyle("-fx-background-color: gray");
        Fiend.setStyle("-fx-background-color: gray");
        SerpentSea.setStyle("-fx-background-color: gray");
        Rock.setStyle("-fx-background-color: gray");
        Cybrese.setStyle("-fx-background-color: gray");
    }

    public void countPrice(MouseEvent mouseEvent) {
        priceLabel.setText(String.valueOf(countThePrice()));
    }

    public void back(MouseEvent mouseEvent) throws Exception {
        new MainMenu().start(stage);
    }
}
