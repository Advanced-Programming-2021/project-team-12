package controllers.move;

import models.Address;
import models.Board;
import models.PlayerTurn;
import controllers.Game;

public class SetSpell {
    public static boolean doAnyOneHaveForest(){
        return true;
        //adlfkhasdfihsafkjshdf
    }

    public static boolean doIHaveMessengerOfPeace() {
        return true;
        //klsdhfsalkdfhsadf
    }

    public static void destroyMessengerOfPeace() {

    }

    public static boolean doIHaveClosedForest() {
        return true;
        //klsdhfsalkdfhsadf
    }

    public static boolean doAnyOneHaveUmiruka() {
        return true;
        //klsdhfsalkdfhsadf
    }

    public void run(Address address){
        
    }
    public void spellActivation(){






        doSpellAbsorptionEffect();
    }

    private void doSpellAbsorptionEffect() {
        if(doIHaveSpellAbsorptionCard()) Game.whoseTurnPlayer().increaseLP(500);
    }

    private boolean doIHaveSpellAbsorptionCard() {
        return true;
        //klsdhfsalkdfhsadf
    }
    public static boolean doAnyOneHaveMessengerOfPeace(){
        return true;
        //klsdhfsalkdfhsadf
    }
    public static boolean doAnyOneHaveYami(){
        return true;
        //klsdhfsalkdfhsadf
    }
}
