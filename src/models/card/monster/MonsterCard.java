package models.card.monster;

import controllers.move.Attack;
import controllers.move.SetSpell;
import models.Address;
import models.Board;
import models.Player;
import models.PositionOfCardInBoard;
import models.card.monster.monster_effect.MonsterEffect;
import models.card.spell.SpellCard;
import view.Effect;
import controllers.Game;
//import card.spell.SpellCard;

import java.util.ArrayList;

public class MonsterCard {
    private String description;
    private int level;
    private int attack;
    private int defence;
    private String effect;
    private MonsterMode monsterMode;
    private boolean isRitual;
    private String name;
    private int price;
    private Attribute attribute;
    private MonsterEffect monsterEffect;
    private static ArrayList<MonsterCard> monsterCards;

    static {
        monsterCards = new ArrayList<>();
    }

    public MonsterCard(int level, int attack, int defenceis, MonsterMode monsterMode,
                       boolean isRitual, String name, int price, Attribute attribute, String description) {
        this.level = level;
        this.attack = attack;
        this.defence = defence;
        this.effect = effect;
        this.monsterMode = monsterMode;
        this.isRitual = isRitual;
        this.name = name;
        this.price = price;
        this.attribute = attribute;
        this.description = description;
        monsterCards.add(this);
    }


    public void runEffect() {
        this.monsterEffect.run();
    }

    public int getLevel() {
        return level;
    }

    public int getAttack(Address address) {
        Player currentPlayer = Game.whoseTurnPlayer();
        int attack = this.attack;
        int indexOfAttacker = currentPlayer.getIndexOfThisCardByAddress(address);
        int spellPlace = currentPlayer.getFromMonsterToSpellEquip(address.getNumber());
        if (spellPlace > 0) {
            Address spellAddress = new Address(spellPlace, "spell", true);
            attack = doEquipSpellEffect(attack, currentPlayer.getSpellCardByAddress(spellAddress), false, address);
        }
        if (name.equals("Beast King Barbaros"))
            if (!currentPlayer.didBeastKingBarbarosSummonedSuperHighLevel(indexOfAttacker)) attack -= 1900;
        if (Attack.whatKindOfCardIsDefenderNow() != null && Attack.whatKindOfCardIsDefenderNow().equals("Suijin") && !Attack.isDefenderFacedDown()) {
            if (whenSuijinIsDefending())
                return 0;
        }
        if (currentPlayer.doIHaveSpellCard("Umiiruka")) {
            if (monsterMode == MonsterMode.AQUA) {
                attack += 500;
            }
        }
        if (monsterMode == MonsterMode.BEAST || monsterMode == MonsterMode.BEAST_WARRIOR) {
            if (currentPlayer.doIHaveSpellCard("Closed Forest")) {
                attack += 100 * Board.numberOfAllMonstersInGraveYard();
            }
        }
        if (currentPlayer.doIHaveSpellCard("Forest")) {
            if (monsterMode == MonsterMode.BEAST || monsterMode == MonsterMode.BEAST_WARRIOR || monsterMode == MonsterMode.INSECT)
                attack += 200;
        }
        if (currentPlayer.doIHaveSpellCard("Yami")) {
            if (monsterMode == MonsterMode.SPELLCASTER || monsterMode == MonsterMode.FIEND) attack += 200;
            if (monsterMode == MonsterMode.FAIRY) attack -= 200;
        }
        if (Board.doThisMonsterExistFacedUp("Command Knight")) attack = +400;
        if (name.equals("Calculator")) return 300 * Board.sumOfLevelOfFacedUpMonsters();//doubt
        if (attack < 0)
            return 0;
        return attack;
    }

    private int doEquipSpellEffect(int attackOrDefence, SpellCard spellCard, boolean isItDefence, Address address) {
        if (isItDefence) {
            if (spellCard.getName().equals("Sword of dark destruction")) {
                if (monsterMode.equals(MonsterMode.SPELLCASTER) || monsterMode.equals(MonsterMode.FIEND))
                    attackOrDefence -= 200;
            }
            if (spellCard.getName().equals("Magnum Shield")) {
                if (monsterMode.equals(MonsterMode.WARRIOR)) {
                    PositionOfCardInBoard positionOfCardInBoard = Game.whoseTurnPlayer().getMonsterPosition(address.getNumber());
                    if (positionOfCardInBoard.equals(PositionOfCardInBoard.DH) || positionOfCardInBoard.equals(PositionOfCardInBoard.DO))
                        attackOrDefence += attack;
                }
            }
        } else {
            if (spellCard.getName().equals("Sword of dark destruction")) {
                if (monsterMode.equals(MonsterMode.SPELLCASTER) || monsterMode.equals(MonsterMode.FIEND))
                    attackOrDefence += 400;
            }
            if (spellCard.getName().equals("Black Pendant")) attackOrDefence += 500;
            if (spellCard.getName().equals("Magnum Shield")) {
                if (monsterMode.equals(MonsterMode.WARRIOR)) {
                    if (Game.whoseTurnPlayer().getMonsterPosition(address.getNumber()).equals(PositionOfCardInBoard.OO))
                        attackOrDefence += defence;
                }
            }
        }
        if (spellCard.getName().equals("United We Stand"))
            attackOrDefence += (800 * Game.whoseTurnPlayer().getMonsterZoneCard().size());

        return attackOrDefence;
    }

    public int getDefenceNumber() {
        return defence;
    }

    public int getDefence(boolean isFacedUp, Address address) {
        Player currentPlayer = Game.whoseTurnPlayer();
        int defence = this.defence;
        int spellPlace = currentPlayer.getFromMonsterToSpellEquip(address.getNumber());
        if (spellPlace > 0) {
            Address spellAddress = new Address(spellPlace, "spell", true);
            defence = doEquipSpellEffect(defence, currentPlayer.getSpellCardByAddress(spellAddress), true, address);
        }
        if (currentPlayer.doIHaveSpellCard("Umiiruka")) {
            if (monsterMode == MonsterMode.AQUA) {
                defence -= 400;
            }
        }
        if (currentPlayer.doIHaveSpellCard("Forest")) {
            if (monsterMode == MonsterMode.BEAST || monsterMode == MonsterMode.BEAST_WARRIOR || monsterMode == MonsterMode.INSECT)
                defence += 200;
        }
        if (currentPlayer.doIHaveSpellCard("Yami")) {
            if (monsterMode == MonsterMode.SPELLCASTER || monsterMode == MonsterMode.FIEND) defence += 200;
            if (monsterMode == MonsterMode.FAIRY) defence -= 200;
        }
        if ((name.equals("Command Knight")) && (Board.howManyMonsterIsOnTheBoard() > 1) && isFacedUp) return -1;
        return defence;
    }

    public boolean whenSuijinIsDefending() {
        int index = Attack.whatIndexOfDefender();
        if (!Attack.whichPlayerIsAttacker().doIndexExistInSuijin(index)) {
            if (Effect.run("Suijin").equals("yes")) {
                Attack.whichPlayerIsAttacker().addIndexToSuijin(index);
                return true;
            }
        }
        return false;
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

    public MonsterMode getMonsterMode() {
        return monsterMode;
    }

    public static ArrayList<MonsterCard> getMonsterCards() {
        return monsterCards;
    }

    public Attribute getAttribute() {
        return attribute;
    }

    public boolean isRitual() {
        return isRitual;
    }

    public static MonsterCard getMonsterCardByName(String name) {
        for (MonsterCard monsterCard : monsterCards)
            if (monsterCard.name.equals(name)) return monsterCard;
        return null;
    }

    public static void welcomeToEffect() {
        if (Game.whoseRivalPlayer().getMonsterCardByAddress(Attack.defenderAddress) == null)
            if (Attack.defenderMonsterName.equals("Yomi Ship")) Attack.destroyThisAddress(Attack.attackerAddress);
        if (Attack.defenderMonsterName.equals("Marshmallon"))
            if (Attack.isDefenderFacedDown()) Attack.whichPlayerIsAttacker().decreaseLP(1000);
    }

    public static void welcomeToEffectStandBy() {

    }

    public int getNormalAttack() {
        return attack;
    }

    public int getNormalDefence() {
        return defence;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setAttack(int attack) {
        this.attack = attack;
    }

    public void setAttribute(Attribute attribute) {
        this.attribute = attribute;
    }

    public void setDefence(int defence) {
        this.defence = defence;
    }

    public void setEffect(String effect) {
        this.effect = effect;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setRitual(boolean ritual) {
        isRitual = ritual;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public void setMonsterMode(MonsterMode monsterMode) {
        this.monsterMode = monsterMode;
    }

    public void setMonsterEffect(MonsterEffect monsterEffect) {
        this.monsterEffect = monsterEffect;
    }
}
