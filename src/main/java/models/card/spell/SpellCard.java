package models.card.spell;

import Exceptions.MyException;
import controllers.move.Attack;
import models.Address;
import models.Player;
import controllers.Game;
import view.GameView;
import view.Main;
import view.phase.BattlePhase;

import java.util.ArrayList;

public class SpellCard {
    private boolean isOriginal;
    private String description;
    private String effect;
    private int price;
    private SpellMode spellMode;
    private boolean isLimit;
    private String realName;
    private ArrayList<String> namesForEffect = new ArrayList<>();
    private static ArrayList<SpellCard> spellCards;

    static {
        spellCards = new ArrayList<>();
    }

    public SpellCard(String name, SpellMode spellMode, boolean isLimit, int price, String description) {
        realName = name;
        this.spellMode = spellMode;
        this.isLimit = isLimit;
        this.price = price;
        this.description = description;
        isOriginal = true;
        namesForEffect.add(realName);
        spellCards.add(this);
        namesForEffect.add(name);
    }

    public SpellCard(String realName, SpellMode spellMode, int price, String description, ArrayList<String> names, boolean isOriginal) {
        this.realName = realName;
        this.spellMode = spellMode;
        this.price = price;
        this.description = description;
        this.namesForEffect = names;
        this.isOriginal = isOriginal;
        spellCards.add(this);
    }

    public boolean isNew() {
        return !isOriginal;
    }

    public static ArrayList<SpellCard> getOriginalSpellCards() {
        ArrayList<SpellCard> originalSpellCards= new ArrayList<>();
        for (SpellCard spellCard : spellCards)
            if(!spellCard.isNew()) originalSpellCards.add(spellCard);
        return originalSpellCards;
    }

    public void doEffect(Address address) throws MyException {
        Player currentPlayer = Game.whoseTurnPlayer();
        if (namesForEffect.contains("Terraforming")) {
            if ((currentPlayer.isThereAnyFieldSpellInDeck()) && (!currentPlayer.isHandFull())) {
                Game.getGameView().runEffect("Terraforming", null, null);
            } else if (!Game.isAITurn()) throw new MyException("This effect can't be done.");
            currentPlayer.removeCard(address);
        }
        if (namesForEffect.contains("Pot of Greed")) {
            if (!currentPlayer.isHandFull()) {
                currentPlayer.addCardFromUnusedToHand();
                if (!currentPlayer.isHandFull()) {
                    currentPlayer.addCardFromUnusedToHand();
                } else if (!Game.isAITurn()) throw new MyException("This effect can't be done completely.");
            } else if (!Game.isAITurn()) throw new MyException("This effect can't be done.");
            currentPlayer.removeCard(address);
        }
        if (namesForEffect.contains("Raigeki")) {
            Attack.destroyAllRivalMonstersInTheBoard();
            currentPlayer.removeCard(address);
        }
        if (namesForEffect.contains("Harpie's Feather Duster")) {
            Attack.destroyAllRivalMonstersAndTrapInTheBoard();
            currentPlayer.removeCard(address);
        }
        if (namesForEffect.contains("Dark Hole")) {
            Attack.destroyAllMonstersInTheBoard();
            currentPlayer.removeCard(address);
        }
        if (namesForEffect.contains("Twin Twisters")) {
            Game.getGameView().runEffect("Twin Twisters", null, null);
        }
        if (namesForEffect.contains("Mystical space typhoon")) {
            System.out.println("here");
            Game.getGameView().runEffect("Mystical space typhoon", null, null);
        }
        if (namesForEffect.contains("Monster Reborn")) {
            Game.getGameView().summonAMonsterCardFromGraveyard();
            currentPlayer.removeCard(address);
        }
        if (spellMode.equals(SpellMode.EQUIP)) {
            String input;
            if (!Game.isAITurn()) {
                Game.getGameView().getMonsterForEquip(address);
            } else{
                input = Integer.toString(BattlePhase.getInstance().getStrongestMonster(Game.whoseTurnPlayer()));
                Game.whoseTurnPlayer().setFromMonsterToSpellEquip(address.getNumber(), Integer.parseInt(input));
            }
        }
    }

    public static void doEquipSpellEffect(Address address) {
        Game.whoseTurnPlayer().setFromMonsterToSpellEquip(address.getNumber(), Integer.parseInt(Game.getGameView().answer));
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getRealName() {
        return realName;
    }

    public static boolean canWeActivateThisSpell(Address address) {
        return true;
        //sdfjhsdfkjsahdf
    }

    public static void doSpellAbsorptionEffect() {
        if (Game.firstPlayer.isOneHisSpellAbsorptionActivated())
            Game.firstPlayer.increaseLP(500);
        if (Game.secondPlayer.isOneHisSpellAbsorptionActivated())
            Game.secondPlayer.increaseLP(500);
    }

    public static void doMysticalSpaceTyphoonEffect(Player currentPlayer) {
        String input = Game.getGameView().answer;
        Address address1 = new Address(Integer.parseInt(input), "spell", false);
        currentPlayer.removeCard(address1);
    }

    public static void doTwinTwistersEffect(Player currentPlayer) {
        Address address1 = new Address(Integer.parseInt(Game.getGameView().twinTwister1), "hand", true);
        Address address2 = new Address(Integer.parseInt(Game.getGameView().twinTwister2), "spell", false);
        Address address3 = new Address(Integer.parseInt(Game.getGameView().twinTwister3), "spell", false);
        currentPlayer.removeCard(address1);
        currentPlayer.removeCard(address2);
        currentPlayer.removeCard(address3);
    }

    public static void doTerraformingEffect(Player currentPlayer) throws MyException {
        String fieldSpellName = Game.getGameView().answer;
        SpellCard spellCard = SpellCard.getSpellCardByName(fieldSpellName);
        if ((spellCard != null) && (spellCard.spellMode == SpellMode.FIELD) && (currentPlayer.isThisCardInDeck("Terraforming"))) {
            currentPlayer.bringCardFromDeckToHand("Terraforming");
        } else if (!Game.isAITurn()) throw new MyException("you chose the wrong card.");
    }

    public SpellMode getSpellMode() {
        return spellMode;
    }

    public boolean checkIsLimit() {
        return isLimit;
    }

    public int getPrice() {
        return price;
    }

    public String getEffect() {
        return effect;
    }

    public ArrayList<String> getNamesForEffect() {
        return namesForEffect;
    }

    public static SpellCard getSpellCardByName(String name) {
        for (SpellCard spellCard : spellCards)
            if (spellCard.realName.equals(name)) return spellCard;
        return null;
    }

    public static void welcomeToEffect() {

    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public void setEffect(String effect) {
        this.effect = effect;
    }

    public void setSpellMode(SpellMode spellMode) {
        this.spellMode = spellMode;
    }

}
