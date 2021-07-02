package controllers;

import controllers.move.SetSpell;
import view.AIPhase.StandBy;
import view.Main;

public class NextPhaseController {
    public void standByPhase() {
        if (Game.isAITurn())
            new AI().standBy();
        if (SetSpell.doIHaveMessengerOfPeace())
            //aaaaaaaaaaaaaaaaaaaaaaaaaaaa
        PhaseControl.getInstance().resetMoves();
        PhaseControl.getInstance().checkIfGameEnded();
        Game.playTurn("MainPhase1");
    }

    public void mainPhase() {

    }

    public void drawPhase() {

    }

    public void battlePhase() {

    }

    public void endPhase() {

    }
}
