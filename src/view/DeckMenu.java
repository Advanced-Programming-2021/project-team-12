package view;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
        matcher = getCommandMatcher(input, "deck create ([\\w]+)");
        matcher.find();
        deckName = matcher.group(1);
        if (Deck.getDeckByName(deckName) != null)
            System.out.println("deck with name " + deckName + " already exists");
        else {
            new Deck(deckName);
            System.out.println("deck created successfully!");
        }
    }

    public void deleteDeck(String input) {
        Matcher matcher;
        String deckName;
        matcher = getCommandMatcher(input, "deck delete ([\\w]+)");
        matcher.find();
        deckName = matcher.group(1);
        if (Deck.getDeckByName(deckName) == null)
            System.out.println("deck with name " + deckName + " does not exists");
        else {
            Deck.deleteDeck(deckName);
            System.out.println("deck deleted successfully!");
        }
    }

    public void setDeckActive(String input) {
        Matcher matcher;
        String deckName;
        matcher = getCommandMatcher(input, "deck set-activate ([\\w]+)");
        matcher.find();
        deckName = matcher.group(1);
        if (Deck.getDeckByName(deckName) == null)
            System.out.println("deck with name " + deckName + " does not exists");
        else {
            Deck deck = Deck.getDeckByName(deckName);
            deck.setAcctive();
            System.out.println("deck activated successfully!");
        }
    }

    public void addCardToDeck(String input) {
        Boolean isSide = false;
        String cardName;
        String deckName;
        Matcher matcher = getCommandMatcher(input, "(--card|-c) ([\\w]+)");
        matcher.find();
        cardName = matcher.group(2);
        matcher = getCommandMatcher(input, "(--deck|-d) ([\\w]+)");
        matcher.find();
        deckName = matcher.group(2);
        matcher = getCommandMatcher(input, "( --side| -s)");
        if (matcher.find())
            isSide = true;
        if (!user.checkIfHasCard(Card.getCardByName(cardName)))
            System.out.println("card with name " + cardName + " does not exists");
        else if (Deck.getDeckByName(deckName) == null)
            System.out.println("deck with name " + deckName + " does not exists");
        else {
            Deck deck = Deck.getDeckByName(deckName);
            if (!isSide) {
                if (deck.isMainCardsFull())
                    System.out.println("main deck is full");
                else if (deck.checkIfThereIsThree(cardName))
                    System.out.println("there are already three cards with name " + cardName + " in deck " + deckName);
                else  {   
                    deck.addCard(cardName, "m");
                    System.out.println("card added to deck successfully");
                }
            }
            else {
                if (deck.isSideCardsFull())
                    System.out.println("side deck is full");
                else if (deck.checkIfThereIsThree(cardName))
                    System.out.println("there are already three cards with name " + cardName + " in deck " + deckName);
                else  {   
                    deck.addCard(cardName, "s");
                    System.out.println("card added to deck successfully");
                }
            }
        }
    }

    public void removeCardFromDeck(String input) {
        Boolean isSide = false;
        String cardName;
        String deckName;
        Matcher matcher = getCommandMatcher(input, "(--card|-c) ([\\w]+)");
        matcher.find();
        cardName = matcher.group(2);
        matcher = getCommandMatcher(input, "(--deck|-d) ([\\w]+)");
        matcher.find();
        deckName = matcher.group(2);
        matcher = getCommandMatcher(input, "( --side| -s)");
        if (matcher.find())
            isSide = true;
        if (Deck.getDeckByName(deckName) == null)
            System.out.println("deck with name " + deckName + " does not exists");
        else {
            Deck deck = Deck.getDeckByName(deckName);
            if (!isSide) {
                if (!deck.containsCardInMain(cardName))
                    System.out.println("card with name " + cardName + " does not exist in main deck");
                else  {   
                    deck.removeCard(cardName, "m");
                    System.out.println("card removed form deck successfully");
                }
            }
            else {
                if (!deck.containsCardInSide(cardName))
                    System.out.println("card with name " + cardName + " does not exist in side deck");
                else  {   
                    deck.removeCard(cardName, "s");
                    System.out.println("card removed form deck successfully");
                }
            }
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
        Matcher matcher = getCommandMatcher(input, "deck show --deck-name ([\\w]+)( --side|-s)*");
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
        Collections.sort(cards, new Comparator<Card>() {
            public int compare(Card c1, Card c2) {
                    return c1.getCardName().compareTo(c2.getCardName());
            }
        }); 
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
        for (Card card : allCards) 
            System.out.println(card.getCardName() + ": " + card.getDescription());
    }

    public static Matcher getCommandMatcher(String input, String regex) {
        Pattern pattern;
        Matcher matcher;
        pattern = Pattern.compile(regex);
        matcher = pattern.matcher(input);
        return matcher;
    }
}
