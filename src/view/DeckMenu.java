package view;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.regex.Matcher;

import Exceptions.*;
import Utility.CommandMatcher;
import controllers.DeckControllers;
import controllers.SaveFile;
import models.Card;
import models.Deck;
import models.User;

public class DeckMenu {
    private User user;
    public DeckMenu() {
        this.user = MainMenu.user;
        String input;
        while (true) {
            input = Main.scanner.nextLine().trim();
            if (input.compareTo("user logout") == 0) {
                System.out.println("user logged out successfully!");
                return;
            }
            else if (input.matches("deck create [\\w]+"))
                createDeck(input);
            else if (input.matches("deck delete [\\w]+"))
                deleteDeck(input);
            else if (input.matches("deck set-activate [\\w]+"))
                setDeckActive(input);
            else if (input.matches("deck add-card (--cards|-c) [\\w]+ (--deck|-d) [\\w]+( --side| -s)*")
                    || input.matches("deck add-card (--deck|-d) [\\w]+ (--cards|-c) [\\w]+( --side| -s)*"))
                addCardToDeck(input);
            else if (input.matches("deck rm-card (--cards|-c) [\\w]+ (--deck|-d) [\\w]+( --side| -s)*")
                    || input.matches("deck rm-card (--deck|-d) [\\w]+ (--cards|-c) [\\w]+( --side| -s)*"))
                removeCardFromDeck(input);
            else if (input.matches("deck show (--all|-a)"))
                showAllDecks();
            else if (input.matches("deck show --deck-name [\\w]+( --side| -s)*"))
                showDeckByName(input);
            else if (input.matches("deck show (--cards|-c)"))
                showCards();
            else if (input.matches("menu show-current"))
                System.out.println("Shop");
            else 
                System.out.println("invalid command!");
        }
    }

    public void createDeck(String input) {
        Matcher matcher;
        String deckName;
        matcher = CommandMatcher.getCommandMatcher(input, "deck create ([\\w]+)");
        matcher.find();
        deckName = matcher.group(1);
        try {
            new DeckControllers().createDeck(deckName);
            System.out.println("deck created successfully!");
        } catch (DeckName e) {
            System.out.println("deck with name " + deckName + " already exists");
        }
    }

    public void deleteDeck(String input) {
        Matcher matcher;
        String deckName;
        matcher = CommandMatcher.getCommandMatcher(input, "deck delete ([\\w]+)");
        matcher.find();
        deckName = matcher.group(1);
        try {
            new DeckControllers().deleteDeck(deckName);
            System.out.println("deck deleted successfully!");
        } catch (DeckName e) {
            System.out.println("deck with name " + deckName + " does not exists");
        }
    }

    public void setDeckActive(String input) {
        Matcher matcher;
        String deckName;
        matcher = CommandMatcher.getCommandMatcher(input, "deck set-activate ([\\w]+)");
        matcher.find();
        deckName = matcher.group(1);
        try {
            new DeckControllers().setActive(deckName);
            System.out.println("deck activated successfully!");
        } catch (DeckName e) {
            System.out.println("deck with name " + deckName + " does not exists");
        }
    }

    public void addCardToDeck(String input) {
        Boolean isSide = false;
        String cardName;
        String deckName;
        Matcher matcher = CommandMatcher.getCommandMatcher(input, "(--card|-c) ([\\w]+)");
        matcher.find();
        cardName = matcher.group(2);
        matcher = CommandMatcher.getCommandMatcher(input, "(--deck|-d) ([\\w]+)");
        matcher.find();
        deckName = matcher.group(2);
        matcher = CommandMatcher.getCommandMatcher(input, "( --side| -s)");
        if (matcher.find())
            isSide = true;
        try {
            new DeckControllers().addCard(cardName, deckName, isSide);
            System.out.println("card added to deck successfully");
        } catch (CardName e) {
            System.out.println("card with name " + cardName + " does not exists");
        } catch (DeckName e) {
            System.out.println("deck with name " + deckName + " does not exists");
        } catch (FullDeck e) {
            System.out.println("main deck is full");
        } catch (ThreeCardDeck e) {
            System.out.println("there are already three cards with name " + cardName + " in deck " + deckName);
        }
    }

    public void removeCardFromDeck(String input) {
        Boolean isSide = false;
        String cardName;
        String deckName;
        Matcher matcher = CommandMatcher.getCommandMatcher(input, "(--card|-c) ([\\w]+)");
        matcher.find();
        cardName = matcher.group(2);
        matcher = CommandMatcher.getCommandMatcher(input, "(--deck|-d) ([\\w]+)");
        matcher.find();
        deckName = matcher.group(2);
        matcher = CommandMatcher.getCommandMatcher(input, "( --side| -s)");
        if (matcher.find())
            isSide = true;
        try {
            new DeckControllers().removeCard(cardName, deckName, isSide);
            System.out.println("card removed form deck successfully");
        } catch (DeckName e) {
            System.out.println("deck with name " + deckName + " does not exists");
        } catch (MainCard e) {
            System.out.println("card with name " + cardName + " does not exist in main deck");
        } catch (SideCard e) {
            System.out.println("card with name " + cardName + " does not exist in side deck");
        }
    }

    public void showAllDecks() {
        System.out.println("Decks:");
        System.out.println("Active deck:");
        Deck activeDeck = Deck.getActiveDeckOfUser(user.getName());
        showDeck(activeDeck);
        System.out.println("Other dekcs:");
        for (Deck deck : Deck.getDecksOfUser(user.getName())) 
            if (!deck.checkIsActive())
                showDeck(deck);
    }
    
    public void showDeck(Deck deck) {
        if (deck == null)
            return;
        else {
            String isValid = "invalid";
            if (deck.isValid()) isValid = "valid";
            System.out.println(deck.getName() + ": " + "main deck " + deck.getNumberOfCard("m") + ", side deck " + deck.getNumberOfCard("s") + ", " +
            isValid);
        }
    }

    public void showDeckByName(String input) {
        Matcher matcher = CommandMatcher.getCommandMatcher(input, "deck show --deck-name ([\\w]+)( --side|-s)*");
        matcher.find();
        String deckName = matcher.group(1);
        Deck deck = Deck.getDeckByName(deckName);
        if (deck.getDeckByName(deckName) == null) {
            System.out.println("deck with name " + deckName + " does not exists");
            return;
        }
        ArrayList<Card> cards;
        System.out.println("Deck: " + deckName);
        if (matcher.group(2).isEmpty()) {
            System.out.println("Main deck:");
            cards = deck.getMainCards();
        }
        else {
            cards = deck.getSideCards();
            System.out.println("Side deck:");
        }
        System.out.println("Monsters:");
        for (Card card : cards) 
            if (card.getKind().equals("monster"))
                System.out.println(card.getCardName() + ": " + card.getDescription());
        System.out.println("Spell and Traps:");
        for (Card card : cards) 
            if (!card.getKind().equals("monster"))
                System.out.println(card.getCardName() + ": " + card.getDescription());
    }

    public void showCards() {
        ArrayList<Card> allCards = Deck.getCardOfUser(MainMenu.user);
        for (Card card : allCards) 
            System.out.println(card.getCardName() + ": " + card.getDescription());
    }

}
