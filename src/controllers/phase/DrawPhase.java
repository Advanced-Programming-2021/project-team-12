package controllers.phase;
import controllers.PhaseControl;
import models.PlayerTurn;
import controllers.Game;
import view.Main;

public class DrawPhase {
    public void run(){
        System.out.println("phase: draw phase");
        String input;
        System.out.println(PhaseControl.getInstance().drawOneCard());
        while (true) {
            input = Main.scanner.nextLine().trim();
            if (input.matches("^[ ]*next phase[ ]*$"))
                break;
            else
                System.out.println("invalid command");
        }
    }
}
