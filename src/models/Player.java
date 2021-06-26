package models;

import models.card.monster.MonsterCard;
import models.card.spell.SpellCard;
import models.card.spell.SpellMode;
import models.card.trap.TrapCard;
import controllers.Game;

import java.util.*;

public class Player {
    private User user;
    private boolean[] isThisSpellActivated;
    private boolean isOneHisMonstersDestroyedInThisRound;
    private HashMap<Address, PositionOfCardInBoard> positionOfCardInBoardByAddress;
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
    private HashMap<Address, Integer> indexOfCard = new HashMap<>();
    private HashMap<Integer, Boolean> isSpellFaceUp = new HashMap<>();
    private boolean[] didBeastKingBarbarosSummonedSuperHighLevel;
    private Card[] cardByIndex;
    private ArrayList<Integer> indexOfCardUsedSuijin;
    private int[] fromMonsterToSpellEquip;

    {
        fromMonsterToSpellEquip = new int[10];
        cardByIndex = new Card[100];
        isThisSpellActivated = new boolean[100];
        didBeastKingBarbarosSummonedSuperHighLevel = new boolean[100];
        positionOfCardInBoardByAddress = new HashMap<>();
        indexOfCardUsedSuijin = new ArrayList<>();
        didWeChangePositionThisCardInThisTurn = new boolean[100];
        didWeAttackByThisCardInThisCard = new boolean[100];
    }

    public Player(User user) {
        this.user = user;
        this.nickName = user.getNickName();
        this.name = user.getName();
        LP = 8000;
        unusedCards = (ArrayList<Card>) Deck.getActiveDeckOfUser(user.getName()).getMainCards().clone();
        secondaryCard = (ArrayList<Card>) Deck.getActiveDeckOfUser(user.getName()).getSideCards().clone();
        for (int i = 0; i < 5; i++)
            addCardFromUnusedToHand();
    }

    public Player() {
        this.user = new User();
        this.nickName = user.getNickName();
        this.name = user.getName();
        LP = 8000;
        for (int i = 0; i < 75; i++)
            if (i % 2 == 0 || i % 3 == 0)
                unusedCards.add(Card.getAllCards().get(i + 1));
        for (int i = 0; i < 75; i++)
            if (i % 7 == 0)
                secondaryCard.add(Card.getAllCards().get(i + 1));
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

    public Address addCardToAddress(Card card, String cardState, int index) {
        HashMap<Integer, Card> stateHashMap = getHashMapByKind(cardState);
        int place = 1;
        if (cardState.equals("field")) {
            moveToField(card);
        } else if (cardState.equals("graveyard")) {
            place = moveToGraveyard(card);
        } else {
            int max = 5;
            if (cardState.equals("hand"))
                max = 6;
            place = getFirstEmptyPlace(stateHashMap, max);
            stateHashMap.put(place, card);
        }
        indexOfCard.put(new Address(place, cardState, true), index);
        if (cardState.equals("spell"))
            isSpellFaceUp.put(place, false);
        if (cardState.equals("monster"))
            positionOfCardInBoardByAddress.put(new Address(place, cardState, true), PositionOfCardInBoard.DH);
        return new Address(place, cardState, true);
    }

    public HashMap<Integer, Boolean> getFaceHashMapByKind(String kind) {
        if (kind.equals("monster"))
//            return isMonsterFaceUp;
            if (kind.equals("spell"))
                return isSpellFaceUp;
        return null;
    }

    private int moveToGraveyard(Card card) {
        int place;
        graveyardCardNumbers.put(graveyardCardNumbers.size() + 1, card);
        place = graveyardCardNumbers.size();
        return place;
    }

    private void moveToField(Card card) {
        if (fieldCardNumbers.containsKey(1))
            moveCardWithKind(new Address(1, "field", true), "graveyard");
        fieldCardNumbers.put(1, card);
    }

    public Address moveCardWithKind(Address beginningAddress, String kind) {
        HashMap<Integer, Card> beginningHashMap = getHashMapByAddress(beginningAddress);
        int index = indexOfCard.get(beginningAddress);
        return addCardToAddress(beginningHashMap.get(beginningAddress.getNumber()), kind, index);
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

    public Card getCardHand(int i) {
        return handCardNumbers.get(i);
    }

    public Card getCardGraveyard(int i) {
        return graveyardCardNumbers.get(i);
    }

    public Card getCardMonster(int i) {
        return monsterZoneCardNumbers.get(i);
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
        if (!handCardNumbers.containsKey(address.getNumber()) || isMonsterZoneFull())
            return null;
        int place = getFirstEmptyPlace(monsterZoneCardNumbers, 5);
        monsterZoneCardNumbers.put(place, handCardNumbers.get(address.getNumber()));
        Address add = new Address(place, "monster", true);
        indexOfCard.put(add, indexOfCard.get(address));
        handCardNumbers.remove(address.getNumber());
        positionOfCardInBoardByAddress.put(add, PositionOfCardInBoard.DH);
        return add;
    }

    public void setCardFromHandToSpellZone(String address) {
        Address add = new Address(address);
        if (!handCardNumbers.containsKey(add.getNumber()) || isSpellZoneFull())
            return;
        int place = getFirstEmptyPlace(spellZoneCardNumbers, 5);
        spellZoneCardNumbers.put(place, handCardNumbers.get(add.getNumber()));
        Address adres = new Address(place, "spell", true);
        indexOfCard.put(adres, indexOfCard.get(add));
        handCardNumbers.remove(add.getNumber());
        isSpellFaceUp.put(place, false);
    }

    public void setIsSpellFaceUp(int place, boolean isFacedUp) {
        isSpellFaceUp.put(place, isFacedUp);
    }

    public String addCardFromUnusedToHand() {
        int count = unusedCards.size();
        if (count == 0)
            return "unused is empty";
        if (isHandFull())
            return "Hand is full";
        Random random = new Random();
        int i = random.nextInt(count - 1);
        int place = getFirstEmptyPlace(handCardNumbers, 6);
        handCardNumbers.put(place, unusedCards.get(i));
        cardByIndex[indexOfCard.size() + 1] = unusedCards.get(i);
        unusedCards.remove(i);
        Address address = new Address(place, "hand", true);
        indexOfCard.put(address, indexOfCard.size() + 1);
        return handCardNumbers.get(place).getCardName();
    }

    public void removeCard(Address address) {
        if (Board.isAddressEmpty(address))
            return;
        if (getMonsterCardByAddress(address) != null) {
            unSetFromMonsterToSpellEquip(address.getNumber());
            if (address.checkIsMine()) Game.whoseTurnPlayer().setOneHisMonstersDestroyedInThisRound(true);
            else Game.whoseRivalPlayer().setOneHisMonstersDestroyedInThisRound(true);
        }
        if (address.getKind().equals("graveyard")) {
            graveyardCardNumbers.remove(address.getNumber());
            for (int i = address.getNumber(); i < graveyardCardNumbers.size(); i++) {
                graveyardCardNumbers.put(i, graveyardCardNumbers.get(i + 1));
                indexOfCard.put(new Address(i, "graveyard", true), indexOfCard.get(new Address(i + 1, "graveyard", true)));
            }
        } else if (address.getKind().matches("field")) {
            graveyardCardNumbers.put(graveyardCardNumbers.size() + 1, fieldCardNumbers.get(1));
            indexOfCard.put(new Address(graveyardCardNumbers.size(), "graveyard", true), indexOfCard.get(new Address(1, "field", true)));
            fieldCardNumbers.remove(1);
        } else {
            int place = address.getNumber();
            HashMap<Integer, Card> removeCardHashMap = getHashMapByAddress(address);
            graveyardCardNumbers.put(graveyardCardNumbers.size() + 1, removeCardHashMap.get(place));
            indexOfCard.put(new Address(graveyardCardNumbers.size(), "graveyard", true), indexOfCard.get(address));
            removeCardHashMap.remove(place);
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

    public Address summonCardToMonsterZone(String address) {
        Address add = new Address(address);
        Address adres = setCardFromHandToMonsterZone(add);
        positionOfCardInBoardByAddress.put(adres, PositionOfCardInBoard.OO);
        return adres;
    }

    public boolean isMonsterInThisMonsterZoneTypeAddress(int monsterZoneTypeAddress) {
        return monsterZoneCardNumbers.containsKey(monsterZoneTypeAddress);
    }

    public void removeMonsterByInt(int monsterNumber) {
        Address address = new Address(monsterNumber, "monster", true);
        removeCard(address);
    }

    public MonsterCard getMonsterCardByStringAddress(String address) {
        Address cardAddress = new Address(address);
        if (!getCardByAddress(cardAddress).getKind().equals("Monster"))
            return null;
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

    public SpellCard getSpellCardByStringAddress(String address) {
        Address cardAddress = new Address(address);
        return SpellCard.getSpellCardByName(getCardByAddress(cardAddress).getCardName());
    }

    public TrapCard getTrapCardByStringAddress(String address) {
        Address cardAddress = new Address(address);
        return TrapCard.getTrapCardByName(getCardByAddress(cardAddress).getCardName());
    }

    public boolean isThisMonsterOnDHPosition(String address) {
        Address cardAddress = new Address(address);
        if (positionOfCardInBoardByAddress.get(cardAddress).equals(PositionOfCardInBoard.DH))
            return true;
        else return false;
    }

    public void convertThisMonsterFromDHToOO(String address) {
        Address cardAddress = new Address(address);
        positionOfCardInBoardByAddress.put(cardAddress, PositionOfCardInBoard.OO);
    }

    public void convertThisMonsterFromAttackToDefence(String address) {
        Address cardAddress = new Address(address);
        positionOfCardInBoardByAddress.put(cardAddress, PositionOfCardInBoard.DO);
    }

    public void convertThisMonsterFromDefenceToAttack(String address) {
        Address cardAddress = new Address(address);
        positionOfCardInBoardByAddress.put(cardAddress, PositionOfCardInBoard.OO);
    }

    public boolean isThisMonsterOnAttackPosition(String address) {
        Address cardAddress = new Address(address);
        if (!positionOfCardInBoardByAddress.get(cardAddress).equals(PositionOfCardInBoard.DH))
            return true;
        else return false;
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

    public int getIndexOfThisCardByAddress(Address address) {
        return indexOfCard.get(address);
    }

    public void setPositionOfCardInBoardByAddress(Address address, PositionOfCardInBoard positionOfCardInBoard) {
        positionOfCardInBoardByAddress.put(address, positionOfCardInBoard);
    }

    public PositionOfCardInBoard positionOfCardInBoardByAddress(Address address) {
        return positionOfCardInBoardByAddress.get(address);
    }

    public MonsterCard getMonsterCardByAddress(Address address) {
        return MonsterCard.getMonsterCardByName(getCardByAddress(address).getCardName());
    }

    public boolean isThereAnyRitualModeSpellInOurHand() {
        return true;
        //sdfsadfkjhaskdf
    }

    public boolean isThereAnyRitualTypeMonsterInOurHand() {
        for (int i = 1; i <= 5; i++)
            if (monsterZoneCardNumbers.containsKey(i) && monsterZoneCardNumbers.get(i).checkIsRitual())
                return true;
        return false;
    }

    public ArrayList<Integer> sumOfLevelOfAllSubsetsOfMonsterZone(HashMap<Integer, Card> hasCard) {
        ArrayList<Integer> integers = new ArrayList<>();
        for (int i = 1; i < 32; i++)
            integers.add(getSubLeve(i, hasCard));
        return integers;
    }

    private Integer getSubLeve(int setNumber, HashMap<Integer, Card> hasCard) {
        int number = 0;
        for (int i = 1; i <= 5; i++) {
            if (setNumber % 2 == 1 && hasCard.containsKey(i))
                number += hasCard.get(i).getLevel();
            setNumber /= 2;
        }
        return number;
    }

    public ArrayList<Integer> levelOfRitualMonstersOnOurHand() {
        ArrayList<Integer> Levels = new ArrayList<>();
        for (int i = 1; i <= 5; i++)
            if (monsterZoneCardNumbers.containsKey(i) && monsterZoneCardNumbers.get(i).checkIsRitual())
                Levels.add(i, monsterZoneCardNumbers.get(i).getLevel());
        return Levels;
    }

    public boolean isOneOfLevelOfRitualMonstersInTheHandIsEqualToSumOfLevelOfSubsetOfMonsterZone() {
        for (int i = 0; i < sumOfLevelOfAllSubsetsOfMonsterZone(monsterZoneCardNumbers).size(); i++)
            for (int j = 0; j < levelOfRitualMonstersOnOurHand().size(); j++)
                if (sumOfLevelOfAllSubsetsOfMonsterZone(monsterZoneCardNumbers).get(i).equals(levelOfRitualMonstersOnOurHand().get(j)))
                    return true;
        return false;
    }

    public int howManyCardIsInTheHandCard() {
        int number = 0;
        for (int i = 1; i <= 6; i++)
            if (handCardNumbers.containsKey(i))
                number++;
        return number;
    }

    public Address addressOfAttackerCard() {
        return null;
        //sd;fkhskdfjh
    }

    public boolean doWeHaveThisCardInBoard(String cardName) {
        for (int i = 1; i <= 5; i++)
            if (monsterZoneCardNumbers.containsKey(i) && monsterZoneCardNumbers.get(i).getCardName().equals(cardName))
                return true;
        for (int i = 1; i <= 5; i++)
            if (spellZoneCardNumbers.containsKey(i) && spellZoneCardNumbers.get(i).getCardName().equals(cardName))
                return true;
        return false;
    }

    public boolean didBeastKingBarbarosSummonedSuperHighLevel(int index) {
        return didBeastKingBarbarosSummonedSuperHighLevel[index];
    }

    public void setDidBeastKingBarbarosSummonedSuperHighLevel(boolean didBeastKingBarbarosSummonedSuperHighLevel,
                                                              int index) {
        this.didBeastKingBarbarosSummonedSuperHighLevel[index] = didBeastKingBarbarosSummonedSuperHighLevel;
    }

    public boolean isThereThreeCardInMonsterZone() {
        int count = 0;
        for (int i = 1; i <= 5; i++)
            if (monsterZoneCardNumbers.containsKey(i))
                count++;
        return count > 2;
    }

    public int howManyHeraldOfCreationDoWeHave() {
        int number = 0;
        for (int i = 1; i <= 5; i++)
            if (monsterZoneCardNumbers.containsKey(i) && monsterZoneCardNumbers.get(i).getCardName().equals("Herald of Creation"))
                number++;
        return number;
    }

    public Address setCardFromGraveyardToHand(Address comeBackFromGraveyard) {
        if (isMonsterZoneFull() || !graveyardCardNumbers.containsKey(comeBackFromGraveyard.getNumber()))
            return null;
        int place = getFirstEmptyPlace(monsterZoneCardNumbers, 5);
        monsterZoneCardNumbers.put(place, graveyardCardNumbers.get(comeBackFromGraveyard.getNumber()));
        graveyardCardNumbers.remove(comeBackFromGraveyard.getNumber());
        Address address = new Address(place, "monster", true);
        indexOfCard.put(address, indexOfCard.get(comeBackFromGraveyard));
        return address;
    }

    public boolean isThereAnyFieldSpellInDeck() {
        for (int i = 0; i < unusedCards.size(); i++)
            if (unusedCards.get(i).getKind().equals("Spell") && unusedCards.get(i).getSpellMode().equals(SpellMode.FIELD))
                return true;
        return false;
    }

    public boolean isThisCardInDeck(String cardName) {
        for (int i = 0; i < unusedCards.size(); i++)
            if (unusedCards.get(i).getCardName().equals(cardName))
                return true;
        return false;
    }

    public void bringCardFromDeckToHand(String cardName) {
        if (isHandFull() || !unusedCards.contains(Card.getCardByName(cardName)))
            return;
        int place = getFirstEmptyPlace(handCardNumbers, 6);
        handCardNumbers.put(place, Card.getCardByName(cardName));
        unusedCards.remove(Card.getCardByName(cardName));
        Address address = new Address(place, "hand", true);
        indexOfCard.put(address, indexOfCard.size() + 1);
        cardByIndex[indexOfCard.size() + 1] = Card.getCardByName(cardName);
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
            if ((isThisSpellActivated[i]) && (cardByIndex[i].getCardName().equals("Absorption"))) return true;
        return false;
    }

    public void removeOneOfTrapOrSpell(String name) {
        for (int i = 1; i <= 5; i++) {
            if (spellZoneCardNumbers.containsKey(i) && spellZoneCardNumbers.get(i).getCardName().equals(name)) {
                removeCard(new Address(i, "spell", true));
                break;
            }
        }
    }

    public boolean doIHaveSpellCard(String cardName) {
        for (int i = 1; i <= 5; i++)
            if (spellZoneCardNumbers.containsKey(i) && spellZoneCardNumbers.get(i).getCardName().equals(cardName))
                if (spellZoneCardNumbers.get(i).getKind().equals("Trap")
                        || isThisSpellActivated[indexOfCard.get(new Address(i, "spell", true))])
                    return true;
        return false;
    }

    public int getNumberOFHandCard() {
        return handCardNumbers.size();
    }

    public int getNumberOfUnusedCard() {
        return unusedCards.size();
    }

    public boolean getSpellPosition(int i) {
        return isSpellFaceUp.get(i);
    }

    public PositionOfCardInBoard getMonsterPosition(int i) {
        Address address = new Address(i, "monster", true);
        return positionOfCardInBoardByAddress.get(address);
    }

    public int getNumberOfGraveyardCard() {
        return graveyardCardNumbers.size();
    }

    public void reset() {
        LP = 8000;
        unusedCards = (ArrayList<Card>) Deck.getActiveDeckOfUser(user.getName()).getMainCards().clone();
        secondaryCard = (ArrayList<Card>) Deck.getActiveDeckOfUser(user.getName()).getSideCards().clone();
        isOneHisMonstersDestroyedInThisRound = false;
        positionOfCardInBoardByAddress = new HashMap<>();
        handCardNumbers = new HashMap<>();
        graveyardCardNumbers = new HashMap<>();
        fieldCardNumbers = new HashMap<>();
        monsterZoneCardNumbers = new HashMap<>();
        spellZoneCardNumbers = new HashMap<>();
        indexOfCard = new HashMap<>();
        isSpellFaceUp = new HashMap<>();
        cardByIndex = new Card[100];
        isThisSpellActivated = new boolean[100];
        didBeastKingBarbarosSummonedSuperHighLevel = new boolean[100];
        positionOfCardInBoardByAddress = new HashMap<>();
        indexOfCardUsedSuijin = new ArrayList<>();
        didWeChangePositionThisCardInThisTurn = new boolean[100];
        didWeAttackByThisCardInThisCard = new boolean[100];
    }

    public void setHandCard() {
        for (int i = 0; i < 5; i++)
            addCardFromUnusedToHand();
    }

    public ArrayList<Card> getMainCards() {
        return unusedCards;
    }

    public ArrayList<Card> getSideCards() {
        return secondaryCard;
    }

    public void specialSummonThisKindOfCardFromHandOrDeckOrGraveyard(String monsterName) {
        if (isMonsterZoneFull())
            return;
        int place = getFirstEmptyPlace(monsterZoneCardNumbers, 5);
        Address add = new Address(place, "monster", true);
        positionOfCardInBoardByAddress.put(add, PositionOfCardInBoard.OO);
        for (int i = 1; i <= 6; i++) {
            if (handCardNumbers.containsKey(i) && handCardNumbers.get(i).getCardName().equals(monsterName)) {
                monsterZoneCardNumbers.put(place, handCardNumbers.get(i));
                indexOfCard.put(add, indexOfCard.get(new Address(i, "hand", true)));
                handCardNumbers.remove(i);
                return;
            }
        }
        for (int i = 1; i <= graveyardCardNumbers.size(); i++) {
            if (graveyardCardNumbers.containsKey(i) && graveyardCardNumbers.get(i).getCardName().equals(monsterName)) {
                monsterZoneCardNumbers.put(place, graveyardCardNumbers.get(i));
                indexOfCard.put(add, indexOfCard.get(new Address(i, "graveyard", true)));
                removeCard(new Address(i, "graveyard", true));
                return;
            }
        }
        for (int i = 1; i <= unusedCards.size(); i++) {
            if (unusedCards.get(i).getCardName().equals(monsterName)) {
                monsterZoneCardNumbers.put(place, unusedCards.get(i));
                indexOfCard.put(add, indexOfCard.size() + 1);
                cardByIndex[indexOfCard.size()] = Card.getCardByName(monsterName);
                unusedCards.remove(i);
            }
        }
    }

    public boolean doIHaveMirageDragonMonster() {
        for (int i = 1; i <= 5; i++)
            if (monsterZoneCardNumbers.containsKey(i) && monsterZoneCardNumbers.get(i).getCardName().equals("Migrage Dragon"))
                return true;
        return false;
    }

    public void summonAMonsterCardFromGraveyard() {
        if (isMonsterZoneFull() || graveyardCardNumbers.size() == 0)
            return;
        int place = getFirstEmptyPlace(monsterZoneCardNumbers, 5);
        Address add = new Address(place, "monster", true);
        positionOfCardInBoardByAddress.put(add, PositionOfCardInBoard.OO);
        monsterZoneCardNumbers.put(place, graveyardCardNumbers.get(graveyardCardNumbers.size()));
        indexOfCard.put(add, indexOfCard.get(new Address(graveyardCardNumbers.size(), "graveyard", true)));
        removeCard(new Address(graveyardCardNumbers.size(), "graveyard", true));
    }

    public boolean canIContinueTribute(int number, List<Address> monsterCardsAddress) {
        HashMap<Integer, Card> monsters = new HashMap<>();
        for (int i = 1; i <= 5; i++)
            if (monsterZoneCardNumbers.containsKey(i))
                monsters.put(i, monsterZoneCardNumbers.get(i));
        for (Address cardsAddress : monsterCardsAddress)
            monsters.remove(cardsAddress.getNumber());
        ArrayList<Integer> subLevels = sumOfLevelOfAllSubsetsOfMonsterZone(monsters);
        return subLevels.contains(number);
    }

    public Address getOneOfRitualSpellCardAddress() {
        for (int i = 1; i <= 5; i++)
            if (spellZoneCardNumbers.containsKey(i) && spellZoneCardNumbers.get(i).getCardName().equals("Advanced Ritual Art"))
                return new Address(i, "spell", true);
        return null;
    }

    public SpellCard getSpellCardByAddress(Address address) {
        return SpellCard.getSpellCardByName(getCardByAddress(address).getCardName());
    }

    public TrapCard getTrapCardByAddress(Address address) {
        return TrapCard.getTrapCardByName(getCardByAddress(address).getCardName());
    }

    public void setFromMonsterToSpellEquip(int spellPlace, int monsterPlace) {
        fromMonsterToSpellEquip[monsterPlace] = spellPlace;
    }

    public void unSetFromMonsterToSpellEquip(int monsterPlace) {
        fromMonsterToSpellEquip[monsterPlace] = -1;
    }

    public int getFromMonsterToSpellEquip(int monsterPlace) {
        return fromMonsterToSpellEquip[monsterPlace];
    }

    public void removeOneOfHandCard() {
        for (int i = 1; i <= 5; i++) {
            if (handCardNumbers.containsKey(i)) {
                removeCard(new Address(i, "hand", true));
                break;
            }
        }
    }

    public void removeAllCardWithThisNameInMyHand(String cardName) {
        for (int i = 1; i <=5; i++)
            if (handCardNumbers.containsKey(i) && handCardNumbers.get(i).getCardName().equals(cardName))
                removeCard(new Address(i, "hand", true));
    }

    public boolean doIHaveCardWithThisNameInMyHand(String cardName) {
        for (int i = 1; i <= 5; i++)
            if (handCardNumbers.containsKey(i) && handCardNumbers.get(i).getCardName().equals(cardName))
                return true;
        return false;
    }

    public void setSlideToMain(int slideNumber) {
        unusedCards.add(secondaryCard.get(slideNumber - 1));
        secondaryCard.remove(slideNumber - 1);
    }

    public void setMainToSlide(int mainNumber) {
        secondaryCard.add(unusedCards.get(mainNumber - 1));
        unusedCards.remove(mainNumber - 1);
    }

    public boolean doIHaveMonsterCardInMonsterZone(String cardName) {
        for (int i = 1; i <= 5; i++)
            if (monsterZoneCardNumbers.containsKey(i) && monsterZoneCardNumbers.get(i).getCardName().equals(cardName))
                return true;
        return false;
    }

    public void setIndex(Address address, int index) {
        indexOfCard.put(address, index);
    }
}