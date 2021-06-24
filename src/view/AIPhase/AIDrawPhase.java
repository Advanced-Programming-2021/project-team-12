package view.AIPhase;

import controllers.PhaseControl;
import view.Main;

public class AIDrawPhase {
    public void run(){
        System.out.println("phase: draw phase");
        System.out.println(PhaseControl.getInstance().drawOneCard());
    }
}
