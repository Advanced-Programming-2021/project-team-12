package controllers;

import Exceptions.NewPassException;
import Exceptions.NickNameException;
import Exceptions.WrongPassException;
import models.User;
import view.MainMenu;

public class ChangePass {
    public void change(String currentPass, String newPas) throws WrongPassException, NewPassException {
        User user = User.getUserByNickName(nickName);
        if (user != null)
            throw new NickNameException();
        else {
            MainMenu.user.setNickName(nickName);
            SaveFile.saveUser(MainMenu.user);
        }
    }
}
