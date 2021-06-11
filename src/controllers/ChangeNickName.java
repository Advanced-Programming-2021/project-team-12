package controllers;

import Exceptions.NickNameException;
import Exceptions.WrongUserOrPassException;
import models.User;
import view.MainMenu;

public class ChangeNickName {
    public void change(String nickName) throws NickNameException {
        User user = User.getUserByNickName(nickName);
        if (user != null)
            throw new NickNameException();
        else {
            MainMenu.user.setNickName(nickName);
            SaveFile.saveUser(MainMenu.user);
        }
    }
}
