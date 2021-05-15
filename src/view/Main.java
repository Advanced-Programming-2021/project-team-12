package view;
import java.util.Scanner;
import controllers.SaveFile;
import models.User;

public class Main{
    public static Scanner scanner = new Scanner(System.in);
    public static void main(String[] args) {
        User user = new User("aa", "rr", "kk");
        new SaveFile();
        new RegistrationMenu();
    }
}