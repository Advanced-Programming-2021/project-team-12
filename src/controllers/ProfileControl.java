package controllers;

import Exceptions.MyException;
import Exceptions.NewPassException;
import Exceptions.NickNameException;
import Exceptions.WrongPassException;
import models.User;
import view.MainMenu;

public class ProfileControl {
    public void changePass(String currentPass, String newPas) throws MyException {
        User user = MainMenu.user;
        if (!user.checkPassword(currentPass))
            throw new MyException("current password is invalid");
        else if (user.getPassword().equals(newPas))
            throw new MyException("please enter a new password");
        else {
            user.setPassword(newPas);
            SaveFile.saveUser(MainMenu.user);
        }
    }

    public void changeNickName(String nickName) throws MyException {
        User user = User.getUserByNickName(nickName);
        if (user != null)
            throw new MyException("\"user with nickname \" + nickName + \" already exists\"");
        else {
            MainMenu.user.setNickName(nickName);
            SaveFile.saveUser(MainMenu.user);
        }
    }
}
