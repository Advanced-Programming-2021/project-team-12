package view;

import java.util.regex.Matcher;

import Utility.CommandMatcher;
import controllers.DuelControl;

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
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private static void duelWithAi(String input) {
        Matcher matcher = CommandMatcher.getCommandMatcher(input, "(--rounds|-r) ([\\d])");
        matcher.find();
        int round = Integer.parseInt(matcher.group(2));
        try {
            new DuelControl(round);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

}
