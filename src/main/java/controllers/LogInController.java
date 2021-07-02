package controllers;

import Exceptions.MyException;
import models.User;

public class LogInController {
    public void checkData(String userName, String password) throws MyException {
        User user = User.getUserByName(userName);
        if (user == null || !user.checkPassword(password))
            throw new MyException("Username and password didn’t match!");
    }
}
