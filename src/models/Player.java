package models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class Player {
    private String name;
    private int LP;
    private ArrayList<Card> unusedCards = new ArrayList<>();
    private HashMap<Integer, Card> handCardNumbers = new HashMap<>();
    private HashMap<Integer, Card> graveyardCardNumbers = new HashMap<>();
    private HashMap<Integer, Card> fieldCardNumbers = new HashMap<>();
    private HashMap<Integer, Card> monsterZoneCardNumbers = new HashMap<>();
    private HashMap<Integer, Card> spellZoneCardNumbers = new HashMap<>();
    private ArrayList<Card> allCardsOfPlayer = new ArrayList<>();
    private ArrayList<Integer> indexOfCardUsedSuijin;
    {
        indexOfCardUsedSuijin = new ArrayList<>();
    }

    public Player(String name) {
        this.name = name;
        LP = 1000;
        User user = User.getUserByName(name);
        unusedCards = (ArrayList<Card>) user.getActiveCards().clone();
        allCardsOfPlayer = (ArrayList<Card>) user.getActiveCards().clone();
        for (int i = 0; i < 6; i++) 
            addCardFromUnusedToHand();
    }

    public String setCard(Card card, String cardState) {
        HashMap<Integer, Card> stateHashMap = getHashMapByKind(cardState);
        if (cardState.equals("field")
            || cardState.equals("graveyard"))
            stateHashMap.put(stateHashMap.size() + 1, card);
        else {
            int max = 5;
            if (cardState.equals("hand")) 
                max = 6;
            int place = getFirstEmptyPlace(stateHashMap, max);
            if (place == 0)
                return "it is full";
            else     
                stateHashMap.put(place, card);
        }
        return "set successfully";
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getLP() {
        return LP;
    }

    public void increaseLP(int LP) {
        this.LP += LP;
    }

    public void decreaseLP(int LP) {
        this.LP -= LP;
    }

    public HashMap<Integer, Card> getHandCard() {
        return handCardNumbers;
    }

    public HashMap<Integer, Card> getGraveyadCard() {
        return graveyardCardNumbers;
    }

    public HashMap<Integer, Card> getFieldCard() {
        return fieldCardNumbers;
    }

    public HashMap<Integer, Card> getMonsterZoneCard() {
        return monsterZoneCardNumbers;
    }

    public HashMap<Integer, Card> getSpellZoneCard() {
        return spellZoneCardNumbers;
    }

    public void addIndexToSuijin(int index) {
        indexOfCardUsedSuijin.add(index);
    }

    public boolean doIndexExistInSuijin(int index) {
        return indexOfCardUsedSuijin.contains(index);
    }

    public Boolean isHandFull() {
        for (int i = 1; i <= 6; i++) 
            if (!handCardNumbers.containsKey(i))
                return false;
        return true;
    }

    public Boolean isMonsterZoneFull() {
        for (int i = 1; i <= 5; i++) 
            if (!monsterZoneCardNumbers.containsKey(i))
                return false;
        return true;
    }

    public Boolean isSpellZoneFull() {
        for (int i = 1; i <= 5; i++) 
            if (!spellZoneCardNumbers.containsKey(i))
                return false;
        return true;
    }

    public String addCardFromUnusedToHand() {
        int count = unusedCards.size();
        if (count == 0)
            return "unused is empty";
        if (isHandFull())
            return "Hand is full";
        Random random = new Random();   
        int i = random.nextInt(count - 1);  
        int place = getFirstEmptyPlace(handCardNumbers, 5);
        handCardNumbers.put(place, unusedCards.get(i));
        unusedCards.remove(i);
        return handCardNumbers.get(place).getCardName();
    }

    public void removeCard(Address address) {
        if (Board.isAddressEmpty(address))
            return;
        HashMap<Integer, Card> removeCardHashMap = getHashMapByAddress(address);
        int place = address.getNumber();
        removeCardHashMap.remove(address.getNumber());
        if (address.getKind().matches("(field|graveyard)")) {
            for (int i = place; i < removeCardHashMap.size(); i++) {
                if (removeCardHashMap.containsKey(i + 1)) {
                    removeCardHashMap.put(i, removeCardHashMap.get(i + 1));
                    removeCardHashMap.remove(i + 1);
                }
            }
        }
    }

    public Card getCardByAddress(Address address) {
        HashMap<Integer, Card> getCardHashMap = getHashMapByAddress(address);
        return getCardHashMap.get(address.getNumber());
    }

    public HashMap<Integer, Card> getHashMapByAddress(Address address) {
        return getHashMapByKind(address.getKind());
    }

    public HashMap<Integer, Card> getHashMapByKind(String kind) {
        if (kind.equals("monster"))
            return monsterZoneCardNumbers;
        if (kind.equals("spell"))
            return spellZoneCardNumbers;
        if (kind.equals("field")) 
            return fieldCardNumbers;
        if (kind.equals("hand"))
            return handCardNumbers;
        if (kind.equals("graveyard"))
            return graveyardCardNumbers;
        return null;
    }

    public int getFirstEmptyPlace(HashMap<Integer, Card> cardNumbers, int max) {
        for(int i = 1; i <= max; i++) 
            if (!cardNumbers.containsKey(i))
                return i;
        return 0;
    }
}
