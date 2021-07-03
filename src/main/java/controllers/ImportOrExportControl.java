package controllers;

import Exceptions.MyException;
import models.Card;
import view.MainMenu;

import java.io.File;

public class ImportOrExportControl {
    public Card doImport(File file, String flag) throws MyException {
        Card card;
        if (file.isDirectory())
            throw new MyException("Wrong Address!");
        if (!file.exists())
            throw new MyException("Empty Address!");
        else
            card = LoadFile.importCard(file, flag);
        SaveFile.saveCards();
        return card;
    }

    public void doExport(File file, String cardName) throws MyException {
        if (Card.getCardByName(cardName) == null)
            throw new MyException("card with this name does not exist");
        if (!file.isDirectory())
            throw new MyException("Wrong Address!");
        else
            SaveFile.exportCard(file, cardName);
    }
}
