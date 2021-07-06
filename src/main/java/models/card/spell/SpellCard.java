package models.card.spell;

import controllers.move.Attack;
import models.Address;
import models.Player;
import view.Effect;
import controllers.Game;
import view.GameView;
import view.Main;
import view.phase.BattlePhase;
import view.phase.MainPhase;

import java.util.ArrayList;

public class SpellCard {
    private String description;
    private String effect;
    private int price;
    private SpellMode spellMode;
    private boolean isLimit;
    private String name;
    private String realName;
    private ArrayList<String> names = new ArrayList<>();
    private static ArrayList<SpellCard> spellCards;

    static {
        spellCards = new ArrayList<>();
    }

    public SpellCard(String name, SpellMode spellMode, boolean isLimit, int price, String description) {
        this.name = name;
        realName = name;
        this.spellMode = spellMode;
        this.isLimit = isLimit;
        this.price = price;
        this.description = description;
        spellCards.add(this);
    }

    public SpellCard(String realName, SpellMode spellMode, int price, String description, ArrayList<String> names) {
        this.realName = realName;
        this.spellMode = spellMode;
        this.price = price;
        this.description = description;
        this.names = names;
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
                String fieldSpellName = GameView.getInstance().runEffect("Terraforming");
                SpellCard spellCard = SpellCard.getSpellCardByName(fieldSpellName);
                if ((spellCard != null) && (spellCard.spellMode == SpellMode.FIELD) && (currentPlayer.isThisCardInDeck("Terraforming"))) {
                    currentPlayer.bringCardFromDeckToHand("Terraforming");
                } else if (!Game.isAITurn()) System.out.println("you chose the wrong card.");
            } else if (!Game.isAITurn()) System.out.println("This effect can't be done.");
            currentPlayer.removeCard(address);
        }
        if (name.equals("Pot of Greed")) {
            if (!currentPlayer.isHandFull()) {
                currentPlayer.addCardFromUnusedToHand();
                if (!currentPlayer.isHandFull()) {
                    currentPlayer.addCardFromUnusedToHand();
                } else if (!Game.isAITurn()) System.out.println("This effect can't be done completely.");
            } else if (!Game.isAITurn()) System.out.println("This effect can't be done.");
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
            String[] input = GameView.getInstance().runEffect("Twin Twisters").split(",");
            Address address1 = new Address(Integer.parseInt(input[0]), "hand", true);
            Address address2 = new Address(Integer.parseInt(input[1]), "spell", false);
            Address address3 = new Address(Integer.parseInt(input[2]), "spell", false);
            currentPlayer.removeCard(address1);
            currentPlayer.removeCard(address2);
            currentPlayer.removeCard(address3);
        }
        if (name.equals("Mystical space typhoon")) {
            String input = GameView.getInstance().runEffect("Mystical space typhoon");
            Address address1 = new Address(Integer.parseInt(input), "spell", false);
            currentPlayer.removeCard(address1);
        }
        if (name.equals("Monster Reborn")) {
            GameView.getInstance().summonAMonsterCardFromGraveyard();
            currentPlayer.removeCard(address);
        }
        if (spellMode.equals(SpellMode.EQUIP)) {
            String input;
            if (!Game.isAITurn()) {
                System.out.println("choose a faced up monster to be equipped with this spell!(type number in monster zone)");
                input = Main.scanner.nextLine();
            }
            else
                input = Integer.toString(BattlePhase.getInstance().getStrongestMonster(Game.whoseTurnPlayer()));
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

    public static SpellCard getSpellCardByName(String name) {
        for (SpellCard spellCard : spellCards)
            if (spellCard.getName().equals(name)) return spellCard;
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

    public void setSpellMode(SpellMode spellMode) {
        this.spellMode = spellMode;
    }

}
