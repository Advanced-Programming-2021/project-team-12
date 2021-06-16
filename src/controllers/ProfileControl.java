package controllers;

import Exceptions.NewPassException;
import Exceptions.NickNameException;
import Exceptions.WrongPassException;
import models.User;
import view.MainMenu;

public class ProfileControl {
    public void changePass(String currentPass, String newPas) throws WrongPassException, NewPassException {
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

    public void changeNickName(String nickName) throws NickNameException {
        User user = User.getUserByNickName(nickName);
        if (user != null)
            throw new NickNameException();
        else {
            MainMenu.user.setNickName(nickName);
            SaveFile.saveUser(MainMenu.user);
        }
    }
}
