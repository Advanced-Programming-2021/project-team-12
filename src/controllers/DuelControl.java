package controllers;

import Exceptions.AcctiveDeck;
import Exceptions.UserNameException;
import Exceptions.ValidDeck;
import Exceptions.WrongRoundNumber;
import models.Deck;
import models.User;
import view.MainMenu;

public class DuelControl {
    public DuelControl(String secPlayer, int round) throws Exception{
        User firstPlayer = MainMenu.user;
        User secondPlayer = User.getUserByName(secPlayer);
        Deck firstPlayerActiveDeck = Deck.getActiveDeckOfUser(firstPlayer.getName());
        if (secondPlayer == null)
            throw new Exception("there is no player with this username");
        Deck secondPlayerActiveDeck = Deck.getActiveDeckOfUser(secondPlayer.getName());
        if (firstPlayerActiveDeck == null)
            throw new Exception(firstPlayer.getName() + " has no active deck");
        else if (secondPlayerActiveDeck == null)
            throw new Exception(secondPlayer.getName() + " has no active deck");
        else if (!firstPlayerActiveDeck.isValid())
            throw new Exception(firstPlayer.getName() + "’s deck is invalid");
        else if (!secondPlayerActiveDeck.isValid())
            throw new Exception(secondPlayer.getName() + "’s deck is invalid");
        else if (round != 1 && round != 3)
            throw new Exception("number of rounds is not supported");
        else
            Game.run(firstPlayer, secondPlayer, round);
    }

    public DuelControl(int round) throws Exception{
        User firstPlayer = MainMenu.user;
        Deck firstPlayerActiveDeck = Deck.getActiveDeckOfUser(firstPlayer.getName());
        if (firstPlayerActiveDeck == null)
            throw new Exception(firstPlayer.getName() + " has no active deck");
        else if (!firstPlayerActiveDeck.isValid())
            throw new Exception(firstPlayer.getName() + "’s deck is invalid");
        else if (round != 1 && round != 3)
            throw new Exception("number of rounds is not supported");
        else
            Game.run(firstPlayer, round);
    }
}
