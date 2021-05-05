package controllers.move;

import models.Address;
import models.Board;
import models.PlayerTurn;
import view.Game;

public class SetSpell {
    public static boolean doAnyOneHaveForest;

    public static boolean doIHaveMessengerOfPeace() {
    }

    public static void destroyMessengerOfPeace() {
    }

    public static boolean doIHaveClosedForest() {

    }

    public static boolean doAnyOneHaveUmiruka() {
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

    }
    public static boolean doAnyOneHaveMessengerOfPeace(){

    }
    public static boolean doAnyOneHaveYami(){

    }
}
