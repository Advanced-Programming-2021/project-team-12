package view;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.regex.Matcher;

import Exceptions.MyException;
import Exceptions.NewPassException;
import Exceptions.NickNameException;
import Exceptions.WrongPassException;
import Utility.CommandMatcher;
import controllers.ProfileControl;

public class Profile {
    public static void run() {
        System.out.println("** PROFILE MENU **");
        String input;
        while (true) {
            input = Main.scanner.nextLine().trim();
            if (input.compareTo("menu exit") == 0)
                return;
            else if (input.matches("profile change (--nickname|-n) [\\w-]+"))
                changeNickName(input);
            else if (input.matches("profile change (--password|-p) (--current|-c) [\\w]+ (--new|-n) [\\w]+")
                    || input.matches("profile change (--password|-p) (--new|-n) [\\w]+ (--current|-c) [\\w]+"))
                changePassword(input);
            else if (input.matches("menu show-current"))
                System.out.println("Profile");
            else if (input.matches("menu enter [\\w]+"))
                System.out.println("menu navigation is not possible");
            else
                System.out.println("invalid command!");
        }
    }

    private static void changeNickName(String input) {
        Matcher matcher;
        String nickName;
        matcher = CommandMatcher.getCommandMatcher(input, "(--nickname|-n) ([\\w-]+)");
        matcher.find();
        nickName = matcher.group(2);
        try {
            new ProfileControl().changeNickName(nickName);
            System.out.println("nickname changed successfully!");
        } catch (MyException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private static void changePassword(String input) {
        Matcher matcher;
        String currentPassword;
        String newPassword;
        matcher = CommandMatcher.getCommandMatcher(input, "(--current|-c) ([\\w]+)");
        matcher.find();
        currentPassword = matcher.group(2);
        matcher = CommandMatcher.getCommandMatcher(input, "(--new|-n) ([\\w]+)");
        matcher.find();
        newPassword = matcher.group(2);
        try {
            new ProfileControl().changePass(currentPassword, newPassword);
            System.out.println("password changed successfully!");
        } catch (MyException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

}
