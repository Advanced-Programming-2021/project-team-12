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

    //Dear Alireza you have to call "timeToEffect" method after every attack(when attack get over)
    public void run(Address address){
        if(Board.whatKindaMonsterIsHere(address)!=null){
            if(Board.whatKindaMonsterIsHere(address).getNormalAttack()>=1500)
                if(SetSpell.doAnyOneHaveMessengerOfPeace())
                    System.out.println("You can't attack by monster with attach equal or more than 1500 " +
                            "cause MessengerOfPeace.");
                //to be continued
        }

    }
    public static String whatKindOfCardIsDefenderNow() {
        
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
        //written by mohamad
    }
    public static String whatKindaCardGotDestroyedNow(){
        //written by mohamad
    }
    public static int whatIndexOfDefender(){

    }
    public static boolean isDefenderFacedDown(){

    }
    public static Player whichPlayerIsAttacker(){

    }
    public static Player whichPlayerIsDefender(){

    }
    public static void destroyAllRivalMonstersInTheBoard() {
    }
    public static void destroyAllMonstersInTheBoard() {
    }
}
