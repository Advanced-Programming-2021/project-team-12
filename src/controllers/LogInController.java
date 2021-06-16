package controllers;

import Exceptions.WrongUserOrPassException;
import models.User;

public class LogInController {
    public void checkData(String userName, String password) throws Exception {
        User user = User.getUserByName(userName);
        if (user == null || !user.checkPassword(password))
            throw new Exception("Username and password didn’t match!");
    }
}
