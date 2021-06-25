package controllers;

import Exceptions.CardNotExistException;
import Exceptions.MyException;
import Exceptions.NotEnoughMoneyException;
import models.Card;
import models.User;
import view.MainMenu;

public class ShopControl {
    public void buyCard(String cardName, User user) throws MyException {
        Card card = Card.getCardByName(cardName);
        if (card == null)
            throw new MyException("there is no card with this name");
        if (card.getPrice() > user.getMoney())
            throw new MyException("not enough money");
        else {
            user.decreaseMoney(card.getPrice());
            user.addCardToAllCard(card);
            SaveFile.saveUser(user);
        }
    }

    public void cheatyIncreaseMoney(int money) {
        MainMenu.user.increaseMoney(money);
    }
}
