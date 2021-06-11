package controllers;

import controllers.move.SetSpell;
import models.PlayerTurn;
import view.Game;
import view.Main;

public class PhaseControl {
    private static PhaseControl instance;

    public static PhaseControl getInstance(){
        if(instance == null){
            instance = new PhaseControl();
        }
        return instance;
    }

    public void payMessengerOfPeaceSpellCardHarm(String answer){
        if(SetSpell.doIHaveMessengerOfPeace()){
            if(answer.equals("yes")) SetSpell.destroyMessengerOfPeace();
            else Game.whoseTurnPlayer().decreaseLP(100);
        }
    }

    public void resetMoves(){
        Game.firstPlayer.setHeSummonedOrSet(false);
        Game.secondPlayer.setHeSummonedOrSet(false);
        Game.firstPlayer.setDidWeChangePositionThisCardInThisTurnCompletelyFalse();
        Game.secondPlayer.setDidWeChangePositionThisCardInThisTurnCompletelyFalse();
        Game.firstPlayer.setDidWeAttackByThisCardInThisCardInThisTurnCompletelyFalse();
        Game.secondPlayer.setDidWeAttackByThisCardInThisCardInThisTurnCompletelyFalse();
        Game.firstPlayer.setOneHisMonstersDestroyedInThisRound(false);
        Game.secondPlayer.setOneHisMonstersDestroyedInThisRound(false);
    }

    public static void checkIfGameEnded(){
        if(Game.firstPlayer.getLP() < 0){
            //mirzaei koja beram
        } else if(Game.secondPlayer.getLP()< 0){
            //mirzaei koja beram
        }
    }

    public String drawOneCard(){
        if(Game.playerTurn== PlayerTurn.FIRSTPLAYER){
            String newAddedCard = Game.firstPlayer.addCardFromUnusedToHand();
            if(newAddedCard.equals("unused is empty")){
                Game.setWinner(Game.secondPlayer);
                return "first player cannot draw a card, second player is the winner";
            } else if(!newAddedCard.equals("Hand is full")) {
                return "new card added to the hand : " + newAddedCard;
            }
        } else{
            String newAddedCard = Game.secondPlayer.addCardFromUnusedToHand();
            if(newAddedCard.equals("unused is empty")){
                Game.setWinner(Game.firstPlayer);
                return "second player is the winner";
            } else if(!newAddedCard.equals("Hand is full")) {
                return "second player cannot draw a card,new card added to the hand : " + newAddedCard;
            }
        }
        return null;
    }

    public void doEffect() {
        if (Game.firstPlayer.isOneHisSupplySquadActivated()) {
            if (Game.firstPlayer.isOneHisMonstersDestroyedInThisRound())
                if (!Game.firstPlayer.isHandFull()) Game.firstPlayer.addCardFromUnusedToHand();
        }
        if (Game.secondPlayer.isOneHisSupplySquadActivated()) {
            if (Game.secondPlayer.isOneHisMonstersDestroyedInThisRound())
                if (!Game.secondPlayer.isHandFull()) Game.secondPlayer.addCardFromUnusedToHand();
        }
    }

    public void switchPlayerTurn() {
        if (Game.playerTurn == PlayerTurn.FIRSTPLAYER) {
            Game.playerTurn = PlayerTurn.SECONDPLAYER;
        } else {
            Game.playerTurn = PlayerTurn.FIRSTPLAYER;
        }
    }

    public String printWhoseTurnIsIt() {
        if (Game.playerTurn == PlayerTurn.FIRSTPLAYER) {
            return "its " + Game.firstPlayer.getNickName() + "’s turn";
        } else {
            return "its " + Game.secondPlayer.getNickName() + "’s turn";
        }
    }
}
