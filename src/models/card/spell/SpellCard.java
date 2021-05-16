package models.card.spell;

import controllers.move.Attack;
import models.Address;
import models.Player;
import models.card.spell.spell_effect.SpellEffect;
import view.Effect;
import view.Game;
//import card.trap.TrapCard;

import java.util.ArrayList;

public class SpellCard {
    private String description;
    private String effect;
    private int price;
    private SpellMode spellMode;
    private boolean isLimit;
    private String name;
    private SpellEffect spellEffect;
    private static ArrayList<SpellCard> spellCards;

    static {
        spellCards = new ArrayList<>();
    }

    public SpellCard(String name, SpellMode spellMode, boolean isLimit, String effect, int price, SpellEffect spellEffect) {
        this.name = name;
        this.spellMode = spellMode;
        this.isLimit = isLimit;
        this.effect = effect;
        this.price = price;
        this.spellEffect = spellEffect;
        spellCards.add(this);
    }

    public static ArrayList<SpellCard> getSpellCards() {
        return spellCards;
    }

    public static boolean canWeActivateThisSpell(Address address) {

    }

    public static void doSpellAbsorptionEffect() {
        if (Game.firstPlayer.isOneHisSpellAbsorptionActivated())
            Game.firstPlayer.increaseLP(500);
        if (Game.secondPlayer.isOneHisSpellAbsorptionActivated())
            Game.secondPlayer.increaseLP(500);
    }

    public void doEffect(Address address) {
        Player currentPlayer = Game.whoseTurnPlayer();
        if (name.equals("Terraforming")) {
            if ((currentPlayer.isThereAnyFieldSpellInDeck()) && (!currentPlayer.isHandFull())) {
                String fieldSpellName = Effect.run("Terraforming");
                SpellCard spellCard = SpellCard.getSpellCardByName(fieldSpellName);
                if ((spellCard != null) && (spellCard.spellMode == SpellMode.FIELD) && (currentPlayer.isThisCardInDeck("Terraforming"))) {
                    currentPlayer.bringCardFromDeckToHand("Terraforming");
                } else System.out.println("you chose the wrong card.");
            } else System.out.println("This effect can't be done.");
            currentPlayer.removeCard(address);
        }
        if (name.equals("PotOfGreed")) {
            if (!currentPlayer.isHandFull()) {
                currentPlayer.addCardFromUnusedToHand();
                if (!currentPlayer.isHandFull()) {
                    currentPlayer.addCardFromUnusedToHand();
                } else System.out.println("This effect can't be done completely.");
            } else System.out.println("This effect can't be done.");
            currentPlayer.removeCard(address);
        }
        if (name.equals("Raigeki")) {
            Attack.destroyAllRivalMonstersInTheBoard();
            currentPlayer.removeCard(address);
        }
        if (name.equals("HarpieFeatherDuster")) {
            Attack.destroyAllRivalMonstersAndTrapInTheBoard();
            currentPlayer.removeCard(address);
        }
        if (name.equals("DarkHole")) {
            Attack.destroyAllMonstersInTheBoard();
            currentPlayer.removeCard(address);
        }
        if(name.equals("TwinTwisters")){
            String[] input=Effect.run("TwinTwisters").split(",");
            Address address1=new Address(Integer.parseInt(input[0]),"hand",true);
            Address address2=new Address(Integer.parseInt(input[1]),"spell",false);
            Address address3=new Address(Integer.parseInt(input[2]),"spell",false);
            currentPlayer.removeCard(address1);
            currentPlayer.removeCard(address2);
            currentPlayer.removeCard(address3);
        }
        if(name.equals("MysticalSpaceTyphoon")){
            String input = Effect.run("MysticalSpaceTyphoon");
            Address address1=new Address(Integer.parseInt(input),"spell",false);
            currentPlayer.removeCard(address1);
        }


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

    public String getName() {
        return name;
    }

    public void runEffect() {
        this.spellEffect.run();
    }

    public static SpellCard getSpellCardByName(String name) {
        for (SpellCard spellCard : spellCards)
            if (spellCard.name.equals(name)) return spellCard;
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

    public void setName(String name) {
        this.name = name;
    }

    public void setEffect(String effect) {
        this.effect = effect;
    }

    public void setLimit(boolean limit) {
        isLimit = limit;
    }

    public void setSpellEffect(SpellEffect spellEffect) {
        this.spellEffect = spellEffect;
    }

    public void setSpellMode(SpellMode spellMode) {
        this.spellMode = spellMode;
    }

}
