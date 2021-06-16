package controllers;

import Exceptions.NewPassException;
import Exceptions.NickNameException;
import Exceptions.WrongPassException;
import models.User;
import view.MainMenu;

public class ChangePass {
    public void change(String currentPass, String newPas) throws WrongPassException, NewPassException {
        User user = MainMenu.user;
        if (!user.checkPassword(currentPass))
            throw new WrongPassException();
        else if (user.getPassword().equals(newPas))
            throw new NewPassException();
        else {
            user.setPassword(newPas);
            SaveFile.saveUser(MainMenu.user);
        }
    }
}
