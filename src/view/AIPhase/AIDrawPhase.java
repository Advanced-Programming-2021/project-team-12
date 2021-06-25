package view.AIPhase;

import controllers.Game;
import controllers.PhaseControl;
import view.Main;

public class AIDrawPhase {
    public void run(){
        System.out.println("111");
        System.out.println("phase: draw phase");
        System.out.println(PhaseControl.getInstance().drawOneCard());
        Game.playTurn("StandByPhase");
    }
}
