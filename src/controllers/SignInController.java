package controllers;

import Exceptions.NickNameException;
import Exceptions.UserNameException;
import Exceptions.WrongUserOrPassException;
import models.User;

public class SignInController {
    public void checkData(String userName, String nickName, String password) throws Exception {
        if (User.getUserByName(userName) != null)
            throw new Exception("user with username \" + userName + \" already exists");
        if (User.getUserByNickName(nickName) != null)
            throw new Exception("user with nickname " + nickName + " already exists");
        else {
            User user = new User(nickName, userName, password);
            SaveFile.saveUser(user);
        }
    }
}
