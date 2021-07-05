package view.AIPhase;

import controllers.Game;
import controllers.PhaseControl;
import view.Main;

public class AIDrawPhase {
    public void run() throws Exception {
        System.out.println(PhaseControl.getInstance().drawOneCard());
        //Game.playTurn("StandByPhase");
    }
}
