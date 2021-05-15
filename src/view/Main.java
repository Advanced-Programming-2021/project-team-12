package view;
import controllers.SaveFile;
import models.User;
import java.util.Scanner;

public class Main{
    public static Scanner scanner = new Scanner(System.in);
    public static void main(String[] args){
        User user = new User("aa", "rr", "kk");
        new SaveFile();
        new RegistrationMenu();
    }
}