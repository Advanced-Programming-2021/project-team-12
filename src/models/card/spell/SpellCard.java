package models.card.spell;

import controllers.move.Attack;
import models.Address;
import models.Player;
import models.card.spell.spell_effect.SpellEffect;
import view.Effect;
import controllers.Game;
import view.Main;
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

    public SpellCard(String name, SpellMode spellMode, boolean isLimit, int price, String description) {
        this.name = name;
        this.spellMode = spellMode;
        this.isLimit = isLimit;
        this.effect = effect;
        this.price = price;
        this.spellEffect = spellEffect;
        this.description = description;
        spellCards.add(this);
    }

    public static ArrayList<SpellCard> getSpellCards() {
        return spellCards;
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
        if (name.equals("Pot of Greed")) {
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
        if (name.equals("Harpie's Feather Duster")) {
            Attack.destroyAllRivalMonstersAndTrapInTheBoard();
            currentPlayer.removeCard(address);
        }
        if (name.equals("Dark Hole")) {
            Attack.destroyAllMonstersInTheBoard();
            currentPlayer.removeCard(address);
        }
        if (name.equals("Twin Twisters")) {
            String[] input = Effect.run("Twin Twisters").split(",");
            Address address1 = new Address(Integer.parseInt(input[0]), "hand", true);
            Address address2 = new Address(Integer.parseInt(input[1]), "spell", false);
            Address address3 = new Address(Integer.parseInt(input[2]), "spell", false);
            currentPlayer.removeCard(address1);
            currentPlayer.removeCard(address2);
            currentPlayer.removeCard(address3);
        }
        if (name.equals("Mystical Space Typhoon")) {
            String input = Effect.run("Mystical Space Typhoon");
            Address address1 = new Address(Integer.parseInt(input), "spell", false);
            currentPlayer.removeCard(address1);
        }
        if (name.equals("Monster Reborn")) {
            currentPlayer.summonAMonsterCardFromGraveyard();
            currentPlayer.removeCard(address);
        }
        if (spellMode.equals(SpellMode.EQUIP)) {
            System.out.println("choose a faced up monster to be equipped with this spell!(type number in monster zone)");
            String input = Main.scanner.nextLine();
            //if(Game.whoseTurnPlayer().getMonsterCardByAddress(new Address(Integer.parseInt(input), "monster", true)).getAttribute().equals(E))
            Game.whoseTurnPlayer().setFromMonsterToSpellEquip(address.getNumber(), Integer.parseInt(input));
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
