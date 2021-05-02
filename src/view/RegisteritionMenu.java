
import java.util.regex.Matcher;
import java.util.regex.Pattern;
public class RegisteritionMenu {
    public RegisteritionMenu() {
        String input;
        while (true) {
            input = Main.scanner.nextLine().trim();
            if (input.compareTo("Exit") == 0)
                return;
            else if (input.matches("user login (--username|-u) [\\w]+ (--password|-p) [\\w]+")
                    || input.matches("user login (--password|-p) [\\w]+ (--username|-u) [\\w]+"))
                login(input);
            else if (input.matches("user create (--username|-u) [\\w]+ (--nickname|-n) [\\w]+ (--password|-p) [\\w]+")
                    || input.matches("user create (--username|-u) [\\w]+ (--password|-p) [\\w]+ (--nickname|-n) [\\w]+")
                    || input.matches("user create (--nickname|-n) [\\w]+ (--password|-p) [\\w]+ (--username|-u) [\\w]+")
                    || input.matches("user create (--nickname|-n) [\\w]+ (--username|-u) [\\w]+ (--password|-p) [\\w]+")
                    || input.matches("user create (--password|-p) [\\w]+ (--username|-u) [\\w]+ (--nickname|-n) [\\w]+")
                    || input.matches("user create (--password|-p) [\\w]+ (--nickname|-n) [\\w]+ (--username|-u) [\\w]+"))
                signin(input);
            else if (input.matches("menu show-current"))
                System.out.println("Login Menu");
            else if (input.matches("menu enter [\\w]+"))
                System.out.println("please login first");
            else
                System.out.println("invalid command!");
        }
    }
    
    public static void login(String input){
        String userName;
        String password;
        userName = getUserNameFromInput(input);
        password = getPasswordFromInput(input);
        if(User.getUserByName(userName) != null && User.getUserByName(userName).checkPassword(password)) {
            System.out.println("user logged in successfully!");
            new MainMenu(User.getUserByName(userName));
        }
        else 
            System.out.println("Username and password didn’t match!");
    }

    public static void signin(String input){
        String userName;
        String password;
        String nickName;
        userName = getUserNameFromInput(input);
        password = getPasswordFromInput(input);
        nickName = getNickNameFromInput(input);
        if(User.getUserByName(userName) != null) 
            System.out.println("user with username " + userName + " already exists");
        else if (User.getUserByNickName(nickName) != null)
            System.out.println("user with nickname " + nickName + " already exists");
        else {
            User user = new User(nickName, userName, password);
            System.out.println("user created successfully!");
            SaveUser.run(user);
        }
    }

    private static String getUserNameFromInput(String input){
        Matcher matcher;
        matcher = getCommandMatcher(input, "(--username|-u) ([\\w]+)");
        matcher.find();
        return  matcher.group(2);
    }

    private static String getPasswordFromInput(String input){
        Matcher matcher;
        matcher = getCommandMatcher(input, "(--password|-p) ([\\w]+)");
        matcher.find();
        return matcher.group(2);
    }

    private static String getNickNameFromInput(String input){
        Matcher matcher;
        matcher = getCommandMatcher(input, "(--nickname|-n) ([\\w]+)");
        matcher.find();
        return matcher.group(2);
    }

    public static Matcher getCommandMatcher(String input, String regex) {
        Pattern pattern;
        Matcher matcher;
        pattern = Pattern.compile(regex);
        matcher = pattern.matcher(input);
        return matcher;
    }
}