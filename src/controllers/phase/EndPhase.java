package controllers.phase;

import models.PlayerTurn;
import view.Game;
import view.Main;

public class EndPhase {
    public void run(){
        System.out.println("phase: end phase");
        checkIfGameEnded();
        swithPlayerTurn();
        printWhosTurnIsIt();
    }
    public void checkIfGameEnded(){
        if(Game.firstPlayer.getLP() < 0){
            //mirzaei koja beram
        } else if(Game.secondPlayer.getLP()< 0){
            //mirzaei koja beram
        }
    }
    public void swithPlayerTurn(){
        if(Game.playerTurn == PlayerTurn.FIRSTPLAYER){
            Game.playerTurn = PlayerTurn.SECONDPLAYER;
        } else {
            Game.playerTurn = PlayerTurn.FIRSTPLAYER;
        }
    }
    public void printWhosTurnIsIt(){
        if(Game.playerTurn == PlayerTurn.FIRSTPLAYER){
            System.out.println("its " + Game.firstPlayer.getNickname() + "’s turn");
        } else {
            System.out.println("its " + Game.secondPlayer.getNickname() + "’s turn");
        }
    }
}
