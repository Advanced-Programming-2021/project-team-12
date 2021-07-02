package controllers;

import Exceptions.MyException;
import models.Card;

import java.io.File;

public class ImportOrExportControl {
    public void doImport(String cardAddress, String flag) throws MyException {
        File file = new File(cardAddress);
        if (!file.isDirectory())
            throw new MyException("Wrong Address!");
        if (!file.exists())
            throw new MyException("Empty Address!");
        else
            new LoadFile().importCard(file, flag);
    }

    public void doExport(String cardAddress, String cardName) throws MyException {
        if (Card.getCardByName(cardName) == null)
            throw new MyException("card with this name does not exist");
        File file = new File(cardAddress);
        if (!file.isDirectory())
            throw new MyException("Wrong Address!");
        else
            SaveFile.exportCard(cardAddress, cardName);
    }
}
