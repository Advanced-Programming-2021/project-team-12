package view;

import java.util.Random;

import controllers.phase.BattlePhase;
import controllers.phase.DrawPhase;
import controllers.phase.EndPhase;
import controllers.phase.MainPhase1;
import controllers.phase.MainPhase2;
import controllers.phase.StandByPhase;
import models.Player;
import models.PlayerTurn;
import models.User;

public class Game {
    public static PlayerTurn playerTurn;
    public static Player firstPlayer;
    public static Player secondPlayer;
    private static User firstUser;
    private static User secondUser;
    private static Player Winner;
    private static boolean isSurrender;
    private static boolean hasWinner;
    private static int round;
    private static int firstPlayerWin = 0;
    private static int secondPlayerWin = 0;
    private static int firstPlayerMaxLP = 0;
    private static int secondPlayerMaxLP = 0;

    private static MainPhase1 mainPhase1;// Dear Ali please do the things with this
    {
        mainPhase1 = new MainPhase1();
    }

    public static void run(User _firstUser, User _secondUser, int _round) {
        restartData(_firstUser, _secondUser, _round);
        int roundCounter = 1;
        while (roundCounter <= round) {
            generateRandomTurn();      
            while (true) {
                playTurn();
                if (hasWinner) {
                    setWinnData();
                    break; 
                }
                switchPlayer();
            }
        }
        if (round == 1) {
            if (Winner.getName().equals(firstPlayer.getName())) {
                firstUser.increaseMoney(firstPlayerMaxLP + 1000);
                firstUser.increaseScore(1000);
            }
        }

    }

    private static void restartData(User _firstUser, User _secondUser, int _round) {
        firstUser = _firstUser;
        secondUser = _secondUser;
        hasWinner = false;
        round = _round;
        firstPlayerWin = 0;
        secondPlayerWin = 0;
        firstPlayerMaxLP = 0;
        secondPlayerMaxLP = 0;
    }

    private static boolean setWinnData() {
        if (Winner.getName().equals(firstPlayer.getName())) {
            if (firstPlayer.getLP() > firstPlayerMaxLP)
                firstPlayerMaxLP = firstPlayer.getLP();
            firstPlayerWin++;
            if (firstPlayerWin > round / 2) return true;
        }
        else {
            if (secondPlayer.getLP() > secondPlayerMaxLP)
                firstPlayerMaxLP = secondPlayer.getLP();
            secondPlayerWin++;
            if (secondPlayerWin > round / 2) return true;
        }
        return false;
    }

    private static void generateRandomTurn() {
        Random random = new Random();  
        int whoIsFirstNumber = random.nextInt(2);
        firstPlayer = new Player(firstUser);
        secondPlayer = new Player(secondUser);
        if (whoIsFirstNumber == 0) 
            playerTurn = PlayerTurn.FIRSTPLAYER;
        else 
            playerTurn = PlayerTurn.SECONDPLAYER;
    }

    public static void run(User firstPlayer, int round) {

    }

    private static void playTurn() {
        new DrawPhase().run();
        if (hasWinner) return;
        new StandByPhase().run();
        if (hasWinner) return;
        new MainPhase1().run();
        if (hasWinner) return;
        new BattlePhase().run();
        if (hasWinner) return;
        new MainPhase2().run();
        if (hasWinner) return;
        new EndPhase().run();
    }

    private static void switchPlayer() {
        if (Game.playerTurn == PlayerTurn.FIRSTPLAYER)
            playerTurn = PlayerTurn.SECONDPLAYER;
        else
            playerTurn = PlayerTurn.FIRSTPLAYER;
    }

    public static Player whoseTurnPlayer() {
        if (Game.playerTurn == PlayerTurn.FIRSTPLAYER)
            return Game.firstPlayer;
        else
            return Game.secondPlayer;
    }

    public static Player whoseRivalPlayer() {
        if (Game.playerTurn == PlayerTurn.FIRSTPLAYER)
            return Game.secondPlayer;
        else
            return Game.firstPlayer;
    }

    public static Player getWinner() {
        return Winner;
    }

    public static boolean isSurrender() {
        return isSurrender;
    }

    public static void setIsSurrender(boolean isSurrender1) {
        isSurrender = isSurrender1;
    }

    public static void setWinner(Player winner) {
        Winner = winner;
        hasWinner = true;
    }

    public static MainPhase1 getMainPhase1() {
        return mainPhase1;
    }

}
