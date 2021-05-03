package models;

import java.util.ArrayList;

import view.MainMenu;

public class Deck {
    private User user;
    private String name;
    private ArrayList<Card> cards = new ArrayList<>();
    private static ArrayList<Deck> decks = new ArrayList<>();

    public Deck(String name) {
        this.name = name;
        this.user = MainMenu.user;
    }

    public String getName() {
        return name;
    }

    public static Deck getDeckByName(String name) {
        for (Deck deck : decks) 
            if (deck.getName().equals(name))
                return deck;
        return null;
    }

    public static void deleteDeck(String name) {
        decks.remove(Deck.getDeckByName(name));
    }

    public void addCard(String cardName) {

    }

    public void removeCard(String cardName) {
        
    }

}
