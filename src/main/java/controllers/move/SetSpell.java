package controllers.move;

import models.Board;
import controllers.Game;

public class SetSpell {
    public static boolean doIHaveMessengerOfPeace() {
        return true;
        //return Game.whoseTurnPlayer().doIHaveSpellCard("Messenger of peace");
    }

    public static void destroyMessengerOfPeace() {
        Game.whoseTurnPlayer().removeOneOfTrapOrSpell("Messenger of peace");
    }

    public static boolean doAnyOneHaveMessengerOfPeace() {
        return Board.getAnyoneHave("Messenger of peace");
    }
}
