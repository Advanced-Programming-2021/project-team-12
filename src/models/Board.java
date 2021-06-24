package models;

import models.card.monster.MonsterCard;
import controllers.Game;
import view.ShowBoard;

import java.util.HashMap;

public class Board {
    private static Player currentPlayer;
    private static Player opponentPlayer;

    public static void showBoard() {
        loadData();
        new ShowBoard(currentPlayer, opponentPlayer);
    }

    public static void loadData() {
        PlayerTurn playerturn = Game.playerTurn;
        if (playerturn.equals(PlayerTurn.FIRSTPLAYER)) {
            currentPlayer = Game.firstPlayer;
            opponentPlayer = Game.secondPlayer;
        } else {
            currentPlayer = Game.secondPlayer;
            opponentPlayer = Game.firstPlayer;
        }
    }

    public static int howManyMonsterIsOnTheBoard() {
        loadData();
        int count = 0;
        count += currentPlayer.getMonsterZoneCard().size();
        count += opponentPlayer.getMonsterZoneCard().size();
        return count;
    }

    public static boolean doThisMonsterExist(String monsterName) {
        Card monsterCard = Card.getCardByName(monsterName);
        if (currentPlayer.getMonsterZoneCard().containsValue(monsterCard)
                || opponentPlayer.getMonsterZoneCard().containsValue(monsterCard))
            return true;
        return false;
    }

    public static boolean doThisMonsterExistFacedUp(String monsterName) {
        for (int i = 1; i <= 5; i++) {
            if (currentPlayer.getMonsterZoneCard().containsKey(i) && currentPlayer.getMonsterZoneCard().get(i).getCardName().equals(monsterName)
                    && !currentPlayer.getMonsterPosition(i).equals(PositionOfCardInBoard.DH))
                return true;
            if (opponentPlayer.getMonsterZoneCard().containsKey(i) && opponentPlayer.getMonsterZoneCard().get(i).getCardName().equals(monsterName)
                    && !opponentPlayer.getMonsterPosition(i).equals(PositionOfCardInBoard.DH))
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
                return currentPlayer.getMonsterZoneCard();
            else
                return opponentPlayer.getMonsterZoneCard();
        }
        if (address.getKind().equals("spell")) {
            if (address.checkIsMine())
                return currentPlayer.getSpellZoneCard();
            else
                return opponentPlayer.getSpellZoneCard();
        }
        if (address.getKind().equals("field")) {
            if (address.checkIsMine())
                return currentPlayer.getFieldCard();
            else
                return opponentPlayer.getFieldCard();
        }
        if (address.getKind().equals("hand")) {
            if (address.checkIsMine())
                return currentPlayer.getHandCard();
            else
                return opponentPlayer.getHandCard();
        }
        if (address.getKind().equals("graveyard")) {
            if (address.checkIsMine())
                return currentPlayer.getGraveyardCard();
            else
                return opponentPlayer.getGraveyardCard();
        }
        return null;
    }

    public static void destroyAllRivalAttackerMonster(Player player) {
        Player opp;
        if (player.getName().equals(currentPlayer.getName()))
            opp = opponentPlayer;
        else
            opp = currentPlayer;
        for (int i = 0; i < 5; i++)
            if (opp.getMonsterZoneCard().containsKey(i) && !opp.getMonsterPosition(i).equals(PositionOfCardInBoard.DH))
                opp.getMonsterZoneCard().remove(i);
    }

    public static void destroyRivalTrapAndSpells(Player player) {
        Player opp;
        if (player.getName().equals(currentPlayer.getName()))
            opp = opponentPlayer;
        else
            opp = currentPlayer;
        for (int i = 0; i < 5; i++)
            if (opp.getSpellZoneCard().containsKey(i))
                opp.getSpellZoneCard().remove(i);
    }

    public String getKindByAddress(Address address) {
        HashMap<Integer, Card> addressHashMap = getHashMapByAddress(address);
        return addressHashMap.get(address.getNumber()).getKind();
    }

    public static int sumOfLevelOfFacedUpMonsters(){
        loadData();
        int number = 0;
        for (int i = 1; i <= opponentPlayer.getGraveyardCard().size(); i++)
            if (opponentPlayer.getCardGraveyard(i).getKind().equals("Monster")
                    && !opponentPlayer.getMonsterPosition(i).equals(PositionOfCardInBoard.DH))
                number += opponentPlayer.getCardGraveyard(i).getLevel();
        for (int i = 1; i <= currentPlayer.getGraveyardCard().size(); i++)
            if (currentPlayer.getCardGraveyard(i).getKind().equals("Monster")
                    && !currentPlayer.getMonsterPosition(i).equals(PositionOfCardInBoard.DH))
                number += currentPlayer.getCardGraveyard(i).getLevel();
        return number;
    }


    public static MonsterCard whatKindaMonsterIsHere(Address address) {
        loadData();
        Card card = getCardByAddress(address);
        return MonsterCard.getMonsterCardByName(card.getCardName());
    }

    public static int numberOfAllMonstersInGraveYard() {
        loadData();
        int number = 0;
        for (int i = 1; i <= opponentPlayer.getGraveyardCard().size(); i++)
            if (opponentPlayer.getCardGraveyard(i).getKind().equals("Monster"))
                number++;
        for (int i = 1; i <= currentPlayer.getGraveyardCard().size(); i++)
            if (currentPlayer.getCardGraveyard(i).getKind().equals("Monster"))
                number++;
        return number;
    }

    public static void showGraveyard() {
        ShowBoard.showGraveyard(currentPlayer, opponentPlayer);
    }

    public static void showFieldZoneCard(boolean isMine) {
        if (isMine)
            ShowBoard.showFieldZone(currentPlayer);
        else
            ShowBoard.showFieldZone(opponentPlayer);
    }
}
