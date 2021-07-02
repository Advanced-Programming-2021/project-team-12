package controllers;

import Exceptions.*;
import models.Deck;
import models.User;
import view.MainMenu;

public class DuelControl {
    public DuelControl(String secPlayer, int round) throws MyException{
        User firstPlayer = MainMenu.user;
        User secondPlayer = User.getUserByName(secPlayer);
        Deck firstPlayerActiveDeck = Deck.getActiveDeckOfUser(firstPlayer.getName());
        if (secondPlayer == null)
            throw new MyException("there is no player with this username");
        Deck secondPlayerActiveDeck = Deck.getActiveDeckOfUser(secondPlayer.getName());
        if (firstPlayerActiveDeck == null)
            throw new MyException(firstPlayer.getName() + " has no active deck");
        else if (secondPlayerActiveDeck == null)
            throw new MyException(secondPlayer.getName() + " has no active deck");
        else if (!firstPlayerActiveDeck.isValid())
            throw new MyException(firstPlayer.getName() + "’s deck is invalid");
        else if (!secondPlayerActiveDeck.isValid())
            throw new MyException(secondPlayer.getName() + "’s deck is invalid");
        else if (round != 1 && round != 3)
            throw new MyException("number of rounds is not supported");
        else
            Game.run(firstPlayer, secondPlayer, round);
    }

    public DuelControl(int round) throws MyException {
        User firstPlayer = MainMenu.user;
        Deck firstPlayerActiveDeck = Deck.getActiveDeckOfUser(firstPlayer.getName());
        if (firstPlayerActiveDeck == null)
            throw new MyException(firstPlayer.getName() + " has no active deck");
        else if (!firstPlayerActiveDeck.isValid())
            throw new MyException(firstPlayer.getName() + "’s deck is invalid");
        else if (round != 1 && round != 3)
            throw new MyException("number of rounds is not supported");
        else
            Game.run(firstPlayer, round);
    }
}
