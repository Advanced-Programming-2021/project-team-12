package view;

import java.util.regex.Matcher;

import Exceptions.AcctiveDeck;
import Exceptions.UserNameException;
import Exceptions.ValidDeck;
import Exceptions.WrongRoundNumber;
import Utility.CommandMatcher;
import controllers.DuelControl;
import controllers.Game;
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
        Matcher matcher = CommandMatcher.getCommandMatcher(input, "--second-player ([\\w-]+)");
        matcher.find();
        String secondPlayer = matcher.group(1);
        matcher = CommandMatcher.getCommandMatcher(input, "(--rounds|-r) ([\\d])");
        matcher.find();
        int round = Integer.parseInt(matcher.group(2));
        try {
            new DuelControl(secondPlayer, round);
        } catch (UserNameException e) {
            System.out.println("there is no player with this username");
        } catch (AcctiveDeck e) {
            e.printStackTrace();
        } catch (ValidDeck e) {
            e.printStackTrace();
        } catch (WrongRoundNumber e) {
            System.out.println("number of rounds is not supported");
        }
    }

    private static void duelWithAi(String input) {
        Matcher matcher = CommandMatcher.getCommandMatcher(input, "(--rounds|-r) ([\\d])");
        matcher.find();
        int round = Integer.parseInt(matcher.group(2));
        try {
            new DuelControl(round);
        } catch (AcctiveDeck | ValidDeck e) {
            e.printStackTrace();
        } catch (WrongRoundNumber e) {
            System.out.println("number of rounds is not supported");
        }
    }

}
