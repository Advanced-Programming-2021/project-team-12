package view.phase;

import controllers.Game;
import controllers.PhaseControl;
import controllers.move.SetSpell;
import view.AIPhase.StandBy;
import view.Main;

public class StandByPhase {
    public void run(){
        System.out.println("phase: standby phase");
        if (Game.isAITurn())
            new StandBy().run();
        if (SetSpell.doIHaveMessengerOfPeace()) {
            System.out.println("Do you want to destroy Messenger Of Peace(If not you'll lose 100 LP)?" +
                    "(type \"yes\" or \"not\")");
            PhaseControl.getInstance().payMessengerOfPeaceSpellCardHarm(Main.scanner.nextLine().trim());
        }
        PhaseControl.getInstance().resetMoves();
        String input;
        PhaseControl.getInstance().checkIfGameEnded();
        while (true) {
            input = Main.scanner.nextLine().trim();
            if (input.matches("^[ ]*next phase[ ]*$"))
                break;
            else
                System.out.println("invalid command");
        }
        Game.playTurn("MainPhase1");
    }
}
