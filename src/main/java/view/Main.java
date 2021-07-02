package view;
import java.util.Scanner;

import controllers.LoadFile;
import controllers.SaveFile;
import models.User;

public class Main{
    public static Scanner scanner = new Scanner(System.in);
    public static void main(String[] args) {
        new LoadFile();
        new RegistrationMenu();
    }
}