package controllers;

import Exceptions.EmptyAddressException;
import Exceptions.WrongAddressException;
import models.Card;

import java.io.File;

public class ImportOrExportControl {
    public void doImport(String cardAddress, String flag) throws Exception{
        File file = new File(cardAddress);
        if (!file.isDirectory())
            throw new Exception("Wrong Address!");
        if (!file.exists())
            throw new Exception("Empty Address!");
        else
            new LoadFile().importCard(file, flag);
    }

    public void doExport(String cardAddress, String cardName) throws Exception {
        if (Card.getCardByName(cardName) == null)
            throw new Exception("card with this name does not exist");
        File file = new File(cardAddress);
        if (!file.isDirectory())
            throw new Exception("Wrong Address!");
        else
            SaveFile.exportCard(cardAddress, cardName);
    }
}
