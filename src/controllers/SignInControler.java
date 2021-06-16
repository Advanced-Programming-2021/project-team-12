package controllers;

import Exceptions.NickNameException;
import Exceptions.UserNameException;
import Exceptions.WrongUserOrPassException;
import models.User;

public class SignInControler {
    public void checkData(String userName, String nickName, String password) throws NickNameException, UserNameException {
        if (User.getUserByName(userName) != null)
            throw new UserNameException();
        if (User.getUserByNickName(nickName) != null)
            throw new NickNameException();
        else {
            User user = new User(nickName, userName, password);
            SaveFile.saveUser(user);
        }
    }
}
