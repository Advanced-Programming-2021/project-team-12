package controllers;

import models.Card;
import models.Deck;
import models.User;

import java.util.ArrayList;

public class SignInController {
    public void checkData(String userName, String nickName, String password) throws Exception {
        if (User.getUserByName(userName) != null)
            throw new Exception("user with username \" + userName + \" already exists");
        if (User.getUserByNickName(nickName) != null)
            throw new Exception("user with nickname " + nickName + " already exists");
        else {
            User user = new User(nickName, userName, password);
            setDefaultDeck(user);
            SaveFile.saveUser(user);
        }
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
