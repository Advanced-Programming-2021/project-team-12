package controllers;

import Exceptions.EmptyAddressException;
import Exceptions.WrongAddressException;

import java.io.File;

public class ImportCard {
    public void run(String cardAddress, String flag) throws WrongAddressException, EmptyAddressException{
        File file = new File(cardAddress);
        if (!file.isDirectory())
            throw new WrongAddressException();
        if (!file.exists())
            throw new EmptyAddressException();
        else
            new LoadFile().importCard(file, flag);
    }
}
