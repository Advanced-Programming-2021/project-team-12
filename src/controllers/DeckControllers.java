package controllers;

import Exceptions.*;
import models.Card;
import models.Deck;
import models.User;
import view.MainMenu;

public class DeckControllers {
    public void createDeck(String deckName) throws DeckName {
        User user = MainMenu.user;
        if (Deck.getDeckByName(deckName) != null)
            throw new DeckName();
        else {
            new Deck(deckName);
            SaveFile.saveUser(user);
        }
    }

    public void deleteDeck(String deckName) throws DeckName {
        User user = MainMenu.user;
        if (Deck.getDeckByName(deckName) == null)
            throw new DeckName();
        else {
            Deck.deleteDeck(deckName);
            SaveFile.saveUser(user);
        }
    }

    public void setActive(String deckName) throws DeckName{
        User user = MainMenu.user;
        if (Deck.getDeckByName(deckName) == null)
            throw new DeckName();
        else {
            Deck deck = Deck.getDeckByName(deckName);
            deck.setAcctive();
            SaveFile.saveUser(user);
        }
    }

    public void removeCard(String cardName, String deckName, boolean isSide) throws DeckName, SideCard, MainCard {
        User user = MainMenu.user;
        if (Deck.getDeckByName(deckName) == null)
            throw new DeckName();
        else {
            Deck deck = Deck.getDeckByName(deckName);
            if (!isSide) {
                if (!deck.containsCardInMain(cardName))
                    throw new MainCard();
                else  {
                    deck.removeCard(cardName, "m");
                    SaveFile.saveUser(user);
                }
            }
            else {
                if (!deck.containsCardInSide(cardName))
                    throw new SideCard();
                else  {
                    deck.removeCard(cardName, "s");
                    SaveFile.saveUser(user);
                }
            }
        }
    }

    public void addCard(String cardName, String deckName, Boolean isSide) throws DeckName, FullDeck, ThreeCardDeck, CardName {
        User user = MainMenu.user;
        if (!user.checkIfHasCard(Card.getCardByName(cardName)))
            throw new CardName();
        else if (Deck.getDeckByName(deckName) == null)
            throw new DeckName();
        else {
            Deck deck = Deck.getDeckByName(deckName);
            if (!isSide) {
                if (deck.isMainCardsFull())
                    throw new FullDeck();
                else if (deck.checkIfThereIsThree(cardName))
                    throw new ThreeCardDeck();
                else  {
                    deck.addCard(cardName, "m");
                    SaveFile.saveUser(user);
                }
            }
            else {
                if (deck.isSideCardsFull())
                    throw new FullDeck();
                else if (deck.checkIfThereIsThree(cardName))
                    throw new ThreeCardDeck();
                else  {
                    deck.addCard(cardName, "s");
                    SaveFile.saveUser(user);
                }
            }
        }
    }
}
