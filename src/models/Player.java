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
        unusedCards = user.getActiveCards();
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

    public HashMap<Integer, Card> getSPellZoneCard() {
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
            return "his hand is empty";
        if (isHandFull())
            return "Hand is full";
        Random random = new Random();   
        int i = random.nextInt(count - 1);  
        int place = getFirstEmptyPlace(handCardNumbers, 5);
        handCardNumbers.put(place, unusedCards.get(i));
        unusedCards.remove(i);
        return "add successfully";
    }

    public void removeCard(Address address) {
        HashMap<Integer, Card> removeCardHashMap = getHashMapByAddress(address);
        removeCardHashMap.remove(address.getNumber());
    }

    public Card getCardByAddress(Address address) {
        HashMap<Integer, Card> getCardHashMap = getHashMapByAddress(address);
        return getCardHashMap.get(address.getNumber());
    }

    public HashMap<Integer, Card> getHashMapByAddress(Address address) {
        if (address.getKind().equals("monster"))
            return monsterZoneCardNumbers;
        if (address.getKind().equals("spell"))
            return spellZoneCardNumbers;
        if (address.getKind().equals("field")) 
            return fieldCardNumbers;
        if (address.getKind().equals("hand"))
            return handCardNumbers;
        return null;
    }

    public int getFirstEmptyPlace(HashMap<Integer, Card> CardNumbers, int max) {
        for(int i = 1; i <= max; i++) 
            if (!CardNumbers.containsKey(i))
                return i;
        return 0;
    }
}
