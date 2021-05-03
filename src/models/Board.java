package models;
import view.Game;
import java.util.HashMap;

public class Board {
    private static Player currentPlayer;
    private static Player opponentPlayer;
    private static int currentPlayerLP;
    private static int opponentPlayerLP;
    private static HashMap<Integer, Card> currentPlayerGraveyardCard = new HashMap<>();
    private static HashMap<Integer, Card> currentPlayerHandCard = new HashMap<>();
    private static HashMap<Integer, Card> currentPlayerFieldCard = new HashMap<>();
    private static HashMap<Integer, Card> currentPlayerMonsterZoneCard = new HashMap<>();
    private static HashMap<Integer, Card> currentPlayerSpellZoneCard = new HashMap<>();
    private static HashMap<Integer, Card> opponentPlayerGraveyardCard = new HashMap<>();
    private static HashMap<Integer, Card> opponentPlayerHandCard = new HashMap<>();
    private static HashMap<Integer, Card> opponentPlayerFieldCard = new HashMap<>();
    private static HashMap<Integer, Card> opponentPlayerMonsterZoneCard = new HashMap<>();
    private static HashMap<Integer, Card> opponentPlayerSpellZoneCard = new HashMap<>();

    public static void run() {

    }

    public static void showBoeard() {

    }

    public static void loadData() {
        PlayerTurn playerturn = Game.playerTurn;
        if (playerturn.compareTo(PlayerTurn.FIRSTPLAYER) == 0) {
            currentPlayer = Game.firstPlayer;
            opponentPlayer = Game.secondPlayer;
        }
        currentPlayerLP = currentPlayer.getLP();
        opponentPlayerLP = opponentPlayer.getLP();
        currentPlayerGraveyardCard = currentPlayer.getGraveyadCard();
        currentPlayerHandCard = currentPlayer.getHandCard();
        currentPlayerFieldCard = currentPlayer.getFieldCard();
        currentPlayerMonsterZoneCard = currentPlayer.getMonsterZoneCard();
        currentPlayerSpellZoneCard = currentPlayer.getSPellZoneCard();
        opponentPlayerGraveyardCard = opponentPlayer.getGraveyadCard();
        opponentPlayerHandCard = opponentPlayer.getHandCard();
        opponentPlayerFieldCard = opponentPlayer.getFieldCard();
        opponentPlayerMonsterZoneCard = opponentPlayer.getMonsterZoneCard();
        opponentPlayerSpellZoneCard = opponentPlayer.getSPellZoneCard();
    }

    public static int howManyMonsterIsOnTheBoard(){

    }

    public static boolean doThisMonsterExist(String monsterName){

    }

    public static boolean doThisMonsterExistFacedUp(String monsterName){

    }

}
