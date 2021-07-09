package controllers;


import Utility.Sounds;
import javafx.stage.Stage;
import view.*;
import view.phase.MainPhase;
import models.Player;
import models.PlayerTurn;
import models.User;

public class Game {
    public static PlayerTurn playerTurn;
    public static Player firstPlayer;
    public static Player secondPlayer;
    private static User firstUser;
    private static User secondUser;
    private static Player winner;
    private static boolean isSurrender;
    private static boolean isAIGame;
    private static int round;
    private static int firstPlayerWin = 0;
    private static int secondPlayerWin = 0;
    private static int firstPlayerMaxLP = 0;
    private static int secondPlayerMaxLP = 0;
    private static boolean didWePassBattle;
    private static MainPhase mainPhase1;
    private static MainPhase mainPhase2;
    private static String phase;
    private static GameView gameView;

    static {
        gameView = new GameView();
    }

    public static GameView getGameView() {
        return gameView;
    }

    static {
        mainPhase1 = new MainPhase();
        mainPhase2 = new MainPhase();
//        mainPhase1.setWhatMainIsPhase(1);
//        mainPhase2.setWhatMainIsPhase(2);
    }

    public static void run(User _firstUser, User _secondUser, int _round) throws Exception {
        isAIGame = false;
        restartData(_firstUser, _secondUser, _round);
        generateRandomTurn();
    }

    public static boolean getIsAI(){
        return isAIGame;
    }

    public static void run(User _firstUser, int _round) throws Exception {
        isAIGame = true;
        restartData(_firstUser, _round);
        generateRandomTurn();
    }

    public static void setDidWePassBattle(boolean didWe) {
        didWePassBattle = didWe;
    }

    private static void restartData(User _firstUser, int _round) {
        firstUser = _firstUser;
        secondUser = new User();
        firstPlayer = new Player(firstUser);
        secondPlayer = new Player();
        round = _round;
        firstPlayerWin = 0;
        secondPlayerWin = 0;
        firstPlayerMaxLP = 0;
        secondPlayerMaxLP = 0;
    }

    public static void EndGame(Stage stage) throws Exception {
        soundEffect(2);
        int floorWin = 0;
        if (round == 3)
            floorWin = 1;
        setWinnData();
        if (firstPlayerWin > floorWin || secondPlayerWin > floorWin) {
            firstPlayer.reset();
            if (Game.isAIGame)
                secondPlayer = new Player();
            else secondPlayer.reset();
            setScoreAndMoneyOfPlayers();
            EndGameMenu.setLabelText(" the whole match with score:\n\n\t\t\t" + firstPlayerWin + "-" + secondPlayerWin);
            new EndGameMenu().start(stage);
        } else {
            firstPlayer.reset();
            if (Game.isAIGame)
                secondPlayer = new Player();
            else secondPlayer.reset();
            SetSideAndMain.setUser(firstUser, firstPlayer);
            SetSideAndMain.setScoreAndWinner(winner.getName() + " won the game\n\nand the score is:\n\n" + firstPlayerWin + "-" + secondPlayerWin);
            new SetSideAndMain().start(stage);
        }
        try {
            Thread.currentThread().stop();
        } catch (Exception e) {

        }
    }

    public static String getWinnerName() {
        return winner.getName();
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
        if (!isAIGame)
            SaveFile.saveUser(secondUser);
    }

    private static void restartData(User _firstUser, User _secondUser, int _round) {
        firstUser = _firstUser;
        secondUser = _secondUser;
        firstPlayer = new Player(firstUser);
        secondPlayer = new Player(secondUser);
        round = _round;
        firstPlayerWin = 0;
        secondPlayerWin = 0;
        firstPlayerMaxLP = 0;
        secondPlayerMaxLP = 0;
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

    private static void generateRandomTurn() throws Exception {
        new RockPaperScissors().start(Duel.stage);
    }

    private static void soundEffect(int number) {
        try {
            if (number == 0)
                Sounds.play("src//main//resources//Sound//CARD_MOVE_1.wav", 1).start();
            if (number == 1)
                Sounds.play("src//main//resources//Sound//Cancel.wav", 1).start();
            if (number == 2)
                Sounds.play("src//main//resources//Sound//DUEL_END.wav", 1).start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void playTurn(String phase) throws Exception {
        switch (phase) {
            case "DrawPhase":
                //new NextPhaseController().drawPhase();
                break;
            case "StandByPhase":
                //new NextPhaseController().standByPhase();
                break;
            case "MainPhase1":
                //mainPhase1.run();
                break;
            case "BattlePhase":
                //new NextPhaseController().battlePhase();
                break;
            case "MainPhase2":
                //mainPhase2.run();
                break;
            case "EndPhase":
                new NextPhaseController().endPhase();
                break;
            case "EndGame":
                //EndGame();
                break;
        }
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


    public static void setIsSurrender(boolean isSurrender1) {
        isSurrender = isSurrender1;
    }

    public static void setWinner(Player winner) {
        Game.winner = winner;
    }

    public static boolean isAITurn() {
        return isAIGame && playerTurn.equals(PlayerTurn.SECONDPLAYER);
    }

    public static MainPhase getMainPhase1() {
        if (!didWePassBattle)
            return mainPhase1;
        else
            return mainPhase2;
    }

    public static MainPhase getMainPhase2() {
        return mainPhase2;
    }

    public static void setPhase(String phase) {
        Game.phase = phase;
    }

    public static String getCurrentPhase() {
        return phase;
    }

    public static void setGameView(GameView _gameView) {
        gameView = _gameView;
    }
    
    public static void startGame(String firstOrSecond) throws Exception {
        if(firstOrSecond.equals("First")){
            playerTurn = PlayerTurn.FIRSTPLAYER;
        } else {
            playerTurn = PlayerTurn.SECONDPLAYER;
        }
        gameView.start(MainMenu.stage);
    }
}
