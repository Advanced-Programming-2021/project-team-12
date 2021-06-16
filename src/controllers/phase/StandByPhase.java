package controllers.phase;

import controllers.PhaseControl;
import controllers.move.SetSpell;
import controllers.Game;
import view.Main;

public class StandByPhase {
    public void run(){
        System.out.println("Do you want to destroy Messenger Of Peace(If not you'll lose 100 LP)?" +
                "(type \"yes\" or \"not\"");
        PhaseControl.getInstance().payMessengerOfPeaceSpellCardHarm(Main.scanner.nextLine().trim());
        PhaseControl.getInstance().resetMoves();
        System.out.println("phase: standby phase");
        String input;
        //mamad function here
        PhaseControl.getInstance().checkIfGameEnded();
        while (true) {
            input = Main.scanner.nextLine().trim();
            if (input.matches("^[ ]*next phase[ ]*$"))
                break;
            else
                System.out.println("invalid command");
        }
    }
}
