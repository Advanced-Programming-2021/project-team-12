package models;

import models.card.monster.MonsterCard;
import models.card.spell.SpellCard;
import models.card.trap.TrapCard;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;

public class Player {
    private boolean[] didWeActivateThisSpell;
    private boolean[] didWeAttackByThisCardInThisCard;
    private boolean isHeSummonedOrSet;
    private boolean[] didWeChangePositionThisCardInThisTurn;
    private String name;
    private String nickName;
    private int LP;
    private ArrayList<Card> unusedCards = new ArrayList<>();//might be wrong
    private HashMap<Integer, Card> handCardNumbers = new HashMap<>();
    private HashMap<Integer, Card> graveyardCardNumbers = new HashMap<>();
    private HashMap<Integer, Card> fieldCardNumbers = new HashMap<>();
    private HashMap<Integer, Card> monsterZoneCardNumbers = new HashMap<>();
    private HashMap<Integer, Card> spellZoneCardNumbers = new HashMap<>();
    private ArrayList<Card> allCardsOfPlayer = new ArrayList<>();//might be wrong
    private HashMap<Integer, Boolean> isMonsterFaceUp = new HashMap<>();
    private HashMap<Integer, Boolean> isSpellFaceUp = new HashMap<>();
    private ArrayList<Integer> indexOfCardUsedSuijin;

    {
        indexOfCardUsedSuijin = new ArrayList<>();
        didWeChangePositionThisCardInThisTurn = new boolean[100];
        didWeAttackByThisCardInThisCard = new boolean[100];
    }

    public Player(User user) {
        this.name = user.getName();
        ;
        LP = 8000;
        this.nickName = user.getNickName();
        unusedCards = (ArrayList<Card>) user.getActiveCards().clone();
        allCardsOfPlayer = (ArrayList<Card>) user.getActiveCards().clone();
        for (int i = 0; i < 6; i++)
            addCardFromUnusedToHand();
    }

    public String getNickName() {
        return nickName;
    }

    public Boolean isFieldEmpty() {
        return !fieldCardNumbers.containsKey(1);
    }

    public Boolean isCardFaceUp(Address address) {
        if (address.getKind().equals("monster")) {
            if (isMonsterFaceUp.containsKey(address.getNumber()))
                return isMonsterFaceUp.get(address.getNumber());
        }
        if (address.getKind().equals("spell")) {
            if (isSpellFaceUp.containsKey(address.getNumber()))
                return isSpellFaceUp.get(address.getNumber());
        }
        return null;
    }

    public Address setCard(Card card, String cardState) {
        HashMap<Integer, Card> stateHashMap = getHashMapByKind(cardState);
        int place = 1;
        if (cardState.equals("field")) {
            graveyardCardNumbers.put(stateHashMap.size() + 1, fieldCardNumbers.get(1));
            fieldCardNumbers.remove(1);
            fieldCardNumbers.put(1, card);
        } else if (cardState.equals("graveyard"))
            graveyardCardNumbers.put(graveyardCardNumbers.size() + 1, card);
        else {
            int max = 5;
            if (cardState.equals("hand"))
                max = 6;
            place = getFirstEmptyPlace(stateHashMap, max);
            if (place == 0)
                return "it is full";
            else
                stateHashMap.put(place, card);
        }
        if (getFaceHashMapByKind(cardState) != null) {
            getFaceHashMapByKind(cardState).put(place, false);
        }
        return "set successfully";
    }

    public void setCardFaceUp(Address address) {
        getFaceHashMapByKind(address.getKind()).put(address.getNumber(), true);
    }

    public HashMap<Integer, Boolean> getMonsterFaceCards() {
        return isMonsterFaceUp;
    }

    public HashMap<Integer, Boolean> getSpellFaceCards() {
        return isSpellFaceUp;
    }

    public void setCardFaceDown(Address address) {
        getFaceHashMapByKind(address.getKind()).put(address.getNumber(), false);
    }

    public HashMap<Integer, Boolean> getFaceHashMapByKind(String kind) {
        if (kind.equals("monster"))
            return isMonsterFaceUp;
        if (kind.equals("spell"))
            return isSpellFaceUp;
        return null;
    }

    public String moveCardWithAddress(Address beginningAddress, Address destinationAddress) {
        HashMap<Integer, Card> beginningHashMap = getHashMapByAddress(beginningAddress);
        if (!beginningHashMap.containsKey(beginningAddress.getNumber()))
            return "there is no card in beginningAddress";
        return setCard(beginningHashMap.get(beginningAddress.getNumber()), destinationAddress.getKind());
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

    public HashMap<Integer, Card> getGraveyardCard() {
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

    public void setCardFromHandToMonsterZone(String address) {

    }

    public void setCardFromHandToSpellZone(String address) {

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
        if (address.getKind().matches("graveyard")) {
            for (int i = place; i < removeCardHashMap.size(); i++) {
                if (removeCardHashMap.containsKey(i + 1)) {
                    removeCardHashMap.put(i, removeCardHashMap.get(i + 1));
                    removeCardHashMap.remove(i + 1);
                }
            }
        } else if (address.getKind().matches("(field)"))
            fieldCardNumbers.remove(1);
        if (getFaceHashMapByKind(address.getKind()) != null)
            getFaceHashMapByKind(address.getKind()).remove(address.getNumber());
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
        for (int i = 1; i <= max; i++)
            if (!cardNumbers.containsKey(i))
                return i;
        return 0;
    }

    public void setHeSummonedOrSet(boolean heSummonedOrSet) {
        isHeSummonedOrSet = heSummonedOrSet;
    }

    public boolean isHeSummonedOrSet() {
        return isHeSummonedOrSet;
    }

    public void summonCardFromHandToMonsterZone(String address) {
    }

    public boolean isMonsterInThisMonsterZoneTypeAddress(int monsterZoneTypeAddress) {
    }

    public void removeThisMonsterZoneTypeAddressForTribute(int monsterZoneTypeAddress) {
    }

    public MonsterCard getMonsterCardByStringAddress(String address) {
    }

    public String whatKindaCardIsInThisAddress(String address) {

    }

    public boolean isThereAnyCardInMonsterZone() {
    }

    public boolean isThereTwoCardInMonsterZone() {
    }

    public static void destroyAllRivalTrapAndSpells() {
    }

    public SpellCard getSpellCardByStringAddress(String address) {
    }

    public TrapCard getTrapCardByStringAddress(String address) {
    }

    public boolean isThisMonsterOnDHPosition(String address) {

    }

    public void convertThisMonsterFromDHToOO(String address) {

    }

    public void convertThisMonsterFromAttackToDefence(String address) {

    }

    public void convertThisMonsterFromDefenceToAttack(String address) {

    }

    public boolean isThisMonsterOnAttackPosition(String address) {

    }

    public void setDidWeChangePositionThisCardInThisTurn(int index) {
        didWeChangePositionThisCardInThisTurn[index] = true;
    }

    public boolean didWeChangePositionThisCardInThisTurn(int index) {
        return didWeChangePositionThisCardInThisTurn[index];
    }

    public void setDidWeChangePositionThisCardInThisTurnCompletelyFalse() {
        Arrays.fill(didWeChangePositionThisCardInThisTurn, false);
    }

    public void setDidWeAttackByThisCardInThisCardInThisTurn(int index) {
        didWeAttackByThisCardInThisCard[index] = true;
    }

    public boolean didWeAttackByThisCardInThisCardInThisTurn(int index) {
        return didWeAttackByThisCardInThisCard[index];
    }

    public void setDidWeAttackByThisCardInThisCardInThisTurnCompletelyFalse() {
        Arrays.fill(didWeAttackByThisCardInThisCard, false);
    }

    public void setDidWeActivateThisSpell(int index) {
        didWeActivateThisSpell[index]=true;
    }
    public boolean didWeActivateThisSpell(int index){
        return didWeActivateThisSpell[index];
    }
    public void setDidWeActivateThisSpellCompletelyFalse() {
        Arrays.fill(didWeAttackByThisCardInThisCard, false);
    }

    public int getIndexOfThisCardByAddress(String address) {

    }

}
