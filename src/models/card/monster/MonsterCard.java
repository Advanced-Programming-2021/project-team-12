package models.card.monster;

import controllers.move.Attack;
import models.Board;
import models.Player;
import models.PlayerTurn;
import models.card.monster.monster_effect.MonsterEffect;
import models.card.monster.monster_effect.MonsterEffect;
import models.Board.*;
import view.Effect;
import view.Game;
//import card.spell.SpellCard;

import java.util.ArrayList;

public class MonsterCard {
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

    public MonsterCard(int level, int attack, int defence, String effect, MonsterMode monsterMode,
                       boolean isRitual, String name, int price, Attribute attribute, MonsterEffect monsterEffect) {
        this.level = level;
        this.attack = attack;
        this.defence = defence;
        this.effect = effect;
        this.monsterMode = monsterMode;
        this.isRitual = isRitual;
        this.name = name;
        this.price = price;
        this.attribute = attribute;
        this.monsterEffect = monsterEffect;
        monsterCards.add(this);
    }

    public void runEffect() {
        this.monsterEffect.run();
    }

    public int getLevel() {
        return level;
    }

    public int getAttack() {
        if (Board.doThisMonsterExistFacedUp("CommandKnight")) return (attack + 400);
        return attack;
    }

    public int getDefence(boolean isFacedUp) {
        if ((name.equals("CommandKnight")) && (Board.howManyMonsterIsOnTheBoard() > 1) && isFacedUp) return 100000000;
        // I should regard effect on or not
        if (isFacedUp) {
            if (name.equals("Suijin")) if(whenSuijinIsDefending()) return 100000000;
        }
        return defence;
    }

    public boolean whenSuijinIsDefending() {
        int index = Attack.whatIndexOfDefender();
        if (Game.playerTurn == PlayerTurn.FIRSTPLAYER) {
            if (!Game.firstPlayer.doIndexExistInSuijin(index))
                if (Effect.run("Suijin").equals("yes")) {
                   Game.firstPlayer.addIndexToSuijin(index);
                     return true;
                }
        } else {
            if (!Game.secondPlayer.doIndexExistInSuijin(index))
                if (Effect.run("Suijin").equals("yes")) {
                    Game.secondPlayer.addIndexToSuijin(index);
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

    public boolean checkIsRitual() {
        return isRitual;
    }

    public static MonsterCard getMonsterCardByName(String name) {
        for (MonsterCard monsterCard : monsterCards)
            if (monsterCard.name.equals(name)) return monsterCard;
        return null;
    }

    public static void welcomeToEffect() {
        String addressDestroyer = Attack.whatAddressDestroyedNow();
        String destroyer = Attack.whatKindaCardGetDestroyedNow();
        String defender = Attack.whatKindaCardIsDefenderNow();
        if (destroyer.equals("YomiShip")) {
            Attack.destroyThisAddress(destroyer);
        }

    }

    public static void welcomeToEffectStandBy() {

    }
}
