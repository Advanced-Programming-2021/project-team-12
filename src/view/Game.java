package view;

import models.Player;
import models.PlayerTurn;

public class Game {
    public static PlayerTurn playerTurn;
    public static Player firstPlayer;
    public static Player secondPlayer;
    private static Player Winner;
    private static boolean isSurrender;
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
    public static void setIsSurrender(){
        isSurrender=true;
    }
}
