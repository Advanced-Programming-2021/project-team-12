package view;

import controllers.phase.MainPhase1;
import models.Player;
import models.PlayerTurn;

public class Game {
    public static PlayerTurn playerTurn;
    public static Player firstPlayer;
    public static Player secondPlayer;
    private static Player Winner;
    private static boolean isSurrender;
    private static MainPhase1 mainPhase1;//Dear Ali please do the things with this
    {
        mainPhase1=new MainPhase1();
    }
    public static Player whoseTurnPlayer(){
        if(Game.playerTurn==PlayerTurn.FIRSTPLAYER) return Game.firstPlayer;
        else return Game.secondPlayer;
    }
    public static Player whoseRivalPlayer(){
        if(Game.playerTurn==PlayerTurn.FIRSTPLAYER) return Game.secondPlayer;
        else return Game.firstPlayer;
    }

    public static Player getWinner() {
        return Winner;
    }
    public static boolean isSurrender(){
        return isSurrender;
    }
    public static void setIsSurrender(boolean isSurrender1){
        isSurrender=isSurrender1;
    }

    public static void setWinner(Player winner) {
        Winner = winner;
    }
    public static MainPhase1 getMainPhase1(){
        return mainPhase1;
    }
}
