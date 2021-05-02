package view;

import models.User;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Scoreboard {
    public static void run(){
        ArrayList<User> sortedUser = sortUser();
        int rank = 1;
        for (User user : sortedUser) {
            System.out.println(rank + "- " + user.getNickName + ": " + user.getScore);
            rank++;
        }
    }

    public static ArrayList<User> sortUser() {
        ArrayList<User> users = User.getUsers();
        Collections.sort(users, new Comparator<User>() {
            public int compare(User u1, User u2) {
                if (u1.getScore() != u2.getScore())
                    return (u1.getScore() > u2.getScore()) ? 1 : -1;
                else
                    return u1.getNickName().compareTo(u2.getNickName());
            }
        });
        return users;
    }
}
