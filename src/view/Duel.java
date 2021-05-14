package view;

import java.nio.file.FileStore;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import models.Deck;
import models.User;

public class Duel {
    public static void run() {
        String input;
        while (true) {
            input = Main.scanner.nextLine().trim();
            if (input.compareTo("menu exit") == 0)
                return;
            else if (input.matches("duel (--new|-n) --second-player [\\w-]+ (--rounds|-r) [\\d]")
                    || input.matches("duel (--new|-n) (--rounds|-r) [\\d] --second-player [\\w-]+"))
                duelWithAnotherPlayer(input);
            else if (input.matches("duel (--new|-n) --ai (--rounds|-r) [\\d]")
                    || input.matches("duel (--new|-n) (--rounds|-r) [\\d] --ai"))
                duelWithAi(input);
            else if (input.matches("menu show-current"))
                System.out.println("Duel");
            else if (input.matches("menu enter [\\w]+"))
                System.out.println("menu navigation is not possible");
            else
                System.out.println("invalid command!");
        }
    }

    private static void duelWithAnotherPlayer(String input) {
        User secondPlayer;
        User firstPlayer = MainMenu.user;
        int round;
        Matcher matcher = getCommandMatcher(input, "--second-player ([\\w-]+)");
        matcher.find();
        secondPlayer = User.getUserByName(matcher.group(1));
        matcher = getCommandMatcher(input, "(--rounds|-r) ([\\d])");
        matcher.find();
        round = Integer.parseInt(matcher.group(2));
        Deck firstPlayerActiveDeck = Deck.getActiveDeckOfUser(firstPlayer.getName());
        Deck secondPlayerActiveDeck = Deck.getActiveDeckOfUser(secondPlayer.getName());
        if (secondPlayer == null)
            System.out.println("there is no player with this username");
        else if (firstPlayerActiveDeck == null)
            System.out.println(firstPlayer.getName() + " has no active deck");
        else if (secondPlayerActiveDeck == null) 
            System.out.println(secondPlayer.getName() + " has no active deck");
        else if (!firstPlayerActiveDeck.isValid())
            System.out.println(firstPlayer.getName() + "’s deck is invalid");
        else if (!secondPlayerActiveDeck.isValid())
            System.out.println(secondPlayer.getName() + "’s deck is invalid");
        else if (round != 1 && round != 3)
            System.out.println("number of rounds is not supported");
        else 
            Game.run(firstPlayer, secondPlayer, round);
    }

    private static void duelWithAi(String input) {
        User firstPlayer = MainMenu.user;
        int round;
        Matcher matcher;
        matcher = getCommandMatcher(input, "(--rounds|-r) ([\\d])");
        matcher.find();
        round = Integer.parseInt(matcher.group(2));
        Deck firstPlayerActiveDeck = Deck.getActiveDeckOfUser(firstPlayer.getName());
        if (firstPlayerActiveDeck == null)
            System.out.println(firstPlayer.getName() + " has no active deck");
        else if (!firstPlayerActiveDeck.isValid())
            System.out.println(firstPlayer.getName() + "’s deck is invalid");
        else if (round != 1 && round != 3)
            System.out.println("number of rounds is not supported");
        else 
            Game.run(firstPlayer, round);
    }

    private static Matcher getCommandMatcher(String input, String regex) {
        Pattern pattern;
        Matcher matcher;
        pattern = Pattern.compile(regex);
        matcher = pattern.matcher(input);
        return matcher;
    }
}
