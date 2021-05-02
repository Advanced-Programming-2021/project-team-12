
public class MainMenu {
    public User user;
    public MainMenu(User user) {
        this.user = user;
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
                Shop.run();
            else if (input.matches("menu enter deck"))
               Deck.run();
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
