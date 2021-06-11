package controllers;

import Exceptions.CardNotExistException;
import Exceptions.EmptyAddressException;
import Exceptions.WrongAddressException;
import models.Card;

import java.io.File;

public class ExportCard {
    public void run(String cardAddress, String cardName) throws WrongAddressException, CardNotExistException {
        if (Card.getCardByName(cardName) == null)
            throw new CardNotExistException();
        File file = new File(cardAddress);
        if (!file.isDirectory())
            throw new WrongAddressException();
        else
            SaveFile.exportCard(cardAddress, cardName);
    }
}
