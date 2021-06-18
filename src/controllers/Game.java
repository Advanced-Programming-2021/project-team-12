package controllers;

import java.util.Random;

import controllers.phase.*;
import controllers.phase.MainPhase;
import models.Player;
import models.PlayerTurn;
import models.User;
import view.MainMenu;
import view.SetMainCards;

public class Game {
    private static int roundCounter;
    public static PlayerTurn playerTurn;
    public static Player firstPlayer;
    public static Player secondPlayer;
    private static User firstUser;
    private static User secondUser;
    private static Player winner;
    private static boolean isSurrender;
    private static boolean hasWinner;
    private static int round;
    private static int firstPlayerWin = 0;
    private static int secondPlayerWin = 0;
    private static int firstPlayerMaxLP = 0;
    private static int secondPlayerMaxLP = 0;

    private static MainPhase mainPhase1;
    private static MainPhase mainPhase2;

    static {
        mainPhase1 = new MainPhase();
        mainPhase2 = new MainPhase();
        mainPhase1.setWhatMainIsPhase(1);
        mainPhase2.setWhatMainIsPhase(2);
    }

    public static void run(User _firstUser, User _secondUser, int _round) {
        restartData(_firstUser, _secondUser, _round);
        generateRandomTurn();
        playTurn("DrawPhase");
    }

    private static void EndGame() {
        int floorWin = 0;
        if (round == 3)
            floorWin = 1;
        setWinnData();
        if (firstPlayerWin > floorWin || secondPlayerWin > floorWin) {
            setScoreAndMoneyOfPlayers();
            new MainMenu(firstUser);
        } else {
            firstPlayer.reset();
            secondPlayer.reset();
            new SetMainCards(firstPlayer, secondPlayer);
            generateRandomTurn();
            playTurn("DrawPhase");
        }
    }

    private static void setScoreAndMoneyOfPlayers() {
        if (round == 1) {
            if (winner.getName().equals(firstPlayer.getName())) {
                firstUser.increaseMoney(firstPlayerMaxLP + 1000);
                firstUser.increaseScore(1000);
                secondUser.increaseMoney(100);
            } else {
                secondUser.increaseMoney(secondPlayerMaxLP + 1000);
                secondUser.increaseScore(1000);
                firstUser.increaseMoney(100);
            }
        }
        if (round == 3) {
            if (winner.getName().equals(firstPlayer.getName())) {
                firstUser.increaseMoney(firstPlayerMaxLP * 3 + 3000);
                firstUser.increaseScore(3000);
                secondUser.increaseMoney(300);
            } else {
                secondUser.increaseMoney(secondPlayerMaxLP * 3 + 3000);
                secondUser.increaseScore(3000);
                firstUser.increaseMoney(300);
            }
        }
        SaveFile.saveUser(firstUser);
        SaveFile.saveUser(secondUser);
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
        roundCounter = 1;
    }

    private static void setWinnData() {
        if (winner.getName().equals(firstPlayer.getName())) {
            if (firstPlayer.getLP() > firstPlayerMaxLP)
                firstPlayerMaxLP = firstPlayer.getLP();
            firstPlayerWin++;
        } else {
            if (secondPlayer.getLP() > secondPlayerMaxLP)
                firstPlayerMaxLP = secondPlayer.getLP();
            secondPlayerWin++;
        }
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

        SaveFile.saveUser(firstUser);
    }

    private static void playTurn(String phase) {
        switch (phase) {
            case "DrawPhase":
                new DrawPhase().run();
                break;
            case "StandByPhase":
                new StandByPhase().run();
                break;
            case "MainPhase1":
                mainPhase1.run();
                break;
            case "BattlePhase":
                goToBattlePhase();
                break;
            case "MainPhase2":
                mainPhase2.run();
                break;
            case "EndPhase":
                switchPlayer();
                new EndPhase().run();
                break;
            case "EndGame":
                EndGame();
                break;
        }
    }

    private static void goToBattlePhase() {
        BattlePhase.getInstance().run();
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
        return winner;
    }

    public static boolean isSurrender() {
        return isSurrender;
    }

    public static void setIsSurrender(boolean isSurrender1) {
        isSurrender = isSurrender1;
    }

    public static void setWinner(Player winner) {
        Game.winner = winner;
        hasWinner = true;
    }

    public static MainPhase getMainPhase1() {
        return mainPhase1;
    }

    public static MainPhase getMainPhase2() {
        return mainPhase2;
    }
}
