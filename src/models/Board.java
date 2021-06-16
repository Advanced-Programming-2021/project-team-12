package models;

import models.card.monster.MonsterCard;
import controllers.Game;
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
    private static HashMap<Integer, Boolean> currentPlayerIsMonsterFaceUp = new HashMap<>();
    private static HashMap<Integer, Boolean> currentPlayerIsSPellFaceUp = new HashMap<>();
    private static HashMap<Integer, Card> opponentPlayerGraveyardCard = new HashMap<>();
    private static HashMap<Integer, Card> opponentPlayerHandCard = new HashMap<>();
    private static HashMap<Integer, Card> opponentPlayerFieldCard = new HashMap<>();
    private static HashMap<Integer, Card> opponentPlayerMonsterZoneCard = new HashMap<>();
    private static HashMap<Integer, Card> opponentPlayerSpellZoneCard = new HashMap<>();
    private static HashMap<Integer, Boolean> opponentPlayerIsMonsterFaceUp = new HashMap<>();
    private static HashMap<Integer, Boolean> opponentPlayerIsSPellFaceUp = new HashMap<>();

    public static void run() {

    }

    public static void showBoard() {
        loadData();
    }

    public static void loadData() {
        PlayerTurn playerturn = Game.playerTurn;
        if (playerturn.compareTo(PlayerTurn.FIRSTPLAYER) == 0) {
            currentPlayer = Game.firstPlayer;
            opponentPlayer = Game.secondPlayer;
        } else {
            currentPlayer = Game.secondPlayer;
            opponentPlayer = Game.firstPlayer;
        }
        currentPlayerLP = currentPlayer.getLP();
        opponentPlayerLP = opponentPlayer.getLP();
        currentPlayerGraveyardCard = currentPlayer.getGraveyardCard();
        currentPlayerHandCard = currentPlayer.getHandCard();
        currentPlayerFieldCard = currentPlayer.getFieldCard();
        currentPlayerMonsterZoneCard = currentPlayer.getMonsterZoneCard();
        currentPlayerSpellZoneCard = currentPlayer.getSpellZoneCard();
        currentPlayerIsMonsterFaceUp = currentPlayer.getMonsterFaceCards();
        currentPlayerIsSPellFaceUp = currentPlayer.getSpellFaceCards();
        opponentPlayerGraveyardCard = opponentPlayer.getGraveyardCard();
        opponentPlayerHandCard = opponentPlayer.getHandCard();
        opponentPlayerFieldCard = opponentPlayer.getFieldCard();
        opponentPlayerMonsterZoneCard = opponentPlayer.getMonsterZoneCard();
        opponentPlayerSpellZoneCard = opponentPlayer.getSpellZoneCard();
        opponentPlayerIsMonsterFaceUp = opponentPlayer.getMonsterFaceCards();
        opponentPlayerIsSPellFaceUp = opponentPlayer.getSpellFaceCards();
    }

    public static int howManyMonsterIsOnTheBoard() {
        loadData();
        int count = 0;
        count += currentPlayerMonsterZoneCard.size();
        count += opponentPlayerMonsterZoneCard.size();
        return count;
    }

    public static boolean doThisMonsterExist(String monsterName) {
        Card monsterCard = Card.getCardByName(monsterName);
        if (currentPlayerMonsterZoneCard.containsValue(monsterCard)
                || opponentPlayerMonsterZoneCard.containsValue(monsterCard))
            return true;
        return false;
    }

    public static boolean doThisMonsterExistFacedUp(String monsterName) {
        for (int i = 1; i <= 5; i++) {
            if (currentPlayerMonsterZoneCard.containsKey(i) && currentPlayerIsMonsterFaceUp.get(i)
                    && currentPlayerMonsterZoneCard.get(i).getCardName().equals(monsterName))
                return true;
            if (opponentPlayerMonsterZoneCard.containsKey(i) && opponentPlayerIsMonsterFaceUp.get(i)
                    && opponentPlayerMonsterZoneCard.get(i).getCardName().equals(monsterName))
                return true;
        }
        return false;
    }

    public static boolean isAddressEmpty(Address address) {
        HashMap<Integer, Card> addressHashMap = getHashMapByAddress(address);
        return addressHashMap.containsKey(address.getNumber());
    }

    public static Card getCardByAddress(Address address) {
        HashMap<Integer, Card> addressHashMap = getHashMapByAddress(address);
        return addressHashMap.get(address.getNumber());
    }

    public static HashMap<Integer, Card> getHashMapByAddress(Address address) {
        loadData();
        if (address.getKind().equals("monster")) {
            if (address.checkIsMine())
                return currentPlayerMonsterZoneCard;
            else
                return opponentPlayerMonsterZoneCard;
        }
        if (address.getKind().equals("spell")) {
            if (address.checkIsMine())
                return currentPlayerSpellZoneCard;
            else
                return opponentPlayerSpellZoneCard;
        }
        if (address.getKind().equals("field")) {
            if (address.checkIsMine())
                return currentPlayerFieldCard;
            else
                return opponentPlayerFieldCard;
        }
        if (address.getKind().equals("hand")) {
            if (address.checkIsMine())
                return currentPlayerHandCard;
            else
                return opponentPlayerHandCard;
        }
        if (address.getKind().equals("graveyard")) {
            if (address.checkIsMine())
                return currentPlayerGraveyardCard;
            else
                return opponentPlayerGraveyardCard;
        }
        return null;
    }

    public String getKindByAddress(Address address) {
        HashMap<Integer, Card> addressHashMap = getHashMapByAddress(address);
        return addressHashMap.get(address.getNumber()).getKind();
    }

    public static int sumOfLevelOfFacedUpMonsters(){


    }


    public static MonsterCard whatKindaMonsterIsHere(Address address) {




        return null;
    }

    public static int numberOfAllMonstersInGraveYard() {
        
    }

    public static void showGraveyard() {
    //dear ali please print by yourself
    }
    public static void showFieldZoneCard(boolean isMine) {
        //dear ali please print by yourself
    }

    public static void putThisCardInThisAddress(Address address,String MonsterCard){

    }
}
