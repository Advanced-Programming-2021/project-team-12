package models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import view.MainMenu;

public class Deck {
    private User user;
    private String name;
    private ArrayList<Card> mainCards = new ArrayList<>();
    private ArrayList<Card> sideCards = new ArrayList<>();
    private static ArrayList<Deck> decks = new ArrayList<>();
    private Boolean isActive;

    public Deck(String name, User user) {
        this.name = name;
        this.user = user;
        isActive = false;
        decks.add(this);
    }

    public String getName() {
        return name;
    }

    public Boolean isValid() {
        if (mainCards.size() >= 40)
            return true;
        return false;
    }

    public void changeIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Boolean checkIsActive() {
        return isActive;
    }

    public static Deck getDeckByName(String name) {
        for (Deck deck : decks) 
            if (deck.getName().equals(name))
                return deck;
        return null;
    }

    public static ArrayList<Deck> getDecksOfUser(String userName) {
        ArrayList<Deck> decksOfUser = new ArrayList<>();
        for (Deck deck : decks) 
            if (deck.getUser().getName().equals(userName))
                decksOfUser.add(deck);
        return decksOfUser;
    }

    public static Deck getActiveDeckOfUser(String userName) {
        ArrayList<Deck> decksOfUser = Deck.getDecksOfUser(userName);
        for (Deck deck : decksOfUser)
            if (deck.checkIsActive())
                return deck;
        return null;
    }

    public static void deleteDeck(String name) {
        Deck deck = Deck.getDeckByName(name);
        ArrayList<Card> cards = deck.getMainCards();
        for (Card card : cards) 
            deck.getUser().addCardToAllCard(card);
        cards = deck.getSideCards();
        for (Card card : cards) 
            deck.getUser().addCardToAllCard(card);
        decks.remove(deck);
    }

    public ArrayList<Card> getMainCards() {
        Collections.sort(mainCards, Comparator.comparing(Card::getCardName));
        return mainCards;
    }

    public ArrayList<Card> getSideCards() {
        Collections.sort(sideCards, Comparator.comparing(Card::getCardName));
        return sideCards;
    }

    public void addCard(String cardName, String flag) {
        Card card = Card.getCardByName(cardName);
        this.user.removeCardFromAllCard(card);
        if (flag.equals("s"))
            sideCards.add(card);
        else      
            mainCards.add(card);
    }

    public void removeCard(String cardName, String flag) {
        Card card = Card.getCardByName(cardName);
        this.user.addCardToAllCard(card);
        if (flag.equals("s"))
            sideCards.remove(card);
        else      
            mainCards.remove(card);
    }

    public void setAcctive() {
        Deck deck = Deck.getActiveDeckOfUser(user.getName());
        deck.changeIsActive(false);
        this.isActive = true;
    }

    public int getNumberOfCard(String flag) {
        if (flag.equals("s"))
            return sideCards.size();
        else return mainCards.size();
    }

    public Boolean isMainCardsFull() {
        return mainCards.size() >= 60;
    }

    public Boolean isSideCardsFull() {
        return sideCards.size() >= 15;
    }

    public Boolean checkIfThereIsThree(String cardName) {
        int count = 0;
        for (Card card : mainCards) 
            if (card.getCardName().equals(cardName))
                count++;
        for (Card card : sideCards) 
            if (card.getCardName().equals(cardName))
                count++;
        if (count == 3)
            return true;
        return false;
    }

    public User getUser() {
        return user;
    }

    public Boolean containsCardInMain(String name) {
        for (Card card : mainCards) 
            if (card.getCardName().equals(name))
                return true;
        return false;
    }

    public Boolean containsCardInSide(String name) {
        for (Card card : sideCards)
            if (card.getCardName().equals(name))
                return true;
        return false;
    }

    public static ArrayList<Deck> getDecksOfUser(User user) {
        ArrayList<Deck> deckOfUser = new ArrayList<>();
        for (Deck deck : decks) 
            if (deck.getUser().equals(user))
                deckOfUser.add(deck);
        return deckOfUser;
    }

}
