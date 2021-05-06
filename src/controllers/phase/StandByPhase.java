package controllers.phase;

import controllers.move.SetSpell;
import view.Game;
import view.Main;

public class StandByPhase {
    public void run(){
        resetMoves();
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
    public void resetMoves(){
        Game.firstPlayer.setHeSummonedOrSet(false);
        Game.secondPlayer.setHeSummonedOrSet(false);
        Game.firstPlayer.setDidWeChangePositionThisCardInThisTurnCompletelyFalse();
        Game.secondPlayer.setDidWeChangePositionThisCardInThisTurnCompletelyFalse();
        Game.firstPlayer.setDidWeAttackByThisCadInThisCardInThisTurnCompletelyFalse();
        Game.secondPlayer.setDidWeAttackByThisCadInThisCardInThisTurnCompletelyFalse();
    }
    public void checkIfGameEnded(){
        if(Game.firstPlayer.getLP() < 0){
            //mirzaei koja beram
        } else if(Game.secondPlayer.getLP()< 0){
            //mirzaei koja beram
        }
    }
    public static void payMessengerOfPeaceSpellCardHarm(){
        if(SetSpell.doIHaveMessengerOfPeace()){
            System.out.println("Do you want to destroy Messenger Of Peace(If not you'll lose 100 LP)?" +
                    "(type \"yes\" or \"not\"");
            if(Main.scanner.nextLine().equals("yes")) SetSpell.destroyMessengerOfPeace();
            else Game.whoseTurnPlayer().decreaseLP(100);

        }
    }
}
