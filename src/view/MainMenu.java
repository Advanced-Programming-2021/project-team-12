package view;
import models.User;

public class MainMenu {
    public static User user;
    public MainMenu(User user) {
        MainMenu.user = user;
        String input;
        while (true) {
            input = Main.scanner.nextLine().trim();
            if (input.compareTo("user logout") == 0) {
                System.out.println("user logged out successfully!");
                return;
            }
            else if (input.matches("scoreboard show"))
                Scoreboard.run();
            else if (input.matches("menu enter profile"))
                Profile.run();
            else if (input.matches("menu enter shop"))
                new Shop();
            else if (input.matches("menu enter deck"))
                new DeckMenu();
            else if (input.matches("menu enter import or export"))
                ImportOrExport.run();
            else if (input.matches("menu enter duel"))
                Duel.run();
            else if (input.matches("menu show-current"))
                System.out.println("Main Menu");
            else 
                System.out.println("invalid command!");
        }
    }
}
