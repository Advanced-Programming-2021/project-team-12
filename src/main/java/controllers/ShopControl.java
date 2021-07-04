package controllers;

import Exceptions.MyException;
import models.Card;
import models.User;
import view.MainMenu;

public class ShopControl {
    public void buyCard(Card card, User user) {
        user.decreaseMoney(card.getPrice());
        user.addCardToAllCard(card);
        SaveFile.saveUser(user);
    }

    public void cheatyIncreaseMoney(int money) {
        MainMenu.user.increaseMoney(money);
    }


}
