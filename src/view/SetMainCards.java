package view;

import Utility.CommandMatcher;
import com.sun.jdi.IntegerType;
import controllers.Game;
import models.Card;
import models.Deck;
import models.Player;

import java.util.ArrayList;
import java.util.Random;
import java.util.regex.Matcher;

public class SetMainCards {
    public SetMainCards(Player firstPlayer, Player secondPlayer) {
        setCards(firstPlayer, "First");
        if (!Game.isAITurn())
            setCards(secondPlayer, "Second");
        else
            setAICards();
    }

    private void setAICards() {
        Player AI = Game.secondPlayer;
        for (int i = 1; i < 5; i++) {
            Random random = new Random();
            int slideCard = (random.nextInt() % 15) + 1;
            int mainCard = (random.nextInt() % 40) + 1;
            AI.setSlideToMain(slideCard, mainCard);
        }
        AI.setHandCard();
    }

    private void setCards(Player player, String flag) {
        System.out.println("Set Main Card Of " + flag + " Player\n");
        System.out.println("--- change slide card (slide number) with main card (main number) ---");
        System.out.println("--- show main and slide deck ---");
        System.out.println("--- end ---\n");
        while (true) {
            String input = Main.scanner.nextLine().trim();
            if (input.compareTo("end") == 0)
                break;
            else if (input.matches("change card [\\d]+ with [\\d]+"))
                changeCard(input, player);
            else if (input.matches("show main and slide deck"))
                showDeck(player);
            else
                System.out.println("invalid command!");
        }
        player.setHandCard();
    }

    private void showDeck(Player player) {
        ArrayList<Card> cards;
        cards = player.getMainCards();
        System.out.println("Main Deck:");
        for (int i = 1; i <= cards.size(); i++)
            System.out.println(i + ": " + cards.get(i - 1).getCardName());
        cards = player.getSideCards();
        System.out.println("Side deck: ");
        for (int i = 1; i <= cards.size(); i++)
            System.out.println(i + ": " + cards.get(i - 1).getCardName());
    }

    private void changeCard(String input, Player player) {
        Matcher matcher;
        int slideNumber;
        int mainNumber;
        matcher = CommandMatcher.getCommandMatcher(input, "change slide card ([\\d]+) with main card ([\\d]+)");
        matcher.find();
        slideNumber = Integer.parseInt(matcher.group(1));
        mainNumber = Integer.parseInt(matcher.group(2));
        player.setSlideToMain(slideNumber, mainNumber);
    }
}
