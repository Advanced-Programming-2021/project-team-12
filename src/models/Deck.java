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

    public Deck(String name) {
        this.name = name;
        this.user = MainMenu.user;
        isActive = false;
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
        ArrayList<Deck> deckOfUser = Deck.getDecksOfUser(userName);
        for (Deck deck : deckOfUser) 
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
        Collections.sort(mainCards, new Comparator<Card>() {
            public int compare(Card c1, Card c2) {
                return c1.getCardName().compareTo(c2.getCardName());
            }
        });
        return mainCards;
    }

    public ArrayList<Card> getSideCards() {
        Collections.sort(sideCards, new Comparator<Card>() {
            public int compare(Card c1, Card c2) {
                return c1.getCardName().compareTo(c2.getCardName());
            }
        });
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
        if (mainCards.size() >= 60)
            return true;
        return false;
    }

    public Boolean isSideCardsFull() {
        if (sideCards.size() >= 15)
            return true;
        return false;
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

    public static ArrayList<Card> getCardOfUser(User user) {
        ArrayList<Deck> decks;
        ArrayList<Card> allCards = new ArrayList<>();
        decks = Deck.getDecksOfUser(user.getName());
        for (Deck deck : decks) {
            ArrayList<Card> deckCards;
            deckCards = deck.getMainCards();
            for (Card card : deckCards)
                allCards.add(card);
            deckCards = deck.getSideCards();
            for (Card card : deckCards)
                allCards.add(card);
        }
        Collections.sort(allCards, new Comparator<Card>() {
            public int compare(Card c1, Card c2) {
                return c1.getCardName().compareTo(c2.getCardName());
            }
        });
        return allCards;
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
