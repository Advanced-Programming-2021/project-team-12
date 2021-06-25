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
    private static Address defenderAddress;
    private static Address attackerAddress;

    public static void setAddress(String stringAddress, boolean isAttacker) {
        if (isAttacker)
            attackerAddress = new Address(stringAddress);
        else
            defenderAddress = new Address(stringAddress);
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

    //Dear Alireza you have to call "timeToEffect" method after every attack(when attack get over)
    public void run(Address address){
        if(Board.whatKindaMonsterIsHere(address)!=null){
            if(Board.whatKindaMonsterIsHere(address).getNormalAttack()>=1500)
                if(SetSpell.doAnyOneHaveMessengerOfPeace())
                    System.out.println("You can't attack by monster with attack equal or more than 1500 " +
                            "because of MessengerOfPeace.");
                //to be continued
        }

    }

    public static String whatKindOfCardIsDefenderNow() {
        Card card = Board.getCardByAddress(defenderAddress);
        return card.getCardName();
    }

    public static void timeToEffectAfterAttack(){
        MonsterCard.welcomeToEffect();
        TrapCard.welcomeToEffect();
        SpellCard.welcomeToEffect();
    }

    public static void destroyThisAddress(Address address){
        Board.removeCardByAddress(address);
    }

    public static Address whatAddressHasDestroyedNow(){
        return null;
        //written by mohamad
    }

    public static String whatKindaCardGotDestroyedNow(){
        return null;
        //written by mohamad
    }

    public static int whatIndexOfDefender(){
        return Game.whoseRivalPlayer().getIndexOfThisCardByAddress(defenderAddress);
    }

    public static boolean isDefenderFacedDown(){
        return Game.whoseRivalPlayer().getSpellPosition(defenderAddress.getNumber());
    }

    public static Player whichPlayerIsAttacker(){
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
