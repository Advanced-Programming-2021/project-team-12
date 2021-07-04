package controllers;

import Exceptions.MyException;
import models.Card;
import models.Deck;
import models.User;

import java.util.ArrayList;

public class SignInController {
    public void checkData(String userName, String nickName, String password) throws Exception {
        if (User.getUserByName(userName) != null)
            throw new MyException("User With Username " + userName + " Already Exists");
        if (User.getUserByNickName(nickName) != null)
            throw new MyException("User With Nickname " + nickName + " Already Exists");
        else {
            User user = new User(nickName, userName, password);
            setDefaultDeck(user);
            setDefaultUserCards(user);
            SaveFile.saveUser(user);
        }
    }

    private void setDefaultUserCards(User user) {
        ArrayList<Card> cards = Card.getAllCards();
        for (int i = 0; i < cards.size(); i++)
            if (i % 11 == 0 || i % 13 == 0)
                user.addCardToAllCard(cards.get(i));
    }

    private void setDefaultDeck(User user) {
        String deckName = "default " + user.getName() + " deck";
        try {
            new DeckControllers().createDeck(deckName, user);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        setDefaultCards(Deck.getDeckByName("default " + user.getName() + " deck"));
    }

    private void setDefaultCards(Deck deck) {
        ArrayList<Card> cards = Card.getAllCards();
        for (int i = 0; i < cards.size(); i++)
            if (i % 2 == 0 || i % 3 == 0)
                deck.addCard(cards.get(i).getCardName(), "m");
        for (int i = 0; i < cards.size(); i++)
            if (i % 7 == 0)
                deck.addCard(cards.get(i).getCardName(), "s");
        deck.changeIsActive(true);
    }

}
