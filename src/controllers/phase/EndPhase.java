package controllers.phase;

import models.PlayerTurn;
import view.Game;
import view.Main;

public class EndPhase {
    public void run(){
        System.out.println("phase: end phase");
        checkIfGameEnded();
        switchPlayerTurn();
        printWhoseTurnIsIt();
    }
    public void checkIfGameEnded(){
        if(Game.firstPlayer.getLP() < 0){
            //mirzaei koja beram
        } else if(Game.secondPlayer.getLP()< 0){
            //mirzaei koja beram
        }
    }
    public void switchPlayerTurn(){
        if(Game.playerTurn == PlayerTurn.FIRSTPLAYER){
            Game.playerTurn = PlayerTurn.SECONDPLAYER;
        } else {
            Game.playerTurn = PlayerTurn.FIRSTPLAYER;
        }
    }
    public void printWhoseTurnIsIt(){
        if(Game.playerTurn == PlayerTurn.FIRSTPLAYER){
            System.out.println("its " + Game.firstPlayer.getNickName() + "’s turn");
        } else {
            System.out.println("its " + Game.secondPlayer.getNickName() + "’s turn");
        }
    }
}
