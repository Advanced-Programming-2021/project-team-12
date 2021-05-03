package view;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import models.User;
public class Profile {
    public static void run() {
        String input;
        while (true) {
            input = Main.scanner.nextLine().trim();
            if (input.compareTo("menu exit") == 0)
                return;
            else if (input.matches("profile change (--nickname|-n) [\\w-]+"))
                changeNickName(input);
            else if (input.matches("profile change (--password|-p) --current [\\w]+ --new [\\w]+")
                    || input.matches("profile change (--password|-p) --new [\\w]+ --current [\\w]+"))
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
        matcher = getCommandMatcher(input, "(--nickname|-n) ([\\w-]+)");
        nickName = matcher.group(2);
        if (User.getUserByNickName(nickName) == null)
            System.out.println("user with nickname " + nickName + " already exists");
        else {
            MainMenu.user.setNickName(nickName);
            System.out.println("nickname changed successfully!");
        }
    }

    private static void changePassword(String input) {
        Matcher matcher;
        String currentPassword;
        String newPassword;
        matcher = getCommandMatcher(input, "--current ([\\w]+)");
        currentPassword = matcher.group(2);
        matcher = getCommandMatcher(input, "--new ([\\w]+)");
        newPassword = matcher.group(2);
        if (!MainMenu.user.checkPassword(currentPassword))
            System.out.println("current password is invalid");
        else if (MainMenu.user.checkPassword(newPassword)) 
            System.out.println("please enter a new password");
        else {
            MainMenu.user.setPassword(newPassword);
            System.out.println("password changed successfully!");
        }
    }

    public static Matcher getCommandMatcher(String input, String regex) {
        Pattern pattern;
        Matcher matcher;
        pattern = Pattern.compile(regex);
        matcher = pattern.matcher(input);
        return matcher;
    }
}
