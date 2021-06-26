package view;
import Exceptions.MyException;
import Exceptions.NickNameException;
import Exceptions.UserNameException;
import Exceptions.WrongUserOrPassException;
import Utility.CommandMatcher;
import controllers.LogInController;
import controllers.SignInController;
import models.User;
import java.util.regex.Matcher;

public class RegistrationMenu {
    public RegistrationMenu() {
        System.out.println(" ** WELCOME TO REGISTRATION MENU **");
        String input;
        while (true) {
            input = Main.scanner.nextLine().trim();
            if (input.compareTo("Exit") == 0)
                return;
            else if (input.matches("user login (--username|-u) [\\w-]+ (--password|-p) [\\w]+")
                    || input.matches("user login (--password|-p) [\\w]+ (--username|-u) [\\w-]+"))
                login(input);
            else if (input.matches("user create (--username|-u) [\\w-]+ (--nickname|-n) [\\w-]+ (--password|-p) [\\w]+")
                    || input.matches("user create (--username|-u) [\\w-]+ (--password|-p) [\\w]+ (--nickname|-n) [\\w-]+")
                    || input.matches("user create (--nickname|-n) [\\w-]+ (--password|-p) [\\w]+ (--username|-u) [\\w-]+")
                    || input.matches("user create (--nickname|-n) [\\w-]+ (--username|-u) [\\w-]+ (--password|-p) [\\w]+")
                    || input.matches("user create (--password|-p) [\\w]+ (--username|-u) [\\w-]+ (--nickname|-n) [\\w-]+")
                    || input.matches("user create (--password|-p) [\\w]+ (--nickname|-n) [\\w-]+ (--username|-u) [\\w-]+"))
                signin(input);
            else if (input.matches("menu show-current"))
                System.out.println("Login Menu");
            else if (input.matches("menu enter [\\w]+"))
                System.out.println("please login first");
            else {
                System.out.println("invalid command!");
                continue;
            }
            System.out.println("** REGISTRATION MENU **");
        }
    }
    
    public static void login(String input){
        String userName;
        String password;
        userName = getUserNameFromInput(input);
        password = getPasswordFromInput(input);
        try {
            new LogInController().checkData(userName, password);
            System.out.println("user logged in successfully!");
            new MainMenu(User.getUserByName(userName));
        } catch (MyException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public static void signin(String input){
        String userName;
        String password;
        String nickName;
        userName = getUserNameFromInput(input);
        password = getPasswordFromInput(input);
        nickName = getNickNameFromInput(input);
        try {
            new SignInController().checkData(userName, nickName, password);
            System.out.println("user created successfully!");
        } catch (MyException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private static String getUserNameFromInput(String input){
        Matcher matcher;
        matcher = CommandMatcher.getCommandMatcher(input, "(--username|-u) ([\\w-]+)");
        matcher.find();
        return  matcher.group(2);
    }

    private static String getPasswordFromInput(String input){
        Matcher matcher;
        matcher = CommandMatcher.getCommandMatcher(input, "(--password|-p) ([\\w]+)");
        matcher.find();
        return matcher.group(2);
    }

    private static String getNickNameFromInput(String input){
        Matcher matcher;
        matcher = CommandMatcher.getCommandMatcher(input, "(--nickname|-n) ([\\w-]+)");
        matcher.find();
        return matcher.group(2);
    }


}