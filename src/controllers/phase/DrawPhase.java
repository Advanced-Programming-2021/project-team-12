package controllers.phase;
import models.PlayerTurn;
import view.Game;
import view.Main;

public class DrawPhase {
    public void run(){
        System.out.println("phase: draw phase");
        String input;
        drawOneCard();
        while (true) {
            input = Main.scanner.nextLine().trim();
            if (input.matches("^[ ]*next phase[ ]*$"))
                nextPhase();
            else
                System.out.println("invalid command");
        }
    }
    public void drawOneCard(){
        if(Game.playerTurn==PlayerTurn.FIRSTPLAYER){
            Game.firstPlayer.
        } else{

        }
    }
}
