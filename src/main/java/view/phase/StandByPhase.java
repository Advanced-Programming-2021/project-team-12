package view.phase;

import controllers.Game;
import controllers.PhaseControl;
import controllers.move.SetSpell;
import view.AIPhase.StandBy;
import view.Main;

public class StandByPhase {
    public void run() throws Exception {
        if (Game.isAITurn())
            new StandBy().run();
        if (SetSpell.doIHaveMessengerOfPeace()) {
            System.out.println("Do you want to destroy Messenger Of Peace(If not you'll lose 100 LP)?" +
                    "(type \"yes\" or \"not\")");
            PhaseControl.getInstance().payMessengerOfPeaceSpellCardHarm(Main.scanner.nextLine().trim());
        }
        PhaseControl.getInstance().resetMoves();
        PhaseControl.getInstance().checkIfGameEnded();
        Game.playTurn("MainPhase1");
    }
}
