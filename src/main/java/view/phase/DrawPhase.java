package view.phase;
import com.sun.source.tree.WhileLoopTree;
import controllers.Game;
import controllers.PhaseControl;
import view.AIPhase.AIDrawPhase;
import view.Main;

public class DrawPhase {
    public void run(){
        System.out.println(PhaseControl.getInstance().printWhoseTurnIsIt());
        System.out.println("phase: draw phase");
        Game.setDidWePassBattle(false);
        if (Game.isAITurn())
            new AIDrawPhase().run();
        else {
            String input;
            System.out.println(PhaseControl.getInstance().drawOneCard());
            while (true) {
                input = Main.scanner.nextLine().trim();
                if (input.matches("^[ ]*next phase[ ]*$"))
                    break;
                else
                    System.out.println("invalid command");
            }
            Game.playTurn("StandByPhase");
        }
    }
}
