package controllers.move;

import models.Board;
import controllers.Game;

public class SetSpell {
    public static boolean doIHaveMessengerOfPeace() {
        return Game.whoseTurnPlayer().doIHaveSpellCard("Messenger of Peace");
    }

    public static void destroyMessengerOfPeace() {
        Game.whoseTurnPlayer().removeOneOfTrapOrSpell("Messenger of Peace");
    }

    public static boolean doAnyOneHaveMessengerOfPeace() {
        return Board.getAnyoneHave("Messenger of Peace");

    }
}
