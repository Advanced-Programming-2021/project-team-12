package controllers.phase;

import view.Game;
import view.Main;

public class StandByPhase {
    public void run(){
        System.out.println("phase: standby phase");
        String input;
        //mamad function here
        checkIfGameEnded();
        while (true) {
            input = Main.scanner.nextLine().trim();
            if (input.matches("^[ ]*next phase[ ]*$"))
                break;
            else
                System.out.println("invalid command");
        }
    }
    public void checkIfGameEnded(){
        if(Game.firstPlayer.getLP() < 0){
            //mirzaei koja beram
        } else if(Game.secondPlayer.getLP()< 0){
            //mirzaei koja beram
        }
    }     
}
