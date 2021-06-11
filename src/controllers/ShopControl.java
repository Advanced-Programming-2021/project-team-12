package controllers;

import Exceptions.CardNotExistException;
import Exceptions.NotEnoughMoneyException;
import models.Card;
import models.User;

public class ShopControl {
    public void buyCard(String cardName, User user) throws NotEnoughMoneyException, CardNotExistException {
        Card card = Card.getCardByName(cardName);
        if (card == null)
            throw new CardNotExistException();
        if (card.getPrice() > user.getMoney())
            throw new NotEnoughMoneyException();
        else {
            user.decreaseMoney(card.getPrice());
            user.addCardToAllCard(card);
            SaveFile.saveUser(user);
        }
    }
}
