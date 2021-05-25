package models;

import models.card.monster.MonsterCard;
import models.card.spell.SpellCard;
import models.card.trap.TrapCard;
import controllers.Game;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;

public class Player {
    private boolean[] isThisSpellActivated;
    private boolean isOneHisMonstersDestroyedInThisRound;
    private HashMap<Address, PositionOfCardInBoard> positionOfCardInBoardByAddress;// Dear Ali you should make board based on this HashMap
    private boolean[] didWeActivateThisSpell;
    private boolean[] didWeAttackByThisCardInThisCard;
    private boolean isHeSummonedOrSet;
    private boolean[] didWeChangePositionThisCardInThisTurn;
    private String name;
    private String nickName;
    private int LP;
    private ArrayList<Card> unusedCards = new ArrayList<>();
    private ArrayList<Card> secondaryCard = new ArrayList<>();
    private HashMap<Integer, Card> handCardNumbers = new HashMap<>();
    private HashMap<Integer, Card> graveyardCardNumbers = new HashMap<>();
    private HashMap<Integer, Card> fieldCardNumbers = new HashMap<>();
    private HashMap<Integer, Card> monsterZoneCardNumbers = new HashMap<>();
    private HashMap<Integer, Card> spellZoneCardNumbers = new HashMap<>();
    private HashMap<Integer, Boolean> isMonsterFaceUp = new HashMap<>();
    private HashMap<Address, Integer> indexOfCard = new HashMap<>();
    private HashMap<Integer, Boolean> isSpellFaceUp = new HashMap<>();
    private boolean[] didBeastKingBarbarosSummonedSuperHighLevel;
    private Card[] cardByIndex;//dear Ali do the thing
    private ArrayList<Integer> indexOfCardUsedSuijin;

    {
        cardByIndex = new Card[100];
        isThisSpellActivated = new boolean[100];
        didBeastKingBarbarosSummonedSuperHighLevel = new boolean[100];
        positionOfCardInBoardByAddress = new HashMap<>();
        indexOfCardUsedSuijin = new ArrayList<>();
        didWeChangePositionThisCardInThisTurn = new boolean[100];
        didWeAttackByThisCardInThisCard = new boolean[100];
    }

    public Player(User user) {
        this.nickName = user.getNickName();
        this.name = user.getName();
        LP = 8000;
        this.nickName = user.getNickName();
        unusedCards = (ArrayList<Card>) Deck.getActiveDeckOfUser(user.getName()).getMainCards().clone();
        secondaryCard = (ArrayList<Card>) Deck.getActiveDeckOfUser(user.getName()).getSideCards().clone();
        for (int i = 0; i < 5; i++)
            addCardFromUnusedToHand();
    }

    public String getName() {
        return name;
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

    public Address addCardToAddress(Card card, String cardState, int index) {
        HashMap<Integer, Card> stateHashMap = getHashMapByKind(cardState);
        int place = 1;
        if (cardState.equals("field")) {
            if (fieldCardNumbers.containsKey(1))
                moveCardWithKind(new Address(1, "field", true), "graveyard");
            fieldCardNumbers.put(1, card);
        } else if (cardState.equals("graveyard")) {
            graveyardCardNumbers.put(graveyardCardNumbers.size() + 1, card);
            place = graveyardCardNumbers.size();
        } else {
            int max = 5;
            if (cardState.equals("hand"))
                max = 6;
            place = getFirstEmptyPlace(stateHashMap, max);
            stateHashMap.put(place, card);
        }
        indexOfCard.put(new Address(place, cardState, true), index);
        if (getFaceHashMapByKind(cardState) != null)
            getFaceHashMapByKind(cardState).put(place, false);
        return new Address(place, cardState, true);
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

    public Address moveCardWithKind(Address beginningAddress, String kind) {
        HashMap<Integer, Card> beginningHashMap = getHashMapByAddress(beginningAddress);
        int index = indexOfCard.get(beginningAddress);
        return addCardToAddress(beginningHashMap.get(beginningAddress.getNumber()), kind, index);
    }

    public void moveCardWithAddress(Address beginningAddress, Address destinationAddress) {
        HashMap<Integer, Card> beginningHashMap = getHashMapByAddress(beginningAddress);
        HashMap<Integer, Card> destinationHashMap = getHashMapByAddress(destinationAddress);
        int index = indexOfCard.get(beginningAddress);
        destinationHashMap.put(destinationAddress.getNumber(), beginningHashMap.get(beginningAddress.getNumber()));
        beginningHashMap.remove(beginningAddress.getNumber());
        indexOfCard.remove(beginningAddress);
        indexOfCard.put(destinationAddress, index);
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
        if (this.LP < LP)
            this.LP = 0;
        else
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

    public Address setCardFromHandToMonsterZone(Address address) {
        int place = getFirstEmptyPlace(monsterZoneCardNumbers, 5);
        return moveCardWithKind(address, "monster");
    }

    public void setCardFromHandToSpellZone(String address) {
        Address beginningAddress = new Address(address);
        int place = getFirstEmptyPlace(spellZoneCardNumbers, 5);
        moveCardWithKind(beginningAddress, "spell");
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
        Address address = new Address(place, "hand", true);
        indexOfCard.put(address, indexOfCard.size() + 1);
        return handCardNumbers.get(place).getCardName();
    }

    public void removeCard(Address address) {
        if (getMonsterCardByAddress(address) != null) {
            if (address.checkIsMine()) Game.whoseTurnPlayer().setOneHisMonstersDestroyedInThisRound(true);
            else Game.whoseRivalPlayer().setOneHisMonstersDestroyedInThisRound(true);
        }
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
        indexOfCard.remove(address);
        address.setIsScanner(false);
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

    public Address summonCardFromHandToMonsterZone(String address) {
        //face up
        //attack
    }

    public boolean isMonsterInThisMonsterZoneTypeAddress(int monsterZoneTypeAddress) {
        return monsterZoneCardNumbers.containsKey(monsterZoneTypeAddress);
    }

    public void removeThisMonsterZoneTypeAddressForTribute(int monsterZoneTypeAddress) {
        monsterZoneCardNumbers.remove(monsterZoneTypeAddress);
    }

    public MonsterCard getMonsterCardByStringAddress(String address) {
        Address cardAddress = new Address(address);
        return MonsterCard.getMonsterCardByName(getCardByAddress(cardAddress).getCardName());
    }

    public String whatKindaCardIsInThisAddress(String address) {
        Address cardAddress = new Address(address);
        return getCardByAddress(cardAddress).getKind();
    }

    public boolean isThereAnyCardInMonsterZone() {
        return !monsterZoneCardNumbers.isEmpty();
    }

    public boolean isThereTwoCardInMonsterZone() {
        int count = monsterZoneCardNumbers.size();
        if (count >= 2)
            return true;
        return false;
    }

    public static void destroyAllRivalTrapAndSpells() {

    }

    public SpellCard getSpellCardByStringAddress(String address) {
        Address cardAddress = new Address(address);
        return SpellCard.getSpellCardByName(getCardByAddress(cardAddress).getCardName());
    }

    public TrapCard getTrapCardByStringAddress(String address) {
        Address cardAddress = new Address(address);
        return TrapCard.getTrapCardByName(getCardByAddress(cardAddress).getCardName());
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

    public boolean didWeAttackByThisCardInThisCardInThisTurn(int index) {
        return didWeAttackByThisCardInThisCard[index];
    }

    public void setDidWeAttackByThisCardInThisCardInThisTurn(int index) {
        didWeAttackByThisCardInThisCard[index] = true;
    }

    public void setDidWeAttackByThisCardInThisCardInThisTurnCompletelyFalse() {
        Arrays.fill(didWeAttackByThisCardInThisCard, false);
    }

    public void setDidWeActivateThisSpell(int index) {
        didWeActivateThisSpell[index] = true;
    }

    public boolean didWeActivateThisSpell(int index) {
        return didWeActivateThisSpell[index];
    }

    public void setDidWeActivateThisSpellCompletelyFalse() {
        Arrays.fill(didWeAttackByThisCardInThisCard, false);
    }

    public int getIndexOfThisCardByAddress(Address address) {
        return indexOfCard.get(address);
    }

    public void setPositionOfCardInBoardByAddress(Address address, PositionOfCardInBoard positionOfCardInBoard) {
        positionOfCardInBoardByAddress.put(address, positionOfCardInBoard);
    }

    public PositionOfCardInBoard positionOfCardInBoardByAddress(Address address) {
        positionOfCardInBoardByAddress.get(address);
    }

    public MonsterCard getMonsterCardByAddress(Address address) {

    }

    public boolean isThereAnyRitualModeSpellInOurHand() {

    }

    public boolean isThereAnyRitualTypeMonsterInOurHand() {

    }

    public ArrayList<Integer> sumOfLevelOfAllSubsetsOfMonsterZone() {

    }

    public ArrayList<Integer> levelOfRitualMonstersOnOurHand() {

    }

    public boolean isOneOfLevelOfRitualMonstersInTheHandIsEqualToSumOfLevelOfSubsetOfMonsterZone() {
        for (int i = 0; i < sumOfLevelOfAllSubsetsOfMonsterZone().size(); i++)
            for (int j = 0; j < levelOfRitualMonstersOnOurHand().size(); j++)
                if (sumOfLevelOfAllSubsetsOfMonsterZone().get(i).equals(levelOfRitualMonstersOnOurHand().get(j)))
                    return true;
        return false; //don't bother I've already written it.
    }

    public int howManyCardIsInTheHandCard() {

    }

    public Address addressOfAttackerCard() {

    }

    public boolean doWeHaveThisCardInBoard(String card) {
    }

    public boolean didBeastKingBarbarosSummonedSuperHighLevel(int index) {
        return didBeastKingBarbarosSummonedSuperHighLevel[index];
    }

    public void setDidBeastKingBarbarosSummonedSuperHighLevel(boolean didBeastKingBarbarosSummonedSuperHighLevel, int index) {
        this.didBeastKingBarbarosSummonedSuperHighLevel[index] = didBeastKingBarbarosSummonedSuperHighLevel;
    }

    public boolean isThereThreeCardInMonsterZone() {
    }

    public int howManyHeraldOfCreationDoWeHave(String cardName) {
    }

    public Address setCardFromGraveyardToMonsterZone(Address comeBackFromGraveyard) {
    }

    public boolean isThereAnyFieldSpellInDeck() {
    }

    public boolean isThisCardInDeck(String cardName) {
    }

    public void bringCardFromDeckToHand(String cardName) {
        //dear Ali you should bring this card from deck to hand thanks!
    }

    public boolean isOneHisMonstersDestroyedInThisRound() {
        return isOneHisMonstersDestroyedInThisRound;
    }

    public void setOneHisMonstersDestroyedInThisRound(boolean oneHisMonstersDestroyedInThisRound) {
        isOneHisMonstersDestroyedInThisRound = oneHisMonstersDestroyedInThisRound;
    }

    public boolean isThisSpellActivated(int index) {
        return isThisSpellActivated[index];
    }

    public void setIsThisSpellActivated(boolean isSpellActivated, int index) {
        isThisSpellActivated[index] = isSpellActivated;
    }

    public boolean isOneHisSupplySquadActivated() {
        for (int i = 0; i < isThisSpellActivated.length; i++)
            if ((isThisSpellActivated[i]) && (cardByIndex[i].getCardName().equals("SupplySquad"))) return true;
        return false;
    }
    public boolean isOneHisSpellAbsorptionActivated() {
        for (int i = 0; i < isThisSpellActivated.length; i++)
            if ((isThisSpellActivated[i]) && (cardByIndex[i].getCardName().equals("SpellAbsorption"))) return true;
        return false;
    }

    public boolean doIHaveActivatedTrapNamedMagicCylinder() {
    }

    public boolean doIHaveActivatedTrapNamedMirrorForce() {

    }

    public void destroyAllRivalMonstersWhichInAttackMode() {

    }

    public void removeOneOfMyTrapHoleTrapOnTheBoard() {
    }

    public boolean doIHaveTrapHoleTrapOnTheBoard() {
    }

    public boolean doIHaveTorrentialTributeTrapOnTheBoard() {
    }

    public void removeOneOfMyTorrentialTributeTrapOnTheBoard() {
    }

    public boolean doIHaveActivatedTrapNamedNegateAttack() {
    }
}
