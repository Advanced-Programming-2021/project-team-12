package controllers.move;

import controllers.Game;
import models.Address;
import models.Board;
import models.Card;
import models.Player;
import models.card.monster.MonsterCard;
import models.card.spell.SpellCard;
import models.card.trap.TrapCard;

public class Attack {
    public static Address defenderAddress;
    public static Address attackerAddress;
    public static String defenderMonsterName;

    public static void setAddress(Address address, boolean isAttacker) {
        if (isAttacker)
            attackerAddress = address;
        else {
            defenderAddress = address;
            defenderMonsterName = Game.whoseRivalPlayer().getMonsterCardByAddress(defenderAddress).getName();
        }
    }

    public static void destroyAllRivalMonstersAndTrapInTheBoard() {
        Board.destroyAllMonster(Game.whoseRivalPlayer());
        Board.destroyAllTrapAndSpells(Game.whoseRivalPlayer());
    }

    public static void destroyAllMonsters() {
        Board.destroyAllMonster(Game.whoseRivalPlayer());
        Board.destroyAllMonster(Game.whoseTurnPlayer());
    }

    public static void destroyAllRivalMonsters() {
        Board.destroyAllMonster(Game.whoseRivalPlayer());
    }

    public static String whatKindOfCardIsDefenderNow() {
        if (Board.getCardByAddress(defenderAddress) == null)
            return null;
        Card card = Board.getCardByAddress(defenderAddress);
        return card.getCardName();
    }

    public static void timeToEffectAfterAttack() {
        MonsterCard.welcomeToEffect();
        TrapCard.welcomeToEffect();
        SpellCard.welcomeToEffect();
    }

    public static void destroyThisAddress(Address address) {
        Board.removeCardByAddress(address);
    }

    public static int whatIndexOfDefender() {
        return Game.whoseRivalPlayer().getIndexOfThisCardByAddress(defenderAddress);
    }

    public static boolean isDefenderFacedDown() {
        return Game.whoseRivalPlayer().getSpellPosition(defenderAddress.getNumber());
    }

    public static Player whichPlayerIsAttacker() {
        return Game.whoseTurnPlayer();
    }

    public static void destroyAllRivalMonstersInTheBoard() {
        Board.destroyAllMonster(Game.whoseRivalPlayer());
    }

    public static void destroyAllMonstersInTheBoard() {
        Board.destroyAllMonster(Game.whoseRivalPlayer());
        Board.destroyAllMonster(Game.whoseTurnPlayer());
    }
}
