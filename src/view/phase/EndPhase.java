package view.phase;

import controllers.PhaseControl;
import models.Address;
import controllers.Game;
import view.Main;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EndPhase {
    public void run() {
        PhaseControl.getInstance().doEffectEndPhase();
        String input;
        System.out.println("phase: end phase");
        if (Game.whoseTurnPlayer().howManyCardIsInTheHandCard() == 6) {
            System.out.println("select a card to be deleted from your hand!");
            input = Main.scanner.nextLine();
            while (!input.matches("(^[ ]*select --hand [123456]{1}[ ]*$)")) {
                System.out.println("invalid command!");
                input = Main.scanner.nextLine();
            }
            Matcher matcher = getCommandMatcher(input, "(^[ ]*select --hand [123456]{1}[ ]*$)");
            if (matcher.find()) {
                Address address = new Address(matcher.group(1));
                Game.whoseTurnPlayer().removeCard(address);
            }
        }
        PhaseControl.getInstance().checkIfGameEnded();
        PhaseControl.getInstance().switchPlayerTurn();
        System.out.println(PhaseControl.getInstance().printWhoseTurnIsIt());
    }

    private static Matcher getCommandMatcher(String input, String regex) {
        input.trim();
        Pattern pattern = Pattern.compile(regex);
        return pattern.matcher(input);
    }
}
