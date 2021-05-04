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
                break;
            else
                System.out.println("invalid command");
        }
    }
    public void drawOneCard(){
        if(Game.playerTurn==PlayerTurn.FIRSTPLAYER){
            String newAddedCard = Game.firstPlayer.addCardFromUnusedToHand();
            System.out.println("new card added to the hand : " + newAddedCard);
        } else{
            String newAddedCard = Game.secondPlayer.addCardFromUnusedToHand();
            System.out.println("new card added to the hand : " + newAddedCard);
        }
        //if no card loser
        //esme card ro bayad bedoonam
    }
    public void checkIfGameEnded(){
        if(Game.firstPlayer.getLP() < 0){
            //mirzaei koja beram
        } else if(Game.secondPlayer.getLP()< 0){
            //mirzaei koja beram
        }
    }
}
