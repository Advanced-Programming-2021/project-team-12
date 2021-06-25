package view.AIPhase;

import controllers.Game;
import controllers.PhaseControl;
import controllers.move.SetSpell;
import view.Main;

public class StandBy {
    public void run(){
        if (SetSpell.doIHaveMessengerOfPeace()) {
            String answer = "yes";
            if (Game.whoseTurnPlayer().getLP() > 2000)
                answer = "not";
            PhaseControl.getInstance().payMessengerOfPeaceSpellCardHarm(answer);
        }
        PhaseControl.getInstance().resetMoves();
        PhaseControl.getInstance().checkIfGameEnded();
        Game.playTurn("MainPhase1");
    }
}
