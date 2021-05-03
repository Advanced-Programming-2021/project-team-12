package controllers.move;

import models.Address;
import models.card.monster.MonsterCard;
import models.card.spell.SpellCard;
import models.card.trap.TrapCard;

public class Attack {
    public static String whatKindaCardIsDefenderNow() {
    }

    //Dear Alireza you have to call "timeToEffect" method after every attack(when attack get over)
    public void run(Address address){
        
    }
    public void timeToEffect(){
        MonsterCard.welcomeToEffect();
        TrapCard.welcomeToEffect();
        SpellCard.welcomeToEffect();
    }
    public static void destroyThisAddress(String address){
        //written by mohamad
    }
    public static String whatAddressDestroyedNow(){
        //written by mohamad
    }
    public static String whatKindaCardGetDestroyedNow(){
        //written by mohamad
    }
    public static int whatIndexOfDefender(){

    }
}
