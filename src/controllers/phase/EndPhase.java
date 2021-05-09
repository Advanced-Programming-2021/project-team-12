package controllers.phase;

import models.Address;
import models.PlayerTurn;
import view.Game;
import view.Main;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EndPhase {
    public void run() {
        String input;
        System.out.println("phase: end phase");
        if (Game.whoseTurnPlayer().howManyCardIsInTheHandCard() == 6) {
            System.out.println("select a card to be deleted from your hand!");
            input = Main.scanner.nextLine();
            while (!input.matches("(^[ ]*select --hand [123456]{1}[ ]*$)")) {
                input = Main.scanner.nextLine();
                System.out.println("invalid command!");
            }
            Matcher matcher=getCommandMatcher(input,"(^[ ]*select --hand [123456]{1}[ ]*$)");
            if(matcher.find()){
                Address address=new Address(matcher.group(1));
                Game.whoseTurnPlayer().removeCard(address);
            }
        }
        checkIfGameEnded();
        switchPlayerTurn();
        printWhoseTurnIsIt();
    }

    public void checkIfGameEnded() {
        if (Game.firstPlayer.getLP() < 0) {
            //mirzaei koja beram
        } else if (Game.secondPlayer.getLP() < 0) {
            //mirzaei koja beram
        }
    }

    public void switchPlayerTurn() {
        if (Game.playerTurn == PlayerTurn.FIRSTPLAYER) {
            Game.playerTurn = PlayerTurn.SECONDPLAYER;
        } else {
            Game.playerTurn = PlayerTurn.FIRSTPLAYER;
        }
    }

    public void printWhoseTurnIsIt() {
        if (Game.playerTurn == PlayerTurn.FIRSTPLAYER) {
            System.out.println("its " + Game.firstPlayer.getNickName() + "’s turn");
        } else {
            System.out.println("its " + Game.secondPlayer.getNickName() + "’s turn");
        }
    }
    private static Matcher getCommandMatcher(String input, String regex) {
        input.trim();
        Pattern pattern = Pattern.compile(regex);
        return pattern.matcher(input);
    }
}
