package controllers;

import Exceptions.MyException;
import models.User;
import view.MainMenu;

import java.io.File;

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
            throw new MyException("user with nickname " + nickName + " already exists");
        else {
            MainMenu.user.setNickName(nickName);
            SaveFile.saveUser(MainMenu.user);
        }
    }

    public void changeAvatar(File file) throws Exception{
        SaveFile.addAvatarImage(file, MainMenu.user);
        MainMenu.user.increaseCountAvatar();
        MainMenu.user.setAvatar(MainMenu.user.getAvatarCount());
        SaveFile.saveUser(MainMenu.user);
    }
}
