package controllers;

import Exceptions.*;
import models.Card;
import models.Deck;
import models.User;
import view.MainMenu;

public class DeckControllers {
    public void createDeck(String deckName, User user) throws MyException {
        if (Deck.getDeckByName(deckName) != null)
            throw new MyException("deck with name " + deckName + " already exists");
        else {
            new Deck(deckName, user);
            SaveFile.saveUser(user);
        }
    }

    public void deleteDeck(String deckName) throws MyException {
        User user = MainMenu.user;
        if (Deck.getDeckByName(deckName) == null)
            throw new MyException("deck with name " + deckName + " does not exists");
        else {
            Deck.deleteDeck(deckName);
            SaveFile.saveUser(user);
        }
    }

    public void setActive(String deckName) throws MyException{
        User user = MainMenu.user;
        if (Deck.getDeckByName(deckName) == null)
            throw new MyException("deck with name " + deckName + " does not exists");
        else {
            Deck deck = Deck.getDeckByName(deckName);
            deck.setAcctive();
            SaveFile.saveUser(user);
        }
    }

    public void removeCard(String cardName, String deckName, boolean isSide) throws MyException {
        User user = MainMenu.user;
        if (Deck.getDeckByName(deckName) == null)
            throw new MyException("deck with name " + deckName + " does not exists");
        else {
            Deck deck = Deck.getDeckByName(deckName);
            if (!isSide) {
                if (!deck.containsCardInMain(cardName))
                    throw new MyException("card with name " + cardName + " does not exist in main deck");
                else  {
                    deck.removeCard(cardName, "m");
                    SaveFile.saveUser(user);
                }
            }
            else {
                if (!deck.containsCardInSide(cardName))
                    throw new MyException("card with name " + cardName + " does not exist in side deck");
                else  {
                    deck.removeCard(cardName, "s");
                    SaveFile.saveUser(user);
                }
            }
        }
    }

    public void addCard(String cardName, String deckName, Boolean isSide) throws MyException {
        User user = MainMenu.user;
        if (!user.checkIfHasCard(Card.getCardByName(cardName)))
            throw new MyException("card with name " + cardName + " does not exists");
        else if (Deck.getDeckByName(deckName) == null)
            throw new MyException("deck with name " + deckName + " does not exists");
        else {
            Deck deck = Deck.getDeckByName(deckName);
            if (!isSide) {
                if (deck.isMainCardsFull())
                    throw new MyException("main deck is full");
                else if (deck.checkIfThereIsThree(cardName))
                    throw new MyException("there are already three cards with name " + cardName + " in deck " + deckName);
                else  {
                    deck.addCard(cardName, "m");
                    SaveFile.saveUser(user);
                }
            }
            else {
                if (deck.isSideCardsFull())
                    throw new MyException("main deck is full");
                else if (deck.checkIfThereIsThree(cardName))
                    throw new MyException("there are already three cards with name " + cardName + " in deck " + deckName);
                else  {
                    deck.addCard(cardName, "s");
                    SaveFile.saveUser(user);
                }
            }
        }
    }
}
