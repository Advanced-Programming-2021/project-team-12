package controllers;

import Exceptions.CardNotExistException;
import Exceptions.NotEnoughMoneyException;
import models.Card;
import models.User;

public class ShopControl {
    public void buyCard(String cardName, User user) throws Exception {
        Card card = Card.getCardByName(cardName);
        if (card == null)
            throw new Exception("there is no card with this name");
        if (card.getPrice() > user.getMoney())
            throw new Exception("not enough money");
        else {
            user.decreaseMoney(card.getPrice());
            user.addCardToAllCard(card);
            SaveFile.saveUser(user);
        }
    }
}
