package view;

import Exceptions.MyException;
import Utility.Sounds;
import controllers.BattlePhaseController;
import controllers.Game;
import controllers.PhaseControl;
import controllers.move.SetSpell;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import models.*;
import models.card.monster.MonsterCard;
import models.card.spell.SpellCard;
import view.phase.BattlePhase;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class GameView extends Application {
    private static Stage stage;
    public Pane pane;
    public Button messageBox;
    public ProgressBar turnProgress;
    public ProgressBar rivalProgress;
    public int howManyHeraldOfCreationDidWeUseEffect = 0;
    public Label turnName;
    public Label rivalName;
    public Label turnLP;
    public Label rivalLP;
    public ImageView turnAvatar;
    public ImageView rivalAvatar;
    public ImageView imageViewInfo;
    public Button exitButton;
    public Button resumeButton;
    public Button pauseBackground;
    public Address selectedCardAddress;
    public Address submitedAddrees;
    public ImageView[] turnHand = new ImageView[8];
    public ImageView[] turnMonsters = new ImageView[7];
    public ImageView[] turnSpells = new ImageView[7];
    public ImageView[] rivalHand = new ImageView[8];
    public ImageView[] rivalMonsters = new ImageView[7];
    public ImageView[] rivalSpells = new ImageView[7];
    public boolean[] wasDisable = new boolean[4];
    public ImageView[][] graveYardSmallImages = new ImageView[11][7];
    public Button submitButton;
    public TextField messageFromPlayer;
    public Button drawPhase;
    public Button standbyPhase;
    public Button mainPhase1;
    public Button battlePhase;
    public Button mainPhase2;
    public Button endPhase;
    public ImageView pauseButton;
    public ImageView turnGraveyard;
    public ImageView rivalGraveyard;
    public ImageView turnField;
    public ImageView rivalField;
    public Label turnUserName;
    public Label rivalUserName;
    public Boolean sendData = false;
    public String firstInput;
    public String secondInput;
    public String thirdInput;
    public String tributeCard1;
    public String tributeCard2;
    public String tributeCard3;
    public String cardName;
    public String answer;
    public Boolean yesOrNo;
    public int i = 1;
    public ImageView rivalDeck;
    public int sumOfLevel = 0;
    public List<Address> monsterCardsAddress = new ArrayList<>();
    public int counterTwinTwister = 0;
    public String twinTwister1;
    public String twinTwister2;
    public String twinTwister3;

    public void summonForTribute(int numberOfTributes, Address address) throws MyException {
        newMessageToLabel(Game.getCurrentPhase());
        if (!Game.isAITurn())
            addMessageToLabel("Select" + numberOfTributes + "monsters for tribute\nType numbers from monster zone from 1 to 5 and submit separately\nType 'cancel' to cancel the tribute");
        if (numberOfTributes == 1) PhaseControl.getInstance().summonAMediumLevelMonster(address);
        else if (numberOfTributes == 2) PhaseControl.getInstance().summonAHighLevelMonster(address);
        else if (numberOfTributes == 3) PhaseControl.getInstance().summonASuperHighLevelMonster(address);
    }

    public void scanForTribute(int i, Address address, MonsterCard monsterCard, String size) {
        setStateOfSubmit(false);
        setButtonsActivate(true);
        newMessageToLabel(Game.getCurrentPhase());
        if (Game.isAITurn()) {
            setStateOfSubmit(true);
            setButtonsActivate(false);
            if (i == 1) {
                tributeCard1 = scanForAITribute(i);
            } else if (i == 2) {
                tributeCard2 = scanForAITribute(i);
            } else if (i == 3) {
                tributeCard3 = scanForAITribute(i);
            }
        } else {
            addMessageToLabel("Select a monsters for tribute\nType a number from monster zone from 1 to 5\nType 'cancel' to cancel the tribute");
            if (i == 1) {
                tributeCard1 = messageFromPlayer.getText();
            } else if (i == 2) {
                tributeCard2 = messageFromPlayer.getText();;
            } else if (i == 3) {
                tributeCard3 = messageFromPlayer.getText();;
            }
            setStateOfSubmit(false);
            setButtonsActivate(true);
            submitButton.setOnMouseClicked(e -> {
                String currentTributeCard = null;
                setTributeCard(i);
                if (i == 1) {
                    currentTributeCard = tributeCard1;
                } else if (i == 2) {
                    currentTributeCard = tributeCard2;
                } else if (i == 3) {
                    currentTributeCard = tributeCard3;
                }
                assert currentTributeCard != null;
                if (!(currentTributeCard.matches("[12345]{1}") && !(currentTributeCard.matches("cancel")))) {
                    newMessageToLabel(Game.getCurrentPhase());
                    addMessageToLabel("Incorrect input");
                    addMessageToLabel("Select a monsters for tribute\nType a number from monster zone from 1 to 5\nType 'cancel' to cancel the tribute");
                } else {
                    setStateOfSubmit(true);
                    setButtonsActivate(false);
                    if (size.equals("medium")) {
                        try {
                            PhaseControl.getInstance().continueMediumLevelSummon(address, Game.whoseTurnPlayer(), monsterCard, currentTributeCard);
                        } catch (MyException myException) {
                            newMessageToLabel(Game.getCurrentPhase());
                            addMessageToLabel(myException.getMessage());
                        }
                    } else if (size.equals("high1")) {
                        try {
                            PhaseControl.getInstance().continueFirstHigh(address, Game.whoseTurnPlayer(), monsterCard, tributeCard1);
                        } catch (MyException myException) {
                            newMessageToLabel(Game.getCurrentPhase());
                            addMessageToLabel(myException.getMessage());
                        }
                    } else if (size.equals("high2")) {
                        try {
                            PhaseControl.getInstance().continueSecondHigh(address, Game.whoseTurnPlayer(), monsterCard, tributeCard1, tributeCard2);
                        } catch (MyException myException) {
                            newMessageToLabel(Game.getCurrentPhase());
                            addMessageToLabel(myException.getMessage());
                        }
                    } else if (size.equals("superHigh1")) {
                        try {
                            PhaseControl.getInstance().continueFirstSuperHigh(address, Game.whoseTurnPlayer(), monsterCard, tributeCard1);
                        } catch (MyException myException) {
                            newMessageToLabel(Game.getCurrentPhase());
                            addMessageToLabel(myException.getMessage());
                        }
                    } else if (size.equals("superHigh2")) {
                        try {
                            PhaseControl.getInstance().continueSecondSuperHigh(address, Game.whoseTurnPlayer(), monsterCard, tributeCard1, tributeCard2);
                        } catch (MyException myException) {
                            newMessageToLabel(Game.getCurrentPhase());
                            addMessageToLabel(myException.getMessage());
                        }
                    } else if (size.equals("superHigh3")) {
                        try {
                            PhaseControl.getInstance().continueThirdSuperHigh(address, Game.whoseTurnPlayer(), tributeCard1, tributeCard2, tributeCard3);
                        } catch (MyException myException) {
                            newMessageToLabel(Game.getCurrentPhase());
                            addMessageToLabel(myException.getMessage());
                        }
                    }
                    Game.getGameView().messageFromPlayer.setText("");
                }
            });
        }
    }

    public void ritualSummon(Address monsterCardAddress, int monsterLevel) {
        sumOfLevel = 0;
        setStateOfSubmit(false);
        setButtonsActivate(true);
        newMessageToLabel(Game.getCurrentPhase());
        Address ritualSpellCardAddress = Game.whoseTurnPlayer().getOneOfRitualSpellCardAddress();
        if (ritualSpellCardAddress != null) {
            if (Game.whoseTurnPlayer().canIContinueTribute(monsterLevel, monsterCardsAddress)) {
                if (Game.isAITurn())
                    monsterCardsAddress = selectAIRitual(monsterCardsAddress, sumOfLevel, monsterLevel);
                else {
                    getMonsterForRitual(monsterLevel, ritualSpellCardAddress, monsterCardAddress);
                }
            } else if (!Game.isAITurn()) {
                newMessageToLabel(Game.getCurrentPhase());
                addMessageToLabel("You do not have the requirements to Summon this card");
                setStateOfSubmit(true);
                setButtonsActivate(false);
                Game.getGameView().messageFromPlayer.setText("");
            }
        }
    }

    private void getMonsterForRitual(int monsterLevel, Address ritualSpellCardAddress, Address monsterCardAddress) {
        addMessageToLabel("Please choose some monsters from your hand or on the board for tribute!\n" +
                "Sum of the chosen monsters' level should be equal to level the monster you want to summon ritually" +
                "\nSubmit them separately");
        setStateOfSubmit(false);
        setButtonsActivate(true);
        submitButton.setOnMouseClicked(e -> {
            if (sumOfLevel < monsterLevel && Game.whoseTurnPlayer().canIContinueTribute(monsterLevel - sumOfLevel, monsterCardsAddress)) {
                Address address = new Address(messageFromPlayer.getText());
                MonsterCard monsterCard1 = Board.whatKindaMonsterIsHere(address);
                monsterCardsAddress.add(address);
                sumOfLevel += monsterCard1.getLevel();
                getMonsterForRitual(monsterLevel, ritualSpellCardAddress, monsterCardAddress);
            } else if (sumOfLevel == monsterLevel) {
                tributeThisCards(monsterCardsAddress);
                for (Address cardsAddress : monsterCardsAddress) Game.whoseTurnPlayer().removeCard(cardsAddress);
                Game.whoseTurnPlayer().removeCard(ritualSpellCardAddress);
                Game.whoseTurnPlayer().summonCardToMonsterZone(monsterCardAddress);
                int index = Game.whoseTurnPlayer().getIndexOfThisCardByAddress(monsterCardAddress);
                Game.whoseTurnPlayer().setDidWeChangePositionThisCardInThisTurn(index);
                Game.whoseTurnPlayer().setHeSummonedOrSet(true);
            } else {
                giveError(monsterLevel, ritualSpellCardAddress, monsterCardAddress);
            }
            messageFromPlayer.setText("");
        });
    }

    private void giveError(int monsterLevel, Address ritualSpellCardAddress, Address monsterCardAddress) {
        newMessageToLabel(Game.getCurrentPhase());
        addMessageToLabel("You can't tribute this card");
        getMonsterForRitual(monsterLevel, ritualSpellCardAddress, monsterCardAddress);
    }

    private List<Address> selectAIRitual(List<Address> monsterCardsAddress, int sumOfLevel, int monsterLevel) {
        for (int i = 1; i <= 5; i++) {
            Address address = new Address(i, "monster", true);
            if (Game.whoseTurnPlayer().getCardByAddress(address) == null)
                continue;
            monsterCardsAddress.add(address);
            MonsterCard monsterCard1 = Board.whatKindaMonsterIsHere(address);
            sumOfLevel += monsterCard1.getLevel();
            if (!(sumOfLevel < monsterLevel && Game.whoseTurnPlayer().canIContinueTribute(monsterLevel - sumOfLevel, monsterCardsAddress))) {
                monsterCardsAddress.remove(address);
                sumOfLevel -= monsterCard1.getLevel();
            }
        }
        return monsterCardsAddress;
    }

    private void setTributeCard(int i) {
        if (i == 1) {
            tributeCard1 = messageFromPlayer.getText();
        } else if (i == 2) {
            tributeCard2 = messageFromPlayer.getText();
        } else if (i == 3) {
            tributeCard3 = messageFromPlayer.getText();
        }
    }

    private void tributeThisCards(List<Address> monsterCardsAddress) {
        for (Address cardsAddress : monsterCardsAddress) {
            Game.whoseTurnPlayer().removeCard(cardsAddress);
        }
    }

    private String scanForAITribute(int number) {
        Player player = Game.whoseTurnPlayer();
        int minAttack = 1000000;
        int place1 = 0;
        int place2 = 0;
        int place3 = 0;
        for (int i = 1; i < 6; i++) {
            if (player.getMonsterZoneCard().containsKey(i) && player.getCardMonster(i).getAttack() < minAttack) {
                minAttack = player.getCardMonster(i).getAttack();
                place1 = i;
            }
        }
        minAttack = 1000000;
        for (int i = 1; i < 6; i++) {
            if (player.getMonsterZoneCard().containsKey(i) && player.getCardMonster(i).getAttack() < minAttack && i != place1) {
                minAttack = player.getCardMonster(i).getAttack();
                place2 = i;
            }
        }
        minAttack = 1000000;
        for (int i = 1; i < 6; i++) {
            if (player.getMonsterZoneCard().containsKey(i) && player.getCardMonster(i).getAttack() < minAttack && i != place1 && i != place2) {
                minAttack = player.getCardMonster(i).getAttack();
                place3 = i;
            }
        }
        if (number == 1)
            return String.valueOf(place1);
        if (number == 2)
            return String.valueOf(place2);
        else
            return String.valueOf(place3);
    }

    public void AIEffect(String cardName) {
        if (cardName.equals("Suijin"))
            answer = "yes";
        if (cardName.equals("Man-Eater Bug"))
            answer = Integer.toString(BattlePhase.getInstance().getStrongestMonster(Game.whoseRivalPlayer()));
        if (cardName.equals("Scanner"))
            answer = "1";
        if (cardName.equals("Beast King Barbaros"))
            answer = "2";
        if (cardName.equals("Herald of Creation1"))
            answer = "0";
        if (cardName.equals("Herald of Creation2")) {
            Game.getGameView().scanForTribute(1, null, null, null);
            answer = Game.getGameView().tributeCard1;
        }
        if (cardName.equals("Herald of Creation3"))
            answer = "0";
        if (cardName.equals("Terratiger, the Empowered Warrior"))
            answer = "0";
        if (cardName.equals("Terraforming"))
            answer = "Umiiruka";
        if (cardName.equals("Twin Twisters")) {
            twinTwister1 = "1";
            twinTwister3 = "1";
            twinTwister3 = "2";
        }
        if (cardName.equals("Mystical space typhoon"))
            answer = "1";
        else answer = "0";
    }

    public void runEffect(String cardName, Address address, Address shouldBeRemoved) {
        newMessageToLabel(Game.getCurrentPhase());
        setButtonsActivate(true);
        setStateOfSubmit(false);
        Game.getGameView().newMessageToLabel(Game.getCurrentPhase());
        if (Game.isAITurn()) {
            setButtonsActivate(false);
            setStateOfSubmit(true);
            AIEffect(cardName);
        }
        if (cardName.equals("Suijin"))
            addMessageToLabel("Do you want to use Suijin's effect?\nType 'yes' or 'no'");
        if (cardName.equals("Man-Eater Bug"))
            addMessageToLabel("Which rival's monster card you want to be destroyed because of the effect of Man Eater Bug?\nType monster zone number from 1 to 5");
        if (cardName.equals("Scanner"))
            addMessageToLabel("Which card do you want to be scanned from rival's graveyard for Scanner?\nType graveyard number");
        if (cardName.equals("Beast King Barbaros"))
            addMessageToLabel("How many monsters do you want to tribute for Beast King Barbaros?\nType 2 or 3");
        if (cardName.equals("Herald of Creation1"))
            addMessageToLabel("How many Herald Of Creation cards do you want to use?");
        if (cardName.equals("Herald of Creation2"))
            addMessageToLabel("Choose a card for tribute\nType monster zone number from 1 to 5");
        if (cardName.equals("Herald of Creation3"))
            addMessageToLabel("Choose a monster card with level 7 or more from your graveyard\ntype number");
        if (cardName.equals("Terratiger, the Empowered Warrior"))
            addMessageToLabel("Choose a monster card with level 4 or less from your hand!\nType a hand zone number from 1 to 6 or 0 if you want to cancel");
        if (cardName.equals("Terraforming"))
            addMessageToLabel("Type the name of the field spell card from your deck to come to your hand");
        if (cardName.equals("Twin Twisters"))
            addMessageToLabel("Choose a card from your hand to remove and choose two of rival's trap or trap to be destroyed!\ntype 3 numbers and submit them separately");
        if (cardName.equals("Mystical space typhoon"))
            addMessageToLabel("Choose one of rival's spell or trap to be destroyed\ntype a spell zone number from 1 to 5");
        checkSpellInput(cardName, address, shouldBeRemoved);
    }

    private void checkSpellInput(String cardName, Address address, Address shouldBeRemoved) {
        submitButton.setOnMouseClicked(e -> {
            if (cardName.equals("Suijin"))
                checkSuijinInput(cardName, address, shouldBeRemoved);
            if (cardName.equals("Man-Eater Bug"))
                checkManEaterBugInput(cardName, address, shouldBeRemoved);
            if (cardName.equals("Scanner"))
                checkScannerInput(cardName, address, shouldBeRemoved);
            if (cardName.equals("Beast King Barbaros")) {
                try {
                    checkBeastKingBarbarosInput(cardName, address, shouldBeRemoved);
                } catch (MyException myException) {
                    newMessageToLabel(Game.getCurrentPhase());
                    addMessageToLabel(myException.getMessage());
                    reset();
                }
            }
            if (cardName.equals("Herald of Creation1"))
                checkHeraldOfCreation1Input(cardName, address, shouldBeRemoved);
            if (cardName.equals("Herald of Creation2"))
                checkHeraldOfCreation2Input(cardName, address, shouldBeRemoved);
            if (cardName.equals("Herald of Creation3"))
                checkHeraldOfCreation3Input(cardName, address, shouldBeRemoved);
            if (cardName.equals("Terratiger, the Empowered Warrior")) {
                try {
                    checkTerratigerInput(cardName, address, shouldBeRemoved);
                } catch (MyException myException) {
                    newMessageToLabel(Game.getCurrentPhase());
                    addMessageToLabel(myException.getMessage());
                    reset();
                }
            }
            if (cardName.equals("Terraforming")) {
                try {
                    checkTerraformingInput(cardName, address, shouldBeRemoved);
                } catch (MyException myException) {
                    newMessageToLabel(Game.getCurrentPhase());
                    addMessageToLabel(myException.getMessage());
                    reset();
                }
            }
            if (cardName.equals("Twin Twisters"))
                checkTwinTwistersInput(cardName, address, shouldBeRemoved);
            if (cardName.equals("Mystical space typhoon"))
                checkMysticSpaceTyphoonInput(cardName, address, shouldBeRemoved);
            messageFromPlayer.setText("");
        });
    }

    private void checkMysticSpaceTyphoonInput(String cardName, Address address, Address shouldBeRemoved) {
        if (messageFromPlayer.getText().matches("[012345]{1}")) {
            answer = messageFromPlayer.getText();
            setButtonsActivate(false);
            setStateOfSubmit(true);
            SpellCard.doMysticalSpaceTyphoonEffect(Game.whoseTurnPlayer());
        } else {
            newMessageToLabel(Game.getCurrentPhase());
            addMessageToLabel("Incorrect input");
            addMessageToLabel("Choose one of rival's spell or trap to be destroyed\ntype a spell zone number from 1 to 5");
            checkSpellInput(cardName, address, shouldBeRemoved);
        }
    }

    private void checkTwinTwistersInput(String cardName, Address address, Address shouldBeRemoved) {
        if (counterTwinTwister == 0) {
            if (messageFromPlayer.getText().matches("[123456]{1}")) {
                twinTwister1 = messageFromPlayer.getText();
                counterTwinTwister++;
                checkTwinTwistersInput(cardName, address, shouldBeRemoved);
            } else {
                newMessageToLabel(Game.getCurrentPhase());
                addMessageToLabel("Incorrect input");
                addMessageToLabel("Choose a card from your hand to remove and choose two of rival's spell or trap to be destroyed!\ntype 3 numbers and submit them separately");
                checkSpellInput(cardName, address, shouldBeRemoved);
            }
        } else if (counterTwinTwister == 1) {
            if (messageFromPlayer.getText().matches("[12345]{1}")) {
                setButtonsActivate(false);
                setStateOfSubmit(true);
                twinTwister2 = messageFromPlayer.getText();
                counterTwinTwister++;
                checkTwinTwistersInput(cardName, address, shouldBeRemoved);
            } else {
                newMessageToLabel(Game.getCurrentPhase());
                addMessageToLabel("Incorrect input");
                addMessageToLabel("Choose a card from your hand to remove and choose two of rival's spell or trap to be destroyed!\ntype 3 numbers and submit them separately");
                checkSpellInput(cardName, address, shouldBeRemoved);
            }
        } else if (counterTwinTwister == 2) {
            if (messageFromPlayer.getText().matches("[123456]{1}")) {
                twinTwister3 = messageFromPlayer.getText();
                counterTwinTwister = 0;
                SpellCard.doTwinTwistersEffect(Game.whoseTurnPlayer());
            } else {
                newMessageToLabel(Game.getCurrentPhase());
                addMessageToLabel("Incorrect input");
                addMessageToLabel("Choose a card from your hand to remove and choose two of rival's spell or trap to be destroyed!\ntype 3 numbers and submit them separately");
                checkSpellInput(cardName, address, shouldBeRemoved);
            }
        }
    }

    private void checkTerraformingInput(String cardName, Address address, Address shouldBeRemoved) throws MyException {
        setButtonsActivate(false);
        setStateOfSubmit(true);
        answer = messageFromPlayer.getText();
        SpellCard.doTerraformingEffect(Game.whoseTurnPlayer());
    }

    private void checkTerratigerInput(String cardName, Address address, Address shouldBeRemoved) throws MyException {
        if (messageFromPlayer.getText().matches("[0123456]{1}")) {
            setButtonsActivate(false);
            setStateOfSubmit(true);
            answer = messageFromPlayer.getText();
            PhaseControl.getInstance().doTerratigerEffect(Game.whoseTurnPlayer());
        } else {
            newMessageToLabel(Game.getCurrentPhase());
            addMessageToLabel("Incorrect input");
            addMessageToLabel("Choose a monster card with level 4 or less from your hand!\nType a hand zone number from 1 to 6 or 0 if you want to cancel");
            checkSpellInput(cardName, address, shouldBeRemoved);
        }
    }

    private void checkHeraldOfCreation3Input(String cardName, Address address, Address shouldBeRemoved) {
        if (messageFromPlayer.getText().matches("[\\d]+")) {
            setButtonsActivate(false);
            setStateOfSubmit(true);
            answer = messageFromPlayer.getText();
            PhaseControl.getInstance().doHeraldOfCreation3Effect(Game.whoseTurnPlayer(), shouldBeRemoved);
        } else {
            newMessageToLabel(Game.getCurrentPhase());
            addMessageToLabel("Incorrect input");
            addMessageToLabel("Choose a monster card with level 7 or more from your graveyard\ntype number");
            checkSpellInput(cardName, address, shouldBeRemoved);
        }
    }

    private void checkHeraldOfCreation2Input(String cardName, Address address, Address shouldBeRemoved) {
        if (messageFromPlayer.getText().matches("[12345]{1}")) {
            setButtonsActivate(false);
            setStateOfSubmit(true);
            answer = messageFromPlayer.getText();
            PhaseControl.getInstance().doHeraldOfCreation2Effect(Game.whoseTurnPlayer());
        } else {
            newMessageToLabel(Game.getCurrentPhase());
            addMessageToLabel("Incorrect input");
            addMessageToLabel("Choose a card for tribute\nType monster zone number from 1 to 5");
            checkSpellInput(cardName, address, shouldBeRemoved);
        }
    }

    private void checkHeraldOfCreation1Input(String cardName, Address address, Address shouldBeRemoved) {
        if (messageFromPlayer.getText().matches("[\\d]+")) {
            setButtonsActivate(false);
            setStateOfSubmit(true);
            answer = messageFromPlayer.getText();
            PhaseControl.getInstance().doHeraldOfCreation1Effect(Game.whoseTurnPlayer());
        } else {
            newMessageToLabel(Game.getCurrentPhase());
            addMessageToLabel("Incorrect input");
            addMessageToLabel("How many Herald Of Creation cards do you want to use?");
            checkSpellInput(cardName, address, shouldBeRemoved);
        }
    }

    private void checkBeastKingBarbarosInput(String cardName, Address address, Address shouldBeRemoved) throws MyException {
        if (messageFromPlayer.getText().matches("[23]{1}")) {
            setButtonsActivate(false);
            setStateOfSubmit(true);
            answer = messageFromPlayer.getText();
            PhaseControl.getInstance().doBeastKingBarbarosEffect(address);
        } else {
            newMessageToLabel(Game.getCurrentPhase());
            addMessageToLabel("Incorrect input");
            addMessageToLabel("How many monsters do you want to tribute for Beast King Barbaros?\nType 2 or 3");
            checkSpellInput(cardName, address, shouldBeRemoved);
        }
    }

    private void checkScannerInput(String cardName, Address address, Address shouldBeRemoved) {
        if (messageFromPlayer.getText().matches("[\\d]+")) {
            setButtonsActivate(false);
            setStateOfSubmit(true);
            answer = messageFromPlayer.getText();
        } else {
            newMessageToLabel(Game.getCurrentPhase());
            addMessageToLabel("Incorrect input");
            addMessageToLabel("Which card do you want to be scanned from rival's graveyard for Scanner?\nType graveyard number");
            checkSpellInput(cardName, address, shouldBeRemoved);
        }
    }

    private void checkManEaterBugInput(String cardName, Address address, Address shouldBeRemoved) {
        if (messageFromPlayer.getText().matches("^[12345]{1}$")) {
            setButtonsActivate(false);
            setStateOfSubmit(true);
            answer = messageFromPlayer.getText();
            PhaseControl.getInstance().doManEaterBugEffect();
        } else {
            newMessageToLabel(Game.getCurrentPhase());
            addMessageToLabel("Incorrect input");
            addMessageToLabel("Which rival's monster card you want to be destroyed because of the effect of Man Eater Bug?\nType monster zone number from 1 to 5");
            checkSpellInput(cardName, address, shouldBeRemoved);
        }
    }

    private void checkSuijinInput(String cardName, Address address, Address shouldBeRemoved) {
        if (messageFromPlayer.getText().matches("^(yes|no)$")) {
            setButtonsActivate(false);
            setStateOfSubmit(true);
            answer = messageFromPlayer.getText();
            MonsterCard.doSuijinEffect();
        } else {
            newMessageToLabel(Game.getCurrentPhase());
            addMessageToLabel("Incorrect input");
            addMessageToLabel("Do you want to use Suijin's effect?\nType 'yes' or 'no'");
            checkSpellInput(cardName, address, shouldBeRemoved);
        }
    }

    public void getMonsterForEquip(Address address) {
        newMessageToLabel(Game.getCurrentPhase());
        addMessageToLabel("Choose a faced up monster to be equipped with this spell\nType a number from 1 to 5 in monster zone");
        checkInputEquip(address);
    }

    private void checkInputEquip(Address address) {
        setButtonsActivate(true);
        setStateOfSubmit(false);
        submitButton.setOnMouseClicked(e -> {
            if(messageFromPlayer.getText().matches("^[12345]{1}$")){
                answer = messageFromPlayer.getText();
                setButtonsActivate(false);
                setStateOfSubmit(true);
                SpellCard.doEquipSpellEffect(address);
            } else {
                newMessageToLabel(Game.getCurrentPhase());
                addMessageToLabel("Incorrect input");
                addMessageToLabel("Choose a faced up monster to be equipped with this spell\nType a number from 1 to 5 in monster zone");
                checkInputEquip(address);
            }
            messageFromPlayer.setText("");
        });
    }

    private void increaseCount() {
        i++;
    }

    private void setFirstInput() {
        firstInput = messageFromPlayer.getText();
    }

    private void setSecondInput() {
        secondInput = messageFromPlayer.getText();
    }

    private void setThirdInput() {
        thirdInput = messageFromPlayer.getText();
    }

    private void sendDataTrue() {
        sendData = true;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        stage = primaryStage;
        pane = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/fxml/GameView.fxml")));
        createBackground("normal");
        createBackgroundCards();
        createPlayers();
        Scene scene = new Scene(pane);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    public void initialize() throws Exception {
        setClassStyle();
        Game.setGameView(this);
        Game.setPhase("Draw Phase");
        setAvatar();
        setNames();
        setLP();
        createImageView(turnHand, 260, 535, 6);
        createImageView(rivalHand, 260, -50, 6);
        createImageView(turnMonsters, 338, 310, 5);
        createImageView(rivalMonsters, 338, 190, 5);
        createImageView(turnSpells, 338, 420, 5);
        createImageView(rivalSpells, 338, 90, 5);
        createFieldsEvent(turnField);
        createFieldsEvent(rivalField);
        createGraveYardSmallImages();
        createGraveYardEvents(turnGraveyard, false);
        createGraveYardEvents(rivalGraveyard, true);
        createPauseButtons();
        setHand();
        initializeMouseClick();
        doDrawPhase(Game.whoseTurnPlayer());
    }

    private void setClassStyle() {
        File file = new File ("src//main//resources//CSS//Buttons.css");
        pane.getStylesheets().add(file.toURI().toString());
    }

    private void createPauseButtons() {
        setPauseBackground();
        createResume();
        createExit();
    }

    private void createExit() {
        exitButton = new Button();
        exitButton.setLayoutX(470);
        exitButton.setLayoutY(310);
        exitButton.setText("Exit");
        resumeAndExitCreate(exitButton);
        exitButton.setOnMouseClicked(e -> {
            exit();
        });
    }

    private void resumeAndExitCreate(Button button) {
        button.setPrefWidth(140);
        button.setPrefHeight(47);
        button.setDisable(true);
        button.setVisible(false);
        button.setFont(Font.font("Yu-Gi-Oh! StoneSerif LT", FontWeight.BOLD, 19));
        button.setId("pauseButton");
        pane.getChildren().add(button);
    }

    private void createResume() {
        resumeButton = new Button();
        resumeButton.setLayoutX(470);
        resumeButton.setLayoutY(240);
        resumeButton.setText("Resume");
        resumeAndExitCreate(resumeButton);
        resumeButton.setOnMouseClicked(e -> {
            resumeGame();
        });
    }

    private void setPauseBackground() {
        pauseBackground = new Button();
        pauseBackground.setLayoutX(420);
        pauseBackground.setLayoutY(200);
        pauseBackground.setDisable(true);
        pauseBackground.setVisible(false);
        pauseBackground.setPrefWidth(235);
        pauseBackground.setPrefHeight(200);
        pauseBackground.setOpacity(0.75);
        pauseBackground.setStyle("-fx-background-color: #f8b090");
        pane.getChildren().add(pauseBackground);
    }

    private void createGraveYardSmallImages() {
        for (int i = 1; i <= 10; i++) {
            for (int j = 1; j <= 6; j++) {
                graveYardSmallImages[i][j] = new ImageView();
                graveYardSmallImages[i][j].setLayoutX(15 + 35 * (j - 1));
                graveYardSmallImages[i][j].setLayoutY(130 + 50 * (i - 1));
                graveYardSmallImages[i][j].setFitWidth(50);
                graveYardSmallImages[i][j].setFitHeight(65);
                pane.getChildren().add(graveYardSmallImages[i][j]);
            }
        }
    }

    private void createGraveYardEvents(ImageView graveyard, boolean isRival) {
        graveyard.setOnMouseEntered(e -> {
            imageViewInfo.setVisible(false);
            setGraveYardImages(isRival);
        });
        graveyard.setOnMouseExited(e -> {
            imageViewInfo.setVisible(true);
            removeGraveYardButtons();
        });
    }

    private void setGraveYardImages(boolean isRival) {
        HashMap<Integer, Card> graveyardZone = Game.whoseTurnPlayer().getGraveyardCard();
        if (isRival)
            graveyardZone = Game.whoseRivalPlayer().getGraveyardCard();
        for (int i = 1; i <= 10; i++) {
            for (int j = 1; j <= 6; j++) {
                if (graveyardZone.containsKey(6 * (i - 1) + j)) {
                    graveYardSmallImages[i][j].setImage(createImage(graveyardZone.get(6 * (i - 1) + j).getCardName()));
                    graveYardSmallImages[i][j].setVisible(true);
                    graveYardSmallImages[i][j].setDisable(false);
                } else {
                    graveYardSmallImages[i][j].setVisible(false);
                    graveYardSmallImages[i][j].setDisable(true);
                }
            }
        }
    }

    private void removeGraveYardButtons() {
        for (int i = 1; i <= 10; i++) {
            for (int j = 1; j <= 6; j++) {
                graveYardSmallImages[i][j].setVisible(false);
                graveYardSmallImages[i][j].setDisable(true);
            }
        }
    }

    private void createFieldsEvent(ImageView field) {
        field.setOnMouseEntered(e -> {
            imageViewInfo.setImage(field.getImage());
            imageViewInfo.setVisible(true);
        });
        field.setOnMouseExited(e -> {
            imageViewInfo.setVisible(false);
        });
    }

    private void setFieldAndGraveYard() {
        filedAndGraveyardSet(Game.whoseTurnPlayer().getFieldCard(), turnField);
        filedAndGraveyardSet(Game.whoseRivalPlayer().getFieldCard(), rivalField);
        filedAndGraveyardSet(Game.whoseTurnPlayer().getGraveyardCard(), turnGraveyard);
        filedAndGraveyardSet(Game.whoseRivalPlayer().getGraveyardCard(), rivalGraveyard);
    }

    private void filedAndGraveyardSet(HashMap<Integer, Card> zone, ImageView imageView) {
        if (zone.containsKey(1)) {
            imageView.setImage(createImage(zone.get(1).getCardName()));
            imageView.setDisable(false);
            imageView.setVisible(true);
        } else {
            imageView.setDisable(true);
            imageView.setVisible(false);
        }
    }

    private void setStateOfSubmit(boolean isDisable) {
        submitButton.setDisable(isDisable);
        messageFromPlayer.setDisable(isDisable);
    }

    public void reset() {
        setHand();
        setMonster();
        setSpells();
        setLP();
        setAvatar();
        setNames();
        setFieldAndGraveYard();
    }

    private void createImageView(ImageView[] imageView, int X, int Y, int size) {
        for (int i = 1; i <= size; i++) {
            imageView[i] = new ImageView();
            imageView[i].setFitWidth(70);
            imageView[i].setFitHeight(85);
            imageView[i].setPreserveRatio(false);
            imageView[i].setLayoutX(X + 80 * (i - 1));
            imageView[i].setLayoutY(Y);
            int finalI = i;
            imageView[i].setOnMouseEntered(e -> {
                if (Y < 200 || Y > 450)
                    imageViewInfo.setImage(imageView[finalI].getImage());
                else if (Y < 350)
                    imageViewInfo.setImage(createImage(Game.whoseTurnPlayer().getCardMonster(finalI).getCardName()));
                else imageViewInfo.setImage(createImage(Game.whoseTurnPlayer().getCardSpell(finalI).getCardName()));
                imageViewInfo.setVisible(true);
            });
            imageView[i].setOnMouseExited(e -> {
                imageViewInfo.setVisible(false);
            });
            pane.getChildren().add(imageView[i]);
        }
    }

    private void initializeMouseClick() {
        setRivalDeckOnMouseClick(rivalDeck);
        for (int i = 1; i <= 6; i++) {
            setHandOnMouseClick(turnHand[i], i);
        }
        for (int i = 1; i <= 5; i++)
            setMonsterOnMouseClicked(turnMonsters[i], i);
        for (int i = 1; i <= 5; i++)
            setRivalMonsterOnMouseClicked(rivalMonsters[i], i);
        for (int j = 0; j <= 5; j++)
            setSpellOrTrapOnMouseClicked(turnSpells[i], i);
    }

    private void setRivalDeckOnMouseClick(ImageView rivalDeck) {
        rivalDeck.setOnMouseClicked(e -> {
            if (Game.getCurrentPhase().equals("Battle Phase")) {
                if (selectedCardAddress.getKind().equals("monster") && selectedCardAddress.checkIsMine()) {
                    try {
                        soundEffect(3);
                        BattlePhaseController.getInstance().directAttack(selectedCardAddress);
                        reset();
                    } catch (MyException myException) {
                        newMessageToLabel(Game.getCurrentPhase());
                        addMessageToLabel(myException.getMessage());
                        reset();
                    }
                    reset();
                }
            }
        });
    }

    private void setSelectedCard(String zone, int i, Boolean isMine) {
        selectedCardAddress = new Address(i, zone, isMine);
    }

    private void setNames() {
        turnName.setText("Name: " + Game.whoseTurnPlayer().getNickName());
        rivalName.setText("Name: " + Game.whoseRivalPlayer().getNickName());
        turnUserName.setText("User Name: " + Game.whoseTurnPlayer().getName());
        rivalUserName.setText("User Name: " + Game.whoseRivalPlayer().getName());
    }

    private void setLP() {
        turnLP.setText("LP: " + Game.whoseTurnPlayer().getLP());
        rivalLP.setText("LP: " + Game.whoseRivalPlayer().getLP());
        turnProgress.setProgress((double) Game.whoseTurnPlayer().getLP() / 8000);
        rivalProgress.setProgress((double) Game.whoseRivalPlayer().getLP() / 8000);
    }

    private void setAvatar() {
        turnAvatar.setImage(createAvatarImage(Game.whoseTurnPlayer()));
        rivalAvatar.setImage(createAvatarImage(Game.whoseRivalPlayer()));
    }

    private Image createAvatarImage(Player player) {
        File file = new File(player.getUser().getAvatarAddress());
        String string = file.toURI().toString();
        return new Image(string);
    }

    private void doDrawPhase(Player player) throws Exception {
        setStateOfSubmit(true);
        setButtonsActivate(false);
        PhaseControl.getInstance().resetMoves();
        Game.setPhase("Draw Phase");
        newMessageToLabel(Game.getCurrentPhase());
        reset();
        addMessageToLabel(Game.whoseTurnPlayer().getNickName() + "'s turn");
        Game.setDidWePassBattle(false);
        String drawCardMessage = PhaseControl.getInstance().drawOneCard();
        addMessageToLabel(drawCardMessage);
        if (drawCardMessage.startsWith("GAME"))
            Game.playTurn("EndGame");
        reset();
    }

    private void doStandByPhase() {
        Game.setPhase("Standby Phase");
        newMessageToLabel(Game.getCurrentPhase());
        reset();
        if (SetSpell.doIHaveMessengerOfPeace()) {
            addMessageToLabel("Do you want to destroy Messenger Of Peace(If not you'll lose 100 LP)?\nEnter 'yes' or 'no'");
            setStateOfSubmit(false);
            setButtonsActivate(true);
            submitButton.setOnMouseClicked(e -> {
                newMessageToLabel("Standby Phase");
                if (!messageFromPlayer.getText().equals("yes") && !messageFromPlayer.getText().equals("no")) {
                    addMessageToLabel("Incorrect input\nDo you want to destroy Messenger Of Peace(If not you'll lose 100 LP)?\nEnter 'yes' or 'no'");
                    setStateOfSubmit(false);
                    setButtonsActivate(true);
                } else if (messageFromPlayer.getText().equals("yes")) {
                    addMessageToLabel("Messenger Of Peace was destroyed");
                    setStateOfSubmit(true);
                    setButtonsActivate(false);
                } else if (messageFromPlayer.getText().equals("no")) {
                    addMessageToLabel("You lost 100 LP because of Messenger Of Peace");
                    PhaseControl.getInstance().payMessengerOfPeaceSpellCardHarm(messageFromPlayer.getText());
                    setStateOfSubmit(true);
                    setButtonsActivate(false);
                }
                messageFromPlayer.setText("");
                reset();
            });
        }
        reset();
        PhaseControl.getInstance().resetMoves();
        PhaseControl.getInstance().checkIfGameEnded();
        reset();
    }

    private void setButtonsActivate(boolean activate) {
        for (int i = 1; i <= 5; i++) {
            turnMonsters[i].setDisable(activate);
            rivalMonsters[i].setDisable(activate);
            turnHand[i].setDisable(activate);
            rivalHand[i].setDisable(activate);
            turnSpells[i].setDisable(activate);
            rivalSpells[i].setDisable(activate);
        }
        turnHand[6].setDisable(activate);
        rivalHand[6].setDisable(activate);
        battlePhase.setDisable(activate);
        mainPhase1.setDisable(activate);
        mainPhase2.setDisable(activate);
        drawPhase.setDisable(activate);
        endPhase.setDisable(activate);
        standbyPhase.setDisable(activate);
    }

    private void doMainPhase1() {
        setStateOfSubmit(true);
        setButtonsActivate(false);
        Game.setPhase("Main Phase 1");
        newMessageToLabel(Game.getCurrentPhase());
        setHowManyHeraldOfCreationDidWeUseEffect(0);
        PhaseControl.getInstance().doEffectMainPhase();
        reset();
    }

    private void doBattlePhase() {
        setStateOfSubmit(true);
        setButtonsActivate(false);
        Game.setPhase("Battle Phase");
        newMessageToLabel(Game.getCurrentPhase());
        reset();
    }

    public void permission() {
        newMessageToLabel(Game.getCurrentPhase());
        addMessageToLabel("Do you want do the effect?\nType 'yes' or 'no'");
        setStateOfSubmit(false);
        setButtonsActivate(true);
        checkAnswer();
    }

    private void checkAnswer() {
        submitButton.setOnMouseClicked(e -> {
            if (!messageFromPlayer.getText().equals("no") || !messageFromPlayer.getText().equals("yes")) {
                newMessageToLabel(Game.getCurrentPhase());
                addMessageToLabel("Incorrect input");
                addMessageToLabel("Do you want do the effect?\nType 'yes' or 'no'");
                checkAnswer();
            } else {
                answer = messageFromPlayer.getText();
                setStateOfSubmit(true);
                setButtonsActivate(false);
            }
            messageFromPlayer.setText("");
        });
    }

    private void setAnswer() {
        answer = messageFromPlayer.getText();
    }

    public void summonCyberse() {
        permission();
        if (answer.equals("yes")) {
            setStateOfSubmit(false);
            setButtonsActivate(true);
            newMessageToLabel(Game.getCurrentPhase());
            addMessageToLabel("Select a Cyberse type monster from your hand or deck or graveyard to be summoned.\nWhich card do you want to special summon?\n1.Texchanger\n2.Leotron");
            checkCyberseInput();
        }
    }

    private void checkCyberseInput() {
        submitButton.setOnMouseClicked(e -> {
            if (messageFromPlayer.getText().equals("1") || messageFromPlayer.getText().equals("2")) {
                setStateOfSubmit(true);
                setButtonsActivate(false);
                Game.whoseTurnPlayer().specialSummonThisKindOfCardFromHandOrDeckOrGraveyard(messageBox.getText());
            } else {
                newMessageToLabel(Game.getCurrentPhase());
                addMessageToLabel("Incorrect input");
                addMessageToLabel("Select a Cyberse type monster from your hand or deck or graveyard to be summoned.\nWhich card do you want to special summon?\n1.Texchanger\n2.Leotron");
                checkCyberseInput();
            }
            messageFromPlayer.setText("");
        });
    }

    public void doMindCrushEffect(Address address) {
        setStateOfSubmit(false);
        setButtonsActivate(true);
        cardName = null;
        if (!Game.isAITurn()) {
            newMessageToLabel(Game.getCurrentPhase());
            addMessageToLabel("type a card name so if rival has this kind of card all of them will be removed else one of your card will be removed randomly");
            submitButton.setOnMouseClicked(e -> {
                setCardName();
                setStateOfSubmit(true);
                setButtonsActivate(false);
                messageFromPlayer.setText("");
                PhaseControl.getInstance().doMindCrushEffect(address);
            });
        } else {
            cardName = "Beast King Barbaros";
            PhaseControl.getInstance().doMindCrushEffect(address);
        }
    }

    public void doSolemnWarningEffect(Address address, int number, MonsterCard monsterCard) throws MyException {
        if (Game.whoseRivalPlayer().doIHaveSpellCard("Solemn Warning")) {
            if (!Game.isAITurn()) {
                getPermissionForTrap("Solemn Warning", false, null, address, number, monsterCard);
            } else {
                Player currentPlayer = Game.whoseTurnPlayer();
                yesOrNo = (monsterCard.getNormalAttack() >= 2000) && (currentPlayer.getLP() > 5000);
                if (number == 1) {
                    PhaseControl.getInstance().doSolemnWarningEffect1(address);
                } else if (number == 2) {
                    PhaseControl.getInstance().doSolemnWarningEffect2(address, Game.whoseTurnPlayer(), monsterCard);
                }
            }
        } else {
            yesOrNo = false;
            if (number == 1) {
                PhaseControl.getInstance().doSolemnWarningEffect1(address);
            } else if (number == 2) {
                PhaseControl.getInstance().doSolemnWarningEffect2(address, Game.whoseTurnPlayer(), monsterCard);
            }
        }
    }

    private void doSolemnWarningGameView1(Address address, int number, MonsterCard monsterCard) throws MyException {
        if (yesOrNo) {
            Game.whoseRivalPlayer().decreaseLP(2000);
            Game.whoseTurnPlayer().removeCard(address);
            if (number == 1) {
                PhaseControl.getInstance().doSolemnWarningEffect1(address);
            } else if (number == 2) {
                PhaseControl.getInstance().doSolemnWarningEffect2(address, Game.whoseTurnPlayer(), monsterCard);
            }
        } else {
            if (number == 1) {
                PhaseControl.getInstance().doSolemnWarningEffect1(address);
            } else if (number == 2) {
                PhaseControl.getInstance().doSolemnWarningEffect2(address, Game.whoseTurnPlayer(), monsterCard);
            }
        }
    }

    public void getPermissionForTrap(String cardName, boolean isMine, MonsterCard myMonsterCard, Address address, int number, MonsterCard monsterCard) throws MyException {
        setStateOfSubmit(false);
        setButtonsActivate(true);
        if (!isMine && (!Game.getIsAI() || Game.isAITurn())) {
            newMessageToLabel(Game.getCurrentPhase());
            addMessageToLabel("Dear " + Game.whoseRivalPlayer().getNickName() + ",do you want to activate " + cardName + "trap?\nType 'yes' or 'no'");
            getAnswer(cardName, myMonsterCard, address, number, monsterCard);
        } else if (!Game.isAITurn() && isMine) {
            newMessageToLabel(Game.getCurrentPhase());
            addMessageToLabel("Dear " + Game.whoseTurnPlayer().getNickName() + ",do you want to activate " + cardName + "trap?\nType 'yes' or 'no'");
            getAnswer(cardName, myMonsterCard, address, number, monsterCard);
        } else {
            yesOrNo = !cardName.equals("Solemn Warning");
            if (number == 1) {
                PhaseControl.getInstance().doSolemnWarningEffect1(address);
            } else if (number == 2) {
                PhaseControl.getInstance().doSolemnWarningEffect2(address, Game.whoseTurnPlayer(), monsterCard);
            }
        }
    }

    private void getAnswer(String cardName, MonsterCard myMonsterCard, Address address, int number, MonsterCard monsterCard) {
        answer = null;
        sendData = false;
        submitButton.setOnMouseClicked(e -> {
            if (!messageFromPlayer.getText().equals("yes") && !messageFromPlayer.getText().equals("no")) {
                newMessageToLabel("Incorrect input");
                addMessageToLabel(Game.getCurrentPhase());
                addMessageToLabel("Dear " + Game.whoseRivalPlayer().getNickName() + ",do you want to activate " + cardName + "trap?\nType 'yes' or 'no'");
                getAnswer(cardName, myMonsterCard, address, number, monsterCard);
            } else {
                yesOrNo = messageFromPlayer.getText().equals("yes");
                setStateOfSubmit(true);
                setButtonsActivate(false);
                if (cardName.equals("Negate Attack")) {
                    BattlePhaseController.getInstance().doNegateAttack();
                } else if (cardName.equals("Mirror Force")) {
                    BattlePhaseController.getInstance().doMirrorForce();
                } else if (cardName.equals("Magic Cylinder")) {
                    try {
                        BattlePhaseController.getInstance().doMagicCylinder(Game.whoseTurnPlayer(), myMonsterCard);
                    } catch (MyException myException) {
                        newMessageToLabel(Game.getCurrentPhase());
                        addMessageToLabel(myException.getMessage());
                        reset();
                    }
                } else if (cardName.equals("Mind Crush")) {
                    PhaseControl.getInstance().doMindCrush(address, Game.whoseTurnPlayer());
                } else if (cardName.equals("Time Seal")) {
                    PhaseControl.getInstance().doTimeSeal(address, Game.whoseTurnPlayer());
                } else if (cardName.equals("Call of The Haunted")) {
                    PhaseControl.getInstance().doCallOfTheHaunted(address, Game.whoseTurnPlayer());
                } else if (cardName.equals("Magic Jammer")) {
                    PhaseControl.getInstance().doMagicJammer(address);
                } else if (cardName.equals("Solemn Warning")) {
                    try {
                        doSolemnWarningGameView1(address, number, monsterCard);
                    } catch (MyException myException) {
                        newMessageToLabel(Game.getCurrentPhase());
                        addMessageToLabel(myException.getMessage());
                        reset();
                    }
                }
                reset();
            }
            messageFromPlayer.setText("");
        });
    }

    public void summonAMonsterCardFromGraveyard() {
//        if (!Game.isAITurn()) {
//            newMessageToLabel(Game.getCurrentPhase());
//            addMessageToLabel("type a card name so if rival has this kind of card all of them will be removed else one of your card will be removed randomly.");
//            submitButton.setOnMouseClicked(e ->{
//                setCardName();
//            });
//            System.out.println("whose graveyard you want to summon from?(yours/rival's)");
////            Board.showGraveyard();
//            String input = Main.scanner.nextLine();
//            doCallOfTheHauntedEffect(input.equals("yours"));
//        }
//        else {
//            Player player1 = Game.whoseTurnPlayer();
//            Player player2 = Game.whoseRivalPlayer();
//            int place1 = getMaxAttackCard(player1.getGraveyardCard());
//            int place2 = getMaxAttackCard(player2.getGraveyardCard());
//            if (place1 == 0 && place2 == 0)
//                return;
//            if (place2 == 0)
//                Board.summonThisCardFromGraveYardToMonsterZone(new Address(place1, "graveyard", true));
//            else if (place1 == 0)
//                Board.summonThisCardFromGraveYardToMonsterZone(new Address(place2, "graveyard", false));
//            else if (player1.getCardGraveyard(place1).getAttack() >= player2.getCardGraveyard(place2).getAttack())
//                Board.summonThisCardFromGraveYardToMonsterZone(new Address(place1, "graveyard", true));
//            else
//                Board.summonThisCardFromGraveYardToMonsterZone(new Address(place2, "graveyard", false));
//        }
    }

    public boolean removeCardFromMyHand() {
        setStateOfSubmit(false);
        setButtonsActivate(true);
        newMessageToLabel(Game.getCurrentPhase());
        addMessageToLabel("choose a card from your hand to be removed\nType hand number from 1 to 6");
        submitButton.setOnMouseClicked(e -> {
            setSubmitedAddrees();
            setStateOfSubmit(true);
            setButtonsActivate(false);
            messageFromPlayer.setText("");
        });
        if (Game.whoseTurnPlayer().getCardByAddress(submitedAddrees) != null) {
            Game.whoseTurnPlayer().removeCard(submitedAddrees);
            return true;
        }
        return false;
    }

    private void setSubmitedAddrees() {
        submitedAddrees = new Address(Integer.parseInt(messageFromPlayer.getText()), "hand", true);
        ;
    }

    private int getMaxAttackCard(HashMap<Integer, Card> graveyardCard) {
        int place = 0;
        int maxAttack = -1;
        for (int i = 1; i <= graveyardCard.size(); i++) {
            if (graveyardCard.containsKey(i) && graveyardCard.get(i).getKind().equals("Monster")
                    && graveyardCard.get(i).getAttack() >= maxAttack) {
                place = i;
                maxAttack = graveyardCard.get(i).getAttack();
            }
        }
        return place;
    }

    public void doCallOfTheHauntedEffect(boolean isMine) {
//        if (isMine) System.out.println("choose a monster from your graveyard to be summoned!(only type number)");
//        else System.out.println("choose a monster from your rival's graveyard to be summoned!(only type number)");
////        Board.showGraveyard();
//        Address address = new Address(Integer.parseInt(Main.scanner.nextLine()), "graveyard", isMine);
//        if (Game.whoseTurnPlayer().getMonsterCardByAddress(address) != null)
//            Board.summonThisCardFromGraveYardToMonsterZone(address);
    }

    private void setCardName() {
        cardName = messageFromPlayer.getText();
    }

    private void doMainPhase2() {
        setStateOfSubmit(true);
        setButtonsActivate(false);
        Game.setPhase("Main Phase 2");
        newMessageToLabel(Game.getCurrentPhase());
        reset();
    }

    private void doEndPhase() throws Exception {
        Game.setPhase("End Phase");
        newMessageToLabel(Game.getCurrentPhase());
        reset();
        PhaseControl.getInstance().doEffectEndPhase();
        if (Game.whoseTurnPlayer().howManyCardIsInTheHandCard() == 6) {
            reset();
            addMessageToLabel("Select a card to be deleted from your hand\nEnter a number from 1 to 6");
            setStateOfSubmit(false);
            setButtonsActivate(true);
            submitButton.setOnMouseClicked(e -> {
                if (!messageFromPlayer.getText().matches("[123456]")) {
                    newMessageToLabel("Incorrect input\nSelect a card to be deleted from your hand\nEnter a number from 1 to 6");
                    messageFromPlayer.setText("");
                    setStateOfSubmit(false);
                    setButtonsActivate(true);
                    reset();
                } else {
                    Address address = new Address(Integer.parseInt(messageFromPlayer.getText()), "hand", true);
                    Game.whoseTurnPlayer().removeCard(address);
                    newMessageToLabel("Card has been successfully removed from hand");
                    messageFromPlayer.setText("");
                    reset();
                    PhaseControl.getInstance().checkIfGameEnded();
                    PhaseControl.getInstance().switchPlayerTurn();
                    setStateOfSubmit(true);
                    setButtonsActivate(false);
                    reset();
                    try {
                        doDrawPhase(Game.whoseTurnPlayer());
                    } catch (Exception exception) {
                        exception.printStackTrace();
                    }
                }
                reset();
                messageFromPlayer.setText("");
            });
        } else {
            reset();
            PhaseControl.getInstance().checkIfGameEnded();
            PhaseControl.getInstance().switchPlayerTurn();
            doDrawPhase(Game.whoseTurnPlayer());
        }
    }

    private void drawCardFromDeckToHand(Player player, Boolean isRival) {
        HashMap<Integer, Card> handCards = player.getHandCard();
        for (int i = 1; i <= 6; i++) {
            if (handCards.containsKey(i) && (!handCards.containsKey(i + 1))) {
                ImageView ImageView;
                if (!isRival) {
                    ImageView = createImage(handCards.get(i).getCardName(), 260 + 80 * (i - 1), 535, 75, 100);
                } else {
                    ImageView = createImage("Unknown", 260 + 80 * (i - 1), -50, 75, 100);
                }
                int finalI = i;
                ImageView.setOnMouseEntered(e -> {
                    ImageView imageViewInfo;
                    if (!isRival) {
                        imageViewInfo = createImage(handCards.get(finalI).getCardName(), 7, 152, 205, 296);
                    } else {
                        imageViewInfo = createImage("Unknown", 7, 152, 205, 296);
                    }
                    pane.getChildren().add(imageViewInfo);
                });
                ImageView.setOnMouseExited(e -> {
                    removeCard(7, 152);
                });
                pane.getChildren().add(ImageView);
            }
        }
    }

    public void newMessageToLabel(String newMessage) {
        messageBox.setText(newMessage);
    }

    public void addMessageToLabel(String message) {
        String oldMessage = messageBox.getText();
        String newMessage = oldMessage + "\n" + message;
        messageBox.setText(newMessage);
    }

//    private void setHand(Player player, Boolean isRival) {
//        for (int i = 1; i <= 6; i++) {
//            HashMap<Integer, Card> handCards = player.getHandCard();
//            if (handCards.containsKey(i)) {
//                ImageView imageView;
//                if (!isRival) {
//                    imageView = createImage(handCards.get(i).getCardName(), 260 + 80 * (i - 1), 535, 75, 100);
//                } else {
//                    imageView = createImage("Unknown", 260 + 80 * (i - 1), -50, 75, 100);
//                }
//                int finalI = i;
//                imageView.setOnMouseEntered(e -> {
//                    ImageView imageViewInfo;
//                    if (!isRival) {
//                        imageViewInfo = createImage(handCards.get(finalI).getCardName(), 7, 152, 205, 296);
//                    } else {
//                        imageViewInfo = createImage("Unknown", 7, 152, 205, 296);
//                    }
//                    pane.getChildren().add(imageViewInfo);
//                });
//                imageView.setOnMouseExited(e -> {
//                    removeCard(7, 152);
//                });
//                if (isRival)
//                    rivalHand[i] = imageView;
//                else turnHand[i] = imageView;
//                pane.getChildren().add(imageView);
//            }
//        }
//    }

    public void setHand() {
        for (int i = 1; i <= 6; i++) {
            handsImageSet(Game.whoseTurnPlayer(), turnHand, i);
        }
        for (int i = 1; i <= 6; i++)
            handsImageSet(Game.whoseRivalPlayer(), rivalHand, i);
    }

    public void setSpells() {
        Player turnPlayer = Game.whoseTurnPlayer();
        Player rivalPlayer = Game.whoseRivalPlayer();
        for (int i = 1; i <= 5; i++)
            spellsImageSet(turnPlayer, turnSpells, i);
        for (int i = 1; i <= 5; i++)
            spellsImageSet(rivalPlayer, rivalSpells, i);
    }

    public void setMonster() {
        Player turnPlayer = Game.whoseTurnPlayer();
        Player rivalPlayer = Game.whoseRivalPlayer();
        for (int i = 1; i <= 5; i++)
            monstersImageSet(turnPlayer, turnMonsters, i);
        for (int i = 1; i <= 5; i++)
            monstersImageSet(rivalPlayer, rivalMonsters, i);
    }

    private void handsImageSet(Player player, ImageView[] hand, int i) {
        if (player.getHandCard().containsKey(i)) {
            if (player.getName().equals(Game.whoseTurnPlayer().getName()))
                hand[i].setImage(createImage(player.getCardHand(i).getCardName()));
            else
                hand[i].setImage(new Image(getClass().getResource("/PNG/Cards1/Unknown.jpg").toExternalForm()));
            hand[i].setVisible(true);
            hand[i].setDisable(false);
        } else {
            hand[i].setVisible(false);
            hand[i].setDisable(true);
        }
    }

    private void monstersImageSet(Player player, ImageView[] monsters, int i) {
        if (player.getMonsterZoneCard().containsKey(i)) {
            monsters[i].setImage(createImage(player.getCardMonster(i).getCardName()));
            if (player.getMonsterPosition(i).equals(PositionOfCardInBoard.DH))
                monsters[i].setImage(new Image(getClass().getResource("/PNG/Cards1/Unknown.jpg").toExternalForm()));
            monsters[i].setRotate(90);
            if (player.getMonsterPosition(i).equals(PositionOfCardInBoard.OO))
                monsters[i].setRotate(0);
            monsters[i].setVisible(true);
            monsters[i].setDisable(false);
        } else {
            monsters[i].setVisible(false);
            monsters[i].setDisable(true);
        }
    }

    private void spellsImageSet(Player player, ImageView[] spells, int i) {
        if (player.getSpellZoneCard().containsKey(i)) {
            spells[i].setImage(createImage(player.getCardSpell(i).getCardName()));
            if (!player.isSpellFaceUp(i))
                spells[i].setImage(new Image(getClass().getResource("/PNG/Cards1/Unknown.jpg").toExternalForm()));
            spells[i].setVisible(true);
            spells[i].setDisable(false);
        } else {
            spells[i].setVisible(false);
            spells[i].setDisable(true);
        }
    }

    private void setSpellOrTrapOnMouseClicked(ImageView spellZoneCard, int i) {
        spellZoneCard.setOnMouseClicked(e -> {
            if (Game.getCurrentPhase().equals("Main Phase 1") || Game.getCurrentPhase().equals("Main Phase 2") || Game.getCurrentPhase().equals("Battle Phase")) {
                setSelectedCard("spell", i, true);
                if (Game.whoseTurnPlayer().getCardByAddress(selectedCardAddress).getKind().equals("Spell")) {
                    if (!Game.whoseTurnPlayer().isSpellFaceUp(i)) {
                        try {
                            PhaseControl.getInstance().activeSpell(selectedCardAddress);
                            reset();
                        } catch (MyException myException) {
                            newMessageToLabel(Game.getCurrentPhase());
                            addMessageToLabel(myException.getMessage());
                            reset();
                        }
                    }
                }
            }
            reset();
        });
    }

    private void setMonsterOnMouseClicked(ImageView monsterZoneCard, int i) {
        monsterZoneCard.setOnMouseClicked(e -> {
            if (Game.getCurrentPhase().equals("Main Phase 1") || Game.getCurrentPhase().equals("Main Phase 2")) {
                setSelectedCard("monster", i, true);
                if (Game.whoseTurnPlayer().isThisMonsterOnAttackPosition(selectedCardAddress)) {
                    try {
                        PhaseControl.getInstance().setPosition("defence", selectedCardAddress);
                        reset();
                    } catch (MyException myException) {
                        newMessageToLabel(Game.getCurrentPhase());
                        addMessageToLabel(myException.getMessage());
                        reset();
                    }
                } else if (!Game.whoseTurnPlayer().isThisMonsterOnDHPosition(selectedCardAddress)) {
                    try {
                        PhaseControl.getInstance().setPosition("attack", selectedCardAddress);
                        reset();
                    } catch (MyException myException) {
                        newMessageToLabel(Game.getCurrentPhase());
                        addMessageToLabel(myException.getMessage());
                        reset();
                    }
                } else {
                    try {
                        PhaseControl.getInstance().flipSummon(selectedCardAddress);
                        reset();
                    } catch (MyException myException) {
                        newMessageToLabel(Game.getCurrentPhase());
                        addMessageToLabel(myException.getMessage());
                        reset();
                    }
                }
            } else if (Game.getCurrentPhase().equals("Battle Phase")) {
                setSelectedCard("monster", i, true);
            }
            reset();
        });
    }

    private void setRivalMonsterOnMouseClicked(ImageView monsterZoneCard, int i) {
        monsterZoneCard.setOnMouseClicked(e -> {
            if (Game.getCurrentPhase().equals("Battle Phase")) {
                if (selectedCardAddress != null && selectedCardAddress.getKind().equals("monster") && selectedCardAddress.checkIsMine()) {
                    try {
                        soundEffect(2);
                        BattlePhaseController.getInstance().attack(new Address(i, "monster", false), selectedCardAddress);
                        reset();
                    } catch (MyException myException) {
                        newMessageToLabel(Game.getCurrentPhase());
                        addMessageToLabel(myException.getMessage());
                    }
                }
            }
            reset();
        });
    }

    private void setHandOnMouseClick(ImageView handCard, int i) {
        handCard.setOnMouseClicked(e -> {
            if (Game.getCurrentPhase().equals("Main Phase 1") || Game.getCurrentPhase().equals("Main Phase 2")) {
                setSelectedCard("hand", i, true);
                if (e.getButton() == MouseButton.SECONDARY) {
                    try {
                        if (Board.getCardByAddress(selectedCardAddress).getKind().equals("Spell"))
                            PhaseControl.getInstance().spellSet(selectedCardAddress);
                        else if (Board.getCardByAddress(selectedCardAddress).getKind().equals("Monster")) {
                            PhaseControl.getInstance().monsterSet(selectedCardAddress);
                        } else if (Board.getCardByAddress(selectedCardAddress).getKind().equals("Trap"))
                            PhaseControl.getInstance().trapSet(selectedCardAddress);
                        soundEffect(0);
                        reset();
                    } catch (MyException myException) {
                        soundEffect(1);
                        newMessageToLabel(Game.getCurrentPhase());
                        addMessageToLabel(myException.getMessage());
                    }
                } else {
                    try {
                        if (Board.getCardByAddress(selectedCardAddress).getKind().equals("Spell"))
                            PhaseControl.getInstance().spellSet(selectedCardAddress);
                        else if (Board.getCardByAddress(selectedCardAddress).getKind().equals("Monster")) {
                            PhaseControl.getInstance().summonControl(selectedCardAddress);
                        } else if (Board.getCardByAddress(selectedCardAddress).getKind().equals("Trap"))
                            PhaseControl.getInstance().trapSet(selectedCardAddress);
                        soundEffect(0);
                        reset();
                    } catch (MyException myException) {
                        soundEffect(1);
                        newMessageToLabel(Game.getCurrentPhase());
                        addMessageToLabel(myException.getMessage());
                    }
                }
                reset();
            } else {
                soundEffect(1);
                newMessageToLabel(Game.getCurrentPhase());
                addMessageToLabel("You can't set/summon cards at this phase");
            }
        });
    }

    private void soundEffect(int number) {
        try {
            if (number == 0)
                Sounds.play("src//main//resources//Sound//CARD_MOVE_1.wav", 1).start();
            if (number == 1)
                Sounds.play("src//main//resources//Sound//Cancel.wav", 1).start();
            if (number == 2)
                Sounds.play("src//main//resources//Sound//sfx_winged_beast_type.wav", 1).start();
            if (number == 3)
                Sounds.play("src//main//resources//Sound//sfx_small_attack.wav", 1).start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void increaseHowManyHeraldOfCreationDidWeUseEffect() {
        howManyHeraldOfCreationDidWeUseEffect++;
    }

    public void setHowManyHeraldOfCreationDidWeUseEffect(int i) {
        howManyHeraldOfCreationDidWeUseEffect = i;
    }

    private Image createImage(String cardName) {
        return new Image(getClass().getResource("/PNG/Cards1/" + cardName + ".jpg").toExternalForm());
    }

    private void removeCard(int layoutX, int layoutY) {
        for (int i = 0; i < pane.getChildren().size(); i++) {
            if (pane.getChildren().get(i).getLayoutX() == layoutX && pane.getChildren().get(i).getLayoutY() == layoutY) {
                pane.getChildren().remove(i);
            }
        }
    }

    private ImageView createImage(String cardName, int X, int Y, int layoutX, int layoutY) {
        Image image = new Image(getClass().getResource("/PNG/Cards1/" + cardName + ".jpg").toExternalForm());
        ImageView imageView = new ImageView();
        imageView.setImage(image);
        imageView.setFitHeight(layoutY);
        imageView.setFitWidth(layoutX);
        imageView.setLayoutX(X);
        imageView.setLayoutY(Y);
        return imageView;
    }

    private void createBackgroundCards() {
//        Image image = new Image(getClass().getResource("/PNG/Cards/Monsters/Unknown.jpg").toExternalForm());
//        ImageView imageView = new ImageView();
//        imageView.setImage(image);
//        imageView.setImage(image);
//        imageView.setFitWidth(600.0);
//        imageView.setFitHeight(600.0);
//        imageView.setPreserveRatio(false);
//        imageView.setX(200);
//        imageView.setY(0);
//        pane.getChildren().add(imageView);
    }

    private void createPlayers() {
//        File pictureFile =  new File(.getAvatarAddress());
//        String string = pictureFile.toURI().toString();
//        Image image = new Image(getClass().getResource(string).toExternalForm());
//        ImageView imageView = new ImageView();
//        imageView.setImage(image);
//        imageView.setImage(image);
//        imageView.setFitWidth(600.0);
//        imageView.setFitHeight(600.0);
//        imageView.setPreserveRatio(false);
//        imageView.setX(200);
//        imageView.setY(0);
    }

    private void createBackground(String type) {
//        Image image = new Image(getClass().getResource("/PNG/Field/FieldPNG/fie_" + type + ".png").toExternalForm());
//        ImageView imageView = new ImageView();
//        imageView.setImage(image);
//        imageView.setImage(image);
//        imageView.setFitWidth(600.0);
//        imageView.setFitHeight(600.0);
//        imageView.setPreserveRatio(false);
//        imageView.setX(200);
//        imageView.setY(0);
//        pane.getChildren().add(imageView);
    }

    public void checkSurrender(MouseEvent mouseEvent) {

    }

    public void goToDrawPhase(ActionEvent actionEvent) {
    }

    public void goToStandByPhase(ActionEvent actionEvent) {
        if (Game.getCurrentPhase().equals("Draw Phase")) {
            doStandByPhase();
        }
    }

    public void goToMainPhaseOne(ActionEvent actionEvent) {
        if (Game.getCurrentPhase().equals("Draw Phase") || Game.getCurrentPhase().equals("Standby Phase")) {
            doMainPhase1();
        }
    }

    public void goToBattlePhase(ActionEvent actionEvent) {
        if (Game.getCurrentPhase().equals("Draw Phase") || Game.getCurrentPhase().equals("Standby Phase") || Game.getCurrentPhase().equals("Main Phase 1")) {
            doBattlePhase();
        }
    }

    public void goToMainPhaseTwo(ActionEvent actionEvent) {
        if (Game.getCurrentPhase().equals("Draw Phase") || Game.getCurrentPhase().equals("Standby Phase") || Game.getCurrentPhase().equals("Main Phase 1")
                || Game.getCurrentPhase().equals("Battle Phase")) {
            doMainPhase2();
        }
    }

    public void goToEndPhase(ActionEvent actionEvent) throws Exception {
        if (Game.getCurrentPhase().equals("Draw Phase") || Game.getCurrentPhase().equals("Standby Phase") || Game.getCurrentPhase().equals("Main Phase 1")
                || Game.getCurrentPhase().equals("Battle Phase") || Game.getCurrentPhase().equals("Main Phase 2")) {
            doEndPhase();
        }
    }

    public void pauseGame(MouseEvent mouseEvent) {
        wasDisable[0] = battlePhase.isDisable();
        wasDisable[1] = turnGraveyard.isDisable();
        wasDisable[2] = submitButton.isDisable();
        setButtonsActivate(true);
        submitButton.setDisable(true);
        turnGraveyard.setDisable(true);
        rivalGraveyard.setDisable(true);
        pauseBackground.setVisible(true);
        exitButton.setVisible(true);
        exitButton.setDisable(false);
        resumeButton.setVisible(true);
        resumeButton.setDisable(false);
    }

    public void resumeGame() {
        setButtonsActivate(wasDisable[0]);
        submitButton.setDisable(wasDisable[2]);
        turnGraveyard.setDisable(wasDisable[1]);
        rivalGraveyard.setDisable(wasDisable[1]);
        pauseBackground.setVisible(false);
        exitButton.setVisible(false);
        exitButton.setDisable(true);
        resumeButton.setVisible(false);
        resumeButton.setDisable(true);
        pauseButton.setDisable(false);
    }

    public void exit() {
        try {
            new MainMenu().start(stage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
