package controllers.move;

import models.Board;
import controllers.Game;

public class SetSpell {
    public static boolean doAnyOneHaveForest(){
        return Board.getAnyoneHave("Forest");
    }

    public static boolean doIHaveMessengerOfPeace() {
        return Game.whoseTurnPlayer().doIHaveSpellCard("Messenger of Peace");
    }

    public static void destroyMessengerOfPeace() {
        Game.whoseTurnPlayer().removeOneOfTrapOrSpell("Messenger of Peace");
    }

    public static boolean doIHaveClosedForest() {
        return Game.whoseTurnPlayer().doIHaveSpellCard("Closed Forest");
    }

    public static boolean doAnyOneHaveUmiiruka() {
        return Board.getAnyoneHave("Umiiruka");
    }

    public static boolean doAnyOneHaveMessengerOfPeace(){
        return Board.getAnyoneHave("Messenger of Peace");
    }

    public static boolean doAnyOneHaveYami(){
        return Board.getAnyoneHave("Yami");
    }
}
