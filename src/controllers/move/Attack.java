package controllers.move;

import models.Address;
import models.Board;
import models.Player;
import models.card.monster.MonsterCard;
import models.card.spell.SpellCard;
import models.card.trap.TrapCard;

public class Attack {


    public static void destroyAllRivalMonstersAndTrapInTheBoard() {
    }

    public static void destroyAllMonsters() {
    }

    public static void destroyAllRivalMonsters() {
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
        return null;
        //sadflksdf
    }

    public static void timeToEffectAfterAttack(){
        MonsterCard.welcomeToEffect();
        TrapCard.welcomeToEffect();
        SpellCard.welcomeToEffect();
    }
    public static void destroyThisAddress(Address address){
        //written by mohamad
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
        return 1;
        //sa;lkdfjsldfkjsad;kjfhsd
    }
    public static boolean isDefenderFacedDown(){
        return true;
        //s;kdfhslkdfjhsf
    }
    public static Player whichPlayerIsAttacker(){
        return null;
        //sadlfkhskldfjhsaldkjf
    }
    public static Player whichPlayerIsDefender(){
        return null;
        //asdlfhslkdfjhsad
    }
    public static void destroyAllRivalMonstersInTheBoard() {
    }
    public static void destroyAllMonstersInTheBoard() {
    }
}
