package view;
import models.User;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Scoreboard {
    public static void run(){
        System.out.println("\n ** SCOREBOARD **");
        ArrayList<User> sortedUser = User.getUsers();
        int rank = 1;
        for (User user : sortedUser) {
            System.out.println(rank + "- " + user.getNickName() + ": " + user.getScore());
            rank++;
        }
        System.out.println();
    }

}
